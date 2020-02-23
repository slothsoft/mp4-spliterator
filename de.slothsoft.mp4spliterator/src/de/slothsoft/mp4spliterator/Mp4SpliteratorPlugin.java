package de.slothsoft.mp4spliterator;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

public class Mp4SpliteratorPlugin extends AbstractUIPlugin {

	public static final String ID = "de.slothsoft.mp4spliterator";

	private static Mp4SpliteratorPlugin instance;

	public static Mp4SpliteratorPlugin getDefault() {
		return instance;
	}

	private final Map<String, Image> images = new HashMap<>();

	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		instance = this;
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		instance = null;
		super.stop(context);
	}

	public Image getImage(String path) {
		Image result = this.images.get(path);
		if (result == null) {
			result = imageDescriptorFromPlugin(ID, path).createImage();
			this.images.put(path, result);
		}
		return result;
	}
}
