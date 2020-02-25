package de.slothsoft.mp4spliterator.core;

public final class StringifyUtil {

	public static String stringifyTime(long startTime) {
		long seconds = startTime / 1000;
		final long minutes = seconds / 60;
		seconds = seconds % 60;
		return String.format("%02d:%02d", Long.valueOf(minutes), Long.valueOf(seconds));
	}

	private StringifyUtil() {
		// hide me
	}
}
