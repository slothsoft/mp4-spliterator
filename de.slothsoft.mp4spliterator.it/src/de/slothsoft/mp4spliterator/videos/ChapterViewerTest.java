package de.slothsoft.mp4spliterator.videos;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableItem;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import de.slothsoft.mp4spliterator.core.Chapter;

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
		final List<Chapter> model = Arrays.asList(new Chapter(UUID.randomUUID().toString()));
		this.chapterViewer.setModel(model);

		Assert.assertEquals(1, this.chapterViewer.viewer.getTable().getItemCount());
		Assert.assertEquals(model, this.chapterViewer.getModel());
	}

	@Test
	public void testSetModelMultiple() throws Exception {
		final List<Chapter> model = Arrays.asList(new Chapter(UUID.randomUUID().toString()),
				new Chapter(UUID.randomUUID().toString()), new Chapter(UUID.randomUUID().toString()));
		this.chapterViewer.setModel(model);

		Assert.assertEquals(3, this.chapterViewer.viewer.getTable().getItemCount());
		Assert.assertEquals(model, this.chapterViewer.getModel());
	}

	@Test
	public void testSetModelEmpty() throws Exception {
		final List<Chapter> model = Collections.emptyList();
		this.chapterViewer.setModel(model);

		Assert.assertEquals(0, this.chapterViewer.viewer.getTable().getItemCount());
		Assert.assertEquals(model, this.chapterViewer.getModel());
	}

	@Test
	public void testModel() throws Exception {
		final List<Chapter> model = Arrays.asList(new Chapter(UUID.randomUUID().toString()));
		this.chapterViewer.model(model);

		Assert.assertEquals(1, this.chapterViewer.viewer.getTable().getItemCount());
		Assert.assertEquals(model, this.chapterViewer.getModel());
	}

	@Test
	public void testModelMultiple() throws Exception {
		final List<Chapter> model = Arrays.asList(new Chapter(UUID.randomUUID().toString()),
				new Chapter(UUID.randomUUID().toString()), new Chapter(UUID.randomUUID().toString()));
		this.chapterViewer.model(model);

		Assert.assertEquals(3, this.chapterViewer.viewer.getTable().getItemCount());
		Assert.assertEquals(model, this.chapterViewer.getModel());
	}

	@Test
	public void testModelEmpty() throws Exception {
		final List<Chapter> model = Collections.emptyList();
		this.chapterViewer.model(model);

		Assert.assertEquals(0, this.chapterViewer.viewer.getTable().getItemCount());
		Assert.assertEquals(model, this.chapterViewer.getModel());
	}

	@Test
	public void testCheckAllTrue() throws Exception {
		this.chapterViewer.checkAll(true);

		for (final TableItem item : this.chapterViewer.viewer.getTable().getItems()) {
			Assert.assertTrue(item.getChecked());
		}
	}

	@Test
	public void testCheckAllFalse() throws Exception {
		this.chapterViewer.checkAll(false);

		for (final TableItem item : this.chapterViewer.viewer.getTable().getItems()) {
			Assert.assertFalse(item.getChecked());
		}
	}

	@Test
	public void testGetSelectedChapters() throws Exception {
		final Chapter chapter1 = new Chapter(UUID.randomUUID().toString());
		final Chapter chapter2 = new Chapter(UUID.randomUUID().toString());
		final Chapter chapter3 = new Chapter(UUID.randomUUID().toString());
		this.chapterViewer.setModel(Arrays.asList(chapter1, chapter2, chapter3));
		this.chapterViewer.checkAll(false);

		Assert.assertEquals(new ArrayList<>(), this.chapterViewer.getSelectedChapters());

		this.chapterViewer.viewer.getTable().getItem(0).setChecked(true);

		Assert.assertEquals(Arrays.asList(chapter1), this.chapterViewer.getSelectedChapters());

		this.chapterViewer.viewer.getTable().getItem(2).setChecked(true);

		Assert.assertEquals(Arrays.asList(chapter1, chapter3), this.chapterViewer.getSelectedChapters());

		this.chapterViewer.viewer.getTable().getItem(1).setChecked(true);

		Assert.assertEquals(Arrays.asList(chapter1, chapter2, chapter3), this.chapterViewer.getSelectedChapters());
	}

}
