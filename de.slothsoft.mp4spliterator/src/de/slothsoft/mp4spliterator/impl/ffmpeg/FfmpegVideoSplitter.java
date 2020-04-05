package de.slothsoft.mp4spliterator.impl.ffmpeg;

import java.io.File;
import java.io.IOException;
import java.lang.ProcessBuilder.Redirect;
import java.text.NumberFormat;
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
import de.slothsoft.mp4spliterator.impl.SplitFileNameGenerator;

/**
 * An implementation of {@link VideoSplitter} using
 * <a href="https://www.ffmpeg.org/r">ffmpeg</a>.
 *
 * @author Stef Schulz
 * @since 1.0.0
 */

public class FfmpegVideoSplitter implements VideoSplitter {

	final File ffmpegFile;
	final NumberFormat integerFormat = NumberFormat.getInstance();

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
		final String entireSizeString = this.integerFormat.format(entireSize);
		final List<String> fileNames = new SplitFileNameGenerator(config, targetFolder).createFileNames(chapters);

		for (final VideoPart chapter : chapters) {
			final long startTimeLong = Math.max(chapter.getStartTime() + config.getStartTimeShift(), 0);
			final String startTime = StringifyUtil.stringifyTimeWithMiliSeconds(startTimeLong);
			final long endTimeLong = Math.min(videoLength, chapter.getEndTime() + config.getEndTimeShift());
			final String endTime = StringifyUtil.stringifyTimeWithMiliSeconds(endTimeLong - startTimeLong);
			final String prefix = this.integerFormat.format(index + 1);

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
			commands.add(fileNames.get(index));

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

}
