package de.slothsoft.mp4spliterator.impl.ffmpeg;

import java.io.File;
import java.util.Collection;

import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import de.slothsoft.mp4spliterator.core.VideoSplitter;
import de.slothsoft.mp4spliterator.impl.AbstractVideoSplitterPatternTest;

@RunWith(Parameterized.class)
public class FfmpegVideoSplitterDummyPatternTest extends AbstractVideoSplitterPatternTest {

	@Parameters(name = "{0}")
	public static Collection<Object[]> data() {
		return createData();
	}

	public FfmpegVideoSplitterDummyPatternTest(String pattern, String expectedFileName) {
		super(pattern, expectedFileName);
	}

	@Override
	protected VideoSplitter createVideoSplitter() {
		return new FfmpegVideoSplitter(new File("src/dummy/ffmpeg.bat"));
	}
}