package de.slothsoft.mp4spliterator.videos;

import org.eclipse.ui.IViewPart;
import org.eclipse.ui.PlatformUI;
import org.junit.Assert;
import org.junit.Test;

import de.slothsoft.mp4spliterator.videos.VideoFolderView;

public class VideoFolderViewTest {

	@Test
	public void testOpen() throws Exception {
		final IViewPart view = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
				.showView(VideoFolderView.ID);

		Assert.assertTrue("Error opening view: " + view, view instanceof VideoFolderView);
	}
}
