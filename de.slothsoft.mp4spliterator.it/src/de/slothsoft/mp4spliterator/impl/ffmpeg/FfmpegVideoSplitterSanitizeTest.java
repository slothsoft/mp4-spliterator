package de.slothsoft.mp4spliterator.impl.ffmpeg;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class FfmpegVideoSplitterSanitizeTest {

	@Parameters(name = "{0}")
	public static Collection<Object[]> data() {
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

	public FfmpegVideoSplitterSanitizeTest(String forbiddenSymbol) {
		this.forbiddenSymbol = forbiddenSymbol;
	}

	@Test
	public void testSanitizeForbiddenSymbol() throws Exception {
		Assert.assertEquals("_", FfmpegVideoSplitter.sanitize(this.forbiddenSymbol));
	}

	@Test
	public void testSanitizeForbiddenSymbolInMiddle() throws Exception {
		final String prefix = "abc";
		final String suffix = "123";
		Assert.assertEquals(prefix + "_" + suffix,
				FfmpegVideoSplitter.sanitize(prefix + this.forbiddenSymbol + suffix));
	}

	@Test
	public void testSanitizeForbiddenSymbolInFront() throws Exception {
		final String prefix = "!�$=+#;_";
		Assert.assertEquals(prefix + "_", FfmpegVideoSplitter.sanitize(prefix + this.forbiddenSymbol));
	}

	@Test
	public void testSanitizeForbiddenSymbolInBack() throws Exception {
		final String suffix = "%&()',.-";
		Assert.assertEquals("_" + suffix, FfmpegVideoSplitter.sanitize(this.forbiddenSymbol + suffix));
	}
}
