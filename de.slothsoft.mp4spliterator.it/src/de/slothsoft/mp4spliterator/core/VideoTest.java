package de.slothsoft.mp4spliterator.core;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.junit.Assert;
import org.junit.Test;

public class VideoTest {

	private final Video classUnderTest = new Video("");

	@Test
	public void testSetTitle() throws Exception {
		final String value = UUID.randomUUID().toString();
		this.classUnderTest.setTitle(value);

		Assert.assertEquals(value, this.classUnderTest.getTitle());
	}

	@Test
	public void testTitle() throws Exception {
		final String value = UUID.randomUUID().toString();
		this.classUnderTest.title(value);

		Assert.assertEquals(value, this.classUnderTest.getTitle());
	}

	@Test
	public void testSetLength() throws Exception {
		final long value = 42l;
		this.classUnderTest.setLength(value);

		Assert.assertEquals(value, this.classUnderTest.getLength());
	}

	@Test
	public void testLength() throws Exception {
		final long value = 13l;
		this.classUnderTest.length(value);

		Assert.assertEquals(value, this.classUnderTest.getLength());
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
}
