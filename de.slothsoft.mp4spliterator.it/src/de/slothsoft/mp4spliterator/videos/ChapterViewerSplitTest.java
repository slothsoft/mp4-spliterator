package de.slothsoft.mp4spliterator.videos;

import java.text.MessageFormat;
import java.util.Arrays;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Shell;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import de.slothsoft.mp4spliterator.core.Chapter;
import de.slothsoft.mp4spliterator.core.Section;
import de.slothsoft.mp4spliterator.core.VideoPart;

public class ChapterViewerSplitTest {

	private Shell shell;
	private ChapterViewer chapterViewer;

	@Before
	public void setUp() {
		this.shell = new Shell();
		this.shell.setLayout(new FillLayout());

		this.chapterViewer = new ChapterViewer(this.shell, SWT.NONE);
		this.chapterViewer.setStatusHandler(status -> {
			if (status.getException() != null) {
				status.getException().printStackTrace();
			}
			Assert.fail(status.getMessage());
		});
	}

	@After
	public void tearDown() {
		this.shell.dispose();
	}

	@Test
	public void testSplitSelectedChapters() throws Exception {
		final VideoPart chapter11 = new Chapter("A1").startTime(1).endTime(2);
		final VideoPart chapter12 = new Chapter("A2").startTime(2).endTime(3);
		final VideoPart chapter13 = new Chapter("A3").startTime(3).endTime(4);

		final Section chapter1 = new Section(chapter11);
		chapter1.addPart(chapter12);
		chapter1.addPart(chapter13);

		final VideoPart chapter2 = new Chapter("B").startTime(4).endTime(5);
		final VideoPart chapter3 = new Chapter("C").startTime(5).endTime(6);
		this.chapterViewer.setModel(Arrays.asList(chapter1, chapter2, chapter3));
		this.chapterViewer.setSelectedChapters(Arrays.asList(chapter1));

		// before
		Assert.assertEquals(Arrays.asList(chapter1), this.chapterViewer.getSelectedChapters());
		Assert.assertEquals(3, this.chapterViewer.viewer.getTree().getItemCount());
		Assert.assertEquals(3, this.chapterViewer.getModel().size());

		this.chapterViewer.splitSelectedChapters();

		// after
		Assert.assertEquals(Arrays.asList(chapter11, chapter12, chapter13), this.chapterViewer.getSelectedChapters());
		Assert.assertEquals(5, this.chapterViewer.viewer.getTree().getItemCount());
		Assert.assertEquals(5, this.chapterViewer.getModel().size());

		// this is split
		VideoPart splitPart = this.chapterViewer.getModel().get(0);
		Assert.assertNotNull(splitPart);
		Assert.assertEquals("A1", splitPart.getTitle());
		Assert.assertEquals(1, splitPart.getStartTime());
		Assert.assertEquals(2, splitPart.getEndTime());

		splitPart = this.chapterViewer.getModel().get(1);
		Assert.assertNotNull(splitPart);
		Assert.assertEquals("A2", splitPart.getTitle());
		Assert.assertEquals(2, splitPart.getStartTime());
		Assert.assertEquals(3, splitPart.getEndTime());

		splitPart = this.chapterViewer.getModel().get(2);
		Assert.assertNotNull(splitPart);
		Assert.assertEquals("A3", splitPart.getTitle());
		Assert.assertEquals(3, splitPart.getStartTime());
		Assert.assertEquals(4, splitPart.getEndTime());

		splitPart = this.chapterViewer.getModel().get(3);
		Assert.assertNotNull(splitPart);
		Assert.assertEquals("B", splitPart.getTitle());
		Assert.assertEquals(4, splitPart.getStartTime());
		Assert.assertEquals(5, splitPart.getEndTime());
	}

	@Test
	public void testValidateSplitSelectedChaptersNoSectionedChapters() throws Exception {
		setUpNoSectionedChapters();
		assertNoSectionedChapters(this.chapterViewer.validateSplitSelectedChapters());
	}

	private void setUpNoSectionedChapters() {
		final VideoPart chapter11 = new Chapter("A1").startTime(1).endTime(2);
		final VideoPart chapter12 = new Chapter("A2").startTime(2).endTime(3);
		final VideoPart chapter13 = new Chapter("A3").startTime(3).endTime(4);

		final Section chapter1 = new Section(chapter11);
		chapter1.addPart(chapter12);
		chapter1.addPart(chapter13);

		final VideoPart chapter2 = new Chapter("B").startTime(4).endTime(5);
		final VideoPart chapter3 = new Chapter("C").startTime(5).endTime(6);
		this.chapterViewer.setModel(Arrays.asList(chapter1, chapter2, chapter3));
		this.chapterViewer.setSelectedChapters(Arrays.asList(chapter2));
	}

	private static void assertNoSectionedChapters(final IStatus status) {
		Assert.assertFalse(status.isOK());
		Assert.assertEquals(MessageFormat.format(Messages.getString("ErrorNoSectionedChapters"), "B"),
				status.getMessage());
	}

	@Test
	public void testSplitSelectedChaptersNoSectionedChapters() throws Exception {
		setUpNoSectionedChapters();

		final IStatus[] status = {null};
		this.chapterViewer.statusHandler(s -> status[0] = s);

		this.chapterViewer.splitSelectedChapters();

		Assert.assertNotNull("Status handler was not called!", status[0]);
		assertNoSectionedChapters(status[0]);
	}

}
