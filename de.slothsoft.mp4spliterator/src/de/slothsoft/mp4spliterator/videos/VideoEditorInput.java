package de.slothsoft.mp4spliterator.videos;

import java.io.File;
import java.util.Objects;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;

import de.slothsoft.mp4spliterator.core.Video;

public class VideoEditorInput implements IEditorInput {

	private final File file;
	private final Video video;

	public VideoEditorInput(File file, Video video) {
		this.file = Objects.requireNonNull(file);
		this.video = Objects.requireNonNull(video);
	}

	public File getFile() {
		return this.file;
	}

	public Video getVideo() {
		return this.video;
	}

	@Override
	public String getName() {
		return this.file.getName();
	}

	@Override
	public String getToolTipText() {
		return this.file.toString();
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
		return Objects.hash(this.file);
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
		final VideoEditorInput that = (VideoEditorInput) obj;
		if (!Objects.equals(this.file, that.file)) {
			return false;
		}
		return true;
	}

}
