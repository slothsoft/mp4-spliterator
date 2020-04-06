package de.slothsoft.mp4spliterator.core;

import java.util.UUID;

import org.junit.Assert;
import org.junit.Test;

public class ChapterTest {

	private final Chapter classUnderTest = new Chapter("");

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
	public void testSetStartTime() throws Exception {
		final long value = 42l;
		this.classUnderTest.setStartTime(value);

		Assert.assertEquals(value, this.classUnderTest.getStartTime());
	}

	@Test
	public void testStartTime() throws Exception {
		final long value = 13l;
		this.classUnderTest.startTime(value);

		Assert.assertEquals(value, this.classUnderTest.getStartTime());
	}

	@Test
	public void testSetEndTime() throws Exception {
		final long value = 42l;
		this.classUnderTest.setEndTime(value);

		Assert.assertEquals(value, this.classUnderTest.getEndTime());
	}

	@Test
	public void testEndTime() throws Exception {
		final long value = 13l;
		this.classUnderTest.endTime(value);

		Assert.assertEquals(value, this.classUnderTest.getEndTime());
	}

}
