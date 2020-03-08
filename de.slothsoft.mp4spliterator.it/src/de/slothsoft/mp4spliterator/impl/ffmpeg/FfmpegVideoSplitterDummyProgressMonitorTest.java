package de.slothsoft.mp4spliterator.impl.ffmpeg;

import java.io.File;

import de.slothsoft.mp4spliterator.core.VideoSplitter;
import de.slothsoft.mp4spliterator.impl.AbstractVideoSplitterProgressMonitorTest;

public class FfmpegVideoSplitterDummyProgressMonitorTest extends AbstractVideoSplitterProgressMonitorTest {

	@Override
	protected VideoSplitter createVideoSplitter() {
		return new FfmpegVideoSplitter(new File("src/dummy/ffmpeg.bat"));
	}

	@Override
	protected String getTaskName() {
		return Messages.getString("ExportSplit");
	}
}