package com.dataminer.datetime;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Objects;

public class FastDateTimeFormatter {

	private static final ThreadLocal<HashMap<String, DateTimeFormatter>> formatters = new ThreadLocal<HashMap<String, DateTimeFormatter>>() {
		@Override
		protected HashMap<String, DateTimeFormatter> initialValue() {
			return new HashMap<>();
		}
	};

	public static DateTimeFormatter ofPattern(String pattern) {
		DateTimeFormatter cache = formatters.get().get(pattern);
		if (Objects.isNull(cache)) {
			cache = DateTimeFormatter.ofPattern(pattern);
			formatters.get().put(pattern, cache);
		}
		return cache;
	}
}
