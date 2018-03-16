package org.redhat.vrp.domain;

public class LocationTimeUtility {

	/**
	 * 35 miles per hour is roughly feet per millisecond
	 */
	public static double FEET_PER_MILLISECOND = 0.0513333;

	public static long FEET_IN_MILE = 5280;

	public static long MINUTES_TO_MILLISECONDS = 60000;

	public static long HOURS_TO_MILLISECONDS = 3600000;

	public static long SECONDS_TO_MILLISECONDS = 1000;

	public static long millisecondsToSeconds(long value) {
		return Math.round(value / MINUTES_TO_MILLISECONDS);
	}

	public static long secondsToMillisecond(long value) {
		return value * SECONDS_TO_MILLISECONDS;
	}

	public static long millisecondsToHours(long value) {
		return Math.round(value / HOURS_TO_MILLISECONDS);
	}

}
