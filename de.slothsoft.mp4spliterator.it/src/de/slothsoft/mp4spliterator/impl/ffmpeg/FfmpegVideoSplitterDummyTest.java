package de.slothsoft.mp4spliterator.impl.ffmpeg;

import java.io.File;
import java.util.Arrays;
import java.util.UUID;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import de.slothsoft.mp4spliterator.core.Chapter;

public class FfmpegVideoSplitterDummyTest {

	private File targetFolder;
	private File dummyFile;
	private FfmpegVideoSplitter splitter;

	@Before
	public void setUp() {
		this.targetFolder = new File("target/" + UUID.randomUUID().toString());
		this.targetFolder.mkdirs();

		this.dummyFile = new File("src/dummy/ffmpeg.bat");

		this.splitter = new FfmpegVideoSplitter(this.dummyFile);
	}

	@Test
	public void testGetTargetFileName() throws Exception {
		final String chapterName = UUID.randomUUID().toString();
		this.splitter.splitIntoChapters(new File(""), this.targetFolder, Arrays.asList(new Chapter(chapterName)));

		final File targetFile = new File(this.targetFolder, "1 " + chapterName + ".mp4");
		Assert.assertTrue("File should exist: " + targetFile, targetFile.exists());
	}
}