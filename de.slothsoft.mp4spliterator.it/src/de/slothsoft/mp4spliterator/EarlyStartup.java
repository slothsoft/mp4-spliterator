package de.slothsoft.mp4spliterator;

import java.util.UUID;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.IStartup;

import de.slothsoft.mp4spliterator.impl.ffmpeg.FfmpegPreferencePage;

public class EarlyStartup implements IStartup {

	@Override
	public void earlyStartup() {
		final IPreferenceStore preferences = Mp4SpliteratorPlugin.getDefault().getPreferenceStore();
		final String ffmpegFile = UUID.randomUUID().toString() + ".exe";
		preferences.setValue(FfmpegPreferencePage.FFMPEG_PATH_DEFAULT, ffmpegFile);
		preferences.setDefault(FfmpegPreferencePage.FFMPEG_PATH, ffmpegFile);
	}

}
