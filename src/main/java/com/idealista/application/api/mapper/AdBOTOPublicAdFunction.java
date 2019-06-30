package com.idealista.application.api.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import org.springframework.stereotype.Component;

import com.idealista.infrastructure.api.PublicAd;
import com.idealista.application.service.business.AdBO;

/**
 *  Converter from ad BO format to ad Public format
 */
@Component
public class AdBOTOPublicAdFunction implements Function<List<AdBO>, List<PublicAd>> {

	/**
	 *  Converts from ad BO format to ad Public format
	 *  @param input Ad object in ad BO format
	 *  @return Ad object in ad Public format
	 */
	@Override
	public List<PublicAd> apply(List<AdBO> input) {
		List<PublicAd> responseList = new ArrayList<PublicAd>();
		
		if (input != null) {
			for(AdBO adBO : input) {
				PublicAd publicAd = new PublicAd();
				publicAd.setId(adBO.getId());
				publicAd.setTypology(adBO.getTypology());
				publicAd.setDescription(adBO.getDescription());
				publicAd.setPictureUrls(adBO.getPictureUrls());
				publicAd.setHouseSize(adBO.getHouseSize());
				publicAd.setGardenSize(adBO.getGardenSize());
				
				responseList.add(publicAd);
			}
		}
		
		return responseList;
	}

}
