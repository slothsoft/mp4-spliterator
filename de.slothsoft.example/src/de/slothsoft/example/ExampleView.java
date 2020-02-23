package de.slothsoft.example;

import java.text.DateFormat;
import java.util.ArrayList;
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

import de.slothsoft.random.types.DateRandomField;

public class ExampleView extends ViewPart {

	public static final String ID = "de.slothsoft.example.ExampleView";

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
			return ExampleView.this.workbench.getSharedImages().getImage(ISharedImages.IMG_OBJ_ELEMENT);
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
		this.viewer.setInput(createRandomValues());

		getSite().setSelectionProvider(this.viewer);
	}

	private static Collection<String> createRandomValues() {
		final DateRandomField dateField = new DateRandomField();
		final List<String> result = new ArrayList<>();
		final DateFormat format = DateFormat.getInstance();
		for (int i = 0; i < 7; i++) {
			result.add(format.format(dateField.nextValue()));
		}
		return result;
	}

	@Override
	public void setFocus() {
		this.viewer.getControl().setFocus();
	}
}
