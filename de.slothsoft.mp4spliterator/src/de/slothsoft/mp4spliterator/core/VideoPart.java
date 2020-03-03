package de.slothsoft.mp4spliterator.core;

import java.util.Objects;

public interface VideoPart {

	static boolean areEqual(VideoPart part, Object other) {
		if (part == other) {
			return true;
		}
		if (other == null) {
			return false;
		}
		if (!(other instanceof VideoPart)) {
			return false;
		}
		final VideoPart otherPart = (VideoPart) other;
		if (part.getStartTime() != otherPart.getStartTime()) {
			return false;
		}
		if (part.getEndTime() != otherPart.getEndTime()) {
			return false;
		}
		if (!Objects.deepEquals(part.getTitle(), otherPart.getTitle())) {
			return false;
		}
		return true;
	}

	static int calculateHashCode(VideoPart part) {
		return Objects.hash(part.getTitle(), Long.valueOf(part.getStartTime()), Long.valueOf(part.getEndTime()));
	}

	String getTitle();

	long getStartTime();

	long getEndTime();
}
