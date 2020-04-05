package de.slothsoft.mp4spliterator.impl;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import de.slothsoft.mp4spliterator.core.Chapter;
import de.slothsoft.mp4spliterator.core.VideoSplitterConfig;

/**
 * This abstract test class is used to test if the config for the file pattern is
 * implemented correctly.
 *
 * @since 1.2.0
 * @see AbstractVideoSplitterPatternTest
 */

@RunWith(Parameterized.class)
public class SplitFileNameGeneratorPatternTest {

	@Parameters(name = "{0}")
	public static Collection<Object[]> createData() {
		return Arrays.asList(new Object[][]{

				{VideoSplitterConfig.PATTERN_RUNNING_NUMBER, "1"},

				{VideoSplitterConfig.PATTERN_CHAPTER_TITLE, "A"}

		});
	}

	private final String pattern;
	private final String expectedFileName;

	private File targetFolder;
	private SplitFileNameGenerator generator;

	public SplitFileNameGeneratorPatternTest(String pattern, String expectedFileName) {
		this.pattern = pattern;
		this.expectedFileName = expectedFileName;
	}

	@Before
	public void setUp() {
		this.targetFolder = AbstractVideoSplitterTest.createTargetFolder();

		this.generator = new SplitFileNameGenerator(new VideoSplitterConfig().pattern(this.pattern), this.targetFolder);
	}

	@Test
	public void testPatternUsed() throws Exception {
		final Chapter chapter = new Chapter("A").startTime(1234).endTime(5678);

		final List<String> fileNames = this.generator.createFileNames(Arrays.asList(chapter));

		Assert.assertNotNull(fileNames);
		Assert.assertEquals(1, fileNames.size());
		Assert.assertEquals(new File(this.targetFolder, this.expectedFileName + ".mp4"), new File(fileNames.get(0)));
	}

}