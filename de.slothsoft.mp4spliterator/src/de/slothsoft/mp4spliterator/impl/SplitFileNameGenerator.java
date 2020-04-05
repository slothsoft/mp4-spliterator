package de.slothsoft.mp4spliterator.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import de.slothsoft.mp4spliterator.core.VideoPart;
import de.slothsoft.mp4spliterator.core.VideoSplitterConfig;

public class SplitFileNameGenerator {

	// these are forbidden in windows at last
	private static final String FILE_SYSTEM_FORBIDDEN_LETTERS = "\\\\" + "\\/" + "\\*" + "\\\"" + ":<>|?\t\r\n";

	private VideoSplitterConfig config = new VideoSplitterConfig();
	private final File targetFolder;

	public SplitFileNameGenerator(VideoSplitterConfig config, File targetFolder) {
		this.config = Objects.requireNonNull(config);
		this.targetFolder = Objects.requireNonNull(targetFolder);
	}

	public List<String> createFileNames(List<VideoPart> chapters) {
		final int chaptersSize = chapters.size();
		final List<String> result = new ArrayList<>(chaptersSize);
		int index = 0;
		for (final VideoPart chapter : chapters) {
			result.add(getTargetFileName(this.targetFolder, chapter, getPrefix(index, chaptersSize), this.config));
			index++;
		}
		return result;
	}

	static String getTargetFileName(File targetFolder, final VideoPart chapter, String runningNumber,
			VideoSplitterConfig config) {
		String fileName = config.getPattern();
		fileName = fileName.replace(VideoSplitterConfig.PATTERN_RUNNING_NUMBER, runningNumber);
		fileName = fileName.replace(VideoSplitterConfig.PATTERN_CHAPTER_TITLE, sanitize(chapter.getTitle().trim()));
		return getTargetFileName(targetFolder, fileName);
	}

	static String getTargetFileName(File targetFolder, String fileName) {
		File result = new File(targetFolder, fileName + ".mp4");

		int index = 2;
		while (result.exists()) {
			result = new File(targetFolder, fileName + " (" + index + ").mp4");
			index++;
		}
		return result.toString();
	}

	static String sanitize(final String string) {
		return string.replaceAll("[" + FILE_SYSTEM_FORBIDDEN_LETTERS + "]{1}", "_");
	}

	static String getPrefix(int index, int entireSize) {
		final int prefixLength = ("" + entireSize).length();
		return String.format("%0" + prefixLength + "d", Integer.valueOf(index + 1));
	}
}
