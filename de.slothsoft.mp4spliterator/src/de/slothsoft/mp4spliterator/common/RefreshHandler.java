package de.slothsoft.mp4spliterator.common;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.handlers.HandlerUtil;

public class RefreshHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		final Refreshable refreshable = (Refreshable) HandlerUtil.getActivePart(event);
		refreshable.refresh();
		return null;
	}

}
