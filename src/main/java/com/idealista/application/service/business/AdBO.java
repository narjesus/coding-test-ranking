package com.idealista.application.service.business;

import java.util.Date;
import java.util.List;

/**
 *  Ad BO format
 */
public class AdBO {

	private Integer id;
    private String typology;
    private String description;
    private List<String> pictureUrls;
    private Integer houseSize;
    private Integer gardenSize;
    private Integer score;
    private Date irrelevantSince;
    
    /**
     *  Returns the id of this ad
     *  @return The id of this ad
     */
	public Integer getId() {
		return id;
	}
	
	/**
     *  Sets the id of this ad
     *  @param id The new id of this add
     */
	public void setId(Integer id) {
		this.id = id;
	}
	
	/**
     *  Returns the building type of this ad
     *  @return The building type of this ad
     */
	public String getTypology() {
		return typology;
	}
	
	/**
     *  Sets the building type of this ad
     *  @param typology The new building type of this add
     */
	public void setTypology(String typology) {
		this.typology = typology;
	}
	
	/**
     *  Returns the description of this ad
     *  @return The description of this ad
     */
	public String getDescription() {
		return description;
	}
	
	/**
     *  Sets the description of this ad
     *  @param description The new description of this add
     */
	public void setDescription(String description) {
		this.description = description;
	}
	
	/**
     *  Returns the list of pictures urls related to this ad
     *  @return The list of pictures urls related to this ad
     */
	public List<String> getPictureUrls() {
		return pictureUrls;
	}
	
	/**
     *  Sets the list of pictures urls related to this ad
     *  @param pictureUrls The new picture's urls of this add
     */
	public void setPictureUrls(List<String> pictureUrls) {
		this.pictureUrls = pictureUrls;
	}
	
	/**
     *  Returns the house or chalet size of this ad
     *  @return The house or chalet size of this ad
     */
	public Integer getHouseSize() {
		return houseSize;
	}
	
	/**
     *  Sets the house or chalet size of this ad
     *  @param houseSize The new house or chalet size of this add
     */
	public void setHouseSize(Integer houseSize) {
		this.houseSize = houseSize;
	}
	
	/**
     *  Returns the chalet's garden size of this ad
     *  @return The chalet's garden size of this ad
     */
	public Integer getGardenSize() {
		return gardenSize;
	}
	
	/**
     *  Sets the chalet's garden size of this ad
     *  @param gardenSize The new chalet's garden size of this add
     */
	public void setGardenSize(Integer gardenSize) {
		this.gardenSize = gardenSize;
	}
	
	/**
     *  Returns the score of this ad
     *  @return The score of this ad
     */
	public Integer getScore() {
		return score;
	}
	
	/**
     *  Sets the score of this ad
     *  @param score The new score of this add
     */
	public void setScore(Integer score) {
		this.score = score;
	}
	
	/**
     *  Returns the first irrelevant date of this ad
     *  @return The first irrelevant date of this ad
     */
	public Date getIrrelevantSince() {
		return irrelevantSince;
	}
	
	/**
     *  Sets the first irrelevant date of this ad
     *  @param irrelevantSince The new irrelevant date of this add
     */
	public void setIrrelevantSince(Date irrelevantSince) {
		this.irrelevantSince = irrelevantSince;
	}
}
