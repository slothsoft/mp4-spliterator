package de.slothsoft.mp4spliterator.testplan;

import java.io.File;

import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEditor;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTree;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.junit.Assert;
import org.junit.Test;

import de.slothsoft.mp4spliterator.testplan.constants.InputDialog;
import de.slothsoft.mp4spliterator.testplan.constants.MainWindow;
import de.slothsoft.mp4spliterator.testplan.constants.WithChaptersMp4;

public class VideoSplittingTest extends AbstractMp4SpliteratorTest {

	@Test
	public void testS01_OpenMp4FileDirectly() throws Exception {
		openWithChaptersMp4FileFromMenu();
	}

	private SWTBotEditor openWithChaptersMp4FileFromMenu() {
		final SWTBotEditor videoEditor = MainWindow.openMp4FileFromMenu(this.bot,
				new File(getDemoFilesFolder(), WithChaptersMp4.FILE_NAME));
		addToTearDown(videoEditor::close);

		final SWTBotTree chapterTree = this.bot.treeWithId(MainWindow.EDITOR_VIDEO_CHAPTER_VIEWER_ID);
		final SWTBotTreeItem[] chapterItems = chapterTree.getAllItems();

		Assert.assertNotNull(chapterItems);
		Assert.assertEquals(3, chapterItems.length);
		Assert.assertEquals(WithChaptersMp4.CHAPTER_1, chapterItems[0].getText());
		Assert.assertEquals(WithChaptersMp4.CHAPTER_2, chapterItems[1].getText());
		Assert.assertEquals(WithChaptersMp4.CHAPTER_3, chapterItems[2].getText());

		return videoEditor;
	}

	private static void assertFileExists(File folder, String fileName) {
		final File fileWithName = new File(folder, fileName);
		Assert.assertTrue("File should exist: " + fileWithName, fileWithName.exists());
	}

	@Test
	public void testS01a_ExportChaptersOnDefault() throws Exception {
		openWithChaptersMp4FileFromMenu();

		this.bot.toolbarButtonWithTooltip(MainWindow.TOOLITEM_EXPORT_CHAPTERS).click();

		final File tempFolder = createTempFolder();

		InputDialog.fillInputDialog(this.bot, tempFolder.toString());

		this.bot.sleep(WithChaptersMp4.TIME_TO_EXPORT);

		assertFileExists(tempFolder, "1 " + WithChaptersMp4.CHAPTER_1 + WithChaptersMp4.EXTENSION);
		assertFileExists(tempFolder, "2 " + WithChaptersMp4.CHAPTER_2 + WithChaptersMp4.EXTENSION);
		assertFileExists(tempFolder, "3 " + WithChaptersMp4.CHAPTER_3 + WithChaptersMp4.EXTENSION);
	}

	@Test
	public void testS01b_ExportNoChapters() throws Exception {
		openWithChaptersMp4FileFromMenu();

		this.bot.button(MainWindow.BUTTON_UNCHECK_ALL).click();

		this.bot.toolbarButtonWithTooltip(MainWindow.TOOLITEM_EXPORT_CHAPTERS).click();

		final File tempFolder = createTempFolder();

		InputDialog.fillInputDialog(this.bot, tempFolder.toString());

		this.bot.sleep(WithChaptersMp4.TIME_TO_EXPORT);

		Assert.assertArrayEquals(new String[0], tempFolder.list());
	}

	@Test
	public void testS01c_ExportAllChapters() throws Exception {
		openWithChaptersMp4FileFromMenu();

		this.bot.button(MainWindow.BUTTON_UNCHECK_ALL).click();
		this.bot.button(MainWindow.BUTTON_CHECK_ALL).click();

		this.bot.toolbarButtonWithTooltip(MainWindow.TOOLITEM_EXPORT_CHAPTERS).click();

		final File tempFolder = createTempFolder();

		InputDialog.fillInputDialog(this.bot, tempFolder.toString());

		this.bot.sleep(WithChaptersMp4.TIME_TO_EXPORT);

		assertFileExists(tempFolder, "1 " + WithChaptersMp4.CHAPTER_1 + WithChaptersMp4.EXTENSION);
		assertFileExists(tempFolder, "2 " + WithChaptersMp4.CHAPTER_2 + WithChaptersMp4.EXTENSION);
		assertFileExists(tempFolder, "3 " + WithChaptersMp4.CHAPTER_3 + WithChaptersMp4.EXTENSION);
	}

	@Test
	public void testS02_OpenMp4FileFromView() throws Exception {
		openWithChaptersMp4FileFromView();
	}

	private SWTBotEditor openWithChaptersMp4FileFromView() {
		final SWTBotEditor videoEditor = MainWindow.openMp4FileFromView(this.bot,
				new File(getDemoFilesFolder(), WithChaptersMp4.FILE_NAME));
		addToTearDown(videoEditor::close);

		final SWTBotTree chapterTree = this.bot.treeWithId(MainWindow.EDITOR_VIDEO_CHAPTER_VIEWER_ID);
		final SWTBotTreeItem[] chapterItems = chapterTree.getAllItems();

		Assert.assertNotNull(chapterItems);
		Assert.assertEquals(3, chapterItems.length);
		Assert.assertEquals(WithChaptersMp4.CHAPTER_1, chapterItems[0].getText());
		Assert.assertEquals(WithChaptersMp4.CHAPTER_2, chapterItems[1].getText());
		Assert.assertEquals(WithChaptersMp4.CHAPTER_3, chapterItems[2].getText());

		return videoEditor;
	}

	@Test
	public void testS02a_ExportMergedChapters() throws Exception {
		openWithChaptersMp4FileFromView();

		final SWTBotTree chapterTree = this.bot.treeWithId(MainWindow.EDITOR_VIDEO_CHAPTER_VIEWER_ID);
		chapterTree.select(WithChaptersMp4.CHAPTER_1, WithChaptersMp4.CHAPTER_2);

		this.bot.toolbarButtonWithTooltip(MainWindow.TOOLITEM_MERGE_CHAPTERS).click();
		final String mergedChapter = WithChaptersMp4.CHAPTER_1 + ", " + WithChaptersMp4.CHAPTER_2;

		final SWTBotTreeItem[] chapterItems = chapterTree.getAllItems();
		Assert.assertNotNull(chapterItems);
		Assert.assertEquals(2, chapterItems.length);
		Assert.assertEquals(mergedChapter, chapterItems[0].getText());
		Assert.assertEquals(WithChaptersMp4.CHAPTER_3, chapterItems[1].getText());

		this.bot.toolbarButtonWithTooltip(MainWindow.TOOLITEM_EXPORT_CHAPTERS).click();

		final File tempFolder = createTempFolder();

		InputDialog.fillInputDialog(this.bot, tempFolder.toString());

		this.bot.sleep(WithChaptersMp4.TIME_TO_EXPORT);

		assertFileExists(tempFolder, "1 " + mergedChapter + WithChaptersMp4.EXTENSION);
		assertFileExists(tempFolder, "2 " + WithChaptersMp4.CHAPTER_3 + WithChaptersMp4.EXTENSION);
	}

	@Test
	public void testS02b_ExportSplitChapters() throws Exception {
		openWithChaptersMp4FileFromView();

		// merge chapters

		final SWTBotTree chapterTree = this.bot.treeWithId(MainWindow.EDITOR_VIDEO_CHAPTER_VIEWER_ID);
		chapterTree.select(WithChaptersMp4.CHAPTER_1, WithChaptersMp4.CHAPTER_2);

		this.bot.toolbarButtonWithTooltip(MainWindow.TOOLITEM_MERGE_CHAPTERS).click();
		final String mergedChapter = WithChaptersMp4.CHAPTER_1 + ", " + WithChaptersMp4.CHAPTER_2;

		// split chapters

		chapterTree.select(mergedChapter);
		this.bot.toolbarButtonWithTooltip(MainWindow.TOOLITEM_SPLIT_CHAPTERS).click();

		final SWTBotTreeItem[] chapterItems = chapterTree.getAllItems();
		Assert.assertNotNull(chapterItems);
		Assert.assertEquals(3, chapterItems.length);
		Assert.assertEquals(WithChaptersMp4.CHAPTER_1, chapterItems[0].getText());
		Assert.assertEquals(WithChaptersMp4.CHAPTER_2, chapterItems[1].getText());
		Assert.assertEquals(WithChaptersMp4.CHAPTER_3, chapterItems[2].getText());

		this.bot.toolbarButtonWithTooltip(MainWindow.TOOLITEM_EXPORT_CHAPTERS).click();

		final File tempFolder = createTempFolder();

		InputDialog.fillInputDialog(this.bot, tempFolder.toString());

		this.bot.sleep(WithChaptersMp4.TIME_TO_EXPORT);

		assertFileExists(tempFolder, "1 " + WithChaptersMp4.CHAPTER_1 + WithChaptersMp4.EXTENSION);
		assertFileExists(tempFolder, "2 " + WithChaptersMp4.CHAPTER_2 + WithChaptersMp4.EXTENSION);
		assertFileExists(tempFolder, "3 " + WithChaptersMp4.CHAPTER_3 + WithChaptersMp4.EXTENSION);
	}

	@Test
	public void testS03_ExportChaptersTwice() throws Exception {
		openWithChaptersMp4FileFromMenu();

		this.bot.toolbarButtonWithTooltip(MainWindow.TOOLITEM_EXPORT_CHAPTERS).click();

		final File tempFolder = createTempFolder();
		InputDialog.fillInputDialog(this.bot, tempFolder.toString());
		this.bot.sleep(WithChaptersMp4.TIME_TO_EXPORT);

		this.bot.toolbarButtonWithTooltip(MainWindow.TOOLITEM_EXPORT_CHAPTERS).click();

		InputDialog.fillInputDialog(this.bot, tempFolder.toString());
		this.bot.sleep(WithChaptersMp4.TIME_TO_EXPORT);

		assertFileExists(tempFolder, "1 " + WithChaptersMp4.CHAPTER_1 + WithChaptersMp4.EXTENSION);
		assertFileExists(tempFolder, "2 " + WithChaptersMp4.CHAPTER_2 + WithChaptersMp4.EXTENSION);
		assertFileExists(tempFolder, "3 " + WithChaptersMp4.CHAPTER_3 + WithChaptersMp4.EXTENSION);

		final String two = " (2)";
		assertFileExists(tempFolder, "1 " + WithChaptersMp4.CHAPTER_1 + two + WithChaptersMp4.EXTENSION);
		assertFileExists(tempFolder, "2 " + WithChaptersMp4.CHAPTER_2 + two + WithChaptersMp4.EXTENSION);
		assertFileExists(tempFolder, "3 " + WithChaptersMp4.CHAPTER_3 + two + WithChaptersMp4.EXTENSION);
	}

}
