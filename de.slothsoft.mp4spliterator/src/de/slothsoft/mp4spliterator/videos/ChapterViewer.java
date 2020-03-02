package de.slothsoft.mp4spliterator.videos;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.forms.widgets.FormToolkit;

import de.slothsoft.mp4spliterator.core.Chapter;
import de.slothsoft.mp4spliterator.core.StringifyUtil;
import de.slothsoft.mp4spliterator.videos.VideoEditor.FunctionLabelProvider;

public class ChapterViewer extends Composite {

	final FormToolkit toolkit;
	final TableViewer viewer;

	List<Chapter> model = Collections.emptyList();

	public ChapterViewer(Composite parent, int style) {
		super(parent, style);

		this.toolkit = new FormToolkit(getDisplay());
		addDisposeListener(e -> this.toolkit.dispose());
		this.toolkit.adapt(this);

		final TableColumnLayout layout = new TableColumnLayout();
		setLayout(layout);

		final Table table = this.toolkit.createTable(this,
				SWT.FULL_SELECTION | SWT.CHECK | SWT.H_SCROLL | SWT.V_SCROLL);
		table.setHeaderVisible(true);

		this.viewer = new TableViewer(table);
		this.viewer.setContentProvider(ArrayContentProvider.getInstance());
		this.viewer.setLabelProvider(new LabelProvider());

		final TableViewerColumn titleColumn = createColumn(this.viewer, Messages.getString("Title"));
		titleColumn.setLabelProvider(new FunctionLabelProvider(Chapter::getTitle));
		layout.setColumnData(titleColumn.getColumn(), new ColumnWeightData(150));

		final TableViewerColumn startTimeColumn = createColumn(this.viewer, Messages.getString("StartTime"));
		startTimeColumn.setLabelProvider(new FunctionLabelProvider(c -> StringifyUtil.stringifyTime(c.getStartTime())));
		layout.setColumnData(startTimeColumn.getColumn(), new ColumnWeightData(50));

		final TableViewerColumn endTimeColumn = createColumn(this.viewer, Messages.getString("EndTime"));
		endTimeColumn.setLabelProvider(new FunctionLabelProvider(c -> StringifyUtil.stringifyTime(c.getEndTime())));
		layout.setColumnData(endTimeColumn.getColumn(), new ColumnWeightData(50));

		this.viewer.setInput(this.model);
		checkAll(true);

		final Composite buttonComposite = this.toolkit.createComposite(parent);
		buttonComposite.setLayoutData(GridDataFactory.fillDefaults().grab(false, true).create());
		buttonComposite.setLayout(GridLayoutFactory.fillDefaults().create());

		createButton(this.toolkit, buttonComposite, Messages.getString("CheckAll"), () -> checkAll(true));
		createButton(this.toolkit, buttonComposite, Messages.getString("CheckNone"), () -> checkAll(false));
	}

	private static TableViewerColumn createColumn(TableViewer tableViewer, String columnTitle) {
		final TableViewerColumn result = new TableViewerColumn(tableViewer, SWT.NONE);

		final TableColumn column = result.getColumn();
		column.setText(columnTitle);
		return result;
	}

	void checkAll(boolean checked) {
		for (final TableItem tableItem : this.viewer.getTable().getItems()) {
			tableItem.setChecked(checked);
		}
	}

	private static Button createButton(FormToolkit toolkit, Composite parent, String buttonText, Runnable action) {
		final Button result = toolkit.createButton(parent, buttonText, SWT.PUSH);
		result.addListener(SWT.Selection, e -> action.run());
		result.setLayoutData(GridDataFactory.fillDefaults().create());
		return result;
	}

	public List<Chapter> getSelectedChapters() {
		final List<Chapter> result = new ArrayList<>(this.viewer.getTable().getItemCount());
		for (final TableItem item : this.viewer.getTable().getItems()) {
			if (item.getChecked()) {
				result.add((Chapter) item.getData());
			}
		}
		return result;
	}

	public List<Chapter> getModel() {
		return this.model;
	}

	public ChapterViewer model(List<Chapter> newModel) {
		setModel(newModel);
		return this;
	}

	public void setModel(List<Chapter> model) {
		this.model = Objects.requireNonNull(model);
		this.viewer.setInput(this.model);
		checkAll(true);
	}

}
