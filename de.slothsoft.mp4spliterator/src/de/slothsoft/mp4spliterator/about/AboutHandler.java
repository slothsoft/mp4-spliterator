package de.slothsoft.mp4spliterator.about;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.handlers.HandlerUtil;

public class AboutHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		final AboutDialog dialog = new AboutDialog(HandlerUtil.getActiveShell(event));
		dialog.open();
		return null;
	}

}
