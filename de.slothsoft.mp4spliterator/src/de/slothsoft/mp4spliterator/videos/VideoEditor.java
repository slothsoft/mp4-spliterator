package de.slothsoft.mp4spliterator.videos;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.menus.IMenuService;
import org.eclipse.ui.part.EditorPart;

import de.slothsoft.mp4spliterator.Mp4SpliteratorImages;
import de.slothsoft.mp4spliterator.Mp4SpliteratorPlugin;
import de.slothsoft.mp4spliterator.core.Chapter;
import de.slothsoft.mp4spliterator.core.StringifyUtil;

public class VideoEditor extends EditorPart {

	public static final String ID = "de.slothsoft.mp4spliterator.VideoEditor"; //$NON-NLS-1$

	public static final String URL_GENERAL_SECTION = "toolbar:de.slothsoft.mp4spliterator.generalSection";//$NON-NLS-1$
	public static final String URL_CHAPTER_SECTION = "toolbar:de.slothsoft.mp4spliterator.chapterSection";//$NON-NLS-1$

	private TableViewer viewer;

	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		if (!(input instanceof VideoEditorInput)) {
			throw new PartInitException("Input must be of type VideoEditorInput: " + input);
		}
		setSite(site);
		setInput(input);
	}

	@Override
	public VideoEditorInput getEditorInput() {
		return (VideoEditorInput) super.getEditorInput();
	}

	@Override
	public void createPartControl(Composite parent) {
		final FormToolkit toolkit = new FormToolkit(parent.getDisplay());
		final ScrolledForm form = toolkit.createScrolledForm(parent);
		form.setText(getEditorInput().getName());
		form.setImage(Mp4SpliteratorPlugin.getDefault().getImage(Mp4SpliteratorImages.OBJ_FILE_VIDEO));
		toolkit.decorateFormHeading(form.getForm());

		final Composite formBody = form.getBody();
		formBody.setLayout(new GridLayout());

		final Composite generalSection = createSection(formBody, toolkit, Messages.getString("General"),
				URL_GENERAL_SECTION);
		generalSection.getParent().setLayoutData(GridDataFactory.fillDefaults().grab(true, false).create());
		createGeneralSection(generalSection, toolkit);

		final Composite chapterSection = createSection(formBody, toolkit, Messages.getString("Chapters"),
				URL_CHAPTER_SECTION);
		chapterSection.getParent().setLayoutData(GridDataFactory.fillDefaults().hint(50, 50).grab(true, true).create());
		createChapterSection(chapterSection, toolkit);

		setPartName(getEditorInput().getName());
	}

	private void createGeneralSection(Composite parent, FormToolkit toolkit) {
		parent.setLayout(new GridLayout(2, false));

		toolkit.createLabel(parent, Messages.getString("File") + ':');
		createText(parent, toolkit, getEditorInput().getFile().toString());

		toolkit.createLabel(parent, Messages.getString("Title") + ':');
		createText(parent, toolkit, getEditorInput().getVideo().getTitle());
	}

	private static Text createText(Composite parent, FormToolkit toolkit, String text) {
		final Text result = toolkit.createText(parent, text, SWT.READ_ONLY);
		result.setForeground(parent.getDisplay().getSystemColor(SWT.COLOR_DARK_GRAY));
		result.setLayoutData(GridDataFactory.fillDefaults().grab(true, false).create());
		return result;
	}

	private Composite createSection(Composite parent, FormToolkit toolkit, String title, String toolbarUrl) {
		final Section section = toolkit.createSection(parent, Section.DESCRIPTION | ExpandableComposite.TITLE_BAR);
		section.setText(title);

		if (toolbarUrl != null) {
			final ToolBarManager toolBarManager = new ToolBarManager(SWT.FLAT);
			final ToolBar toolbar = toolBarManager.createControl(section);
			getSite().getService(IMenuService.class).populateContributionManager(toolBarManager, toolbarUrl);
			toolBarManager.update(true);
			section.setTextClient(toolbar);
		}

		final Composite client = toolkit.createComposite(section, SWT.WRAP);
		section.setClient(client);
		return client;
	}

	private void createChapterSection(Composite parent, FormToolkit toolkit) {
		final TableColumnLayout layout = new TableColumnLayout();
		parent.setLayout(layout);

		final Table table = toolkit.createTable(parent, SWT.FULL_SELECTION | SWT.CHECK | SWT.H_SCROLL | SWT.V_SCROLL);
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

		this.viewer.setInput(getEditorInput().getVideo().getChapters());

		for (final TableItem tableItem : table.getItems()) {
			tableItem.setChecked(true);
		}
	}

	private static TableViewerColumn createColumn(TableViewer tableViewer, String columnTitle) {
		final TableViewerColumn result = new TableViewerColumn(tableViewer, SWT.NONE);

		final TableColumn column = result.getColumn();
		column.setText(columnTitle);
		return result;
	}

	@Override
	public void setFocus() {
		if (this.viewer != null) {
			this.viewer.getControl().setFocus();
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

	public List<Chapter> getSelectedChapters() {
		final List<Chapter> result = new ArrayList<>(this.viewer.getTable().getItemCount());
		for (final TableItem item : this.viewer.getTable().getItems()) {
			if (item.getChecked()) {
				result.add((Chapter) item.getData());
			}
		}
		return result;
	}

	/*
	 * Specific implementations
	 */

	static final class FunctionLabelProvider extends ColumnLabelProvider {

		private final Function<Chapter, String> toStringFunction;

		public FunctionLabelProvider(Function<Chapter, String> toStringFunction) {
			this.toStringFunction = toStringFunction;
		}

		@Override
		public String getText(Object element) {
			return this.toStringFunction.apply((Chapter) element);
		}
	}

}