package de.slothsoft.mp4spliterator.core;

import de.slothsoft.mp4spliterator.impl.ffmpeg.FfmpegVideoSplitter;

public interface VideoSplitter {

	static VideoSplitter createInstance() {
		return new FfmpegVideoSplitter();
	}

	void splitIntoChapters(VideoSplit videoSplit) throws VideoSplitterException;
}
