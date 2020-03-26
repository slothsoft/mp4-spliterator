package de.slothsoft.mp4spliterator.impl.ffmpeg;

import java.io.File;

import de.slothsoft.mp4spliterator.core.VideoSplitter;
import de.slothsoft.mp4spliterator.impl.AbstractVideoSplitterTest;

public class FfmpegVideoSplitterDummyTest extends AbstractVideoSplitterTest {

	@Override
	protected VideoSplitter createVideoSplitter() {
		return new FfmpegVideoSplitter(getFfmpegFile());
	}

	static File getFfmpegFile() {
		// absolute location of this module's target/classes folder
		final String targetClassesFolder = FfmpegVideoSplitterDummyTest.class.getProtectionDomain().getCodeSource()
				.getLocation().getFile();
		// absolute location of this module's dummy/ folder
		final File dummyFolder = new File(targetClassesFolder, "../../src/dummy");

		switch (OperatingSystem.getCurrent()) {
			case WINDOWS :
				return new File(dummyFolder, "ffmpeg.bat");
			case LINUX :
				return new File(dummyFolder, "ffmpeg.sh");
			default :
				throw new UnsupportedOperationException("Cannot handle OS " + OperatingSystem.getCurrent() + "!");
		}
	}
}