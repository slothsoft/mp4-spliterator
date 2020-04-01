package de.slothsoft.mp4spliterator.testplan.constants;

import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotText;
import org.junit.Assert;

public interface InputDialog {

	String TITLE = "SWTBot Input";

	String TEXT_INPUT = "Input:";

	public static void fillInputDialog(SWTWorkbenchBot bot, String input) {
		final SWTBotShell activeShell = bot.activeShell();
		Assert.assertEquals(TITLE, activeShell.getText());

		final SWTBotText inputText = bot.textWithLabel(TEXT_INPUT);
		inputText.setText(input);

		bot.button(Common.BUTTON_OK).click();
	}
}
