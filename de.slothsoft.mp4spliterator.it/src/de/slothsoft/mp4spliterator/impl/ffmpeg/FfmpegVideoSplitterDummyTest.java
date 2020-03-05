package de.slothsoft.mp4spliterator.impl.ffmpeg;

import java.io.File;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
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
	public void testSplitIntoChapters() throws Exception {
		final Chapter chapter = new Chapter(UUID.randomUUID().toString()).startTime(1234).endTime(5678);

		this.splitter.splitIntoChapters(new File(""), this.targetFolder, Arrays.asList(chapter));

		final File targetFile = new File(this.targetFolder, "1 " + chapter.getTitle() + ".mp4");
		Assert.assertTrue("File should exist: " + targetFile, targetFile.exists());

		final List<String> lines = Files.readAllLines(targetFile.toPath());
		Assert.assertEquals(Arrays.asList("00:01.234 - 00:05.444"), lines);
	}

	@Test
	public void testSplitIntoChaptersMultiple() throws Exception {
		final Chapter chapter1 = new Chapter(UUID.randomUUID().toString()).startTime(1000).endTime(2000);
		final Chapter chapter2 = new Chapter(UUID.randomUUID().toString()).startTime(3000).endTime(4000);

		this.splitter.splitIntoChapters(new File(""), this.targetFolder, Arrays.asList(chapter1, chapter2));

		final File targetFile1 = new File(this.targetFolder, "1 " + chapter1.getTitle() + ".mp4");
		Assert.assertTrue("File should exist: " + targetFile1, targetFile1.exists());
		final List<String> lines1 = Files.readAllLines(targetFile1.toPath());
		Assert.assertEquals(Arrays.asList("00:01.000 - 00:02.000"), lines1);

		final File targetFile2 = new File(this.targetFolder, "2 " + chapter2.getTitle() + ".mp4");
		Assert.assertTrue("File should exist: " + targetFile2, targetFile2.exists());
		final List<String> lines2 = Files.readAllLines(targetFile2.toPath());
		Assert.assertEquals(Arrays.asList("00:03.000 - 00:02.000"), lines2);
	}
}