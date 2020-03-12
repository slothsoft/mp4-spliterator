package de.slothsoft.mp4spliterator.videos;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.layout.TreeColumnLayout;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.forms.widgets.FormToolkit;

import de.slothsoft.mp4spliterator.common.StatusBuilder;
import de.slothsoft.mp4spliterator.core.Section;
import de.slothsoft.mp4spliterator.core.StringifyUtil;
import de.slothsoft.mp4spliterator.core.VideoPart;
import de.slothsoft.mp4spliterator.videos.VideoEditor.FunctionLabelProvider;

public class ChapterViewer extends Composite {

	final FormToolkit toolkit;
	final TreeViewer viewer;

	List<VideoPart> model = Collections.emptyList();
	Consumer<IStatus> statusHandler = StatusBuilder::showStatus;

	public ChapterViewer(Composite parent, int style) {
		super(parent, style);

		this.toolkit = new FormToolkit(getDisplay());
		addDisposeListener(e -> this.toolkit.dispose());
		this.toolkit.adapt(this);

		final TreeColumnLayout layout = new TreeColumnLayout();
		setLayout(layout);

		final Tree table = this.toolkit.createTree(this,
				SWT.FULL_SELECTION | SWT.CHECK | SWT.H_SCROLL | SWT.V_SCROLL | SWT.MULTI);
		table.setHeaderVisible(true);

		this.viewer = new TreeViewer(table);
		this.viewer.setContentProvider(new TreeArrayContentProvider());
		this.viewer.setLabelProvider(new LabelProvider());
		this.viewer.getTree().addListener(SWT.Selection, this::performCheckIfNecessary);

		final TreeViewerColumn titleColumn = createColumn(this.viewer, Messages.getString("Title"));
		titleColumn.setLabelProvider(new FunctionLabelProvider(VideoPart::getTitle));
		layout.setColumnData(titleColumn.getColumn(), new ColumnWeightData(150));

		final TreeViewerColumn startTimeColumn = createColumn(this.viewer, Messages.getString("StartTime"));
		startTimeColumn.setLabelProvider(new FunctionLabelProvider(c -> StringifyUtil.stringifyTime(c.getStartTime())));
		layout.setColumnData(startTimeColumn.getColumn(), new ColumnWeightData(50));

		final TreeViewerColumn endTimeColumn = createColumn(this.viewer, Messages.getString("EndTime"));
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

	private void performCheckIfNecessary(Event event) {
		if ((event.detail & SWT.CHECK) == SWT.CHECK) {
			performCheck((TreeItem) event.item);
		}
	}

	void performCheck(TreeItem treeItem) {
		final boolean checked = treeItem.getChecked();

		for (final TreeItem child : treeItem.getItems()) {
			child.setChecked(checked);
		}

		final TreeItem parentItem = treeItem.getParentItem();
		if (parentItem != null) {
			parentItem.setChecked(checked);
			for (final TreeItem sibling : parentItem.getItems()) {
				sibling.setChecked(checked);
			}
		}
	}

	private static TreeViewerColumn createColumn(TreeViewer tableViewer, String columnTitle) {
		final TreeViewerColumn result = new TreeViewerColumn(tableViewer, SWT.NONE);

		final TreeColumn column = result.getColumn();
		column.setText(columnTitle);
		return result;
	}

	void checkAll(boolean checked) {
		checkItems(this.viewer.getTree().getItems(), checked);
	}

	private void checkItems(TreeItem[] items, boolean checked) {
		for (final TreeItem tableItem : items) {
			tableItem.setChecked(checked);
			checkItems(tableItem.getItems(), checked);
		}
	}

	private static Button createButton(FormToolkit toolkit, Composite parent, String buttonText, Runnable action) {
		final Button result = toolkit.createButton(parent, buttonText, SWT.PUSH);
		result.addListener(SWT.Selection, e -> action.run());
		result.setLayoutData(GridDataFactory.fillDefaults().create());
		return result;
	}

	public List<VideoPart> getCheckedChapters() {
		final List<VideoPart> result = new ArrayList<>(this.viewer.getTree().getItemCount());
		for (final TreeItem item : this.viewer.getTree().getItems()) {
			if (item.getChecked()) {
				result.add((VideoPart) item.getData());
			}
		}
		return result;
	}

	public void mergeSelectedChapters() {
		final List<VideoPart> selectedParts = getSelectedChapters();
		final IStatus status = validateMergeChapters(selectedParts);
		if (!status.isOK()) {
			this.statusHandler.accept(status);
			return;
		}

		if (!selectedParts.isEmpty()) {
			final List<VideoPart> checkedChapters = new ArrayList<>(getCheckedChapters());

			final List<VideoPart> newModel = new ArrayList<>(this.model);
			final Section mergedSection = mergeParts(selectedParts);
			final int firstIndex = newModel.indexOf(selectedParts.get(0));
			newModel.removeAll(selectedParts);
			newModel.add(firstIndex, mergedSection);

			final ISelection selection = this.viewer.getSelection();
			setModel(newModel);
			this.viewer.setSelection(selection);
			checkAll(false);

			checkedChapters.add(mergedSection);
			checkedChapters.addAll(selectedParts);
			check(checkedChapters, true);
		}
	}

	private static Section mergeParts(final List<VideoPart> selectedParts) {
		Section mergedSection = (Section) selectedParts.stream().filter(p -> p instanceof Section).findAny()
				.orElse(null);

		for (final VideoPart selectedPart : selectedParts) {
			if (selectedPart == mergedSection) {
				continue;
			}
			if (mergedSection == null) {
				// first item
				mergedSection = new Section(selectedPart);
			} else {
				if (selectedPart instanceof Section) {
					mergedSection.addParts(((Section) selectedPart).getParts());
				} else {
					mergedSection.addPart(selectedPart);
				}
			}
		}
		return mergedSection;
	}

	IStatus validateMergeSelectedChapters() {
		return validateMergeChapters(getSelectedChapters());
	}

	IStatus validateMergeChapters(List<VideoPart> selectedParts) {
		selectedParts.sort(Comparator.comparing(this.model::indexOf));

		if (selectedParts.size() < 2) {
			return new StatusBuilder(Messages.getString("ErrorLessThanTwo")).build();
		}

		final int[] indexes = selectedParts.stream().mapToInt(this.model::indexOf).toArray();
		for (int i = 1; i < indexes.length; i++) {
			if (indexes[i] < 0) {
				return new StatusBuilder(MessageFormat.format(Messages.getString("ErrorSectionedChapters"),
						selectedParts.get(i).getTitle())).build();
			}
			if (indexes[i - 1] < 0) {
				return new StatusBuilder(MessageFormat.format(Messages.getString("ErrorSectionedChapters"),
						selectedParts.get(i - 1).getTitle())).build();
			}
			if (indexes[i] - indexes[i - 1] > 1) {
				return new StatusBuilder(Messages.getString("ErrorNoNeighbors")).build();
			}
		}

		return Status.OK_STATUS;
	}

	public void splitSelectedChapters() {
		final List<VideoPart> selectedParts = getSelectedChapters();
		final IStatus status = validateSplitChapters(selectedParts);
		if (!status.isOK()) {
			this.statusHandler.accept(status);
			return;
		}

		if (!selectedParts.isEmpty()) {
			final List<VideoPart> newModel = new ArrayList<>(this.model);
			final List<VideoPart> splitParts = splitParts(selectedParts);
			final int firstIndex = newModel.indexOf(selectedParts.get(0));
			newModel.removeAll(selectedParts);
			newModel.addAll(firstIndex, splitParts);

			setModel(newModel);
			this.viewer.setSelection(new StructuredSelection(splitParts));
			check(splitParts, true);
		}
	}

	private static List<VideoPart> splitParts(final List<VideoPart> selectedParts) {
		return selectedParts.stream().flatMap(p -> Arrays.stream(((Section) p).getParts()))
				.collect(Collectors.toList());
	}

	IStatus validateSplitSelectedChapters() {
		return validateSplitChapters(getSelectedChapters());
	}

	IStatus validateSplitChapters(List<VideoPart> selectedParts) {
		selectedParts.sort(Comparator.comparing(this.model::indexOf));

		for (final VideoPart selectedPart : selectedParts) {
			if (!(selectedPart instanceof Section)) {
				return new StatusBuilder(
						MessageFormat.format(Messages.getString("ErrorNoSectionedChapters"), selectedPart.getTitle()))
								.build();
			}
		}

		return Status.OK_STATUS;
	}

	void check(List<VideoPart> checkedParts, boolean checked) {
		check(this.viewer.getTree().getItems(), checkedParts, checked);
	}

	private void check(TreeItem[] items, List<VideoPart> checkedParts, boolean checked) {
		for (final TreeItem item : items) {
			if (checkedParts.contains(item.getData())) {
				item.setChecked(checked);
			}
			check(item.getItems(), checkedParts, checked);
		}
	}

	public List<VideoPart> getModel() {
		return this.model;
	}

	public ChapterViewer model(List<VideoPart> newModel) {
		setModel(newModel);
		return this;
	}

	public void setModel(List<VideoPart> model) {
		Objects.requireNonNull(model);
		this.model = new ArrayList<>(model);
		this.viewer.setInput(this.model);
		checkAll(true);
	}

	Consumer<IStatus> getStatusHandler() {
		return this.statusHandler;
	}

	ChapterViewer statusHandler(Consumer<IStatus> newStatusHandler) {
		setStatusHandler(newStatusHandler);
		return this;
	}

	void setStatusHandler(Consumer<IStatus> statusHandler) {
		this.statusHandler = Objects.requireNonNull(statusHandler);
	}

	public List<VideoPart> getSelectedChapters() {
		return Arrays.stream(((IStructuredSelection) this.viewer.getSelection()).toArray()).map(p -> (VideoPart) p)
				.collect(Collectors.toList());
	}

	public ChapterViewer selectedChapters(List<VideoPart> newSelectedChapters) {
		setSelectedChapters(newSelectedChapters);
		return this;
	}

	public void setSelectedChapters(List<VideoPart> selectedChapters) {
		this.viewer.setSelection(new StructuredSelection(selectedChapters.toArray()));
	}

	/*
	 * Specific implementatations only for this class.
	 */

	class TreeArrayContentProvider extends ArrayContentProvider implements ITreeContentProvider {

		@Override
		public Object[] getChildren(Object parentElement) {
			return ((Section) parentElement).getParts();
		}

		@Override
		public Object getParent(Object element) {
			final List<VideoPart> theModel = getModel();
			if (theModel.contains(element)) {
				return theModel;
			}
			for (final VideoPart part : theModel) {
				if (part instanceof Section && ((Section) part).containsPart((VideoPart) element)) {
					return part;
				}
			}
			return null;
		}

		@Override
		public boolean hasChildren(Object element) {
			return element instanceof Section;
		}

	}
}
