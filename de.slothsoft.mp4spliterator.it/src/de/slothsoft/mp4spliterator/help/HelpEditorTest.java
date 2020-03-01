package de.slothsoft.mp4spliterator.help;

import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class HelpEditorTest {

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
		openEditor(HelpEditorInput.forDefaultLocale());
	}

	private HelpEditor openEditor(HelpEditorInput input) throws PartInitException {
		final IEditorPart editor = this.activePage.openEditor(input, HelpEditor.ID);
		Assert.assertTrue("Error opening editor: " + editor, editor instanceof HelpEditor);
		return (HelpEditor) editor;
	}

}
