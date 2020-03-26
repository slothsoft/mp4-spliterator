package de.slothsoft.mp4spliterator.impl.ffmpeg;

import de.slothsoft.mp4spliterator.core.VideoSplitter;
import de.slothsoft.mp4spliterator.impl.AbstractVideoSplitterProgressMonitorTest;

public class FfmpegVideoSplitterDummyProgressMonitorTest extends AbstractVideoSplitterProgressMonitorTest {

	@Override
	protected VideoSplitter createVideoSplitter() {
		return new FfmpegVideoSplitter(FfmpegVideoSplitterDummyTest.getFfmpegFile());
	}

	@Override
	protected String getTaskName() {
		return Messages.getString("ExportSplit");
	}
}