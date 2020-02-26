package de.slothsoft.mp4spliterator.impl.sannies;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.mp4parser.Box;
import org.mp4parser.Container;
import org.mp4parser.IsoFile;
import org.mp4parser.boxes.UnknownBox;
import org.mp4parser.boxes.apple.AppleNameBox;
import org.mp4parser.boxes.iso14496.part12.MovieHeaderBox;
import org.mp4parser.tools.IsoTypeReader;

import de.slothsoft.mp4spliterator.core.Chapter;
import de.slothsoft.mp4spliterator.core.Video;
import de.slothsoft.mp4spliterator.core.VideoReader;

/**
 * An implementation of {@link VideoReader} using
 * <a href="https://github.com/sannies/mp4parser">sannies mp4parser</a>.
 *
 * @author Stef Schulz
 * @since 1.0.0
 */

public class SanniesVideoReader implements VideoReader {

	private UnknownBox chapterBox;
	private long timescale;

	@Override
	public Set<String> getSupportedExtensions() {
		return new TreeSet<>(Arrays.asList("mp4"));
	}

	@Override
	public Video readVideo(InputStream input) throws IOException {
		final Video result = new Video("unknown");

		this.chapterBox = null;
		this.timescale = 1;

		try (final IsoFile isoFile = new IsoFile(((FileInputStream) input).getChannel())) {
			traverseContainer(isoFile, result);
		}
		if (this.chapterBox != null) {
			readChapters(result, this.chapterBox, this.timescale);
		}
		return result;
	}

	private void traverseContainer(Container container, Video video) {
		for (final Box box : container.getBoxes()) {
			if (box.getType().equals("chpl")) {
				this.chapterBox = (UnknownBox) box;
			}
			if (box instanceof AppleNameBox) {
				readName(video, (AppleNameBox) box);
			}
			if (box instanceof MovieHeaderBox) {
				this.timescale = ((MovieHeaderBox) box).getTimescale();
				readMovieHeader(video, (MovieHeaderBox) box);
			}

			// recursion

			if (box instanceof Container) {
				traverseContainer((Container) box, video);
			}
		}
	}

	private static void readChapters(Video video, UnknownBox box, long timescale) {
		if (!box.isParsed()) {
			box.parseDetails();
		}

		final ByteBuffer buffer = box.getData();
		buffer.rewind();

		// boxheader

		final long totalSize = IsoTypeReader.readUInt32(buffer);
		IsoTypeReader.readUInt32(buffer); // boxType

		if (totalSize == 1) {
			IsoTypeReader.readUInt64(buffer); // extendedTotalSize
		}

		// read bytes

		final int count = IsoTypeReader.readUInt8(buffer);
		final List<Chapter> chapters = new ArrayList<>(count);

		Chapter lastChapter = null;

		for (int i = 0; i < count; i++) {
			final long timestamp = IsoTypeReader.readUInt64(buffer);

			final int titleSize = IsoTypeReader.readUInt8(buffer);
			final String title = IsoTypeReader.readString(buffer, titleSize);

			final long time = timestamp / timescale / 10;

			if (lastChapter != null) {
				lastChapter.setEndTime(time);
			}
			lastChapter = new Chapter(title).startTime(time);
			chapters.add(lastChapter);
		}
		if (lastChapter != null) {
			lastChapter.setEndTime(video.getLength());
		}
		video.setChapters(chapters);
	}

	static long getInt(ByteBuffer buffer) {
		final byte[] bytes = new byte[8];
		buffer.get(bytes);
		return ByteBuffer.wrap(bytes).getLong();
	}

	private static void readName(Video video, AppleNameBox nameBox) {
		video.setTitle(nameBox.getValue());
	}

	private static void readMovieHeader(Video video, MovieHeaderBox box) {
		video.setLength(box.getDuration());
	}

}
