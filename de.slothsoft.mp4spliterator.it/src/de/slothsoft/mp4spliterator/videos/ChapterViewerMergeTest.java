package de.slothsoft.mp4spliterator.videos;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.HashSet;

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

public class ChapterViewerMergeTest {

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
	public void testMergeSelectedChapters() throws Exception {
		final VideoPart chapter1 = new Chapter("A").startTime(1).endTime(2);
		final VideoPart chapter2 = new Chapter("B").startTime(2).endTime(3);
		final VideoPart chapter3 = new Chapter("C").startTime(3).endTime(4);
		this.chapterViewer.setModel(Arrays.asList(chapter1, chapter2, chapter3));
		this.chapterViewer.setSelectedChapters(Arrays.asList(chapter1, chapter2));

		// before
		Assert.assertEquals(Arrays.asList(chapter1, chapter2), this.chapterViewer.getSelectedChapters());
		Assert.assertEquals(3, this.chapterViewer.viewer.getTree().getItemCount());
		Assert.assertEquals(3, this.chapterViewer.getModel().size());

		this.chapterViewer.mergeSelectedChapters();

		// after
		Assert.assertEquals(Arrays.asList(chapter1, chapter2), this.chapterViewer.getSelectedChapters());
		Assert.assertEquals(2, this.chapterViewer.viewer.getTree().getItemCount());
		Assert.assertEquals(2, this.chapterViewer.getModel().size());

		// this is merged
		final VideoPart mergedPart = this.chapterViewer.getModel().get(0);
		Assert.assertNotNull(mergedPart);
		Assert.assertEquals("A, B", mergedPart.getTitle());
		Assert.assertEquals(1, mergedPart.getStartTime());
		Assert.assertEquals(3, mergedPart.getEndTime());
	}

	@Test
	public void testMergeSelectedChaptersOneSection() throws Exception {
		final VideoPart chapter11 = new Chapter("A1").startTime(1).endTime(2);
		final VideoPart chapter12 = new Chapter("A2").startTime(2).endTime(3);
		final VideoPart chapter13 = new Chapter("A3").startTime(3).endTime(4);

		final Section chapter1 = new Section(chapter11);
		chapter1.addPart(chapter12);
		chapter1.addPart(chapter13);

		final VideoPart chapter2 = new Chapter("B").startTime(4).endTime(5);
		final VideoPart chapter3 = new Chapter("C").startTime(5).endTime(6);
		this.chapterViewer.setModel(Arrays.asList(chapter1, chapter2, chapter3));
		this.chapterViewer.setSelectedChapters(Arrays.asList(chapter1, chapter2));

		// before
		Assert.assertEquals(Arrays.asList(chapter1, chapter2), this.chapterViewer.getSelectedChapters());
		Assert.assertEquals(3, this.chapterViewer.viewer.getTree().getItemCount());
		Assert.assertEquals(3, this.chapterViewer.getModel().size());

		this.chapterViewer.mergeSelectedChapters();

		// after
		Assert.assertEquals(Arrays.asList(chapter1, chapter2), this.chapterViewer.getSelectedChapters());
		Assert.assertEquals(2, this.chapterViewer.viewer.getTree().getItemCount());
		Assert.assertEquals(2, this.chapterViewer.getModel().size());

		// this is merged
		final VideoPart mergedPart = this.chapterViewer.getModel().get(0);
		Assert.assertNotNull(mergedPart);
		Assert.assertEquals("A1, A2, A3, B", mergedPart.getTitle());
		Assert.assertEquals(1, mergedPart.getStartTime());
		Assert.assertEquals(5, mergedPart.getEndTime());

		Assert.assertEquals(4, chapter1.getParts().length);
		Assert.assertEquals(chapter2, chapter1.getParts()[3]);
	}

	@Test
	public void testMergeSelectedChaptersOneSectionLater() throws Exception {
		final VideoPart chapter1 = new Chapter("A").startTime(0).endTime(1);

		final VideoPart chapter21 = new Chapter("B1").startTime(1).endTime(2);
		final VideoPart chapter22 = new Chapter("B2").startTime(2).endTime(3);
		final VideoPart chapter23 = new Chapter("B3").startTime(3).endTime(4);

		final Section chapter2 = new Section(chapter21);
		chapter2.addPart(chapter22);
		chapter2.addPart(chapter23);

		final VideoPart chapter3 = new Chapter("C").startTime(5).endTime(6);
		this.chapterViewer.setModel(Arrays.asList(chapter1, chapter2, chapter3));
		this.chapterViewer.setSelectedChapters(Arrays.asList(chapter1, chapter2));

		// before
		Assert.assertEquals(new HashSet<>(Arrays.asList(chapter1, chapter2)),
				new HashSet<>(this.chapterViewer.getSelectedChapters()));
		Assert.assertEquals(3, this.chapterViewer.viewer.getTree().getItemCount());
		Assert.assertEquals(3, this.chapterViewer.getModel().size());

		this.chapterViewer.mergeSelectedChapters();

		// after
		Assert.assertEquals(Arrays.asList(chapter2, chapter1), this.chapterViewer.getSelectedChapters());
		Assert.assertEquals(2, this.chapterViewer.viewer.getTree().getItemCount());
		Assert.assertEquals(2, this.chapterViewer.getModel().size());

		// this is merged
		final VideoPart mergedPart = this.chapterViewer.getModel().get(0);
		Assert.assertNotNull(mergedPart);
		Assert.assertEquals("A, B1, B2, B3", mergedPart.getTitle());
		Assert.assertEquals(0, mergedPart.getStartTime());
		Assert.assertEquals(4, mergedPart.getEndTime());
	}

	@Test
	public void testMergeSelectedChaptersTwoSections() throws Exception {
		final VideoPart chapter11 = new Chapter("A1").startTime(1).endTime(2);
		final Section chapter1 = new Section(chapter11);

		final VideoPart chapter22 = new Chapter("B1").startTime(2).endTime(3);
		final Section chapter2 = new Section(chapter22);

		final VideoPart chapter3 = new Chapter("C").startTime(3).endTime(4);
		this.chapterViewer.setModel(Arrays.asList(chapter1, chapter2, chapter3));
		this.chapterViewer.setSelectedChapters(Arrays.asList(chapter1, chapter2));

		// before
		Assert.assertEquals(Arrays.asList(chapter1, chapter2), this.chapterViewer.getSelectedChapters());
		Assert.assertEquals(3, this.chapterViewer.viewer.getTree().getItemCount());
		Assert.assertEquals(3, this.chapterViewer.getModel().size());

		this.chapterViewer.mergeSelectedChapters();

		// after
		Assert.assertEquals(Arrays.asList(chapter1, chapter2), this.chapterViewer.getSelectedChapters());
		Assert.assertEquals(2, this.chapterViewer.viewer.getTree().getItemCount());
		Assert.assertEquals(2, this.chapterViewer.getModel().size());

		// this is merged
		final VideoPart mergedPart = this.chapterViewer.getModel().get(0);
		Assert.assertNotNull(mergedPart);
		Assert.assertEquals("A1, B1", mergedPart.getTitle());
		Assert.assertEquals(1, mergedPart.getStartTime());
		Assert.assertEquals(3, mergedPart.getEndTime());

		Assert.assertEquals(2, chapter1.getParts().length);
		Assert.assertEquals(chapter11, chapter1.getParts()[0]);
		Assert.assertEquals(chapter22, chapter1.getParts()[1]);
	}

	// validate & merge tests

	@Test
	public void testValidateMergeSelectedChaptersLessThanTwo() throws Exception {
		setUpLessThanTwo();
		assertLessThanTwo(this.chapterViewer.validateMergeSelectedChapters());
	}

	private void setUpLessThanTwo() {
		final VideoPart chapter1 = new Chapter("A").startTime(1).endTime(2);
		final VideoPart chapter2 = new Chapter("B").startTime(2).endTime(3);
		final VideoPart chapter3 = new Chapter("C").startTime(3).endTime(4);
		this.chapterViewer.setModel(Arrays.asList(chapter1, chapter2, chapter3));
		this.chapterViewer.setSelectedChapters(Arrays.asList(chapter1));
	}

	private static void assertLessThanTwo(final IStatus status) {
		Assert.assertFalse(status.isOK());
		Assert.assertEquals(Messages.getString("ErrorLessThanTwo"), status.getMessage());
	}

	@Test
	public void testMergeSelectedChaptersLessThanTwo() throws Exception {
		setUpLessThanTwo();

		final IStatus[] status = {null};
		this.chapterViewer.statusHandler(s -> status[0] = s);

		this.chapterViewer.mergeSelectedChapters();

		Assert.assertNotNull("Status handler was not called!", status[0]);
		assertLessThanTwo(status[0]);
	}

	@Test
	public void testValidateMergeSelectedChaptersNoNeighbors() throws Exception {
		setUpNoNeighbors();
		assertNoNeighbors(this.chapterViewer.validateMergeSelectedChapters());
	}

	private void setUpNoNeighbors() {
		final VideoPart chapter1 = new Chapter("A").startTime(1).endTime(2);
		final VideoPart chapter2 = new Chapter("B").startTime(2).endTime(3);
		final VideoPart chapter3 = new Chapter("C").startTime(3).endTime(4);
		this.chapterViewer.setModel(Arrays.asList(chapter1, chapter2, chapter3));
		this.chapterViewer.setSelectedChapters(Arrays.asList(chapter1, chapter3));
	}

	private static void assertNoNeighbors(final IStatus status) {
		Assert.assertFalse(status.isOK());
		Assert.assertEquals(Messages.getString("ErrorNoNeighbors"), status.getMessage());
	}

	@Test
	public void testMergeSelectedChaptersNoNeighbors() throws Exception {
		setUpNoNeighbors();

		final IStatus[] status = {null};
		this.chapterViewer.statusHandler(s -> status[0] = s);

		this.chapterViewer.mergeSelectedChapters();

		Assert.assertNotNull("Status handler was not called!", status[0]);
		assertNoNeighbors(status[0]);
	}

	@Test
	public void testValidateMergeSelectedChaptersSectionedChapters() throws Exception {
		setUpSectionedChapters();
		assertSectionedChapters(this.chapterViewer.validateMergeSelectedChapters());
	}

	private void setUpSectionedChapters() {
		final VideoPart chapter11 = new Chapter("A1").startTime(1).endTime(2);
		final VideoPart chapter12 = new Chapter("A2").startTime(2).endTime(3);
		final VideoPart chapter13 = new Chapter("A3").startTime(3).endTime(4);

		final Section chapter1 = new Section(chapter11);
		chapter1.addPart(chapter12);
		chapter1.addPart(chapter13);

		final VideoPart chapter2 = new Chapter("B").startTime(4).endTime(5);
		final VideoPart chapter3 = new Chapter("C").startTime(5).endTime(6);
		this.chapterViewer.setModel(Arrays.asList(chapter1, chapter2, chapter3));
		this.chapterViewer.setSelectedChapters(Arrays.asList(chapter13, chapter3));
	}

	private static void assertSectionedChapters(final IStatus status) {
		Assert.assertFalse(status.isOK());
		Assert.assertEquals(MessageFormat.format(Messages.getString("ErrorSectionedChapters"), "A3"),
				status.getMessage());
	}

	@Test
	public void testMergeSelectedChaptersSectionedChapters() throws Exception {
		setUpSectionedChapters();

		final IStatus[] status = {null};
		this.chapterViewer.statusHandler(s -> status[0] = s);

		this.chapterViewer.mergeSelectedChapters();

		Assert.assertNotNull("Status handler was not called!", status[0]);
		assertSectionedChapters(status[0]);
	}

}
