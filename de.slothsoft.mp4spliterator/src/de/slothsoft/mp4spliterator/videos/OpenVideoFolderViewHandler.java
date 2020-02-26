package de.slothsoft.mp4spliterator.videos;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import de.slothsoft.mp4spliterator.common.StatusBuilder;

public class OpenVideoFolderViewHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		try {
			PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(VideoFolderView.ID);
		} catch (final PartInitException e) {
			new StatusBuilder(Messages.getString("CannotOpenView")).exception(e).show();
		}
		return null;
	}

}
