package de.slothsoft.mp4spliterator.impl;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import de.slothsoft.mp4spliterator.core.Chapter;
import de.slothsoft.mp4spliterator.core.VideoSplit;
import de.slothsoft.mp4spliterator.core.VideoSplitter;
import de.slothsoft.mp4spliterator.core.VideoSplitterException;

public abstract class AbstractVideoSplitterProgressMonitorTest {

	private File targetFolder;
	private LogProgressMonitor progressMonitor;
	private VideoSplitter splitter;

	@Before
	public void setUp() {
		this.targetFolder = AbstractVideoSplitterTest.createTargetFolder();

		this.progressMonitor = new LogProgressMonitor();

		this.splitter = createVideoSplitter();
	}

	protected abstract VideoSplitter createVideoSplitter();

	protected abstract String getTaskName();

	@Test
	public void testSplitIntoChapters() throws Exception {
		final Chapter chapter = new Chapter(UUID.randomUUID().toString()).startTime(1234).endTime(5678);

		splitIntoChapters(chapter);

		final List<String> log = Arrays.asList( //
				"TASK: " + getTaskName() + " (1)", //
				"SUBTASK: " + chapter.getTitle() + " (1/1)", //
				"WORKED: 1", //
				"DONE." //
		);
		assertEquals(log, this.progressMonitor.getLog());
	}

	private void splitIntoChapters(final Chapter... chapters) throws VideoSplitterException, InterruptedException {
		this.splitter
				.splitIntoChapters(new VideoSplit(new File("source.mp4"), this.targetFolder, Arrays.asList(chapters))
						.progressMonitor(this.progressMonitor));
	}

	private static void assertEquals(List<String> expected, List<String> actual) {
		final int size = expected.size();
		Assert.assertEquals(expected.stream().collect(Collectors.joining(",")) + " >< "
				+ actual.stream().collect(Collectors.joining(",")), size, actual.size());

		for (int i = 0; i < size; i++) {
			Assert.assertEquals(i + " is wrong!", expected.get(i), actual.get(i));
		}
	}

	@Test
	public void testSplitIntoChaptersMultiple() throws Exception {
		final Chapter chapter1 = new Chapter(UUID.randomUUID().toString()).startTime(1000).endTime(2000);
		final Chapter chapter2 = new Chapter(UUID.randomUUID().toString()).startTime(3000).endTime(4000);

		splitIntoChapters(chapter1, chapter2);

		final List<String> log = Arrays.asList( //
				"TASK: " + getTaskName() + " (2)", //
				"SUBTASK: " + chapter1.getTitle() + " (1/2)", //
				"WORKED: 1", //
				"SUBTASK: " + chapter2.getTitle() + " (2/2)", //
				"WORKED: 1", //
				"DONE." //
		);
		assertEquals(log, this.progressMonitor.getLog());
	}

}