package de.slothsoft.mp4spliterator.impl.ffmpeg;

import de.slothsoft.mp4spliterator.core.VideoSplitter;
import de.slothsoft.mp4spliterator.impl.AbstractVideoSplitterProgressMonitorTest;

public class FfmpegVideoSplitterProgressMonitorTest extends AbstractVideoSplitterProgressMonitorTest {

	@Override
	protected VideoSplitter createVideoSplitter() {
		return new FfmpegVideoSplitter(FfmpegVideoSplitterTest.getFfmpegFile()).loggingEnabled(false);
	}

	@Override
	protected String getTaskName() {
		return Messages.getString("ExportSplit");
	}
}