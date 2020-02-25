package de.slothsoft.mp4spliterator;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

import de.slothsoft.mp4spliterator.videos.VideoFolderView;

public class Perspective implements IPerspectiveFactory {

	public static final String ID = "de.slothsoft.mp4spliterator.Perspective";

	@Override
	public void createInitialLayout(IPageLayout layout) {
		layout.setEditorAreaVisible(false);
		layout.setFixed(true);
		layout.addStandaloneView(VideoFolderView.ID, false, IPageLayout.LEFT, 0.2f, layout.getEditorArea());
	}

}
