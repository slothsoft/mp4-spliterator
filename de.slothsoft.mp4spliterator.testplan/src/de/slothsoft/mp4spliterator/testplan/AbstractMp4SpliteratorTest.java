package de.slothsoft.mp4spliterator.testplan;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotText;
import org.eclipse.swtbot.swt.finder.widgets.TimeoutException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;

import de.slothsoft.mp4spliterator.testplan.constants.Common;
import de.slothsoft.mp4spliterator.testplan.constants.InitWizard;
import de.slothsoft.mp4spliterator.testplan.constants.MainWindow;

public abstract class AbstractMp4SpliteratorTest {

	static {
		System.setProperty("org.eclipse.swtbot.search.defaultKey", "id");
	}

	private static File ffmpegFile;
	private static File moduleFolder;

	protected static File getFfmpegFile() {
		if (ffmpegFile == null) {
			// absolute location of the IT module's dummy/ folder
			final File dummyFolder = new File(getModuleFolder(), "../de.slothsoft.mp4spliterator.it/src/dummy").toPath()
					.normalize().toFile();

			final String osName = System.getProperty("os.name").toLowerCase();
			if (osName.contains("win")) {
				ffmpegFile = new File(dummyFolder, "ffmpeg.bat");
			} else {
				ffmpegFile = new File(dummyFolder, "ffmpeg.sh");
			}

			Assert.assertTrue("ffmpeg file should exist: " + ffmpegFile, ffmpegFile.exists());
		}
		return ffmpegFile;
	}

	protected static File getDemoFilesFolder() {
		return new File(getModuleFolder(), "src/files");
	}

	protected static File createTempFolder() {
		final File result = new File(getTargetFolder(), UUID.randomUUID().toString());
		if (!result.exists()) {
			result.mkdirs();
		}
		return result;
	}

	private static File getTargetFolder() {
		return new File(getModuleFolder(), "target/");
	}

	/**
	 * Absolute location of this module's folder.
	 */

	private static File getModuleFolder() {
		if (moduleFolder == null) {
			moduleFolder = new File(
					AbstractMp4SpliteratorTest.class.getProtectionDomain().getCodeSource().getLocation().getFile());

			// in Eclipse the above is the module folder, in Tych it's target/classes
			if ("classes".equals(moduleFolder.getName())) {
				moduleFolder = moduleFolder.getParentFile().getParentFile();
			}
		}
		return moduleFolder;
	}

	protected final SWTWorkbenchBot bot = new SWTWorkbenchBot();
	private final List<Runnable> tearDowns = new ArrayList<>();

	@Before
	public final void skipInitWizard() {
		final SWTBotShell activeShell = this.bot.activeShell();

		if (InitWizard.TITLE.equals(activeShell.getText())) {
			System.out.println("Initialized Application to ffmpeg: " + getFfmpegFile());

			// we have to initialize the the application the very first time
			final SWTBotText fileText = this.bot.textWithLabel(InitWizard.TEXT_FILE);
			fileText.setText(getFfmpegFile().toString());

			try {
				this.bot.button(Common.BUTTON_FINISH).click();
			} catch (final TimeoutException e) {
				this.bot.button(Common.BUTTON_CANCEL).click();
				throw e; // so other tests can run
			}
		}

		final SWTBotShell newActiveShell = this.bot.activeShell();
		Assert.assertEquals(MainWindow.TITLE, newActiveShell.getText());
	}

	@After
	public final void tearDownRunnables() {
		this.tearDowns.forEach(Runnable::run);
	}

	protected void addToTearDown(Runnable runnable) {
		this.tearDowns.add(runnable);
	}

}