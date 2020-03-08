package de.slothsoft.mp4spliterator.impl.ffmpeg;

import java.io.File;
import java.io.IOException;
import java.lang.ProcessBuilder.Redirect;
import java.util.List;

import de.slothsoft.mp4spliterator.Mp4SpliteratorPlugin;
import de.slothsoft.mp4spliterator.core.StringifyUtil;
import de.slothsoft.mp4spliterator.core.VideoPart;
import de.slothsoft.mp4spliterator.core.VideoSplit;
import de.slothsoft.mp4spliterator.core.VideoSplitter;
import de.slothsoft.mp4spliterator.core.VideoSplitterConfig;
import de.slothsoft.mp4spliterator.core.VideoSplitterException;

/**
 * An implementation of {@link VideoSplitter} using
 * <a href="https://www.ffmpeg.org/r">ffmpeg</a>.
 *
 * @author Stef Schulz
 * @since 1.0.0
 */

public class FfmpegVideoSplitter implements VideoSplitter {

	// these are forbidden in windows at last
	private static final String FILE_SYSTEM_FORBIDDEN_LETTERS = "\\\\" + "\\/" + "\\*" + "\\\"" + ":<>|?";

	final File ffmpegFile;

	public FfmpegVideoSplitter() {
		final String ffmpegFileString = Mp4SpliteratorPlugin.getDefault().getPreferenceStore()
				.getString(FfmpegPreferencePage.FFMPEG_PATH);
		this.ffmpegFile = ffmpegFileString.isEmpty() ? null : new File(ffmpegFileString);
	}

	FfmpegVideoSplitter(File ffmpegFile) {
		this.ffmpegFile = ffmpegFile;
	}

	@Override
	public void splitIntoChapters(VideoSplit videoSplit) throws VideoSplitterException {
		if (this.ffmpegFile == null) {
			throw new VideoSplitterException(Messages.getString("FfmpegFileNotSet"));
		}
		final File input = videoSplit.getSourceFile();
		final File targetFolder = videoSplit.getTargetFolder();
		final VideoSplitterConfig config = videoSplit.getConfig();
		final long videoLength = videoSplit.getVideoLength();
		final List<VideoPart> chapters = videoSplit.getChapters();

		final StringBuilder sb = new StringBuilder();
		final int entireSize = chapters.size();
		int index = 0;

		for (final VideoPart chapter : chapters) {
			final long startTimeLong = Math.max(chapter.getStartTime() + config.getStartTimeShift(), 0);
			final String startTime = StringifyUtil.stringifyTimeWithMiliSeconds(startTimeLong);
			final long endTimeLong = Math.min(videoLength, chapter.getEndTime() + config.getEndTimeShift());
			final String endTime = StringifyUtil.stringifyTimeWithMiliSeconds(endTimeLong - startTimeLong);
			final String prefix = getPrefix(index, entireSize);

			sb.setLength(0);
			sb.append('\"').append(this.ffmpegFile).append('\"');
			sb.append(" -i ").append('\"').append(input).append('\"');
			sb.append(" -ss ").append(startTime).append(" -t ").append(endTime);
			sb.append(" -c ").append("copy ");
			sb.append('\"').append(getTargetFileName(targetFolder, chapter, prefix, config)).append('\"');

			try {
				final ProcessBuilder pb = new ProcessBuilder(sb.toString());
				pb.redirectOutput(Redirect.INHERIT);
				pb.redirectError(Redirect.INHERIT);
				pb.start().waitFor();
			} catch (final IOException | InterruptedException e) {
				throw new VideoSplitterException(Messages.getString("FfmpegError") + '\n' + sb.toString(), e);
			}
			index++;
		}
	}

	static String getPrefix(int index, int entireSize) {
		final int prefixLength = ("" + entireSize).length();
		return String.format("%0" + prefixLength + "d", Integer.valueOf(index + 1));
	}

	static String getTargetFileName(File targetFolder, final VideoPart chapter, String runningNumber,
			VideoSplitterConfig config) {
		String fileName = config.getPattern();
		fileName = fileName.replace(VideoSplitterConfig.PATTERN_RUNNING_NUMBER, runningNumber);
		fileName = fileName.replace(VideoSplitterConfig.PATTERN_CHAPTER_TITLE, sanitize(chapter.getTitle()));
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
}
