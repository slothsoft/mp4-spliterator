package de.slothsoft.mp4spliterator.videos;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TreeItem;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import de.slothsoft.mp4spliterator.core.Chapter;
import de.slothsoft.mp4spliterator.core.VideoPart;

public class ChapterViewerTest {

	private Shell shell;
	private ChapterViewer chapterViewer;

	@Before
	public void setUp() {
		this.shell = new Shell();
		this.shell.setLayout(new FillLayout());

		this.chapterViewer = new ChapterViewer(this.shell, SWT.NONE);
	}

	@After
	public void tearDown() {
		this.shell.dispose();
	}

	@Test
	public void testConstructor() throws Exception {
		Assert.assertNotNull(this.chapterViewer);
		Assert.assertNotNull(this.chapterViewer.model);
		Assert.assertNotNull(this.chapterViewer.toolkit);
		Assert.assertNotNull(this.chapterViewer.viewer);
	}

	@Test
	public void testSetModel() throws Exception {
		final List<VideoPart> model = Arrays.asList(new Chapter(UUID.randomUUID().toString()));
		this.chapterViewer.setModel(model);

		Assert.assertEquals(1, this.chapterViewer.viewer.getTree().getItemCount());
		Assert.assertEquals(model, this.chapterViewer.getModel());
	}

	@Test
	public void testSetModelMultiple() throws Exception {
		final List<VideoPart> model = Arrays.asList(new Chapter(UUID.randomUUID().toString()),
				new Chapter(UUID.randomUUID().toString()), new Chapter(UUID.randomUUID().toString()));
		this.chapterViewer.setModel(model);

		Assert.assertEquals(3, this.chapterViewer.viewer.getTree().getItemCount());
		Assert.assertEquals(model, this.chapterViewer.getModel());
	}

	@Test
	public void testSetModelEmpty() throws Exception {
		final List<VideoPart> model = Collections.emptyList();
		this.chapterViewer.setModel(model);

		Assert.assertEquals(0, this.chapterViewer.viewer.getTree().getItemCount());
		Assert.assertEquals(model, this.chapterViewer.getModel());
	}

	@Test
	public void testModel() throws Exception {
		final List<VideoPart> model = Arrays.asList(new Chapter(UUID.randomUUID().toString()));
		this.chapterViewer.model(model);

		Assert.assertEquals(1, this.chapterViewer.viewer.getTree().getItemCount());
		Assert.assertEquals(model, this.chapterViewer.getModel());
	}

	@Test
	public void testModelMultiple() throws Exception {
		final List<VideoPart> model = Arrays.asList(new Chapter(UUID.randomUUID().toString()),
				new Chapter(UUID.randomUUID().toString()), new Chapter(UUID.randomUUID().toString()));
		this.chapterViewer.model(model);

		Assert.assertEquals(3, this.chapterViewer.viewer.getTree().getItemCount());
		Assert.assertEquals(model, this.chapterViewer.getModel());
	}

	@Test
	public void testModelEmpty() throws Exception {
		final List<VideoPart> model = Collections.emptyList();
		this.chapterViewer.model(model);

		Assert.assertEquals(0, this.chapterViewer.viewer.getTree().getItemCount());
		Assert.assertEquals(model, this.chapterViewer.getModel());
	}

	@Test
	public void testCheckAllTrue() throws Exception {
		final VideoPart chapter1 = new Chapter(UUID.randomUUID().toString());
		final VideoPart chapter2 = new Chapter(UUID.randomUUID().toString());
		final VideoPart chapter3 = new Chapter(UUID.randomUUID().toString());
		this.chapterViewer.setModel(Arrays.asList(chapter1, chapter2, chapter3));
		this.chapterViewer.checkAll(true);

		for (final TreeItem item : this.chapterViewer.viewer.getTree().getItems()) {
			Assert.assertTrue(item.getChecked());
		}
	}

	@Test
	public void testCheckAllFalse() throws Exception {
		final VideoPart chapter1 = new Chapter(UUID.randomUUID().toString());
		final VideoPart chapter2 = new Chapter(UUID.randomUUID().toString());
		final VideoPart chapter3 = new Chapter(UUID.randomUUID().toString());
		this.chapterViewer.setModel(Arrays.asList(chapter1, chapter2, chapter3));
		this.chapterViewer.checkAll(false);

		for (final TreeItem item : this.chapterViewer.viewer.getTree().getItems()) {
			Assert.assertFalse(item.getChecked());
		}
	}

	@Test
	public void testCheckTrue() throws Exception {
		final VideoPart chapter1 = new Chapter(UUID.randomUUID().toString());
		final VideoPart chapter2 = new Chapter(UUID.randomUUID().toString());
		final VideoPart chapter3 = new Chapter(UUID.randomUUID().toString());
		this.chapterViewer.setModel(Arrays.asList(chapter1, chapter2, chapter3));
		this.chapterViewer.checkAll(false);

		this.chapterViewer.check(Arrays.asList(chapter2), true);
		final TreeItem[] items = this.chapterViewer.viewer.getTree().getItems();
		Assert.assertFalse(items[0].getChecked());
		Assert.assertTrue(items[1].getChecked());
		Assert.assertFalse(items[2].getChecked());
	}

	@Test
	public void testCheckFalse() throws Exception {
		final VideoPart chapter1 = new Chapter(UUID.randomUUID().toString());
		final VideoPart chapter2 = new Chapter(UUID.randomUUID().toString());
		final VideoPart chapter3 = new Chapter(UUID.randomUUID().toString());
		this.chapterViewer.setModel(Arrays.asList(chapter1, chapter2, chapter3));
		this.chapterViewer.checkAll(true);

		this.chapterViewer.check(Arrays.asList(chapter1, chapter3), false);
		final TreeItem[] items = this.chapterViewer.viewer.getTree().getItems();
		Assert.assertFalse(items[0].getChecked());
		Assert.assertTrue(items[1].getChecked());
		Assert.assertFalse(items[2].getChecked());
	}

	@Test
	public void testGetCheckedChapters() throws Exception {
		final VideoPart chapter1 = new Chapter(UUID.randomUUID().toString());
		final VideoPart chapter2 = new Chapter(UUID.randomUUID().toString());
		final VideoPart chapter3 = new Chapter(UUID.randomUUID().toString());
		this.chapterViewer.setModel(Arrays.asList(chapter1, chapter2, chapter3));
		this.chapterViewer.checkAll(false);

		Assert.assertEquals(new ArrayList<>(), this.chapterViewer.getCheckedChapters());

		this.chapterViewer.viewer.getTree().getItem(0).setChecked(true);

		Assert.assertEquals(Arrays.asList(chapter1), this.chapterViewer.getCheckedChapters());

		this.chapterViewer.viewer.getTree().getItem(2).setChecked(true);

		Assert.assertEquals(Arrays.asList(chapter1, chapter3), this.chapterViewer.getCheckedChapters());

		this.chapterViewer.viewer.getTree().getItem(1).setChecked(true);

		Assert.assertEquals(Arrays.asList(chapter1, chapter2, chapter3), this.chapterViewer.getCheckedChapters());
	}

	@Test
	public void testSetSelectedChapters() throws Exception {
		final VideoPart chapter1 = new Chapter(UUID.randomUUID().toString());
		final VideoPart chapter2 = new Chapter(UUID.randomUUID().toString());
		final VideoPart chapter3 = new Chapter(UUID.randomUUID().toString());
		this.chapterViewer.setModel(Arrays.asList(chapter1, chapter2, chapter3));

		this.chapterViewer.setSelectedChapters(Arrays.asList(chapter1, chapter3));
		Assert.assertEquals(Arrays.asList(chapter1, chapter3), this.chapterViewer.getSelectedChapters());
		Assert.assertArrayEquals(new Object[]{chapter1, chapter3},
				((IStructuredSelection) this.chapterViewer.viewer.getSelection()).toArray());

		this.chapterViewer.setSelectedChapters(Arrays.asList(chapter2));
		Assert.assertEquals(Arrays.asList(chapter2), this.chapterViewer.getSelectedChapters());
		Assert.assertArrayEquals(new Object[]{chapter2},
				((IStructuredSelection) this.chapterViewer.viewer.getSelection()).toArray());

		this.chapterViewer.setSelectedChapters(Arrays.asList());
		Assert.assertEquals(Arrays.asList(), this.chapterViewer.getSelectedChapters());
		Assert.assertArrayEquals(new Object[0],
				((IStructuredSelection) this.chapterViewer.viewer.getSelection()).toArray());
	}

}
