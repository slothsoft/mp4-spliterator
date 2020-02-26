package de.slothsoft.mp4spliterator.videos;

import java.io.File;
import java.io.IOException;
import java.util.Set;

import javax.inject.Inject;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.ViewPart;

import de.slothsoft.mp4spliterator.Mp4SpliteratorImages;
import de.slothsoft.mp4spliterator.Mp4SpliteratorPlugin;
import de.slothsoft.mp4spliterator.Mp4SpliteratorPreferences;
import de.slothsoft.mp4spliterator.common.Refreshable;
import de.slothsoft.mp4spliterator.core.Video;
import de.slothsoft.mp4spliterator.core.VideoReader;

public class VideoFolderView extends ViewPart implements Refreshable {

	public static final String ID = "de.slothsoft.mp4spliterator.VideoFolderView";

	@Inject
	IWorkbench workbench;

	private Set<String> supportedExtensions;
	private TreeViewer viewer;

	@Override
	public void createPartControl(Composite parent) {
		this.supportedExtensions = VideoReader.getAllSupportedExtensions();

		this.viewer = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		this.viewer.setLabelProvider(new FileLabelProvider());
		this.viewer.setContentProvider(new FileTreeContentProvider());
		this.viewer.setInput(new File(Mp4SpliteratorPlugin.getDefault().getPreferenceStore()
				.getString(Mp4SpliteratorPreferences.VIDEO_FOLDER)));
		this.viewer.addDoubleClickListener(e -> openSelectedFile());

		getSite().setSelectionProvider(this.viewer);
	}

	private void openSelectedFile() {
		final File selectedFile = (File) ((IStructuredSelection) this.viewer.getSelection()).getFirstElement();
		if (selectedFile == null) {
			// TODO Auto-generated catch block
		} else if (hasSupportedExtension(selectedFile)) {
			openFile(selectedFile);
		} else {
			// TODO Auto-generated catch block
		}
	}

	public void openFile(File file) {
		try {
			final Video video = VideoReader.readVideo(file);
			final VideoEditorInput input = new VideoEditorInput(file, video);
			this.workbench.getActiveWorkbenchWindow().getActivePage().openEditor(input, VideoEditor.ID);
		} catch (final PartInitException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void setFocus() {
		this.viewer.getControl().setFocus();
	}

	boolean hasSupportedExtension(File file) {
		return this.supportedExtensions.contains(getExtension(file));
	}

	static String getExtension(File file) {
		final String fileName = file.getName();
		final int index = fileName.lastIndexOf('.');
		if (index > 0) {
			return fileName.substring(index + 1).toLowerCase();
		}
		return "";
	}

	@Override
	public void refresh() {
		final Object[] expandedTreePaths = this.viewer.getExpandedElements();
		final ISelection selection = this.viewer.getSelection();
		this.viewer.setInput(new File(Mp4SpliteratorPlugin.getDefault().getPreferenceStore()
				.getString(Mp4SpliteratorPreferences.VIDEO_FOLDER)));
		this.viewer.setExpandedElements(expandedTreePaths);
		this.viewer.setSelection(selection);
	}

	/*
	 * Specific implementations
	 */

	static final class FileTreeContentProvider implements ITreeContentProvider {

		@Override
		public Object[] getElements(Object inputElement) {
			return ((File) inputElement).listFiles();
		}

		@Override
		public Object[] getChildren(Object parentElement) {
			return ((File) parentElement).listFiles();
		}

		@Override
		public Object getParent(Object element) {
			return ((File) element).getParentFile();
		}

		@Override
		public boolean hasChildren(Object element) {
			return ((File) element).isDirectory();
		}
	}

	final class FileLabelProvider extends LabelProvider {

		@Override
		public String getText(Object element) {
			return ((File) element).getName();
		}

		@Override
		public Image getImage(Object obj) {
			if (((File) obj).isDirectory()) {
				return Mp4SpliteratorPlugin.getDefault().getImage(Mp4SpliteratorImages.OBJ_FOLDER);
			}
			if (hasSupportedExtension((File) obj)) {
				return Mp4SpliteratorPlugin.getDefault().getImage(Mp4SpliteratorImages.OBJ_FILE_VIDEO);
			}
			return Mp4SpliteratorPlugin.getDefault().getImage(Mp4SpliteratorImages.OBJ_FILE);
		}
	}

}
