package de.slothsoft.example;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

public class Perspective implements IPerspectiveFactory {

	public static final String ID = "de.slothsoft.example.Perspective";

	@Override
	public void createInitialLayout(IPageLayout layout) {
		layout.setEditorAreaVisible(false);
		layout.setFixed(true);

		final IFolderLayout mainFolder = layout.createFolder("main", IPageLayout.BOTTOM, 0.5f, layout.getEditorArea());
		mainFolder.addView(ExampleView.ID);
	}

}
