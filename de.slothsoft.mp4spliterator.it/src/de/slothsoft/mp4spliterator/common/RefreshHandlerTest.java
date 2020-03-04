package de.slothsoft.mp4spliterator.common;

import java.io.File;
import java.util.HashMap;
import java.util.UUID;

import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.expressions.EvaluationContext;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.ui.ISources;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ICommandService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import de.slothsoft.mp4spliterator.videos.VideoFolderView;

public class RefreshHandlerTest {

	private IWorkbenchPage workbenchPage;
	private IWorkbenchWindow site;
	private Command command;

	@Before
	public void setUp() {
		this.workbenchPage = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		this.site = this.workbenchPage.getWorkbenchWindow();
		this.command = this.site.getService(ICommandService.class).getCommand("de.slothsoft.mp4spliterator.refresh");
	}

	@Test
	public void testExecute() throws Exception {
		final VideoFolderView refreshable = openView();

		final EvaluationContext context = new EvaluationContext(null, new Object());
		context.addVariable(ISources.ACTIVE_PART_NAME, refreshable);
		final ExecutionEvent event = new ExecutionEvent(this.command, new HashMap<>(), null, context);

		final File fakeFile = new File(UUID.randomUUID().toString());
		refreshable.getAdapter(TreeViewer.class).setInput(fakeFile);

		this.command.executeWithChecks(event);

		Assert.assertNotEquals(fakeFile, refreshable.getAdapter(TreeViewer.class).getInput());
	}

	private VideoFolderView openView() throws PartInitException {
		final IViewPart view = this.workbenchPage.showView(VideoFolderView.ID);
		Assert.assertTrue("Error opening view: " + view, view instanceof VideoFolderView);
		return (VideoFolderView) view;
	}

}
