package de.slothsoft.mp4spliterator.impl.ffmpeg;

import java.io.File;

import org.eclipse.jface.preference.IPreferenceStore;

import de.slothsoft.mp4spliterator.Application;
import de.slothsoft.mp4spliterator.Mp4SpliteratorPlugin;
import de.slothsoft.mp4spliterator.init.InitService;
import de.slothsoft.mp4spliterator.init.InitWizardPage;

public class FfmpegInitService implements InitService {

	File applicationFolder = Application.FOLDER;

	@Override
	public void initializePreferences(IPreferenceStore preferences) {
		final String value = preferences.getString(FfmpegPreferencePage.FFMPEG_PATH_DEFAULT);
		if (!value.isEmpty()) {
			preferences.setDefault(FfmpegPreferencePage.FFMPEG_PATH, value);
			return;
		}
		final File ffmpegFile = new File(this.applicationFolder, FfmpegWizardPage.FFMPEG_PATH);
		if (ffmpegFile.exists()) {
			final String ffmpegFileWithoutApplicationFolder = ffmpegFile.toString()
					.substring(this.applicationFolder.toString().length() + 1);
			preferences.setValue(FfmpegPreferencePage.FFMPEG_PATH_DEFAULT, ffmpegFileWithoutApplicationFolder);
			preferences.setDefault(FfmpegPreferencePage.FFMPEG_PATH, ffmpegFileWithoutApplicationFolder);
		}
	}

	@Override
	public InitWizardPage createInitWizardPage() {
		final IPreferenceStore preferences = Mp4SpliteratorPlugin.getDefault().getPreferenceStore();
		if (preferences.getString(FfmpegPreferencePage.FFMPEG_PATH_DEFAULT).isEmpty()) {
			return new FfmpegWizardPage();
		}
		// all initialized
		return null;
	}
}
