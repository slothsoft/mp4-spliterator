package de.slothsoft.mp4spliterator.videos;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.handlers.HandlerUtil;

public class OpenVideoFromViewHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		final VideoFolderView view = (VideoFolderView) HandlerUtil.getActivePart(event);
		view.openSelectedFile();
		return null;
	}

}
