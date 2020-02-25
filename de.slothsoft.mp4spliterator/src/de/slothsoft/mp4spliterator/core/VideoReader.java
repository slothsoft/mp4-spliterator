package de.slothsoft.mp4spliterator.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

import de.slothsoft.mp4spliterator.impl.sannies.SanniesVideoReader;

public interface VideoReader {

	/**
	 * Gets extensions of all possible video readers.
	 *
	 * @return a set of extensions
	 */

	static Set<String> getAllSupportedExtensions() {
		return new SanniesVideoReader().getSupportedExtensions();
	}

	/**
	 * Delegates a file to a specific reader to be read.
	 *
	 * @param input an input file
	 * @return a video
	 * @throws IOException for any kind of exception
	 */

	static Video readVideo(File input) throws IOException {
		final SanniesVideoReader videoReader = new SanniesVideoReader();
		try (InputStream inputStream = new FileInputStream(input)) {
			return videoReader.readVideo(inputStream);
		}
	}

	/**
	 * Gets the extension this video reader can open.
	 *
	 * @return a set of extensions
	 */

	Set<String> getSupportedExtensions();

	/**
	 * Reads an input stream.
	 *
	 * @param input an input stream
	 * @return a video
	 * @throws IOException for any kind of exception
	 */

	Video readVideo(InputStream input) throws IOException;
}
