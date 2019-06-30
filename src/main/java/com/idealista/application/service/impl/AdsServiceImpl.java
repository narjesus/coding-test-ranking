package com.idealista.application.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.idealista.infrastructure.persistence.AdVO;
import com.idealista.infrastructure.persistence.InMemoryPersistence;
import com.idealista.infrastructure.persistence.PictureVO;
import com.idealista.application.service.AdsService;
import com.idealista.application.service.business.AdBO;
import com.idealista.application.service.business.ScoreConstants;

/**
 *  Ads manage service implementation
 */
@Service
public class AdsServiceImpl implements AdsService{
	
	private final Logger LOGGER = LoggerFactory.getLogger(AdsServiceImpl.class);
	
	@Autowired
	private InMemoryPersistence repository;

	/**
	 *  Retrieve an ads list for a quality manager
	 *  @return complete ads list order by ascendant score
	 */
	@Override
	public List<AdBO> getQualityListing() {
		LOGGER.info("Starting ads collect");
		// Return sorted (low to high score) ads list
		return retrieveAds(true).stream().sorted((o1, o2) -> o1.getScore().compareTo(o2.getScore())).collect(Collectors.toList());
	}

	/**
	 *  Retrieve an ads list for a client
	 *  @return ads list with score higher than 40 order by descendant score
	 */
	@Override
	public List<AdBO> getPublicListing() {
		LOGGER.info("Starting relevant ads collect");
		// Return sorted (high to low score) ads list
		return retrieveAds(false).stream().sorted((o1, o2) -> o2.getScore().compareTo(o1.getScore())).collect(Collectors.toList());
	}

	/**
	 *  Updates the score of all ads stocked in memory
	 */
	@Override
	public void putCalculateScores() {
		LOGGER.info("Starting ads score calculation");
		List<AdVO> updatedAdsVO = new ArrayList<AdVO>();
		
		// Retrieve stocked ads
		List<AdVO> adsVO = repository.getAds();
		List<PictureVO> picturesVO = repository.getPictures();
		LOGGER.debug("Received " + adsVO.size() + " ads and " + picturesVO.size() + " pictures from repository");
		
		int score;
		
		// Calculate ad scores
		for (AdVO adVO : adsVO) {
			// Filter for only ad images
			List<PictureVO> adPicturesVO = picturesVO.stream().filter(e -> adVO.getPictures().contains(e.getId())).collect(Collectors.toList());
			
			// Retrieve score for a single ad
			score = calculateSingleScore(adVO, adPicturesVO);
			
			// Score value from 0 to 100
			if (score < 0) {
				score = 0;
			} else if (score > 100) {
				score = 100;
			}
			
			LOGGER.debug("Ad with ID " + adVO.getId() + " final score is " + score + " points");
			
			// Update score and irrelevantSince date
			adVO.setScore(score);
			
			if (adVO.getScore() < 40) {
				if (adVO.getIrrelevantSince() == null) {
					adVO.setIrrelevantSince(new Date());
				}
			} else {
				adVO.setIrrelevantSince(null);
			}
			
			// Set ad in updated list
			updatedAdsVO.add(adVO);
		}
		
		LOGGER.info("Updating ads on repository");
		// Update stocked ads
		repository.setAds(updatedAdsVO);
	}
	
	private List<AdBO> retrieveAds(Boolean qualityDpt) {
		List<AdBO> adsBO = new ArrayList<AdBO>();
		
		// Retrieve stocked ads
		List<AdVO> adsVO = repository.getAds();
		List<PictureVO> picturesVO = repository.getPictures();
		LOGGER.debug("Received " + adsVO.size() + " ads and " + picturesVO.size() + " pictures from repository");
		
		// Add BO format ads to return object
		for (AdVO adVO : adsVO) {
			if (adVO.getScore() != null && (qualityDpt || adVO.getScore() >= ScoreConstants.IRRELEVANT_SCORE)) {
				// Filter for only ad images
				List<PictureVO> adPicturesVO = picturesVO.stream().filter(e -> adVO.getPictures().contains(e.getId())).collect(Collectors.toList());
				adsBO.add(buildAdBO(adVO, adPicturesVO));
			}
		}
		
		LOGGER.info("Returning " + adsBO.size() + " ads");
		return adsBO;
	}
	
	private AdBO buildAdBO(AdVO adVO, List<PictureVO> picturesVO) {
		// A simple VO to BO converter
		AdBO adBO = new AdBO();
		
		adBO.setId(adVO.getId());
		adBO.setTypology(adVO.getTypology());
		adBO.setDescription(adVO.getDescription());
		
		List<String> picturesURL = new ArrayList<String>();
		for (PictureVO pictureVO : picturesVO) {
			picturesURL.add(pictureVO.getUrl());
		}
		adBO.setPictureUrls(picturesURL);
		
		adBO.setHouseSize(adVO.getHouseSize());
		adBO.setGardenSize(adVO.getGardenSize());
		adBO.setScore(adVO.getScore());
		adBO.setIrrelevantSince(adVO.getIrrelevantSince());
		
		return adBO;
	}
	
	private int calculateSingleScore(AdVO adVO, List<PictureVO> picturesVO) {
		int pictureScore = calculatePictureScore(picturesVO);
		LOGGER.debug("Ad with ID " + adVO.getId() + " scored " + pictureScore + " points from pictures");
		
		int descriptionScore = calculateDescriptionScore(adVO.getTypology(), adVO.getDescription());
		LOGGER.debug("Ad with ID " + adVO.getId() + " scored " + descriptionScore + " points from description");
		
		int filledScore = calculateFilledScore(adVO, picturesVO);
		LOGGER.debug("Ad with ID " + adVO.getId() + " scored " + filledScore + " points from completion rate");
		
		return pictureScore + descriptionScore + filledScore;
	}
	
	private int calculatePictureScore(List<PictureVO> picturesVO) {
		int score = 0;
		
		// check picture presence
		if (!picturesVO.isEmpty()) {
			for (PictureVO pictureVO : picturesVO) {
				// check picture quality
				if (pictureVO.getQuality().equals("HD")) {
					score = score + ScoreConstants.PICTURE_HD;
				} else {
					score = score + ScoreConstants.PICTURE_NO_HD;
				}
			}
		} else {
			score = score + ScoreConstants.NO_PICTURE;
		}
		
		return score;
	}
	
	private int calculateDescriptionScore(String type, String description) {
		int score = 0;
		int wordsCount;
		String lowerCaseDescription;
		
		// check description presence
		if (description != null && !description.isEmpty()) {
			score = score + ScoreConstants.DESCRIPTION_PRESENT;
			
			// check description length
			if (type != null) {
				wordsCount = description.split(" ").length;
				
				if (type.equals("FLAT")) {
					if (wordsCount >= 50) {
						score = score + ScoreConstants.FLAT_DESCRIPTION_50;
					} else if (wordsCount >= 20) {
						score = score + ScoreConstants.FLAT_DESCRIPTION_20;
					}
				} else if (type.equals("CHALET")) {
					if (wordsCount > 50) {
						score = score + ScoreConstants.CHALET_DESCRIPTION_50;
					}
				}
			}
			
			// check keywords presence
			lowerCaseDescription = description.toLowerCase();
			
			if (lowerCaseDescription.indexOf("luminoso") >= 0) {
				score = score + ScoreConstants.KEYWORD_IN_DESCRIPTION;
			}
			
			if (lowerCaseDescription.indexOf("nuevo") >= 0) {
				score = score + ScoreConstants.KEYWORD_IN_DESCRIPTION;
			}
			
			if (lowerCaseDescription.indexOf("céntrico") >= 0) {
				score = score + ScoreConstants.KEYWORD_IN_DESCRIPTION;
			}
			
			if (lowerCaseDescription.indexOf("reformado") >= 0) {
				score = score + ScoreConstants.KEYWORD_IN_DESCRIPTION;
			}
			
			if (lowerCaseDescription.indexOf("ático") >= 0) {
				score = score + ScoreConstants.KEYWORD_IN_DESCRIPTION;
			}
		}
		
		return score;
	}
	
	private int calculateFilledScore(AdVO adVO, List<PictureVO> picturesVO) {
		int score = 0;
		
		if (!picturesVO.isEmpty() && adVO.getTypology() != null) {
			// Only picture required for GARAGE
			if (adVO.getTypology().equals("GARAGE")) {
				score = score + ScoreConstants.FILLED;
			}
			
			if (adVO.getDescription() != null && !adVO.getDescription().isEmpty() && adVO.getHouseSize() != null) {
				// No garden size required for FLAT
				if (adVO.getTypology().equals("FLAT")) {
					score = score + ScoreConstants.FILLED;
				}
				
				if (adVO.getTypology().equals("CHALET") && adVO.getGardenSize() != null) {
					score = score + ScoreConstants.FILLED;
				}
			}
		}
		
		return score;
	}

}
