package de.slothsoft.mp4spliterator.testplan.constants;

import java.io.File;

import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotButton;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotText;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTree;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.junit.Assert;

public final class PreferenceDialog {

	public static final String TITLE = "Preferences";

	public static final String TEXT_VIDEO_FOLDER = "Video Folder:";

	public static final String BUTTON_BROWSE = "Browse...";

	public static final String PAGE_GENERAL = "General Settings";

	public static void changeVideoFolder(SWTWorkbenchBot bot, File folder) {
		Assert.assertTrue("Folder should exist: " + folder, folder.exists());

		bot.menu(MainWindow.MENU_WINDOW).menu(MainWindow.MENU_PREFERENCES).click();

		final SWTBotShell activeShell = bot.activeShell();
		Assert.assertEquals(PreferenceDialog.TITLE, activeShell.getText());

		final SWTBotTree preferencesTable = bot.tree();
		final SWTBotTreeItem generalItem = preferencesTable.getTreeItem(PAGE_GENERAL);
		generalItem.click();

		// the initial value is already broken for some reason
		if (tearDownPreferencesErrorDialogIfNecessary(bot)) {
			final SWTBotText videoFolderText = bot.textWithLabel(PreferenceDialog.TEXT_VIDEO_FOLDER);
			Assert.fail("Preference page was already broken: " + videoFolderText.getText());
		}

		final SWTBotText videoFolderText = bot.textWithLabel(PreferenceDialog.TEXT_VIDEO_FOLDER);
		videoFolderText.setText(folder.toString());
		videoFolderText.setFocus();

		bot.button(BUTTON_BROWSE).setFocus();

		try {
			bot.button(Common.BUTTON_APPLY).click();
		} finally {

			// applying failed; we just need to close the preferences now somehow

			tearDownPreferencesErrorDialogIfNecessary(bot);
			tearDownPreferencesIfNecessary(bot);
		}
	}

	private static boolean tearDownPreferencesErrorDialogIfNecessary(SWTWorkbenchBot bot) {
		final SWTBotShell newActiveShell = bot.activeShell();
		final String newActiveShellText = newActiveShell.getText();
		if (!MainWindow.TITLE.equals(newActiveShellText) && !TITLE.equals(newActiveShellText)) {
			tearDownPreferencesErrorDialog(bot, newActiveShell);
			return true;
		}
		return false;
	}

	private static void tearDownPreferencesErrorDialog(SWTWorkbenchBot bot, SWTBotShell errorDialog) {
		try {
			// try to log the dialogs error text
			System.out.println("PreferenceDialog.tearDownPreferencesErrorDialog()");
			System.out.println("Error dialog: " + bot.label(1).getText());
		} catch (final Exception ignoredException) {
			ignoredException.printStackTrace();
		}

		// this is an error dialog of some sort I assume
		errorDialog.widget.getDisplay().syncExec(() -> new SWTBotButton(errorDialog.widget.getDefaultButton()).click());
	}

	private static boolean tearDownPreferencesIfNecessary(SWTWorkbenchBot bot) {
		final SWTBotShell activeShell = bot.activeShell();
		if (TITLE.equals(activeShell.getText())) {
			tearDownPreferences(bot);
			return true;
		}
		return false;
	}

	private static void tearDownPreferences(SWTWorkbenchBot bot) {
		System.out.println("PreferenceDialog.tearDownPreferences()");
		System.out.println("Preference page: " + bot.text(1).getText());
		bot.button(Common.BUTTON_CANCEL).click();
	}

	private PreferenceDialog() {
		// hide this class
	}
}
