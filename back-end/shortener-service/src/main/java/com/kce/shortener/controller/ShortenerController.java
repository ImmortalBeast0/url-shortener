package com.kce.shortener.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kce.shortener.model.ShortenRequest;
import com.kce.shortener.model.ShortenResponse;
import com.kce.shortener.service.ShortenerService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins = "*")

@Tag(name = "URL Shortener Management", description = "Endpoints for shortening URLs, redirects, and link analytics")
public class ShortenerController {

	private final ShortenerService shortenerService;

	public ShortenerController(ShortenerService shortenerService) {
		this.shortenerService = shortenerService;
	}

	@Operation(summary = "Shorten a long URL", description = "Converts a long URL into a short code using Double Rolling Hash algorithm")
	@PostMapping("/shorten")
	public ResponseEntity<ShortenResponse> shortenUrl(@RequestBody ShortenRequest request, HttpServletRequest httpRequest) {
		String clientIp = httpRequest.getRemoteAddr();
		String baseUrl = httpRequest.getScheme() + "://" + httpRequest.getServerName() + ":" + httpRequest.getServerPort() + "/api/v1/r";
		ShortenResponse response = this.shortenerService.shortenUrl(request, clientIp, baseUrl);
		return new ResponseEntity<ShortenResponse>(response, HttpStatus.OK);
	}

	@Operation(summary = "Redirect short URL code", description = "Performs HTTP 302 redirect to original target URL and increments click count")
	@GetMapping("/r/{shortCode}")
	public ResponseEntity<Void> redirectUrl(@PathVariable String shortCode) {
		String originalUrl = this.shortenerService.getOriginalUrlAndIncrementClick(shortCode);
		return ResponseEntity.status(HttpStatus.FOUND)
				.location(URI.create(originalUrl))
				.build();
	}

	@Operation(summary = "View short URL statistics", description = "Fetches click count and metadata for a short code")
	@GetMapping("/stats/{shortCode}")
	public ResponseEntity<ShortenResponse> getUrlStats(@PathVariable String shortCode) {
		ShortenResponse response = this.shortenerService.getUrlStats(shortCode);
		return new ResponseEntity<ShortenResponse>(response, HttpStatus.OK);
	}

	@Operation(summary = "List all shortened URLs", description = "Fetches history of shortened URLs")
	@GetMapping("/urls")
	public ResponseEntity<List<ShortenResponse>> listAllUrls(HttpServletRequest httpRequest) {
		String baseUrl = httpRequest.getScheme() + "://" + httpRequest.getServerName() + ":" + httpRequest.getServerPort() + "/api/v1/r";
		List<ShortenResponse> urls = this.shortenerService.getAllUrls(baseUrl);
		return new ResponseEntity<List<ShortenResponse>>(urls, HttpStatus.OK);
	}
}
