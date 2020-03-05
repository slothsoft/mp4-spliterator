package de.slothsoft.mp4spliterator.impl.ffmpeg;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.UUID;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Shell;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import de.slothsoft.mp4spliterator.Mp4SpliteratorPlugin;

public class FfmpegWizardPageTest {

	private static final String URL_HELLO_WORLD = new File("src/files/ffmpeg.zip").getAbsoluteFile().toURI().toString();

	private Shell shell;
	private Wizard wizard;
	FfmpegWizardPage wizardPage;
	private WizardDialog wizardDialog;
	private Path userDir;

	@Before
	public void setUp() {
		this.userDir = Paths.get("target", UUID.randomUUID().toString());
		System.setProperty("user.dir", this.userDir.toAbsolutePath().toString());

		this.shell = new Shell();
		this.shell.setLayout(new FillLayout());

		this.wizard = new Wizard() {

			@Override
			public void addPages() {
				FfmpegWizardPageTest.this.wizardPage = new FfmpegWizardPage();
				addPage(FfmpegWizardPageTest.this.wizardPage);
			}

			@Override
			public boolean performFinish() {
				return true;
			}
		};
		this.wizardDialog = new WizardDialog(this.shell, this.wizard);
		this.wizardDialog.setBlockOnOpen(false);
	}

	@After
	public void tearDown() {
		this.wizardDialog.close();
		this.shell.dispose();
	}

	@Test
	public void testOpen() throws Exception {
		this.wizardDialog.open();

		Assert.assertNotNull(this.wizardPage);
	}

	@Test
	public void testDownloadAsync() throws Exception {
		final File ffmpegFile = FfmpegWizardPage.downloadAsync(URL_HELLO_WORLD);

		Assert.assertNotNull(ffmpegFile);
		Assert.assertTrue("File should exist: " + ffmpegFile, ffmpegFile.exists());
		Assert.assertEquals(Arrays.asList("correct"), Files.readAllLines(ffmpegFile.toPath()));
	}

	@Test
	public void testDownload() throws Exception {
		this.wizardDialog.open();

		this.wizardPage.setDownloadInFork(false);
		this.wizardPage.urlText.setText(URL_HELLO_WORLD);

		final File ffmpegFile = this.wizardPage.download();
		Assert.assertTrue("File should exist: " + ffmpegFile, ffmpegFile.exists());
		Assert.assertEquals(Arrays.asList("correct"), Files.readAllLines(ffmpegFile.toPath()));
	}

	@Test
	public void testSetDownloadInForkTrue() throws Exception {
		this.wizardDialog.open();
		this.wizardPage.setDownloadInFork(true);

		Assert.assertTrue(this.wizardPage.isDownloadInFork());
	}

	@Test
	public void testSetDownloadInForkFalse() throws Exception {
		this.wizardDialog.open();
		this.wizardPage.setDownloadInFork(false);

		Assert.assertFalse(this.wizardPage.isDownloadInFork());
	}

	@Test
	public void testDownloadInForkFalse() throws Exception {
		this.wizardDialog.open();
		this.wizardPage.downloadInFork(false);

		Assert.assertFalse(this.wizardPage.isDownloadInFork());
	}

	@Test
	public void testDownloadInForkTrue() throws Exception {
		this.wizardDialog.open();
		this.wizardPage.downloadInFork(true);

		Assert.assertTrue(this.wizardPage.isDownloadInFork());
	}

	@Test
	public void testUpdatePageCompleteNullFileText() throws Exception {
		this.wizardDialog.open();
		this.wizardPage.fileText.setText("");
		this.wizardPage.updatePageComplete();

		Assert.assertNotNull(this.wizardPage.getErrorMessage());
	}

	@Test
	public void testUpdatePageCompleteWithFileText() throws Exception {
		this.wizardDialog.open();
		this.wizardPage.fileText.setText("ffmpeg.exe");
		this.wizardPage.updatePageComplete();

		Assert.assertNull(this.wizardPage.getErrorMessage());
	}

	@Test
	public void testPerformFinish() throws Exception {
		this.wizardDialog.open();

		final String fileName = UUID.randomUUID().toString();
		this.wizardPage.fileText.setText(fileName);
		this.wizardPage.performFinish();

		final IPreferenceStore preferences = Mp4SpliteratorPlugin.getDefault().getPreferenceStore();
		Assert.assertEquals(fileName, preferences.getString(FfmpegPreferencePage.FFMPEG_PATH_DEFAULT));
		Assert.assertEquals(fileName, preferences.getDefaultString(FfmpegPreferencePage.FFMPEG_PATH));
	}
}
