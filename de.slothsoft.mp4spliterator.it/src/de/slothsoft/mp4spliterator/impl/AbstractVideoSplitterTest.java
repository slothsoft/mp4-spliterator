package de.slothsoft.mp4spliterator.impl;

import java.io.File;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import de.slothsoft.mp4spliterator.core.Chapter;
import de.slothsoft.mp4spliterator.core.VideoSplit;
import de.slothsoft.mp4spliterator.core.VideoSplitter;
import de.slothsoft.mp4spliterator.core.VideoSplitterConfig;
import de.slothsoft.mp4spliterator.core.VideoSplitterException;

public abstract class AbstractVideoSplitterTest {

	private File targetFolder;
	private VideoSplitter splitter;

	@Before
	public void setUp() {
		this.targetFolder = new File("target/" + UUID.randomUUID().toString());
		this.targetFolder.mkdirs();

		this.splitter = createVideoSplitter();
	}

	protected abstract VideoSplitter createVideoSplitter();

	@Test
	public void testSplitIntoChapters() throws Exception {
		final Chapter chapter = new Chapter(UUID.randomUUID().toString()).startTime(1234).endTime(5678);

		splitIntoChapters(chapter);

		final File targetFile = createChapterFile(1, chapter);
		Assert.assertTrue("File should exist: " + targetFile, targetFile.exists());

		final List<String> lines = Files.readAllLines(targetFile.toPath());
		Assert.assertEquals(Arrays.asList("00:01.234 - 00:04.444"), lines);
	}

	private void splitIntoChapters(final Chapter... chapters) throws VideoSplitterException, InterruptedException {
		this.splitter.splitIntoChapters(new VideoSplit(new File(""), this.targetFolder, Arrays.asList(chapters)));
	}

	private File createChapterFile(int number, final Chapter chapter) {
		return new File(this.targetFolder, number + " " + chapter.getTitle() + ".mp4");
	}

	@Test
	public void testSplitIntoChaptersMultiple() throws Exception {
		final Chapter chapter1 = new Chapter(UUID.randomUUID().toString()).startTime(1000).endTime(2000);
		final Chapter chapter2 = new Chapter(UUID.randomUUID().toString()).startTime(3000).endTime(4000);

		splitIntoChapters(chapter1, chapter2);

		final File targetFile1 = createChapterFile(1, chapter1);
		Assert.assertTrue("File should exist: " + targetFile1, targetFile1.exists());
		final List<String> lines1 = Files.readAllLines(targetFile1.toPath());
		Assert.assertEquals(Arrays.asList("00:01.000 - 00:01.000"), lines1);

		final File targetFile2 = createChapterFile(2, chapter2);
		Assert.assertTrue("File should exist: " + targetFile2, targetFile2.exists());
		final List<String> lines2 = Files.readAllLines(targetFile2.toPath());
		Assert.assertEquals(Arrays.asList("00:03.000 - 00:01.000"), lines2);
	}

	@Test
	public void testSplitIntoChaptersWithStartTimeShift() throws Exception {
		final Chapter chapter = new Chapter(UUID.randomUUID().toString()).startTime(1234).endTime(5678);
		final VideoSplitterConfig config = new VideoSplitterConfig().startTimeShift(-1000);

		Assert.assertEquals(-1000, config.getStartTimeShift());

		splitIntoChapters(config, chapter);

		final File targetFile = createChapterFile(1, chapter);
		Assert.assertTrue("File should exist: " + targetFile, targetFile.exists());

		final List<String> lines = Files.readAllLines(targetFile.toPath());
		Assert.assertEquals(Arrays.asList("00:00.234 - 00:05.444"), lines);
	}

	private void splitIntoChapters(VideoSplitterConfig config, final Chapter... chapters)
			throws VideoSplitterException, InterruptedException {
		this.splitter.splitIntoChapters(
				new VideoSplit(new File(""), this.targetFolder, Arrays.asList(chapters)).config(config));
	}

	@Test
	public void testSplitIntoChaptersWithStartTimeShiftLessThanZero() throws Exception {
		final Chapter chapter = new Chapter(UUID.randomUUID().toString()).startTime(500).endTime(1500);
		final VideoSplitterConfig config = new VideoSplitterConfig().startTimeShift(-1000);

		Assert.assertEquals(-1000, config.getStartTimeShift());

		splitIntoChapters(config, chapter);

		final File targetFile = createChapterFile(1, chapter);
		Assert.assertTrue("File should exist: " + targetFile, targetFile.exists());

		final List<String> lines = Files.readAllLines(targetFile.toPath());
		Assert.assertEquals(Arrays.asList("00:00.000 - 00:01.500"), lines);
	}

	@Test
	public void testSplitIntoChaptersWithEndTimeShift() throws Exception {
		final Chapter chapter = new Chapter(UUID.randomUUID().toString()).startTime(1234).endTime(5678);
		final VideoSplitterConfig config = new VideoSplitterConfig().endTimeShift(1000);

		Assert.assertEquals(1000, config.getEndTimeShift());

		splitIntoChapters(config, chapter);

		final File targetFile = createChapterFile(1, chapter);
		Assert.assertTrue("File should exist: " + targetFile, targetFile.exists());

		final List<String> lines = Files.readAllLines(targetFile.toPath());
		Assert.assertEquals(Arrays.asList("00:01.234 - 00:05.444"), lines);
	}

	@Test
	public void testSplitIntoChaptersWithEndTimeShiftGreaterThanLength() throws Exception {
		final Chapter chapter = new Chapter(UUID.randomUUID().toString()).startTime(1000).endTime(3500);
		final VideoSplitterConfig config = new VideoSplitterConfig().endTimeShift(1000);

		Assert.assertEquals(1000, config.getEndTimeShift());

		this.splitter.splitIntoChapters(new VideoSplit(new File(""), this.targetFolder, Arrays.asList(chapter))
				.videoLength(4000).config(config));

		final File targetFile = createChapterFile(1, chapter);
		Assert.assertTrue("File should exist: " + targetFile, targetFile.exists());

		final List<String> lines = Files.readAllLines(targetFile.toPath());
		Assert.assertEquals(Arrays.asList("00:01.000 - 00:03.000"), lines);
	}
}