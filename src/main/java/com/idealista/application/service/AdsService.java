package com.idealista.application.service;

import java.util.List;

import com.idealista.application.service.business.AdBO;

/**
 *  Ads manage service interface
 */
public interface AdsService {

	/**
	 *  Retrieve an ads list for a quality manager
	 *  @return complete ads list order by ascendant score
	 */
	public List<AdBO> getQualityListing();
	
	/**
	 *  Retrieve an ads list for a client
	 *  @return ads list with score higher than 40 order by descendant score
	 */
	public List<AdBO> getPublicListing();
	
	/**
	 *  Updates the score of all ads stocked in memory
	 */
	public void putCalculateScores();
}
