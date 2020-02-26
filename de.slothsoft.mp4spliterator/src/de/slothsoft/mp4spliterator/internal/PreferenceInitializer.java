package de.slothsoft.mp4spliterator.internal;

import java.io.File;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

import de.slothsoft.mp4spliterator.Mp4SpliteratorPlugin;
import de.slothsoft.mp4spliterator.Mp4SpliteratorPreferences;

/**
 * Class used to initialize default preference values.
 */

public class PreferenceInitializer extends AbstractPreferenceInitializer {

	@Override
	public void initializeDefaultPreferences() {
		final IPreferenceStore preferences = Mp4SpliteratorPlugin.getDefault().getPreferenceStore();
		preferences.setDefault(Mp4SpliteratorPreferences.VIDEO_FOLDER,
				new File(System.getProperty("user.home"), "Videos").toString());
	}

}
