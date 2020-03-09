package de.slothsoft.mp4spliterator.videos;

import java.util.Arrays;
import java.util.UUID;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TreeItem;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import de.slothsoft.mp4spliterator.core.Chapter;
import de.slothsoft.mp4spliterator.core.Section;
import de.slothsoft.mp4spliterator.core.VideoPart;

public class ChapterViewerCheckTest {

	private Shell shell;
	private ChapterViewer chapterViewer;

	private Section sectionWithChapters;
	private Section sectionWithChapter;
	private Chapter chapter;

	@Before
	public void setUp() {
		this.shell = new Shell();
		this.shell.setLayout(new FillLayout());

		this.sectionWithChapters = new Section(new Chapter(UUID.randomUUID().toString()));
		this.sectionWithChapters.addParts(new Chapter(UUID.randomUUID().toString()),
				new Chapter(UUID.randomUUID().toString()));

		this.sectionWithChapter = new Section(new Chapter(UUID.randomUUID().toString()));

		this.chapter = new Chapter(UUID.randomUUID().toString());

		this.chapterViewer = new ChapterViewer(this.shell, SWT.NONE);
		this.chapterViewer.setModel(Arrays.asList(this.sectionWithChapters, this.sectionWithChapter, this.chapter));
		this.chapterViewer.viewer.expandAll();
	}

	@After
	public void tearDown() {
		this.shell.dispose();
	}

	@Test
	public void testCheckSectionWithChapters() throws Exception {
		this.chapterViewer.checkAll(false);

		check(this.sectionWithChapters, true);

		Assert.assertTrue(isChecked(this.sectionWithChapters));
		for (final VideoPart part : this.sectionWithChapters.getParts()) {
			Assert.assertTrue("Part should be checked: " + part, isChecked(part));
		}
	}

	private void check(VideoPart videoPart, boolean check) {
		for (final TreeItem treeItem : this.chapterViewer.viewer.getTree().getItems()) {
			if (treeItem.getData() == videoPart) {
				treeItem.setChecked(check);
				this.chapterViewer.performCheck(treeItem);
				return;
			}
			for (final TreeItem child : treeItem.getItems()) {
				if (child.getData() == videoPart) {
					child.setChecked(check);
					this.chapterViewer.performCheck(child);
					return;
				}
			}
		}
		Assert.fail("Could not find TreeItem for " + videoPart);
	}

	private boolean isChecked(VideoPart videoPart) {
		for (final TreeItem treeItem : this.chapterViewer.viewer.getTree().getItems()) {
			if (treeItem.getData() == videoPart) {
				return treeItem.getChecked();
			}
			for (final TreeItem child : treeItem.getItems()) {
				if (child.getData() == videoPart) {
					return treeItem.getChecked();
				}
			}
		}
		Assert.fail("Could not find TreeItem for " + videoPart);
		return false;
	}

	@Test
	public void testUncheckSectionWithChapters() throws Exception {
		this.chapterViewer.checkAll(true);

		check(this.sectionWithChapters, false);

		Assert.assertFalse(isChecked(this.sectionWithChapters));
		for (final VideoPart part : this.sectionWithChapters.getParts()) {
			Assert.assertFalse("Part should be unchecked: " + part, isChecked(part));
		}
	}

	@Test
	public void testCheckChapterOfSectionWithChapters() throws Exception {
		this.chapterViewer.checkAll(false);

		check(this.sectionWithChapters.getParts()[1], true);

		Assert.assertTrue(isChecked(this.sectionWithChapters));
		for (final VideoPart part : this.sectionWithChapters.getParts()) {
			Assert.assertTrue("Part should be checked: " + part, isChecked(part));
		}
	}

	@Test
	public void testUncheckChapterOfSectionWithChapters() throws Exception {
		this.chapterViewer.checkAll(true);

		check(this.sectionWithChapters.getParts()[1], false);

		Assert.assertFalse(isChecked(this.sectionWithChapters));
		for (final VideoPart part : this.sectionWithChapters.getParts()) {
			Assert.assertFalse("Part should be unchecked: " + part, isChecked(part));
		}
	}

	@Test
	public void testCheckSectionWithChapter() throws Exception {
		this.chapterViewer.checkAll(false);

		check(this.sectionWithChapter, true);

		Assert.assertTrue(isChecked(this.sectionWithChapter));
		for (final VideoPart part : this.sectionWithChapter.getParts()) {
			Assert.assertTrue("Part should be checked: " + part, isChecked(part));
		}
	}

	@Test
	public void testUncheckSectionWithChapter() throws Exception {
		this.chapterViewer.checkAll(true);

		check(this.sectionWithChapter, false);

		Assert.assertFalse(isChecked(this.sectionWithChapter));
		for (final VideoPart part : this.sectionWithChapter.getParts()) {
			Assert.assertFalse("Part should be unchecked: " + part, isChecked(part));
		}
	}

	@Test
	public void testCheckChapterOfSectionWithChapter() throws Exception {
		this.chapterViewer.checkAll(false);

		check(this.sectionWithChapter.getParts()[0], true);

		Assert.assertTrue(isChecked(this.sectionWithChapter));
		for (final VideoPart part : this.sectionWithChapter.getParts()) {
			Assert.assertTrue("Part should be checked: " + part, isChecked(part));
		}
	}

	@Test
	public void testUncheckChapterOfSectionWithChapter() throws Exception {
		this.chapterViewer.checkAll(true);

		check(this.sectionWithChapter.getParts()[0], false);

		Assert.assertFalse(isChecked(this.sectionWithChapter));
		for (final VideoPart part : this.sectionWithChapter.getParts()) {
			Assert.assertFalse("Part should be unchecked: " + part, isChecked(part));
		}
	}

	@Test
	public void testCheckChapter() throws Exception {
		this.chapterViewer.checkAll(false);

		check(this.chapter, true);

		Assert.assertTrue(isChecked(this.chapter));
	}

	@Test
	public void testUncheckChapter() throws Exception {
		this.chapterViewer.checkAll(true);

		check(this.chapter, false);

		Assert.assertFalse(isChecked(this.chapter));
	}
}
