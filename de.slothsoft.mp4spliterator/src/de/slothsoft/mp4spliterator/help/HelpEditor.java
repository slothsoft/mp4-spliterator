package de.slothsoft.mp4spliterator.help;

import java.io.File;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;

public class HelpEditor extends EditorPart {

	public static final String ID = "de.slothsoft.mp4spliterator.HelpEditor"; //$NON-NLS-1$

	private Browser browser;

	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		if (!(input instanceof HelpEditorInput)) {
			throw new PartInitException("Input must be of type HelpEditorInput: " + input);
		}
		setSite(site);
		setInput(input);
	}

	@Override
	public HelpEditorInput getEditorInput() {
		return (HelpEditorInput) super.getEditorInput();
	}

	@Override
	public void createPartControl(Composite parent) {
		this.browser = new Browser(parent, SWT.NONE);
		this.browser.setUrl(fetchUrl());
	}

	private String fetchUrl() {
		final File localHelpFolder = new File(System.getProperty("user.dir"), "docs");
		final File localHelp = new File(localHelpFolder, getEditorInput().getUrl());
		if (localHelp.exists()) {
			return localHelp.toURI().toString();
		}
		return "https://slothsoft.github.io/mp4-spliterator/" + getEditorInput().getUrl();
	}

	public void reset() {
		this.browser.setUrl(fetchUrl());
	}

	@Override
	public void setFocus() {
		if (this.browser != null) {
			this.browser.setFocus();
		}
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		// not supported
	}

	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}

	@Override
	public void doSaveAs() {
		// not supported
	}

	@Override
	public boolean isDirty() {
		return false;
	}

}