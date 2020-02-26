package de.slothsoft.mp4spliterator.core;

import java.io.File;
import java.util.List;

import de.slothsoft.mp4spliterator.impl.ffmpeg.FfmpegVideoSplitter;

public interface VideoSplitter {

	static VideoSplitter createInstance() {
		return new FfmpegVideoSplitter();
	}

	void splitIntoChapters(File input, File targetFolder, List<Chapter> chapters) throws VideoSplitterException;
}
