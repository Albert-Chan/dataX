package com.dataminer.datetime;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class TimeUtil {
	public static long toTimeStamp(LocalDateTime ldt) {
		ZonedDateTime zdt = ldt.atZone(ZoneId.systemDefault());
		return zdt.toInstant().toEpochMilli();
	}

	public static long toTimeStamp(LocalDateTime ldt, String zoneId) {
		ZonedDateTime zdt = ldt.atZone(ZoneId.of(zoneId));
		return zdt.toInstant().toEpochMilli();
	}

	public static LocalDateTime toLocalDateTime(long timestamp) {
		return LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.systemDefault());
	}

	public static LocalDateTime toLocalDateTime(long timestamp, String zoneId) {
		return LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.of(zoneId));
	}
	
	public static LocalDate toLocalDate(long timestamp) {
		return LocalDate.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.systemDefault());
	}
	
	public static LocalDate toLocalDate(long timestamp, String zoneId) {
		return LocalDate.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.of(zoneId));
	}

}
