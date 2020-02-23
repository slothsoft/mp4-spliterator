package de.slothsoft.mp4spliterator.impl.sannies;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
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

import de.slothsoft.mp4spliterator.core.Chapter;
import de.slothsoft.mp4spliterator.core.Video;
import de.slothsoft.mp4spliterator.core.VideoReader;

public class SanniesVideoReader implements VideoReader {

	@Override
	public Set<String> getSupportedExtensions() {
		return new TreeSet<>(Arrays.asList("mp4"));
	}

	@Override
	public Video readVideo(InputStream input) throws IOException {
		final Video result = new Video("unknown");

		try (final IsoFile isoFile = new IsoFile(((FileInputStream) input).getChannel())) {
			traverseContainer(isoFile, result);
		}
		return result;
	}

	private void traverseContainer(Container container, Video video) {
		for (final Box box : container.getBoxes()) {
			if (box.getType().equals("chpl")) {
				readChapters(video, (UnknownBox) box);
			}
			if (box instanceof AppleNameBox) {
				readName(video, (AppleNameBox) box);
			}

			// recursion

			if (box instanceof Container) {
				traverseContainer((Container) box, video);
			}
		}
	}

	private static void readChapters(Video video, UnknownBox box) {
		if (!box.isParsed()) {
			box.parseDetails();
		}

		final ByteBuffer buffer = box.getData();
		buffer.rewind();

		// boxheader

		final int totalSize = buffer.getInt();
		buffer.getInt(); // boxType

		if (totalSize == 1) {
			buffer.getLong(); // extendedTotalSize
		}

		// read bytes

		final int count = buffer.get();
		final List<Chapter> chapters = new ArrayList<>(count);

		for (int i = 0; i < count; i++) {
			buffer.getInt();
			final long timestamp = buffer.getInt();
			final int titleSize = buffer.get();

			final byte[] bytes = new byte[titleSize];
			buffer.get(bytes);
			final String title = new String(bytes, StandardCharsets.UTF_8);

			chapters.add(new Chapter(title).startTime(timestamp));
		}
		video.setChapters(chapters);
	}

	private static void readName(Video video, AppleNameBox nameBox) {
		video.setTitle(nameBox.getValue());
	}

}
