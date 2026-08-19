package com.kce.shortener.util;

import org.springframework.stereotype.Component;

@Component
public class DoubleRollingHashUtil {

	private static final String BASE62 = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private static final int BASE = BASE62.length();

	/**
	 * Inner Hasher class matching user's DSA rolling hash logic.
	 */
	public static class Hasher {
		private long[] fhash;
		private long[] pk;
		private int sz;
		private long p, MOD;

		public void init(String s, long _p, long _MOD) {
			sz = s.length();
			p = _p;
			MOD = _MOD;
			fhash = new long[sz];
			pk = new long[sz];
			pk[0] = 1;
			fhash[0] = s.charAt(0) % MOD;

			for (int i = 1; i < sz; i++) {
				fhash[i] = ((fhash[i - 1] * p) % MOD + (s.charAt(i) % MOD)) % MOD;
				pk[i] = (pk[i - 1] * p) % MOD;
			}
		}

		public long getHash(int l, int r) {
			if (l == 0) {
				return fhash[r];
			}
			return (((fhash[r] - (fhash[l - 1] * pk[r - l + 1]) % MOD) % MOD) + MOD) % MOD;
		}
	}

	/**
	 * Inner DoubleHasher class using h1 (p=31, MOD=1000000007L) and h2 (p=37, MOD=998244353L).
	 */
	public static class DoubleHasher {
		private Hasher h1 = new Hasher();
		private Hasher h2 = new Hasher();

		public void init(String s) {
			h1.init(s, 31, 1000000007L);
			h2.init(s, 37, 998244353L);
		}

		public long[] getHash(int l, int r) {
			return new long[] { h1.getHash(l, r), h2.getHash(l, r) };
		}
	}

	/**
	 * Generates short code using user's DoubleHasher + Base62 string encoding.
	 */
	public String generateShortCode(String input, int attemptCount) {
		String target = (attemptCount > 0) ? input + "#" + attemptCount : input;
		
		DoubleHasher dh = new DoubleHasher();
		dh.init(target);
		
		long[] pair = dh.getHash(0, target.length() - 1);
		
		// Combine double hash pair into a positive 64-bit value
		long combinedHash = Math.abs((pair[0] * 31L) ^ pair[1]);
		
		return encodeBase62(combinedHash);
	}

	private String encodeBase62(long num) {
		if (num == 0) {
			return String.valueOf(BASE62.charAt(0));
		}
		StringBuilder sb = new StringBuilder();
		while (num > 0) {
			int remainder = (int) (num % BASE);
			sb.append(BASE62.charAt(remainder));
			num /= BASE;
		}
		return sb.reverse().toString();
	}
}
