package de.slothsoft.mp4spliterator.videos;

import java.io.File;

import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class OpenVideoHandlerTest {

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
	public void testOpenVideoFile() throws Exception {
		final IEditorPart editor = OpenVideoHandler.openVideoFile(new File("src/files/with_chapters.mp4"));
		Assert.assertTrue("Error opening editor: " + editor, editor instanceof VideoEditor);
	}

}
