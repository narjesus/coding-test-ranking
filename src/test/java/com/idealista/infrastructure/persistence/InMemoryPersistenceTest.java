package com.idealista.infrastructure.persistence;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class InMemoryPersistenceTest {
	
	@Autowired
	private InMemoryPersistence repository;
	
	@Configuration
	static class ContextConfiguration {
		@Bean
		public InMemoryPersistence repository() {
			return new InMemoryPersistence();
		}
	}

	@Test
	public void nominal_repositoryTest() {
		List<AdVO> stockedAdsBeforeSet = repository.getAds();
		List<PictureVO> stockedPictures = repository.getPictures();
		
		List<AdVO> updatedAds = new ArrayList<AdVO>();
		updatedAds.add(new AdVO(1, null, null, null, null, null, null, null));
		updatedAds.add(new AdVO(2, null, null, null, null, null, null, null));
		updatedAds.add(new AdVO(3, null, null, null, null, null, null, null));
		repository.setAds(updatedAds);
		
		List<AdVO> stockedAdsAfterSet = repository.getAds();
		
		Assert.assertNotNull(stockedAdsBeforeSet);
		Assert.assertEquals(8, stockedAdsBeforeSet.size());
		
		Assert.assertNotNull(stockedPictures);
		Assert.assertEquals(8, stockedPictures.size());
		
		Assert.assertNotNull(stockedAdsAfterSet);
		Assert.assertEquals(3, stockedAdsAfterSet.size());
	}
}
