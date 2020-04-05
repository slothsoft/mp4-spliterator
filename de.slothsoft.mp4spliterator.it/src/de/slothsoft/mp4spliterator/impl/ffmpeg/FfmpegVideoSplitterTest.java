package de.slothsoft.mp4spliterator.impl.ffmpeg;

import java.io.File;

import de.slothsoft.mp4spliterator.core.VideoSplitter;
import de.slothsoft.mp4spliterator.impl.AbstractVideoSplitterTest;

public class FfmpegVideoSplitterTest extends AbstractVideoSplitterTest {

	@Override
	protected VideoSplitter createVideoSplitter() {
		return new FfmpegVideoSplitter(getFfmpegFile());
	}

	static File getFfmpegFile() {
		// absolute location of this module's target/classes folder
		final String targetClassesFolder = FfmpegVideoSplitterTest.class.getProtectionDomain().getCodeSource()
				.getLocation().getFile();
		// absolute location of this module's dummy/ folder
		// note: the code source points at target/classes sometimes and sometimes
		// directly at the module's base folder
		final File dummyFolder = targetClassesFolder.contains("classes")
				? new File(targetClassesFolder, "../../src/dummy")
				: new File(targetClassesFolder, "src/dummy");

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