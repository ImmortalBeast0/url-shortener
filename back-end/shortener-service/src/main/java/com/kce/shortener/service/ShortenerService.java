package com.kce.shortener.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.kce.shortener.model.ShortenRequest;
import com.kce.shortener.model.ShortenResponse;

@Service
public interface ShortenerService {

	ShortenResponse shortenUrl(ShortenRequest request, String clientIp, String baseUrl);

	String getOriginalUrlAndIncrementClick(String shortCode);

	ShortenResponse getUrlStats(String shortCode);

	List<ShortenResponse> getAllUrls(String baseUrl);
}
