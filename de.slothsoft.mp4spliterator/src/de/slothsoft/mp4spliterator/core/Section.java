package de.slothsoft.mp4spliterator.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Multiple {@link VideoPart} merged together into one big {@link Section}.
 *
 * @author Stef Schulz
 * @since 1.0.0
 */

public class Section implements VideoPart {

	private final List<VideoPart> videoParts = new ArrayList<>();

	public Section(VideoPart firstPart) {
		Objects.requireNonNull(firstPart);
		this.videoParts.add(firstPart);
	}

	@Override
	public String getTitle() {
		return this.videoParts.stream().map(VideoPart::getTitle).collect(Collectors.joining(", "));
	}

	@Override
	public long getStartTime() {
		return this.videoParts.get(0).getStartTime();
	}

	@Override
	public long getEndTime() {
		return this.videoParts.get(this.videoParts.size() - 1).getEndTime();
	}

	public void addPart(VideoPart part) {
		addParts(part);
	}

	public void addParts(VideoPart... parts) {
		this.videoParts.addAll(Arrays.asList(parts));
		this.videoParts.sort(Comparator.comparing(VideoPart::getStartTime));
	}

	public VideoPart[] getParts() {
		return this.videoParts.toArray(new VideoPart[this.videoParts.size()]);
	}

	public boolean containsPart(VideoPart part) {
		return this.videoParts.contains(part);
	}

	@Override
	public int hashCode() {
		return VideoPart.calculateHashCode(this);
	}

	@Override
	public boolean equals(Object obj) {
		return VideoPart.areEqual(this, obj);
	}

	@Override
	public String toString() {
		return "Section [videoParts=" + this.videoParts.size() + "]";
	}

}
