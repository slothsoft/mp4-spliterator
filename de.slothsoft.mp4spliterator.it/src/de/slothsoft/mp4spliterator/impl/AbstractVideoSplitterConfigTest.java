package de.slothsoft.mp4spliterator.impl;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import de.slothsoft.mp4spliterator.core.Chapter;
import de.slothsoft.mp4spliterator.core.VideoPart;
import de.slothsoft.mp4spliterator.core.VideoSplit;
import de.slothsoft.mp4spliterator.core.VideoSplitter;
import de.slothsoft.mp4spliterator.core.VideoSplitterConfig;

/**
 * This abstract test class is used to test if the config for the file pattern is
 * implemented correctly. Uses the tests for {@link SplitFileNameGenerator} to make sure
 * it is used.
 *
 * @since 1.2.0
 * @see SplitFileNameGeneratorPatternTest
 * @see SplitFileNameGeneratorPrefixTest
 * @see SplitFileNameGeneratorSanitizeTest
 */

public abstract class AbstractVideoSplitterConfigTest {

	public static Collection<Object[]> createData() {
		final List<Object[]> result = new ArrayList<>();
		result.addAll(createPatternData());
		result.addAll(createPrefixData());
		result.addAll(createSanitizeData());
		return result;
	}

	private static Collection<Object[]> createPatternData() {
		final List<Object[]> result = new ArrayList<>();
		for (final Object[] data : SplitFileNameGeneratorPatternTest.createData()) {
			final Chapter chapter1 = new Chapter("A").startTime(1234).endTime(5678);
			final Chapter chapter2 = new Chapter("B").startTime(1234).endTime(5678);
			final Chapter chapter3 = new Chapter("C").startTime(1234).endTime(5678);

			result.add(createDataRow("Pattern " + data[0] + " (single)",
					new VideoSplitterConfig().pattern((String) data[0]), Arrays.asList(chapter1),
					((String[]) data[1])[0]));
			result.add(createDataRow("Pattern " + data[0] + " (multi)",
					new VideoSplitterConfig().pattern((String) data[0]), Arrays.asList(chapter1, chapter2, chapter3),
					(String[]) data[1]));
		}
		return result;
	}

	private static Collection<Object[]> createPrefixData() {
		final List<Object[]> result = new ArrayList<>();

		final VideoSplitterConfig config = new VideoSplitterConfig()
				.pattern(VideoSplitterConfig.PATTERN_RUNNING_NUMBER);

		for (final Object[] possibleData : SplitFileNameGeneratorPrefixTest.data()) {
			final int count = ((Integer) possibleData[1]).intValue();
			// for file creation, the count shouldn't be to high (and not zero)
			if (count > 0 && count < 10) {
				final List<VideoPart> videoParts = createPrefixChapters(((Integer) possibleData[0]).intValue(),
						((Integer) possibleData[1]).intValue());
				result.add(createDataRow("Prefix " + possibleData[0] + " / " + possibleData[1], config, videoParts,
						(String) possibleData[2]));
			}
		}
		return result;
	}

	private static List<VideoPart> createPrefixChapters(int index, int count) {
		final Chapter[] chapters = new Chapter[count];
		for (int i = 0; i < count; i++) {
			chapters[i] = new Chapter(UUID.randomUUID().toString());

			if (i == index) {
				chapters[i].startTime(1234).endTime(5678);
			}
		}
		return Arrays.asList(chapters);
	}

	private static Collection<Object[]> createSanitizeData() {
		final List<Object[]> result = new ArrayList<>();

		final VideoSplitterConfig config = new VideoSplitterConfig().pattern(VideoSplitterConfig.PATTERN_CHAPTER_TITLE);

		for (final Object[] data : SplitFileNameGeneratorSanitizeTest.createData()) {
			final Chapter chapter = new Chapter("abc" + data[0] + "def").startTime(1234).endTime(5678);
			result.add(createDataRow("Sanitize " + data[0], config, Arrays.asList(chapter), "abc_def"));
		}
		return result;
	}

	private static Object[] createDataRow(String displayName, VideoSplitterConfig config, List<VideoPart> videoParts,
			String... expectedFileNames) {
		return new Object[]{displayName, config, videoParts, expectedFileNames};
	}

	private final VideoSplitterConfig config;
	private final List<VideoPart> videoParts;
	private final String[] expectedFileNames;

	private File targetFolder;
	private VideoSplitter splitter;

	public AbstractVideoSplitterConfigTest(@SuppressWarnings("unused") String displayName, VideoSplitterConfig config,
			List<VideoPart> videoParts, String... expectedFileNames) {
		this.config = config;
		this.videoParts = videoParts;
		this.expectedFileNames = expectedFileNames;
	}

	@Before
	public void setUp() {
		this.targetFolder = AbstractVideoSplitterTest.createTargetFolder();

		this.splitter = createVideoSplitter();
	}

	protected abstract VideoSplitter createVideoSplitter();

	@Test
	public void testConfig() throws Exception {
		final VideoSplit videoSplit = new VideoSplit(new File("source.mp4"), this.targetFolder, this.videoParts)
				.config(this.config);
		this.splitter.splitIntoChapters(videoSplit);

		for (final String expectedFileName : this.expectedFileNames) {
			final File targetFile = new File(this.targetFolder, expectedFileName + ".mp4");
			Assert.assertTrue("File should exist: " + targetFile, targetFile.exists());

			final List<String> lines = Files.readAllLines(targetFile.toPath());
			Assert.assertEquals(Arrays.asList("00:01.234 - 00:04.444"), lines);
		}
	}
}