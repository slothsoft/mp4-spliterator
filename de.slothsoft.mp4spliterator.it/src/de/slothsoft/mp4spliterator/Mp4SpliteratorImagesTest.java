package de.slothsoft.mp4spliterator;

import java.lang.reflect.Field;

import org.eclipse.swt.graphics.Image;
import org.junit.Assert;
import org.junit.Test;

public class Mp4SpliteratorImagesTest {

	private final Mp4SpliteratorPlugin plugin = Mp4SpliteratorPlugin.getDefault();

	@Test
	public void testGetImage() throws Exception {
		for (final Field field : Mp4SpliteratorImages.class.getFields()) {
			final String imagePath = (String) field.get(null);
			final Image image = this.plugin.getImage(imagePath);
			Assert.assertNotNull("Field " + field.getName() + " has no image:" + imagePath, image);
		}
	}

}
