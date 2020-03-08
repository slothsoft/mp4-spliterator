package de.slothsoft.mp4spliterator.impl.ffmpeg;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.UUID;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import de.slothsoft.mp4spliterator.core.Chapter;
import de.slothsoft.mp4spliterator.core.VideoSplitterConfig;

public class FfmpegVideoSplitterTest {

	private File targetFolder;

	@Before
	public void setUp() {
		this.targetFolder = new File("target/" + UUID.randomUUID().toString());
		this.targetFolder.mkdirs();
	}

	@Test
	public void testGetTargetFileName() throws Exception {
		final String fileName = UUID.randomUUID().toString();
		Assert.assertEquals(this.targetFolder + "\\" + fileName + ".mp4",
				FfmpegVideoSplitter.getTargetFileName(this.targetFolder, fileName));
	}

	@Test
	public void testGetTargetFileNameWithPart() throws Exception {
		final String fileName = UUID.randomUUID().toString();
		Assert.assertEquals(this.targetFolder + "\\pre " + fileName + ".mp4", FfmpegVideoSplitter
				.getTargetFileName(this.targetFolder, new Chapter(fileName), "pre", new VideoSplitterConfig()));
	}

	@Test
	public void testGetTargetFileNameTwice() throws Exception {
		final String fileName = UUID.randomUUID().toString();
		createDummyFile(FfmpegVideoSplitter.getTargetFileName(this.targetFolder, fileName));

		Assert.assertEquals(this.targetFolder + "\\" + fileName + " (2).mp4",
				FfmpegVideoSplitter.getTargetFileName(this.targetFolder, fileName));
	}

	static void createDummyFile(String targetFileName) throws IOException {
		Files.write(Paths.get(targetFileName), Arrays.asList("Hello World!"));
	}

	@Test
	public void testGetTargetFileNameThrice() throws Exception {
		final String fileName = UUID.randomUUID().toString();
		createDummyFile(FfmpegVideoSplitter.getTargetFileName(this.targetFolder, fileName));
		createDummyFile(FfmpegVideoSplitter.getTargetFileName(this.targetFolder, fileName));

		Assert.assertEquals(this.targetFolder + "\\" + fileName + " (3).mp4",
				FfmpegVideoSplitter.getTargetFileName(this.targetFolder, fileName));
	}

	@Test
	public void testGetTargetFileNameFourTimes() throws Exception {
		final String fileName = UUID.randomUUID().toString();
		createDummyFile(FfmpegVideoSplitter.getTargetFileName(this.targetFolder, fileName));
		createDummyFile(FfmpegVideoSplitter.getTargetFileName(this.targetFolder, fileName));
		createDummyFile(FfmpegVideoSplitter.getTargetFileName(this.targetFolder, fileName));

		Assert.assertEquals(this.targetFolder + "\\" + fileName + " (4).mp4",
				FfmpegVideoSplitter.getTargetFileName(this.targetFolder, fileName));
	}

}
