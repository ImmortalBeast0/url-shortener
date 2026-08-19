package com.kce.shortener.service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Service;

import com.kce.shortener.util.RateLimitExceededException;

@Service
public class RateLimiterService {

	private static final int MAX_REQUESTS_PER_MINUTE = 20; // Medium rate limit threshold
	private static final long ONE_MINUTE_IN_MILLIS = 60_000L;

	private final Map<String, ClientRequestInfo> clientRequestMap = new ConcurrentHashMap<>();

	public void checkRateLimit(String clientIp) {
		long currentTime = System.currentTimeMillis();

		clientRequestMap.compute(clientIp, (ip, info) -> {
			if (info == null || (currentTime - info.startTime.get()) > ONE_MINUTE_IN_MILLIS) {
				return new ClientRequestInfo(new AtomicLong(currentTime), new AtomicInteger(1));
			} else {
				if (info.count.incrementAndGet() > MAX_REQUESTS_PER_MINUTE) {
					throw new RateLimitExceededException("Rate limit exceeded. Maximum " + MAX_REQUESTS_PER_MINUTE + " requests per minute allowed.");
				}
				return info;
			}
		});
	}

	private static class ClientRequestInfo {
		final AtomicLong startTime;
		final AtomicInteger count;

		ClientRequestInfo(AtomicLong startTime, AtomicInteger count) {
			this.startTime = startTime;
			this.count = count;
		}
	}
}
