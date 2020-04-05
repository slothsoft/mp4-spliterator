package de.slothsoft.mp4spliterator.impl;

import java.io.File;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import de.slothsoft.mp4spliterator.core.Chapter;
import de.slothsoft.mp4spliterator.core.VideoSplit;
import de.slothsoft.mp4spliterator.core.VideoSplitter;
import de.slothsoft.mp4spliterator.core.VideoSplitterConfig;
import de.slothsoft.mp4spliterator.core.VideoSplitterException;

/**
 * This abstract test class is used to test if the resulting files have their forbidden
 * symbols escaped.
 *
 * @since 1.2.0
 * @see SplitFileNameGeneratorSanitizeTest
 */

public abstract class AbstractVideoSplitterSanitizeTest {

	public static Collection<Object[]> createData() {
		return SplitFileNameGeneratorSanitizeTest.data();
	}

	private final String forbiddenSymbol;

	private File targetFolder;
	private VideoSplitter splitter;

	public AbstractVideoSplitterSanitizeTest(String forbiddenSymbol) {
		this.forbiddenSymbol = forbiddenSymbol;
	}

	@Before
	public void setUp() {
		this.targetFolder = AbstractVideoSplitterTest.createTargetFolder();

		this.splitter = createVideoSplitter();
	}

	protected abstract VideoSplitter createVideoSplitter();

	@Test
	public void testSplitIntoChaptersWithEndTimeShift() throws Exception {
		final Chapter chapter = new Chapter("abc" + this.forbiddenSymbol + "def").startTime(1234).endTime(5678);

		final VideoSplitterConfig config = new VideoSplitterConfig().pattern(VideoSplitterConfig.PATTERN_CHAPTER_TITLE);

		splitIntoChapters(config, chapter);

		final File targetFile = new File(this.targetFolder, "abc_def.mp4");
		Assert.assertTrue("File should exist: " + targetFile, targetFile.exists());

		final List<String> lines = Files.readAllLines(targetFile.toPath());
		Assert.assertEquals(Arrays.asList("00:01.234 - 00:04.444"), lines);
	}

	private void splitIntoChapters(VideoSplitterConfig config, final Chapter... chapters)
			throws VideoSplitterException, InterruptedException {
		this.splitter.splitIntoChapters(
				new VideoSplit(new File("source.mp4"), this.targetFolder, Arrays.asList(chapters)).config(config));
	}
}
