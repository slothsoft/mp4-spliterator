package de.slothsoft.mp4spliterator;

import org.eclipse.swt.graphics.Image;
import org.junit.Assert;
import org.junit.Test;

public class Mp4SpliteratorPluginTest {

	private final Mp4SpliteratorPlugin plugin = Mp4SpliteratorPlugin.getDefault();

	@Test
	public void testGetImage() {
		final Image image1 = this.plugin.getImage(Mp4SpliteratorImages.OBJ_FILE);
		final Image image2 = this.plugin.getImage(Mp4SpliteratorImages.OBJ_FILE);

		Assert.assertNotNull(image1);
		Assert.assertSame(image1, image2);
	}

}
