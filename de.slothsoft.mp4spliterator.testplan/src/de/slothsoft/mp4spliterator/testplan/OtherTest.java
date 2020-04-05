package de.slothsoft.mp4spliterator.testplan;

import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEditor;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.junit.Assert;
import org.junit.Test;

import de.slothsoft.mp4spliterator.testplan.constants.AboutDialog;
import de.slothsoft.mp4spliterator.testplan.constants.MainWindow;

public class OtherTest extends AbstractMp4SpliteratorTest {

	@Test
	public void testO01_OpenHelp() throws Exception {
		this.bot.menu(MainWindow.MENU_HELP).menu(MainWindow.MENU_HELP).click();

		final SWTBotEditor helpEditor = this.bot.editorByTitle(MainWindow.EDITOR_HELP);
		addToTearDown(helpEditor::close);
	}

	@Test
	public void testO03_OpenAbout() throws Exception {
		this.bot.menu(MainWindow.MENU_HELP).menu(MainWindow.MENU_ABOUT).click();

		final SWTBotShell activeShell = this.bot.activeShell();
		addToTearDown(activeShell::close);
		Assert.assertEquals(AboutDialog.TITLE, activeShell.getText());
	}
}
