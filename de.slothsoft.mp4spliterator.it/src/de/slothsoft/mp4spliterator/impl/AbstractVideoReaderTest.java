package de.slothsoft.mp4spliterator.impl;

import java.io.InputStream;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import de.slothsoft.mp4spliterator.core.Video;
import de.slothsoft.mp4spliterator.core.VideoPart;
import de.slothsoft.mp4spliterator.core.VideoReader;

/**
 * This abstract test class is used to test the basic functionality of the
 * {@link VideoReader}.
 */

public abstract class AbstractVideoReaderTest {

	private VideoReader reader;

	@Before
	public void setUp() {
		this.reader = createVideoReader();
	}

	protected abstract VideoReader createVideoReader();

	@Test
	public void testWithChapters() throws Exception {
		try (InputStream input = getClass().getResourceAsStream("/files/with_chapters.mp4")) {
			final Video video = this.reader.readVideo(input);

			Assert.assertNotNull(video);
			Assert.assertEquals("unknown", video.getTitle());

			final List<VideoPart> chapters = video.getChapters();
			Assert.assertNotNull(chapters);
			Assert.assertEquals(3, chapters.size());

			final VideoPart chapter0 = chapters.get(0);
			Assert.assertEquals("Zero", chapter0.getTitle());
			Assert.assertEquals(0, chapter0.getStartTime());
			Assert.assertEquals(1000, chapter0.getEndTime());

			final VideoPart chapter1 = chapters.get(1);
			Assert.assertEquals("One", chapter1.getTitle());
			Assert.assertEquals(1000, chapter1.getStartTime());
			Assert.assertEquals(2000, chapter1.getEndTime());

			final VideoPart chapter2 = chapters.get(2);
			Assert.assertEquals("Two", chapter2.getTitle());
			Assert.assertEquals(2000, chapter2.getStartTime());
			Assert.assertEquals(5568, chapter2.getEndTime());
		}
	}

	@Test
	public void testSmall() throws Exception {
		try (InputStream input = getClass().getResourceAsStream("/files/small.mp4")) {
			final Video video = this.reader.readVideo(input);

			Assert.assertNotNull(video);
			Assert.assertEquals("unknown", video.getTitle());

			final List<VideoPart> chapters = video.getChapters();
			Assert.assertNotNull(chapters);
			Assert.assertEquals(0, chapters.size());
		}
	}

	@Test
	public void testBunny() throws Exception {
		try (InputStream input = getClass().getResourceAsStream("/files/bunny.mp4")) {
			final Video video = this.reader.readVideo(input);

			Assert.assertNotNull(video);
			Assert.assertEquals("Track", video.getTitle());

			final List<VideoPart> chapters = video.getChapters();
			Assert.assertNotNull(chapters);
			Assert.assertEquals(0, chapters.size());
		}
	}
}
