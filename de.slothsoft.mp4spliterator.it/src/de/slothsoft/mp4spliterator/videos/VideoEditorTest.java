package de.slothsoft.mp4spliterator.videos;

import java.io.File;

import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import de.slothsoft.mp4spliterator.core.Video;

public class VideoEditorTest {

	private IWorkbenchPage activePage;

	@Before
	public void setUp() {
		this.activePage = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		closeAllEditors();
	}

	private void closeAllEditors() {
		this.activePage.closeAllEditors(false);
	}

	@After
	public void tearDown() {
		closeAllEditors();
	}

	@Test
	public void testOpenEditor() throws Exception {
		openEditor(new VideoEditorInput(new File("test.txt"), new Video("Test")));
	}

	private VideoEditor openEditor(VideoEditorInput input) throws PartInitException {
		final IEditorPart editor = this.activePage.openEditor(input, VideoEditor.ID);
		Assert.assertTrue("Error opening editor: " + editor, editor instanceof VideoEditor);
		return (VideoEditor) editor;
	}

}
