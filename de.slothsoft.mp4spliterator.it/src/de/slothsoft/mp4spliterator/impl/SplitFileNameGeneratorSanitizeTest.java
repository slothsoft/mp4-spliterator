package de.slothsoft.mp4spliterator.impl;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

/**
 * This abstract test class is used to test if the resulting files have their forbidden
 * symbols escaped.
 *
 * @since 1.2.0
 * @see AbstractVideoSplitterSanitizeTest
 */

@RunWith(Parameterized.class)
public class SplitFileNameGeneratorSanitizeTest {

	@Parameters(name = "{0}")
	public static Collection<Object[]> createData() {
		return Arrays.asList(new Object[][]{

				{"\\"},

				{"/"},

				{":"},

				{"*"},

				{"?"},

				{"\""},

				{"<"},

				{">"},

				{"|"},

				{"\n"},

				{"\r"},

				{"\t"},

		});
	}

	private final String forbiddenSymbol;

	public SplitFileNameGeneratorSanitizeTest(String forbiddenSymbol) {
		this.forbiddenSymbol = forbiddenSymbol;
	}

	@Test
	public void testSanitizeForbiddenSymbol() throws Exception {
		Assert.assertEquals("_", SplitFileNameGenerator.sanitize(this.forbiddenSymbol));
	}

	@Test
	public void testSanitizeForbiddenSymbolInMiddle() throws Exception {
		final String prefix = "abc";
		final String suffix = "123";
		Assert.assertEquals(prefix + "_" + suffix,
				SplitFileNameGenerator.sanitize(prefix + this.forbiddenSymbol + suffix));
	}

	@Test
	public void testSanitizeForbiddenSymbolInFront() throws Exception {
		final String prefix = "!�$=+#;_";
		Assert.assertEquals(prefix + "_", SplitFileNameGenerator.sanitize(prefix + this.forbiddenSymbol));
	}

	@Test
	public void testSanitizeForbiddenSymbolInBack() throws Exception {
		final String suffix = "%&()',.-";
		Assert.assertEquals("_" + suffix, SplitFileNameGenerator.sanitize(this.forbiddenSymbol + suffix));
	}
}
