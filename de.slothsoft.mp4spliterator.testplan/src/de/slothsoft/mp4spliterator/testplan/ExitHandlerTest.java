package de.slothsoft.mp4spliterator.testplan;

import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotButton;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ExitHandlerTest {

	private final SWTWorkbenchBot bot = new SWTWorkbenchBot();

	@Before
	public void before() {
		final SWTBotShell activeShell = this.bot.activeShell();

		if ("Init Application".equals(activeShell.getText())) {
			final SWTBotButton cancelButton = this.bot.button("Cancel");
			cancelButton.click();
		}

		Assert.assertTrue(true);
	}

	@Test
	public void test() {
		final SWTBotShell activeShell = this.bot.activeShell();

		Assert.assertEquals("MP4 Spliterator", activeShell.getText());
	}

	@Test
	public void test2() {
		final SWTBotShell activeShell = this.bot.activeShell();

		Assert.assertEquals("MP4 Spliterator", activeShell.getText());
	}
}