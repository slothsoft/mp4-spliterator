package de.slothsoft.mp4spliterator.impl.ffmpeg;

import java.util.Collection;
import java.util.List;

import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import de.slothsoft.mp4spliterator.core.VideoPart;
import de.slothsoft.mp4spliterator.core.VideoSplitter;
import de.slothsoft.mp4spliterator.core.VideoSplitterConfig;
import de.slothsoft.mp4spliterator.impl.AbstractVideoSplitterConfigTest;

@RunWith(Parameterized.class)
public class FfmpegVideoSplitterConfigTest extends AbstractVideoSplitterConfigTest {

	@Parameters(name = "{0}")
	public static Collection<Object[]> data() {
		return createData();
	}

	public FfmpegVideoSplitterConfigTest(String displayName, VideoSplitterConfig config, List<VideoPart> videoParts,
			String... expectedFileNames) {
		super(displayName, config, videoParts, expectedFileNames);
	}

	@Override
	protected VideoSplitter createVideoSplitter() {
		return new FfmpegVideoSplitter(FfmpegVideoSplitterTest.getFfmpegFile());
	}
}