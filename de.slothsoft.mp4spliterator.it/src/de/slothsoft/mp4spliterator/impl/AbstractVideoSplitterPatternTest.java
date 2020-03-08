package de.slothsoft.mp4spliterator.impl;

import java.io.File;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Collection;
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

public abstract class AbstractVideoSplitterPatternTest {

	public static Collection<Object[]> createData() {
		return Arrays.asList(new Object[][]{

				{VideoSplitterConfig.PATTERN_RUNNING_NUMBER, "1"},

				{VideoSplitterConfig.PATTERN_CHAPTER_TITLE, "A"}

		});
	}

	private final String pattern;
	private final String expectedFileName;

	private File targetFolder;
	private VideoSplitter splitter;

	public AbstractVideoSplitterPatternTest(String pattern, String expectedFileName) {
		this.pattern = pattern;
		this.expectedFileName = expectedFileName;
	}

	@Before
	public void setUp() {
		this.targetFolder = new File("target/" + UUID.randomUUID().toString());
		this.targetFolder.mkdirs();

		this.splitter = createVideoSplitter();
	}

	protected abstract VideoSplitter createVideoSplitter();

	@Test
	public void testSplitIntoChaptersWithEndTimeShift() throws Exception {
		final Chapter chapter = new Chapter("A").startTime(1234).endTime(5678);

		final VideoSplitterConfig config = new VideoSplitterConfig().pattern(this.pattern);
		Assert.assertEquals(this.pattern, config.getPattern());

		splitIntoChapters(config, chapter);

		final File targetFile = new File(this.targetFolder, this.expectedFileName + ".mp4");
		Assert.assertTrue("File should exist: " + targetFile, targetFile.exists());

		final List<String> lines = Files.readAllLines(targetFile.toPath());
		Assert.assertEquals(Arrays.asList("00:01.234 - 00:04.444"), lines);
	}

	private void splitIntoChapters(VideoSplitterConfig config, final Chapter... chapters)
			throws VideoSplitterException {
		this.splitter.splitIntoChapters(
				new VideoSplit(new File(""), this.targetFolder, Arrays.asList(chapters)).config(config));
	}

	// TODO: test second file as well?

}