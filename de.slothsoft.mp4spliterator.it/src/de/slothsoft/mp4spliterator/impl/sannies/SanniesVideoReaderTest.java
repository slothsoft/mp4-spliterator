package de.slothsoft.mp4spliterator.impl.sannies;

import de.slothsoft.mp4spliterator.core.VideoReader;
import de.slothsoft.mp4spliterator.impl.AbstractVideoReaderTest;

public class SanniesVideoReaderTest extends AbstractVideoReaderTest {

	@Override
	protected VideoReader createVideoReader() {
		return new SanniesVideoReader();
	}

}
