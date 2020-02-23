package de.slothsoft.mp4spliterator.videos;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.part.ViewPart;

public class VideoFolderView extends ViewPart {

	public static final String ID = "de.slothsoft.mp4spliterator.VideoFolderView";

	class ViewLabelProvider extends LabelProvider implements ITableLabelProvider {
		@Override
		public String getColumnText(Object obj, int index) {
			return getText(obj);
		}
		@Override
		public Image getColumnImage(Object obj, int index) {
			return getImage(obj);
		}
		@Override
		public Image getImage(Object obj) {
			return VideoFolderView.this.workbench.getSharedImages().getImage(ISharedImages.IMG_OBJ_ELEMENT);
		}
	}

	@Inject
	IWorkbench workbench;

	private TableViewer viewer;

	@Override
	public void createPartControl(Composite parent) {
		this.viewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		this.viewer.setContentProvider(ArrayContentProvider.getInstance());
		this.viewer.setLabelProvider(new ViewLabelProvider());
		this.viewer.setInput(Arrays.asList("A","B","C"));

		getSite().setSelectionProvider(this.viewer);
	}

	@Override
	public void setFocus() {
		this.viewer.getControl().setFocus();
	}
}
