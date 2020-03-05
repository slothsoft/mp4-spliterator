package de.slothsoft.mp4spliterator.init;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;

public class InitWizard extends Wizard {

	public static void openWizardIfNecessary(Shell parentShell) {
		final InitWizard wizard = new InitWizard();
		if (wizard.wizardPages.isEmpty()) {
			return;
		}
		final WizardDialog dialog = new WizardDialog(parentShell, wizard);
		if (dialog.open() == Window.OK) {
			// nothing to do?
		}
	}

	final List<InitWizardPage> wizardPages = new ArrayList<>();

	public InitWizard() {
		InitService.doToAllServices(service -> {
			final InitWizardPage page = service.createInitWizardPage();
			if (page != null) {
				this.wizardPages.add(page);
			}
		});
		needsPreviousAndNextButtons();
		needsProgressMonitor();
	}

	@Override
	public String getWindowTitle() {
		return Messages.getString("InitWizardTitle");
	}

	@Override
	public void addPages() {
		this.wizardPages.forEach(this::addPage);
	}

	@Override
	public boolean performFinish() {
		this.wizardPages.forEach(InitWizardPage::performFinish);
		return true;
	}
}