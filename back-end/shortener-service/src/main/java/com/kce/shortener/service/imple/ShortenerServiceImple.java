package com.kce.shortener.service.imple;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.kce.shortener.entity.URL;
import com.kce.shortener.model.ShortenRequest;
import com.kce.shortener.model.ShortenResponse;
import com.kce.shortener.repository.ShortenerRepository;
import com.kce.shortener.service.RateLimiterService;
import com.kce.shortener.service.ShortenerService;
import com.kce.shortener.util.DoubleRollingHashUtil;
import com.kce.shortener.util.UrlNotFoundException;

@Service
public class ShortenerServiceImple implements ShortenerService {

	private final ShortenerRepository shortenerRepository;
	private final DoubleRollingHashUtil doubleRollingHashUtil;
	private final RateLimiterService rateLimiterService;

	public ShortenerServiceImple(ShortenerRepository shortenerRepository,
			DoubleRollingHashUtil doubleRollingHashUtil,
			RateLimiterService rateLimiterService) {
		this.shortenerRepository = shortenerRepository;
		this.doubleRollingHashUtil = doubleRollingHashUtil;
		this.rateLimiterService = rateLimiterService;
	}

	@Override
	public ShortenResponse shortenUrl(ShortenRequest request, String clientIp, String baseUrl) {
		// Enforce Rate Limiting
		this.rateLimiterService.checkRateLimit(clientIp);

		String originalUrl = request.getUrl().trim();
		if (!originalUrl.startsWith("http://") && !originalUrl.startsWith("https://")) {
			originalUrl = "https://" + originalUrl;
		}

		String shortCode;
		if (request.getCustomAlias() != null && !request.getCustomAlias().trim().isEmpty()) {
			shortCode = request.getCustomAlias().trim();
			if (this.shortenerRepository.existsByShortCode(shortCode)) {
				throw new IllegalArgumentException("Custom alias '" + shortCode + "' is already taken.");
			}
		} else {
			// Generate using Double Rolling Hash with collision probing
			int attempt = 0;
			do {
				shortCode = this.doubleRollingHashUtil.generateShortCode(originalUrl, attempt);
				attempt++;
			} while (this.shortenerRepository.existsByShortCode(shortCode));
		}

		URL urlEntity = new URL();
		urlEntity.setShortCode(shortCode);
		urlEntity.setOriginalUrl(originalUrl);
		urlEntity.setCreatedAt(LocalDateTime.now());
		urlEntity.setClickCount(0L);

		URL savedEntity = this.shortenerRepository.saveAndFlush(urlEntity);

		return mapToResponse(savedEntity, baseUrl);
	}

	@Override
	public String getOriginalUrlAndIncrementClick(String shortCode) {
		Optional<URL> urlContainer = this.shortenerRepository.findByShortCode(shortCode);
		if (urlContainer.isPresent()) {
			URL url = urlContainer.get();
			url.setClickCount(url.getClickCount() + 1);
			this.shortenerRepository.saveAndFlush(url);
			return url.getOriginalUrl();
		} else {
			throw new UrlNotFoundException("Short URL with code '" + shortCode + "' not found.");
		}
	}

	@Override
	public ShortenResponse getUrlStats(String shortCode) {
		Optional<URL> urlContainer = this.shortenerRepository.findByShortCode(shortCode);
		if (urlContainer.isPresent()) {
			return mapToResponse(urlContainer.get(), "");
		} else {
			throw new UrlNotFoundException("Short URL with code '" + shortCode + "' not found.");
		}
	}

	@Override
	public List<ShortenResponse> getAllUrls(String baseUrl) {
		return this.shortenerRepository.findAll().stream()
				.map(url -> mapToResponse(url, baseUrl))
				.collect(Collectors.toList());
	}

	private ShortenResponse mapToResponse(URL url, String baseUrl) {
		String fullShortUrl = (baseUrl != null && !baseUrl.isEmpty()) 
				? baseUrl + "/" + url.getShortCode() 
				: url.getShortCode();
		return new ShortenResponse(
				url.getShortCode(),
				fullShortUrl,
				url.getOriginalUrl(),
				url.getCreatedAt(),
				url.getClickCount()
		);
	}
}
