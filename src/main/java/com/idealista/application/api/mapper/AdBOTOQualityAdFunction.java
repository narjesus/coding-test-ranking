package com.idealista.application.api.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import org.springframework.stereotype.Component;

import com.idealista.infrastructure.api.QualityAd;
import com.idealista.application.service.business.AdBO;

/**
 *  Converter from ad BO format to ad Quality format
 */
@Component
public class AdBOTOQualityAdFunction implements Function<List<AdBO>, List<QualityAd>> {

	/**
	 *  Converts from ad BO format to ad Quality format
	 *  @param input Ad object in ad BO format
	 *  @return Ad object in ad Quality format
	 */
	@Override
	public List<QualityAd> apply(List<AdBO> input) {
		List<QualityAd> responseList = new ArrayList<QualityAd>();
		
		if (input != null) {
			for(AdBO adBO : input) {
				QualityAd qualityAd = new QualityAd();
				qualityAd.setId(adBO.getId());
				qualityAd.setTypology(adBO.getTypology());
				qualityAd.setDescription(adBO.getDescription());
				qualityAd.setPictureUrls(adBO.getPictureUrls());
				qualityAd.setHouseSize(adBO.getHouseSize());
				qualityAd.setGardenSize(adBO.getGardenSize());
				qualityAd.setScore(adBO.getScore());
				qualityAd.setIrrelevantSince(adBO.getIrrelevantSince());
				
				responseList.add(qualityAd);
			}
		}
		
		return responseList;
	}

}
