package com.idealista.application.service.business;

/**
 *  Constants related to score calculus
 */
public final class ScoreConstants {
	/**
	 *  Limit score value to be irrelevant
	 */
	public static final int IRRELEVANT_SCORE = 40;
	
	/**
	 *  Score obtained by not having any picture
	 */
	public static final int NO_PICTURE = -10;
	
	/**
	 *  Score obtained by having a high quality picture
	 */
	public static final int PICTURE_HD = 20;
	
	/**
	 *  Score obtained by having a low quality picture
	 */
	public static final int PICTURE_NO_HD = 10;
	
	/**
	 *  Score obtained by having any description
	 */
	public static final int DESCRIPTION_PRESENT = 5;
	
	/**
	 *  Score obtained by having a 20 to 49 words description for a flat
	 */
	public static final int FLAT_DESCRIPTION_20 = 10;
	
	/**
	 *  Score obtained by having a more than 50 words description for a flat
	 */
	public static final int FLAT_DESCRIPTION_50 = 30;
	
	/**
	 *  Score obtained by having a more than 50 words description for a chalet
	 */
	public static final int CHALET_DESCRIPTION_50 = 20;
	
	/**
	 *  Score obtained by having a keyword in description
	 */
	public static final int KEYWORD_IN_DESCRIPTION = 5;
	
	/**
	 *  Score obtained by having all required fields filled
	 */
	public static final int FILLED = 40;
}
