package de.slothsoft.mp4spliterator.impl.ffmpeg;

import java.io.File;
import java.io.IOException;
import java.lang.ProcessBuilder.Redirect;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;

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
	private static final String FILE_SYSTEM_FORBIDDEN_LETTERS = "\\\\" + "\\/" + "\\*" + "\\\"" + ":<>|?\t\r\n";

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
	public void splitIntoChapters(VideoSplit videoSplit) throws VideoSplitterException, InterruptedException {
		if (this.ffmpegFile == null) {
			throw new VideoSplitterException(Messages.getString("FfmpegFileNotSet"));
		}
		final File input = videoSplit.getSourceFile();
		final File targetFolder = videoSplit.getTargetFolder();
		final VideoSplitterConfig config = videoSplit.getConfig();
		final long videoLength = videoSplit.getVideoLength();
		final List<VideoPart> chapters = videoSplit.getChapters();

		final List<String> commands = new ArrayList<>();
		final int entireSize = chapters.size();
		int index = 0;
		final IProgressMonitor monitor = videoSplit.getProgressMonitor();
		monitor.beginTask(Messages.getString("ExportSplit"), entireSize);
		final String entireSizeString = getPrefix(entireSize - 1, entireSize);

		for (final VideoPart chapter : chapters) {
			final long startTimeLong = Math.max(chapter.getStartTime() + config.getStartTimeShift(), 0);
			final String startTime = StringifyUtil.stringifyTimeWithMiliSeconds(startTimeLong);
			final long endTimeLong = Math.min(videoLength, chapter.getEndTime() + config.getEndTimeShift());
			final String endTime = StringifyUtil.stringifyTimeWithMiliSeconds(endTimeLong - startTimeLong);
			final String prefix = getPrefix(index, entireSize);

			monitor.subTask(chapter.getTitle() + " (" + prefix + '/' + entireSizeString + ')');

			commands.clear();
			commands.add(this.ffmpegFile.toString());
			commands.add("-i");
			commands.add(input.toString());
			commands.add("-ss");
			commands.add(startTime);
			commands.add("-t");
			commands.add(endTime);
			commands.add("-c");
			commands.add("copy");
			commands.add(getTargetFileName(targetFolder, chapter, prefix, config));

			try {
				final ProcessBuilder pb = new ProcessBuilder(commands.toArray(new String[commands.size()]));
				pb.redirectOutput(Redirect.INHERIT);
				pb.redirectError(Redirect.INHERIT);
				pb.start().waitFor();
			} catch (final IOException | InterruptedException e) {
				throw new VideoSplitterException(Messages.getString("FfmpegError") + '\n' + commands.toString(), e);
			}
			index++;

			monitor.worked(1);
			if (monitor.isCanceled()) {
				throw new InterruptedException("Splitting was cancelled.");
			}
		}
		monitor.done();
	}

	static String getPrefix(int index, int entireSize) {
		final int prefixLength = ("" + entireSize).length();
		return String.format("%0" + prefixLength + "d", Integer.valueOf(index + 1));
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
}
