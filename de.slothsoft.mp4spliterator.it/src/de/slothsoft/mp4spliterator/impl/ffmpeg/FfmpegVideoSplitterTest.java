package de.slothsoft.mp4spliterator.impl.ffmpeg;

import java.io.File;

import org.junit.Assert;
import org.junit.Test;

import de.slothsoft.mp4spliterator.core.VideoSplitter;
import de.slothsoft.mp4spliterator.impl.AbstractVideoSplitterTest;

public class FfmpegVideoSplitterTest extends AbstractVideoSplitterTest {

	private FfmpegVideoSplitter videoSplitter;

	@Override
	protected VideoSplitter createVideoSplitter() {
		this.videoSplitter = new FfmpegVideoSplitter(getFfmpegFile()).loggingEnabled(false);
		return this.videoSplitter;
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

	@Test
	public void testSetLoggingEnabled() throws Exception {
		final boolean value = true;
		this.videoSplitter.setLoggingEnabled(value);

		Assert.assertTrue(this.videoSplitter.isLoggingEnabled());
	}

	@Test
	public void testLoggingEnabled() throws Exception {
		final boolean value = false;
		this.videoSplitter.loggingEnabled(value);

		Assert.assertFalse(this.videoSplitter.isLoggingEnabled());
	}

}