package de.slothsoft.mp4spliterator.core;

import org.eclipse.jface.preference.IPreferenceStore;
import org.junit.Assert;
import org.junit.Test;

import de.slothsoft.mp4spliterator.Mp4SpliteratorPlugin;
import de.slothsoft.mp4spliterator.Mp4SpliteratorPreferences;

public class VideoSplitterConfigTest {

	@Test
	public void testForPreferences() throws Exception {
		final IPreferenceStore preferences = Mp4SpliteratorPlugin.getDefault().getPreferenceStore();
		preferences.setValue(Mp4SpliteratorPreferences.PATTERN, "test");
		preferences.setValue(Mp4SpliteratorPreferences.START_TIME_SHIFT, "12345");
		preferences.setValue(Mp4SpliteratorPreferences.END_TIME_SHIFT, "67890");

		final VideoSplitterConfig config = VideoSplitterConfig.forPreferences();

		Assert.assertEquals("test", config.getPattern());
		Assert.assertEquals(12345, config.getStartTimeShift());
		Assert.assertEquals(67890, config.getEndTimeShift());
	}
}
