package de.slothsoft.mp4spliterator.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Video {

	private String title;
	private long length;
	private List<Chapter> chapters = new ArrayList<>();

	public Video(String title) {
		this.title = Objects.requireNonNull(title);
	}

	public List<Chapter> getChapters() {
		return this.chapters;
	}

	public Video chapters(List<Chapter> newChapters) {
		setChapters(newChapters);
		return this;
	}

	public void setChapters(List<Chapter> chapters) {
		this.chapters = Objects.requireNonNull(chapters);
	}

	public long getLength() {
		return this.length;
	}

	public Video length(long newLength) {
		setLength(newLength);
		return this;
	}

	public void setLength(long length) {
		this.length = length;
	}

	public String getTitle() {
		return this.title;
	}

	public Video title(String newTitle) {
		setTitle(newTitle);
		return this;
	}

	public void setTitle(String title) {
		this.title = Objects.requireNonNull(title);
	}

	@Override
	public String toString() {
		return "Video [title=" + this.title + ", length=" + this.length + ", chapters=" + this.chapters.size() + "]";
	}

}
