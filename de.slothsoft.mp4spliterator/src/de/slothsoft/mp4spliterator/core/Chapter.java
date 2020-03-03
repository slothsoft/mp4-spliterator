package de.slothsoft.mp4spliterator.core;

import java.util.Objects;

public class Chapter implements VideoPart {

	private String title;
	private long startTime;
	private long endTime;

	public Chapter(String title) {
		this.title = Objects.requireNonNull(title);
	}

	@Override
	public long getStartTime() {
		return this.startTime;
	}

	public Chapter startTime(long newStartTime) {
		setStartTime(newStartTime);
		return this;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	@Override
	public String getTitle() {
		return this.title;
	}

	public Chapter title(String newTitle) {
		setTitle(newTitle);
		return this;
	}

	public void setTitle(String title) {
		this.title = Objects.requireNonNull(title);
	}

	@Override
	public long getEndTime() {
		return this.endTime;
	}

	public Chapter endTime(long newEndTime) {
		setEndTime(newEndTime);
		return this;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
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
		return "Chapter [title=" + this.title + ", time=" + this.startTime + " -> " + this.endTime + "]";
	}

}
