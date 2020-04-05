package de.slothsoft.mp4spliterator.impl.ffmpeg;

import java.util.Collection;

import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import de.slothsoft.mp4spliterator.core.VideoSplitter;
import de.slothsoft.mp4spliterator.impl.AbstractVideoSplitterPrefixTest;

@RunWith(Parameterized.class)
public class FfmpegVideoSplitterPrefixTest extends AbstractVideoSplitterPrefixTest {

	@Parameters(name = "{0} / {1}")
	public static Collection<Object[]> data() {
		return AbstractVideoSplitterPrefixTest.createData();
	}

	public FfmpegVideoSplitterPrefixTest(int index, int count, String result) {
		super(index, count, result);
	}

	@Override
	protected VideoSplitter createVideoSplitter() {
		return new FfmpegVideoSplitter(FfmpegVideoSplitterTest.getFfmpegFile());
	}

}
