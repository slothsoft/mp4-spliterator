package de.slothsoft.mp4spliterator.impl.ffmpeg;

import java.io.File;
import java.io.IOException;
import java.lang.ProcessBuilder.Redirect;
import java.util.List;

import de.slothsoft.mp4spliterator.Mp4SpliteratorPlugin;
import de.slothsoft.mp4spliterator.core.StringifyUtil;
import de.slothsoft.mp4spliterator.core.VideoPart;
import de.slothsoft.mp4spliterator.core.VideoSplitter;
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
	private static final String FILE_SYSTEM_FORBIDDEN_LETTERS = "\\\\\\/:\\*?\\\"<>|";

	private final File ffmpegFile;

	public FfmpegVideoSplitter() {
		final String ffmpegFileString = Mp4SpliteratorPlugin.getDefault().getPreferenceStore()
				.getString(FfmpegPreferencePage.FFMPEG_PATH);
		this.ffmpegFile = ffmpegFileString.isEmpty() ? null : new File(ffmpegFileString);
	}

	@Override
	public void splitIntoChapters(File input, File targetFolder, List<VideoPart> chapters)
			throws VideoSplitterException {
		if (this.ffmpegFile == null) {
			throw new VideoSplitterException(Messages.getString("FfmpegFileNotSet"));
		}

		final StringBuilder sb = new StringBuilder();
		final int entireSize = chapters.size();
		int index = 0;

		for (final VideoPart chapter : chapters) {
			final String startTime = StringifyUtil.stringifyTime(chapter.getStartTime());
			final String endTime = StringifyUtil.stringifyTime(chapter.getEndTime() - chapter.getStartTime() + 1000);
			final String prefix = getPrefix(index, entireSize);

			sb.setLength(0);
			sb.append('\"').append(this.ffmpegFile).append('\"');
			sb.append(" -i ").append('\"').append(input).append('\"');
			sb.append(" -ss ").append(startTime).append(" -t ").append(endTime);
			sb.append(" -c ").append("copy ").append('\"').append(getTargetFileName(targetFolder, chapter, prefix))
					.append('\"');

			try {
				final ProcessBuilder pb = new ProcessBuilder(sb.toString());
				pb.redirectOutput(Redirect.INHERIT);
				pb.redirectError(Redirect.INHERIT);
				pb.start();
			} catch (final IOException e) {
				throw new VideoSplitterException(Messages.getString("FfmpegError") + '\n' + sb.toString(), e);
			}
			index++;
		}
	}

	static String getPrefix(int index, int entireSize) {
		final int prefixLength = ("" + entireSize).length();
		return String.format("%0" + prefixLength + "d", Integer.valueOf(index + 1));
	}

	static String getTargetFileName(File targetFolder, final VideoPart chapter, String prefix) {
		return getTargetFileName(targetFolder, prefix + ' ' + sanitize(chapter.getTitle()));
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
