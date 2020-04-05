package de.slothsoft.mp4spliterator.impl.ffmpeg;

import java.util.Collection;

import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import de.slothsoft.mp4spliterator.core.VideoSplitter;
import de.slothsoft.mp4spliterator.impl.AbstractVideoSplitterSanitizeTest;

@RunWith(Parameterized.class)
public class FfmpegVideoSplitterSanitizeTest extends AbstractVideoSplitterSanitizeTest {

	@Parameters(name = "{0}")
	public static Collection<Object[]> data() {
		return createData();
	}

	public FfmpegVideoSplitterSanitizeTest(String forbiddenSymbol) {
		super(forbiddenSymbol);
	}

	@Override
	protected VideoSplitter createVideoSplitter() {
		return new FfmpegVideoSplitter(FfmpegVideoSplitterTest.getFfmpegFile());
	}
}
