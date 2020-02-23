package de.slothsoft.mp4spliterator.videos;

import java.io.File;

import org.eclipse.ui.IViewPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.junit.Assert;
import org.junit.Test;

public class VideoFolderViewTest {

	@Test
	public void testOpenView() throws Exception {
		openView();
	}

	private VideoFolderView openView() throws PartInitException {
		final IViewPart view = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
				.showView(VideoFolderView.ID);
		Assert.assertTrue("Error opening view: " + view, view instanceof VideoFolderView);
		return (VideoFolderView) view;
	}

	@Test
	public void testGetExtension() throws Exception {
		Assert.assertEquals("png", VideoFolderView.getExtension(new File("test.png")));
		Assert.assertEquals("wav", VideoFolderView.getExtension(new File("test.WAV")));
		Assert.assertEquals("jpg", VideoFolderView.getExtension(new File("test.jPg")));
	}

	@Test
	public void testGetExtensionAlmostBroken() throws Exception {
		Assert.assertEquals("", VideoFolderView.getExtension(new File("test")));
		Assert.assertEquals("c", VideoFolderView.getExtension(new File("a.b.c")));
	}

	@Test
	public void testHasSupportedExtension() throws Exception {
		final VideoFolderView view = openView();

		Assert.assertTrue(view.hasSupportedExtension(new File("test.mp4")));
		Assert.assertTrue(view.hasSupportedExtension(new File("test.MP4")));
	}

	@Test
	public void testHasSupportedExtensionFalse() throws Exception {
		final VideoFolderView view = openView();

		Assert.assertFalse(view.hasSupportedExtension(new File("test.wav")));
		Assert.assertFalse(view.hasSupportedExtension(new File("test.png")));
	}
}
