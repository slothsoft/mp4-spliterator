package de.slothsoft.mp4spliterator.testplan.constants;

import java.io.File;

import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEditor;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTree;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;

/**
 * Constants interface so I won't have to change these strings in millions of tests.
 */

public interface MainWindow {

	String TITLE = "MP4 Spliterator";

	String MENU_FILE = "File";
	String MENU_OPEN_VIDEO = "Open Video…";
	String MENU_WINDOW = "Window";
	String MENU_PREFERENCES = "Preferences";
	String MENU_HELP = "Help";
	String MENU_ABOUT = "About…";

	String VIEW_VIDEO_FOLDER = "Video Folder";
	String VIEW_VIDEO_FOLDER_VIEWER_ID = "videoFolderViewer";

	String EDITOR_VIDEO_CHAPTER_VIEWER_ID = "chapterViewer";

	String EDITOR_HELP = "Help";

	String TOOLITEM_MERGE_CHAPTERS = "Merge chapters";
	String TOOLITEM_SPLIT_CHAPTERS = "Split chapters";
	String TOOLITEM_EXPORT_CHAPTERS = "Export Chapters Separately";

	String BUTTON_CHECK_ALL = "Check all";
	String BUTTON_UNCHECK_ALL = "Uncheck all";

	static SWTBotEditor openMp4FileFromView(SWTWorkbenchBot bot, File file) {
		PreferenceDialog.changeVideoFolder(bot, file.getParentFile());

		bot.viewByPartName(VIEW_VIDEO_FOLDER);
		final SWTBotTree videoFolderTree = bot.treeWithId(VIEW_VIDEO_FOLDER_VIEWER_ID);

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
