package de.slothsoft.mp4spliterator.impl;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

/**
 * This abstract test class is used to test if the prefix of the resulting files is
 * calculated correctly.
 *
 * @since 1.2.0
 * @see AbstractVideoSplitterPrefixTest
 */

@RunWith(Parameterized.class)
public class SplitFileNameGeneratorPrefixTest {

	@Parameters(name = "{0} / {1}")
	@SuppressWarnings("boxing")
	public static Collection<Object[]> data() {
		return Arrays.asList(new Object[][]{

				{0, 0, "1"},

				{1, 5, "2"},

				{2, 9, "3"},

				{3, 10, "04"},

				{4, 99, "05"},

				{5, 100, "006"},

				{6, 999, "007"},

				{7, 1000, "0008"}

		});
	}

	private final int index;
	private final int count;
	private final String result;

	public SplitFileNameGeneratorPrefixTest(int index, int count, String result) {
		this.index = index;
		this.count = count;
		this.result = result;
	}

	@Test
	public void testGetPrefix() throws Exception {
		Assert.assertEquals("Prefix is wrong!", this.result, SplitFileNameGenerator.getPrefix(this.index, this.count));
	}

	@Test
	public void testAllNumbers() throws Exception {
		final int resultLength = this.result.length();

		for (int i = 0; i < this.count; i++) {
			final String prefix = SplitFileNameGenerator.getPrefix(i, this.count);
			Assert.assertEquals("Prefix for " + i + " is wrong!", resultLength, prefix.length());
		}
	}

}
