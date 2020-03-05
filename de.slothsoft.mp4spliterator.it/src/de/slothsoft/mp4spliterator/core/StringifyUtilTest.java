package de.slothsoft.mp4spliterator.core;

import org.junit.Assert;
import org.junit.Test;

public class StringifyUtilTest {

	@Test
	public void testStringifyTime() throws Exception {
		Assert.assertEquals("01:23", StringifyUtil.stringifyTime((1 * 60 + 23) * 1000));
	}

	@Test
	public void testStringifyTimeWithMiliSeconds() throws Exception {
		Assert.assertEquals("01:23.456", StringifyUtil.stringifyTimeWithMiliSeconds((1 * 60 + 23) * 1000 + 456));
	}
}
