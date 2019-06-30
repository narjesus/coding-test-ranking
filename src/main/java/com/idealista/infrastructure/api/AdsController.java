package com.idealista.infrastructure.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.idealista.application.api.mapper.AdBOTOPublicAdFunction;
import com.idealista.application.api.mapper.AdBOTOQualityAdFunction;
import com.idealista.application.service.AdsService;

@RestController
@RequestMapping("/ads")
public class AdsController {
	@Autowired
	private AdsService adsService;
	
	@Autowired
	private AdBOTOQualityAdFunction AdBOTOQualityAdConverter;
	
	@Autowired
	private AdBOTOPublicAdFunction AdBOTOPublicAdConverter;

	@GetMapping(value="/quality-listing", produces= MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<QualityAd>> qualityListing() {
        // Retrieve ads and convert them to QualityAd format		
        return ResponseEntity.ok(AdBOTOQualityAdConverter.apply(adsService.getQualityListing()));
    }

	@GetMapping(value="/public-listing", produces= MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<PublicAd>> publicListing() {
		// Retrieve ads and convert them to PublicAd format		
        return ResponseEntity.ok(AdBOTOPublicAdConverter.apply(adsService.getPublicListing()));
    }

	@PutMapping(value="/calculate-score")
    public ResponseEntity<Void> calculateScore() {
        // Update ads scores
		adsService.putCalculateScores();
		
		// No item to be sent back
        return ResponseEntity.ok().build();
    }
}
