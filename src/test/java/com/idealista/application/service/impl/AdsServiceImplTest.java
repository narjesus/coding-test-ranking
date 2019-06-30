package com.idealista.application.service.impl;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.idealista.application.service.AdsService;
import com.idealista.application.service.business.AdBO;
import com.idealista.infrastructure.persistence.AdVO;
import com.idealista.infrastructure.persistence.InMemoryPersistence;
import com.idealista.infrastructure.persistence.PictureVO;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class AdsServiceImplTest {

	@Autowired
	private AdsService adsService;
	
	@Autowired
	private InMemoryPersistence repository;
	
	@Captor
	ArgumentCaptor<List<AdVO>> updatedAdsCaptor;
	
	@Configuration
	static class ContextConfiguration {
		@Bean
		public AdsService adsService() {
			return new AdsServiceImpl();
		}
		
		@Bean
		public InMemoryPersistence repository() {
			return Mockito.mock(InMemoryPersistence.class);
		}
	}
	
	@Before
	public void init() {
		Mockito.reset(repository);
		
		List<PictureVO> pictures = new ArrayList<PictureVO>();
        pictures.add(new PictureVO(1, "pictureUrl_1", "SD"));
        pictures.add(new PictureVO(2, "pictureUrl_2", "SD"));
        pictures.add(new PictureVO(3, "pictureUrl_3", "HD"));
        pictures.add(new PictureVO(4, "pictureUrl_4", "HD"));
        pictures.add(new PictureVO(5, "pictureUrl_5", "HD"));
        pictures.add(new PictureVO(6, "pictureUrl_6", "HD"));
        pictures.add(new PictureVO(7, "pictureUrl_7", "HD"));
        
        Mockito.when(repository.getPictures()).thenReturn(pictures);
        Mockito.doNothing().when(repository).setAds(Mockito.any());
	}
	
	@Test
	public void emptyAds_getQualityListingTest() throws ParseException {
       
        Mockito.when(repository.getAds()).thenReturn(new ArrayList<AdVO>());
        
        List<AdBO> response = adsService.getQualityListing();
        
        Assert.assertNotNull(response);
        Assert.assertEquals(0, response.size());
	}
	
	@Test
	public void nominal_getQualityListingTest() throws ParseException {
		DateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
		
		List<AdVO> ads = new ArrayList<AdVO>();
		ads.add(new AdVO(1, "FLAT", "Flat test description", Arrays.asList(1, 3), 200, null, 60, null));
        ads.add(new AdVO(2, "FLAT", "Another flat test description", Collections.<Integer>emptyList(), 300, null, null, null));
        ads.add(new AdVO(3, "CHALET", "Chalet test description", Collections.<Integer>emptyList(), 300, 100, 20, dateFormatter.parse("20-06-2019")));
        
        Mockito.when(repository.getAds()).thenReturn(ads);
        
        List<AdBO> response = adsService.getQualityListing();
        
        Assert.assertNotNull(response);
        Assert.assertEquals(2, response.size());
        
        Assert.assertEquals(3, (int)response.get(0).getId());
        Assert.assertEquals("CHALET", response.get(0).getTypology());
        Assert.assertEquals("Chalet test description", response.get(0).getDescription());
        Assert.assertEquals(300, (int)response.get(0).getHouseSize());
        Assert.assertEquals(100, (int)response.get(0).getGardenSize());
        Assert.assertEquals(20, (int)response.get(0).getScore());
        Assert.assertEquals("20-06-2019", dateFormatter.format(response.get(0).getIrrelevantSince()));
        Assert.assertEquals(0, response.get(0).getPictureUrls().size());
        
        Assert.assertEquals(1, (int)response.get(1).getId());
        Assert.assertEquals("FLAT", response.get(1).getTypology());
        Assert.assertEquals("Flat test description", response.get(1).getDescription());
        Assert.assertEquals(200, (int)response.get(1).getHouseSize());
        Assert.assertNull(response.get(1).getGardenSize());
        Assert.assertEquals(60, (int)response.get(1).getScore());
        Assert.assertNull(response.get(1).getIrrelevantSince());
        Assert.assertEquals(2, response.get(1).getPictureUrls().size());
        Assert.assertEquals("pictureUrl_1", response.get(1).getPictureUrls().get(0));
        Assert.assertEquals("pictureUrl_3", response.get(1).getPictureUrls().get(1));
	}
	
	@Test
	public void nominal_getPublicListingTest() throws ParseException {
		DateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
		
		List<AdVO> ads = new ArrayList<AdVO>();
		ads.add(new AdVO(1, "FLAT", "Flat test description", Collections.<Integer>emptyList(), 200, null, 60, null));
        ads.add(new AdVO(2, "FLAT", "Another flat test description", Collections.<Integer>emptyList(), 300, null, null, null));
        ads.add(new AdVO(3, "CHALET", "Chalet test description", Arrays.asList(4, 2), 400, 200, 80, null));
        ads.add(new AdVO(4, "CHALET", "Another chalet test description", Collections.<Integer>emptyList(), 300, 100, 20, dateFormatter.parse("20-06-2019")));
        
        Mockito.when(repository.getAds()).thenReturn(ads);
        
        List<AdBO> response = adsService.getPublicListing();
        
        Assert.assertNotNull(response);
        Assert.assertEquals(2, response.size());
        
        Assert.assertEquals(3, (int)response.get(0).getId());
        Assert.assertEquals("CHALET", response.get(0).getTypology());
        Assert.assertEquals("Chalet test description", response.get(0).getDescription());
        Assert.assertEquals(400, (int)response.get(0).getHouseSize());
        Assert.assertEquals(200, (int)response.get(0).getGardenSize());
        Assert.assertEquals(80, (int)response.get(0).getScore());
        Assert.assertNull(response.get(0).getIrrelevantSince());
        Assert.assertEquals(2, response.get(0).getPictureUrls().size());
        Assert.assertEquals("pictureUrl_2", response.get(0).getPictureUrls().get(0));
        Assert.assertEquals("pictureUrl_4", response.get(0).getPictureUrls().get(1));
        
        Assert.assertEquals(1, (int)response.get(1).getId());
        Assert.assertEquals("FLAT", response.get(1).getTypology());
        Assert.assertEquals("Flat test description", response.get(1).getDescription());
        Assert.assertEquals(200, (int)response.get(1).getHouseSize());
        Assert.assertNull(response.get(1).getGardenSize());
        Assert.assertEquals(60, (int)response.get(1).getScore());
        Assert.assertNull(response.get(1).getIrrelevantSince());
        Assert.assertEquals(0, response.get(1).getPictureUrls().size());
	}
	
	@Test
	public void emptyAds_putCalculateScoresTest() throws ParseException {
       
        Mockito.when(repository.getAds()).thenReturn(new ArrayList<AdVO>());
        
        adsService.putCalculateScores();
        
        Mockito.verify(repository).setAds(updatedAdsCaptor.capture());
        
        Assert.assertNotNull(updatedAdsCaptor.getValue());
        Assert.assertEquals(0, updatedAdsCaptor.getValue().size());
	}
	
	@Test
	public void pictureScoreWithDate_putCalculateScoresTest() throws ParseException {
		DateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
		
		List<AdVO> ads = new ArrayList<AdVO>();
		ads.add(new AdVO(1, "FLAT", "", Collections.<Integer>emptyList(), null, null, null, null));
		ads.add(new AdVO(2, "FLAT", "", Arrays.asList(1, 3), null, null, 10, dateFormatter.parse("20-06-2019")));
		ads.add(new AdVO(3, "FLAT", "", Arrays.asList(1, 2, 3, 4), null, null, 20, dateFormatter.parse("20-06-2019")));
		ads.add(new AdVO(4, "FLAT", "", Arrays.asList(2, 3, 4, 5, 6, 7), null, null, 30, dateFormatter.parse("20-06-2019")));
       
        Mockito.when(repository.getAds()).thenReturn(ads);
        
        adsService.putCalculateScores();
        
        Mockito.verify(repository).setAds(updatedAdsCaptor.capture());
        
        Assert.assertNotNull(updatedAdsCaptor.getValue());
        Assert.assertEquals(4, updatedAdsCaptor.getValue().size());
        
        Assert.assertEquals(1, (int)updatedAdsCaptor.getValue().get(0).getId());
        Assert.assertEquals(0, (int)updatedAdsCaptor.getValue().get(0).getScore());
        Assert.assertNotNull(updatedAdsCaptor.getValue().get(0).getIrrelevantSince());
        
        Assert.assertEquals(2, (int)updatedAdsCaptor.getValue().get(1).getId());
        Assert.assertEquals(30, (int)updatedAdsCaptor.getValue().get(1).getScore());
        Assert.assertEquals("20-06-2019", dateFormatter.format(updatedAdsCaptor.getValue().get(1).getIrrelevantSince()));
        
        Assert.assertEquals(3, (int)updatedAdsCaptor.getValue().get(2).getId());
        Assert.assertEquals(60, (int)updatedAdsCaptor.getValue().get(2).getScore());
        Assert.assertNull(updatedAdsCaptor.getValue().get(2).getIrrelevantSince());
        
        Assert.assertEquals(4, (int)updatedAdsCaptor.getValue().get(3).getId());
        Assert.assertEquals(100, (int)updatedAdsCaptor.getValue().get(3).getScore());
        Assert.assertNull(updatedAdsCaptor.getValue().get(3).getIrrelevantSince());
	}
	
	@Test
	public void descriptionScore_putCalculateScoresTest() throws ParseException {
		List<AdVO> ads = new ArrayList<AdVO>();
		ads.add(new AdVO(1, null, "", Arrays.asList(1), null, null, null, null));
		ads.add(new AdVO(2, "", null, Arrays.asList(1), null, null, null, null));
		ads.add(new AdVO(3, null, "Test description", Arrays.asList(1), null, null, null, null));
		ads.add(new AdVO(4, "FLAT", "A simple description for 5 points", Arrays.asList(1), null, null, null, null));
		ads.add(new AdVO(5, "FLAT", "A 20 words test description will give us 10 additional points for a flat as in this case where we have more than 20 words", Arrays.asList(1), null, null, null, null));
		ads.add(new AdVO(6, "FLAT", "A 50 words test description will give us 30 additional points for a flat. To have this 30 points we need to write a 50 words description but in this case I will repeat some words many times until we have the required 50 words and so the 30 points that we are looking for.", Arrays.asList(1), null, null, null, null));
		ads.add(new AdVO(7, "CHALET", "A simple description for 5 points", Arrays.asList(1), 100, null, null, null));
		ads.add(new AdVO(8, "CHALET", "A 50 words test description will give us 20 additional points for a chalet. To have this 20 points we need to write a 50 words description but in this case I will repeat some words many times until we have the required 50 words and so the 20 points that we are looking for.", Arrays.asList(1), 100, null, null, null));
		ads.add(new AdVO(9, "WEIRD_BUILDING", "Some words like luMinOso, NUEVO, céntrico, REFORmado, áTiCO give us 5 points each one but no additional point for to be many times like nuevo, céntrico, CÉNTRICO or NUEVO", Arrays.asList(1), null, null, null, null));
       
        Mockito.when(repository.getAds()).thenReturn(ads);
        
        adsService.putCalculateScores();
        
        Mockito.verify(repository).setAds(updatedAdsCaptor.capture());
        
        Assert.assertNotNull(updatedAdsCaptor.getValue());
        Assert.assertEquals(9, updatedAdsCaptor.getValue().size());
        
        Assert.assertEquals(1, (int)updatedAdsCaptor.getValue().get(0).getId());
        Assert.assertEquals(10, (int)updatedAdsCaptor.getValue().get(0).getScore());

        Assert.assertEquals(2, (int)updatedAdsCaptor.getValue().get(1).getId());
        Assert.assertEquals(10, (int)updatedAdsCaptor.getValue().get(1).getScore());

        Assert.assertEquals(3, (int)updatedAdsCaptor.getValue().get(2).getId());
        Assert.assertEquals(15, (int)updatedAdsCaptor.getValue().get(2).getScore());
        
        Assert.assertEquals(4, (int)updatedAdsCaptor.getValue().get(3).getId());
        Assert.assertEquals(15, (int)updatedAdsCaptor.getValue().get(3).getScore());
        
        Assert.assertEquals(5, (int)updatedAdsCaptor.getValue().get(4).getId());
        Assert.assertEquals(25, (int)updatedAdsCaptor.getValue().get(4).getScore());
        
        Assert.assertEquals(6, (int)updatedAdsCaptor.getValue().get(5).getId());
        Assert.assertEquals(45, (int)updatedAdsCaptor.getValue().get(5).getScore());
        
        Assert.assertEquals(7, (int)updatedAdsCaptor.getValue().get(6).getId());
        Assert.assertEquals(15, (int)updatedAdsCaptor.getValue().get(6).getScore());
        
        Assert.assertEquals(8, (int)updatedAdsCaptor.getValue().get(7).getId());
        Assert.assertEquals(35, (int)updatedAdsCaptor.getValue().get(7).getScore());
        
        Assert.assertEquals(9, (int)updatedAdsCaptor.getValue().get(8).getId());
        Assert.assertEquals(40, (int)updatedAdsCaptor.getValue().get(8).getScore());
	}
	
	@Test
	public void fullFilledScore_putCalculateScoresTest() throws ParseException {
		List<AdVO> ads = new ArrayList<AdVO>();
		ads.add(new AdVO(1, "GARAGE", "With image and description a garage ad is filled", Arrays.asList(1), null, null, null, null));
		ads.add(new AdVO(2, "FLAT", "With image, description and house size a flat ad is filled", Arrays.asList(1), 100, null, null, null));
		ads.add(new AdVO(3, "CHALET", "With image, description, house size and garden size a chalet ad is filled", Arrays.asList(1), 200, 100, null, null));

        Mockito.when(repository.getAds()).thenReturn(ads);
        
        adsService.putCalculateScores();
        
        Mockito.verify(repository).setAds(updatedAdsCaptor.capture());
        
        Assert.assertNotNull(updatedAdsCaptor.getValue());
        Assert.assertEquals(3, updatedAdsCaptor.getValue().size());
        
        Assert.assertEquals(1, (int)updatedAdsCaptor.getValue().get(0).getId());
        Assert.assertEquals(55, (int)updatedAdsCaptor.getValue().get(0).getScore());

        Assert.assertEquals(2, (int)updatedAdsCaptor.getValue().get(1).getId());
        Assert.assertEquals(55, (int)updatedAdsCaptor.getValue().get(1).getScore());

        Assert.assertEquals(3, (int)updatedAdsCaptor.getValue().get(2).getId());
        Assert.assertEquals(55, (int)updatedAdsCaptor.getValue().get(2).getScore());
	}
}
