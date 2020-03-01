package de.slothsoft.mp4spliterator.impl.ffmpeg;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.UUID;

import org.eclipse.jface.preference.IPreferenceStore;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import de.slothsoft.mp4spliterator.Mp4SpliteratorPlugin;

public class FfmpegInitServiceTest {

	private File applicationFolder;
	private FfmpegInitService service;
	private IPreferenceStore preferences;

	@Before
	public void setUp() throws IOException {
		this.applicationFolder = Files.createTempDirectory("app-").toFile();
		this.applicationFolder.mkdirs();

		this.service = new FfmpegInitService();
		this.service.applicationFolder = this.applicationFolder;

		this.preferences = Mp4SpliteratorPlugin.getDefault().getPreferenceStore();
		this.preferences.setToDefault(FfmpegPreferencePage.FFMPEG_PATH);
		this.preferences.setToDefault(FfmpegPreferencePage.FFMPEG_PATH_DEFAULT);

		this.preferences.setDefault(FfmpegPreferencePage.FFMPEG_PATH, "");
		this.preferences.setDefault(FfmpegPreferencePage.FFMPEG_PATH_DEFAULT, "");
	}

	@Test
	public void testNothingExists() throws Exception {
		this.service.initializePreferences(this.preferences);

		// we cannot infer anything from nothing
		Assert.assertEquals("", this.preferences.getString(FfmpegPreferencePage.FFMPEG_PATH));
		Assert.assertEquals("", this.preferences.getDefaultString(FfmpegPreferencePage.FFMPEG_PATH));
		Assert.assertEquals("", this.preferences.getString(FfmpegPreferencePage.FFMPEG_PATH_DEFAULT));
	}

	@Test
	public void testDefaultExists() throws Exception {
		final String id = UUID.randomUUID().toString();
		this.preferences.setValue(FfmpegPreferencePage.FFMPEG_PATH_DEFAULT, id);

		this.service.initializePreferences(this.preferences);

		// use default in other preference
		Assert.assertEquals(id, this.preferences.getString(FfmpegPreferencePage.FFMPEG_PATH));
		Assert.assertEquals(id, this.preferences.getDefaultString(FfmpegPreferencePage.FFMPEG_PATH));
		Assert.assertEquals(id, this.preferences.getString(FfmpegPreferencePage.FFMPEG_PATH_DEFAULT));
	}

	@Test
	public void testFileExists() throws Exception {
		final File ffmpegFile = new File(this.service.applicationFolder, FfmpegWizardPage.FFMPEG_PATH);
		ffmpegFile.getParentFile().mkdirs();
		Files.write(ffmpegFile.toPath(), "".getBytes());

		this.service.initializePreferences(this.preferences);

		// the file was downloaded already so enter it
		final String ffmpegFileWithoutApplicationFolder = new File("ext/ffmpeg.exe").toString();
		Assert.assertEquals(ffmpegFileWithoutApplicationFolder,
				this.preferences.getString(FfmpegPreferencePage.FFMPEG_PATH));
		Assert.assertEquals(ffmpegFileWithoutApplicationFolder,
				this.preferences.getDefaultString(FfmpegPreferencePage.FFMPEG_PATH));
		Assert.assertEquals(ffmpegFileWithoutApplicationFolder,
				this.preferences.getString(FfmpegPreferencePage.FFMPEG_PATH_DEFAULT));
	}

	@Test
	public void testDefaultAndFileExists() throws Exception {
		final File ffmpegFile = new File(this.service.applicationFolder, FfmpegWizardPage.FFMPEG_PATH);
		ffmpegFile.getParentFile().mkdirs();
		Files.write(ffmpegFile.toPath(), "".getBytes());

		final String id = UUID.randomUUID().toString();
		this.preferences.setValue(FfmpegPreferencePage.FFMPEG_PATH_DEFAULT, id);

		this.service.initializePreferences(this.preferences);

		// default is more important than file
		Assert.assertEquals(id, this.preferences.getString(FfmpegPreferencePage.FFMPEG_PATH));
		Assert.assertEquals(id, this.preferences.getDefaultString(FfmpegPreferencePage.FFMPEG_PATH));
		Assert.assertEquals(id, this.preferences.getString(FfmpegPreferencePage.FFMPEG_PATH_DEFAULT));
	}
}
