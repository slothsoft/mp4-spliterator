package de.slothsoft.mp4spliterator.impl.ffmpeg;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

final class Messages {

	private static final String BUNDLE_NAME = "de.slothsoft.mp4spliterator.impl.ffmpeg.messages"; //$NON-NLS-1$
	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);

	public static String getString(String key) {
		try {
			return RESOURCE_BUNDLE.getString(key);
		} catch (final MissingResourceException e) {
			return '!' + key + '!';
		}
	}

	private Messages() {
		// hide
	}
}
