package com.idealista.application.api.mapper;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.idealista.application.service.business.AdBO;
import com.idealista.infrastructure.api.QualityAd;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class AdBOTOQualityAdFunctionTest {

	@Autowired
	private AdBOTOQualityAdFunction adBOTOQualityAdConverter;
	
	@Configuration
	static class ContextConfiguration {
		@Bean
		public AdBOTOQualityAdFunction adBOTOQualityAdConverter() {
			return new AdBOTOQualityAdFunction();
		}
	}
	
	@Test
	public void nullInput_applyTest() {
		List<AdBO> input = null;
		
		List<QualityAd> output = adBOTOQualityAdConverter.apply(input);
		
		Assert.assertNotNull(output);
		Assert.assertEquals(0, output.size());
	}
	
	@Test
	public void emptyInput_applyTest() {
		List<AdBO> input = new ArrayList<AdBO>();
		
		List<QualityAd> output = adBOTOQualityAdConverter.apply(input);
		
		Assert.assertNotNull(output);
		Assert.assertEquals(0, output.size());
	}
	
	@Test
	public void nominal_applyTest() throws ParseException {
		DateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
		
		List<AdBO> input = new ArrayList<AdBO>();
		
		AdBO adBO1 = new AdBO();
		adBO1.setId(1);
		adBO1.setDescription("Test description");
		adBO1.setGardenSize(100);
		adBO1.setHouseSize(200);
		adBO1.setIrrelevantSince(dateFormatter.parse("20-06-2019"));
		adBO1.setScore(50);
		adBO1.setTypology("FLAT");
		List<String> pictureUrls = new ArrayList<String>();
		pictureUrls.add("url1");
		pictureUrls.add("url2");
		adBO1.setPictureUrls(pictureUrls);
		
		AdBO adBO2 = new AdBO();
		adBO2.setId(2);
		adBO2.setDescription(null);
		adBO2.setGardenSize(null);
		adBO2.setHouseSize(null);
		adBO2.setIrrelevantSince(null);
		adBO2.setScore(null);
		adBO2.setTypology(null);
		adBO2.setPictureUrls(null);
		
		input.add(adBO1);
		input.add(adBO2);
		
		List<QualityAd> output = adBOTOQualityAdConverter.apply(input);
		
		Assert.assertNotNull(output);
		Assert.assertEquals(2, output.size());
		
		Assert.assertEquals(1, (int)output.get(0).getId());
		Assert.assertEquals("Test description", output.get(0).getDescription());
		Assert.assertEquals(100, (int)output.get(0).getGardenSize());
		Assert.assertEquals(200, (int)output.get(0).getHouseSize());
		Assert.assertEquals("20-06-2019", dateFormatter.format(output.get(0).getIrrelevantSince()));
		Assert.assertEquals(50, (int)output.get(0).getScore());
		Assert.assertEquals("FLAT", output.get(0).getTypology());
		Assert.assertEquals(2, output.get(0).getPictureUrls().size());
		Assert.assertEquals("url1", output.get(0).getPictureUrls().get(0));
		Assert.assertEquals("url2", output.get(0).getPictureUrls().get(1));
		
		Assert.assertEquals(2, (int)output.get(1).getId());
		Assert.assertNull(output.get(1).getDescription());
		Assert.assertNull(output.get(1).getGardenSize());
		Assert.assertNull(output.get(1).getHouseSize());
		Assert.assertNull(output.get(1).getIrrelevantSince());
		Assert.assertNull(output.get(1).getScore());
		Assert.assertNull(output.get(1).getTypology());
		Assert.assertNull(output.get(1).getPictureUrls());
	}
}
