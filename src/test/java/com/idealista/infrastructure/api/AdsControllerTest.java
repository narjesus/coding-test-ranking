package com.idealista.infrastructure.api;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.idealista.application.api.mapper.AdBOTOPublicAdFunction;
import com.idealista.application.api.mapper.AdBOTOQualityAdFunction;
import com.idealista.application.service.AdsService;

import java.util.ArrayList;

import org.junit.Before;

@RunWith(SpringJUnit4ClassRunner.class)
@WebMvcTest(AdsController.class)
@ContextConfiguration
public class AdsControllerTest {
	
	@Autowired
    private MockMvc mockMvc;
	
	@Autowired
	private AdBOTOQualityAdFunction adBOTOQualityAdConverter;
	
	@Autowired
	private AdBOTOPublicAdFunction adBOTOPublicAdConverter;
	
	@Autowired
	private AdsService adsService;
	
	@Configuration
	static class ContextConfiguration {
		@Bean
		public AdsController adsController() {
			return new AdsController();
		}
		
		@Bean
		public AdsService adsService() {
			return Mockito.mock(AdsService.class);
		}
		
		@Bean
		public AdBOTOQualityAdFunction adBOTOQualityAdConverter() {
			return Mockito.mock(AdBOTOQualityAdFunction.class);
		}
		
		@Bean
		public AdBOTOPublicAdFunction adBOTOPublicAdConverter() {
			return Mockito.mock(AdBOTOPublicAdFunction.class);
		}
	}
	
	@Before
	public void init() {
		Mockito.reset(adsService);
		Mockito.reset(adBOTOQualityAdConverter);
		Mockito.reset(adBOTOPublicAdConverter);
		
		Mockito.when(adsService.getQualityListing()).thenReturn(null);
		Mockito.when(adsService.getPublicListing()).thenReturn(null);
		Mockito.doNothing().when(adsService).putCalculateScores();
		Mockito.when(adBOTOQualityAdConverter.apply(Mockito.any())).thenReturn(new ArrayList<QualityAd>());
		Mockito.when(adBOTOPublicAdConverter.apply(Mockito.any())).thenReturn(new ArrayList<PublicAd>());
	}

	@Test
	public void qualityListingTest() throws Exception {
		this.mockMvc.perform(get("/ads/quality-listing")).andExpect(status().isOk());
	}
	
	@Test
	public void publicListingTest() throws Exception {
		this.mockMvc.perform(get("/ads/public-listing")).andExpect(status().isOk());
	}
	
	@Test
	public void calculateScoreTest() throws Exception {
		this.mockMvc.perform(put("/ads/calculate-score")).andExpect(status().isOk());
	}
}
