package de.slothsoft.mp4spliterator.init;

import java.util.UUID;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Shell;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import de.slothsoft.mp4spliterator.Mp4SpliteratorPlugin;
import de.slothsoft.mp4spliterator.impl.ffmpeg.FfmpegPreferencePage;

public class InitWizardTest {

	private Shell shell;
	private InitWizard wizard;
	private WizardDialog dialog;

	@Before
	public void setUp() {
		this.shell = new Shell();
		this.shell.setLayout(new FillLayout());

		initInitServices();

		this.wizard = new InitWizard();
	}

	private static void initInitServices() {
		// FfmpegInitService
		final IPreferenceStore preferences = Mp4SpliteratorPlugin.getDefault().getPreferenceStore();
		final String ffmpegFile = UUID.randomUUID().toString() + ".exe";
		preferences.setValue(FfmpegPreferencePage.FFMPEG_PATH_DEFAULT, ffmpegFile);
		preferences.setDefault(FfmpegPreferencePage.FFMPEG_PATH, ffmpegFile);
	}

	@After
	public void tearDown() {
		if (this.dialog != null) {
			this.dialog.close();
		}
		this.shell.dispose();
	}

	@Test
	public void testConstruction() throws Exception {
		// test that no services are left to be initialized
		openDialog();

		Assert.assertNotNull(this.wizard.wizardPages);
		Assert.assertEquals(0, this.wizard.wizardPages.size());
	}

	public WizardDialog openDialog() {
		this.dialog = new WizardDialog(this.shell, this.wizard);
		this.dialog.setBlockOnOpen(false);
		this.dialog.open();
		return this.dialog;
	}
}
