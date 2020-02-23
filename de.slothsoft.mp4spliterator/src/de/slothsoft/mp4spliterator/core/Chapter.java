package de.slothsoft.mp4spliterator.core;

import java.util.Objects;

public class Chapter {

	private String title;
	private long startTime;

	public Chapter(String title) {
		this.title = Objects.requireNonNull(title);
	}

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
	public String toString() {
		return "Chapter [title=" + this.title + ", startTime=" + this.startTime + "]";
	}

}
