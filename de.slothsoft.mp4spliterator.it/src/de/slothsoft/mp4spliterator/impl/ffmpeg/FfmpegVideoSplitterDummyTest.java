package de.slothsoft.mp4spliterator.impl.ffmpeg;

import java.io.File;

import de.slothsoft.mp4spliterator.core.VideoSplitter;
import de.slothsoft.mp4spliterator.impl.AbstractVideoSplitterTest;

public class FfmpegVideoSplitterDummyTest extends AbstractVideoSplitterTest {

	@Override
	protected VideoSplitter createVideoSplitter() {
		return new FfmpegVideoSplitter(new File("src/dummy/ffmpeg.bat"));
	}
}