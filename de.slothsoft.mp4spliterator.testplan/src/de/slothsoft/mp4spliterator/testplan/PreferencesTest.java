package de.slothsoft.mp4spliterator.testplan;

import java.io.File;

import org.eclipse.swtbot.swt.finder.widgets.SWTBotTree;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.junit.Assert;
import org.junit.Test;

import de.slothsoft.mp4spliterator.testplan.constants.MainWindow;
import de.slothsoft.mp4spliterator.testplan.constants.PreferenceDialog;

public class PreferencesTest extends AbstractMp4SpliteratorTest {

	@Test
	public void testP01_ChangeVideoFolder() throws Exception {
		this.bot.viewByPartName(MainWindow.VIEW_VIDEO_FOLDER);

		final File tempFolder = createTempFolder();
		PreferenceDialog.changeVideoFolder(this.bot, tempFolder);

		final SWTBotTree videoFolderTree = this.bot.treeWithId(MainWindow.VIEW_VIDEO_FOLDER_VIEWER_ID);
		Assert.assertArrayEquals(new SWTBotTreeItem[0], videoFolderTree.getAllItems());

		final File demoFolder = getDemoFilesFolder();
		PreferenceDialog.changeVideoFolder(this.bot, demoFolder);

		for (final File demoFile : demoFolder.listFiles()) {
			videoFolderTree.getTreeItem(demoFile.getName());
		}
	}
}
