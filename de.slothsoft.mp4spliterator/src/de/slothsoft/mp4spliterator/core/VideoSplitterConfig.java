package de.slothsoft.mp4spliterator.core;

import java.util.Objects;

import org.eclipse.jface.preference.IPreferenceStore;

import de.slothsoft.mp4spliterator.Mp4SpliteratorPlugin;
import de.slothsoft.mp4spliterator.Mp4SpliteratorPreferences;

/**
 * The entire config used to split a video file using the {@link VideoSplitter}.
 *
 * @author Stef Schulz
 * @since 1.1.0
 */

public class VideoSplitterConfig {

	public static final String PATTERN_RUNNING_NUMBER = "{number}";
	public static final String PATTERN_CHAPTER_TITLE = "{chapterTitle}";

	public static final String DEFAULT_PATTERN = PATTERN_RUNNING_NUMBER + " " + PATTERN_CHAPTER_TITLE;

	public static VideoSplitterConfig forPreferences() {
		final IPreferenceStore preferences = Mp4SpliteratorPlugin.getDefault().getPreferenceStore();
		return new VideoSplitterConfig().pattern(preferences.getString(Mp4SpliteratorPreferences.PATTERN))
				.startTimeShift(preferences.getInt(Mp4SpliteratorPreferences.START_TIME_SHIFT))
				.endTimeShift(preferences.getInt(Mp4SpliteratorPreferences.END_TIME_SHIFT));
	}

	private long startTimeShift;
	private long endTimeShift;
	private String pattern = DEFAULT_PATTERN;

	public long getEndTimeShift() {
		return this.endTimeShift;
	}

	public VideoSplitterConfig endTimeShift(long newEndTimeShift) {
		setEndTimeShift(newEndTimeShift);
		return this;
	}

	public void setEndTimeShift(long endTimeShift) {
		this.endTimeShift = endTimeShift;
	}

	public long getStartTimeShift() {
		return this.startTimeShift;
	}

	public VideoSplitterConfig startTimeShift(long newStartTimeShift) {
		setStartTimeShift(newStartTimeShift);
		return this;
	}

	public void setStartTimeShift(long startTimeShift) {
		this.startTimeShift = startTimeShift;
	}

	public String getPattern() {
		return this.pattern;
	}

	public VideoSplitterConfig pattern(String newPattern) {
		setPattern(newPattern);
		return this;
	}

	public void setPattern(String pattern) {
		this.pattern = Objects.requireNonNull(pattern);
	}

}
