package de.slothsoft.mp4spliterator.init;

import org.eclipse.ui.IStartup;
import org.eclipse.ui.PlatformUI;

public class EarlyStartup implements IStartup {

	@Override
	public void earlyStartup() {
		InitService.doToAllServices(InitService::initialize);
		PlatformUI.getWorkbench().getDisplay().asyncExec(() -> {
			InitWizard.openWizardIfNecessary(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell());
		});
	}

}
