package de.slothsoft.mp4spliterator.testplan.constants;

import java.io.File;

import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEditor;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotText;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTree;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.junit.Assert;

/**
 * Constants interface so I won't have to change these strings in millions of tests.
 */

public interface MainWindow {

	String TITLE = "MP4 Spliterator";

	String MENU_FILE = "File";
	String MENU_OPEN_VIDEO = "Open Video…";
	String MENU_WINDOW = "Window";
	String MENU_PREFERENCES = "Preferences";

	String VIEW_VIDEO_FOLDER = "Video Folder";
	String VIEW_VIDEO_FOLDER_VIEWER_ID = "videoFolderViewer";

	String EDITOR_VIDEO_CHAPTER_VIEWER_ID = "chapterViewer";

	String TOOLITEM_MERGE_CHAPTERS = "Merge chapters";
	String TOOLITEM_SPLIT_CHAPTERS = "Split chapters";
	String TOOLITEM_EXPORT_CHAPTERS = "Export Chapters Separately";

	String BUTTON_CHECK_ALL = "Check all";
	String BUTTON_UNCHECK_ALL = "Uncheck all";

	static SWTBotEditor openMp4FileFromView(SWTWorkbenchBot bot, File file) {
		bot.menu(MENU_WINDOW).menu(MENU_PREFERENCES).click();

		final SWTBotShell activeShell = bot.activeShell();
		Assert.assertEquals(PreferenceDialog.TITLE, activeShell.getText());

		final SWTBotTree preferencesTable = bot.tree();
		final SWTBotTreeItem generalItem = preferencesTable.getAllItems()[2];
		generalItem.click();

		final SWTBotText videoFolderText = bot.textWithLabel(PreferenceDialog.TEXT_VIDEO_FOLDER);
		videoFolderText.setText(file.getParent());

		bot.button(Common.BUTTON_APPLY).click();

		// TODO: move this part to where it belongs
		bot.viewByPartName(VIEW_VIDEO_FOLDER);
		final SWTBotTree videoFolderTree = bot.treeWithId(VIEW_VIDEO_FOLDER_VIEWER_ID);

		for (final File itFile : file.getParentFile().listFiles()) {
			videoFolderTree.getTreeItem(itFile.getName());
		}

		// TODO-END

		final String fileName = file.getName();
		final SWTBotTreeItem treeItem = videoFolderTree.getTreeItem(fileName);
		treeItem.doubleClick();

		return bot.editorByTitle(fileName);
	}

	static SWTBotEditor openMp4FileFromMenu(SWTWorkbenchBot bot, File file) {
		bot.menu(MENU_FILE).menu(MENU_OPEN_VIDEO).click();

		InputDialog.fillInputDialog(bot, file.toString());

		return bot.editorByTitle(file.getName());
	}
}
