package de.slothsoft.mp4spliterator.common;

import java.util.Arrays;
import java.util.function.Function;

import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;

public interface GlobalConstants {

	String DATA_ID = "id";

	// TODO: periodically check if the following lines are still necessary

	boolean SWTBOT_MODE = Arrays.stream(Platform.getApplicationArgs()).filter(s -> s.equals("-swtBot")).count() > 0;

	static String openNativeDialog(Shell parentShell, Function<Shell, String> nativeDialog) {
		if (GlobalConstants.SWTBOT_MODE) {
			return GlobalConstants.openInputDialog(parentShell);
		}
		return nativeDialog.apply(parentShell);
	}

	static String openInputDialog(Shell parentShell) {
		final InputDialog dialog = new InputDialog(parentShell, "SWTBot Input", "Input:", null, null);
		if (dialog.open() == Window.OK) {
			return dialog.getValue();
		}
		return null;
	}
}
