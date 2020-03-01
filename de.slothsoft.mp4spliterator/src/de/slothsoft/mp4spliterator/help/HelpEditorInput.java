package de.slothsoft.mp4spliterator.help;

import java.util.Objects;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;

public class HelpEditorInput implements IEditorInput {

	public static HelpEditorInput forDefaultLocale() {
		return new HelpEditorInput(Messages.getString("HelpUrl"));
	}

	private final String url;

	public HelpEditorInput(String url) {
		this.url = Objects.requireNonNull(url);
	}

	public String getUrl() {
		return this.url;
	}

	@Override
	public String getName() {
		return this.url;
	}

	@Override
	public String getToolTipText() {
		return this.url;
	}

	@Override
	public boolean exists() {
		return true;
	}

	@Override
	public <T> T getAdapter(Class<T> adapter) {
		return null;
	}

	@Override
	public ImageDescriptor getImageDescriptor() {
		return null;
	}

	@Override
	public IPersistableElement getPersistable() {
		return null;
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.url);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final HelpEditorInput that = (HelpEditorInput) obj;
		if (!Objects.equals(this.url, that.url)) {
			return false;
		}
		return true;
	}

}
