package de.slothsoft.mp4spliterator.core;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.junit.Assert;
import org.junit.Test;

public class VideoSplitTest {

	private final VideoSplit classUnderTest = new VideoSplit(new File("source"), new File("target"), new ArrayList<>());

	@Test
	public void testSetSourceFile() throws Exception {
		final File value = new File(UUID.randomUUID().toString());
		this.classUnderTest.setSourceFile(value);

		Assert.assertEquals(value, this.classUnderTest.getSourceFile());
	}

	@Test
	public void testSourceFile() throws Exception {
		final File value = new File(UUID.randomUUID().toString());
		this.classUnderTest.sourceFile(value);

		Assert.assertEquals(value, this.classUnderTest.getSourceFile());
	}

	@Test
	public void testSetTargetFolder() throws Exception {
		final File value = new File(UUID.randomUUID().toString());
		this.classUnderTest.setTargetFolder(value);

		Assert.assertEquals(value, this.classUnderTest.getTargetFolder());
	}

	@Test
	public void testTargetFolder() throws Exception {
		final File value = new File(UUID.randomUUID().toString());
		this.classUnderTest.targetFolder(value);

		Assert.assertEquals(value, this.classUnderTest.getTargetFolder());
	}

	@Test
	public void testSetVideoLength() throws Exception {
		final long value = 42l;
		this.classUnderTest.setVideoLength(value);

		Assert.assertEquals(value, this.classUnderTest.getVideoLength());
	}

	@Test
	public void testVideoLength() throws Exception {
		final long value = 13l;
		this.classUnderTest.videoLength(value);

		Assert.assertEquals(value, this.classUnderTest.getVideoLength());
	}

	@Test
	public void testSetChapters() throws Exception {
		final List<VideoPart> value = Arrays.asList(new Chapter(""));
		this.classUnderTest.setChapters(value);

		Assert.assertEquals(value, this.classUnderTest.getChapters());
	}

	@Test
	public void testChapters() throws Exception {
		final List<VideoPart> value = Arrays.asList(new Chapter(""));
		this.classUnderTest.chapters(value);

		Assert.assertEquals(value, this.classUnderTest.getChapters());
	}

	@Test
	public void testSetConfig() throws Exception {
		final VideoSplitterConfig value = new VideoSplitterConfig().startTimeShift(1234);
		this.classUnderTest.setConfig(value);

		Assert.assertEquals(value, this.classUnderTest.getConfig());
	}

	@Test
	public void testConfig() throws Exception {
		final VideoSplitterConfig value = new VideoSplitterConfig().startTimeShift(1234);
		this.classUnderTest.config(value);

		Assert.assertEquals(value, this.classUnderTest.getConfig());
	}

	@Test
	public void testSetProgressMonitor() throws Exception {
		final IProgressMonitor value = new NullProgressMonitor();
		this.classUnderTest.setProgressMonitor(value);

		Assert.assertEquals(value, this.classUnderTest.getProgressMonitor());
	}

	@Test
	public void testProgressMonitor() throws Exception {
		final IProgressMonitor value = new NullProgressMonitor();
		this.classUnderTest.progressMonitor(value);

		Assert.assertEquals(value, this.classUnderTest.getProgressMonitor());
	}

}
