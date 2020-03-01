package de.slothsoft.mp4spliterator.help;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import de.slothsoft.mp4spliterator.common.StatusBuilder;

public class HelpHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		openEditor();
		return null;
	}

	static Object openEditor() {
		try {
			final HelpEditorInput input = HelpEditorInput.forDefaultLocale();
			final IEditorPart editor = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
					.openEditor(input, HelpEditor.ID);
			if (editor instanceof HelpEditor) {
				((HelpEditor) editor).reset();
				return editor;
			}
			return null;
		} catch (final PartInitException e) {
			new StatusBuilder(Messages.getString("CannotOpenEditor")).exception(e).show();
			return null;
		}
	}

}
