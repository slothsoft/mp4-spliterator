package de.slothsoft.mp4spliterator.about;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import org.eclipse.core.runtime.IProduct;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.dialogs.TrayDialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.JFaceColors;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.program.Program;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.IWorkbenchCommandConstants;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.internal.IWorkbenchHelpContextIds;
import org.eclipse.ui.internal.ProductProperties;
import org.eclipse.ui.internal.WorkbenchMessages;
import org.eclipse.ui.internal.about.AboutItem;
import org.eclipse.ui.internal.about.AboutTextManager;
import org.eclipse.ui.menus.CommandContributionItem;
import org.eclipse.ui.menus.CommandContributionItemParameter;
import org.eclipse.ui.services.IServiceLocator;

import de.slothsoft.mp4spliterator.common.StatusBuilder;

/**
 * Displays information about the product.
 */

@SuppressWarnings("restriction")
public class AboutDialog extends TrayDialog {

	private static final String COPY_BUILD_ID_COMMAND = "org.eclipse.ui.ide.copyBuildIdCommand"; //$NON-NLS-1$
	private static final int MAX_IMAGE_WIDTH_FOR_TEXT = 250;

	private String productName;
	private final IProduct product;
	private final ArrayList<Image> images = new ArrayList<>();

	private StyledText text;
	private AboutTextManager aboutTextManager;

	private Browser browser;
	private TableViewer viewer;

	/**
	 * Create an instance of the AboutDialog for the given window.
	 *
	 * @param parentShell The parent of the dialog.
	 */
	public AboutDialog(Shell parentShell) {
		super(parentShell);

		this.product = Platform.getProduct();
		if (this.product != null) {
			this.productName = this.product.getName();
		}
		if (this.productName == null) {
			this.productName = WorkbenchMessages.AboutDialog_defaultProductName;
		}
	}

	@Override
	public boolean close() {
		// dispose all images
		for (int i = 0; i < this.images.size(); ++i) {
			final Image image = this.images.get(i);
			image.dispose();
		}
		return super.close();
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(NLS.bind(WorkbenchMessages.AboutDialog_shellTitle, this.productName));
		PlatformUI.getWorkbench().getHelpSystem().setHelp(newShell, IWorkbenchHelpContextIds.ABOUT_DIALOG);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		final TabFolder folder = new TabFolder(parent, SWT.NONE);
		folder.setLayoutData(GridDataFactory.fillDefaults().grab(true, true).hint(540, 200).create());

		final TabItem mainTab = new TabItem(folder, SWT.NONE);
		mainTab.setText(Messages.getString("About"));
		mainTab.setControl(createMainArea(folder));

		final TabItem thirdPartyTab = new TabItem(folder, SWT.NONE);
		thirdPartyTab.setText(Messages.getString("ThirdParty"));
		thirdPartyTab.setControl(createThirdPartyArea(folder));

		return folder;
	}

	private Control createMainArea(Composite parent) {
		// brand the about box if there is product info
		Image aboutImage = null;
		AboutItem item = null;
		if (this.product != null) {
			final ImageDescriptor imageDescriptor = ProductProperties.getAboutImage(this.product);
			if (imageDescriptor != null) {
				aboutImage = imageDescriptor.createImage();
			}

			// if the about image is small enough, then show the text
			if (aboutImage == null || aboutImage.getBounds().width <= MAX_IMAGE_WIDTH_FOR_TEXT) {
				final String aboutText = ProductProperties.getAboutText(this.product);
				if (aboutText != null) {
					item = AboutTextManager.scan(aboutText);
				}
			}

			if (aboutImage != null) {
				this.images.add(aboutImage);
			}
		}

		// create a composite which is the parent of the top area and the bottom
		// button bar, this allows there to be a second child of this composite with
		// a banner background on top but not have on the bottom
		final Composite workArea = new Composite(parent, SWT.NONE);
		final GridLayout workLayout = new GridLayout();
		workLayout.marginHeight = 0;
		workLayout.marginWidth = 0;
		workLayout.verticalSpacing = 0;
		workLayout.horizontalSpacing = 0;
		workArea.setLayout(workLayout);
		workArea.setLayoutData(new GridData(GridData.FILL_BOTH));

		// page group
		final Color background = JFaceColors.getBannerBackground(parent.getDisplay());
		final Color foreground = JFaceColors.getBannerForeground(parent.getDisplay());
		final Composite top = (Composite) super.createDialogArea(workArea);

		// override any layout inherited from createDialogArea
		GridLayout layout = new GridLayout();
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		layout.verticalSpacing = 0;
		layout.horizontalSpacing = 0;
		top.setLayout(layout);
		top.setLayoutData(new GridData(GridData.FILL_BOTH));
		top.setBackground(background);
		top.setForeground(foreground);

		// the image & text
		final Composite topContainer = new Composite(top, SWT.NONE);
		topContainer.setBackground(background);
		topContainer.setForeground(foreground);

		layout = new GridLayout();
		layout.numColumns = (aboutImage == null || item == null ? 1 : 2);
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		layout.verticalSpacing = 0;
		layout.horizontalSpacing = 0;
		topContainer.setLayout(layout);

		final GC gc = new GC(parent);
		// arbitrary default
		int topContainerHeightHint = 100;
		try {
			// default height enough for 6 lines of text
			topContainerHeightHint = Math.max(topContainerHeightHint, gc.getFontMetrics().getHeight() * 6);
		} finally {
			gc.dispose();
		}

		// image on left side of dialog
		if (aboutImage != null) {
			final Label imageLabel = new Label(topContainer, SWT.NONE);
			imageLabel.setBackground(background);
			imageLabel.setForeground(foreground);

			final GridData data = new GridData();
			data.horizontalAlignment = GridData.FILL;
			data.verticalAlignment = GridData.BEGINNING;
			data.grabExcessHorizontalSpace = false;
			imageLabel.setLayoutData(data);
			imageLabel.setImage(aboutImage);
			topContainerHeightHint = Math.max(topContainerHeightHint, aboutImage.getBounds().height);
		}

		GridData data = new GridData();
		data.horizontalAlignment = GridData.FILL;
		data.verticalAlignment = GridData.FILL;
		data.grabExcessHorizontalSpace = true;
		data.grabExcessVerticalSpace = true;
		data.heightHint = topContainerHeightHint;
		topContainer.setLayoutData(data);

		if (item != null) {
			final int minWidth = 400; // This value should really be calculated
			// from the computeSize(SWT.DEFAULT,
			// SWT.DEFAULT) of all the
			// children in infoArea excluding the
			// wrapped styled text
			// There is no easy way to do this.
			final ScrolledComposite scroller = new ScrolledComposite(topContainer, SWT.V_SCROLL | SWT.H_SCROLL);
			data = new GridData(GridData.FILL_BOTH);
			data.widthHint = minWidth;
			scroller.setLayoutData(data);

			final Composite textComposite = new Composite(scroller, SWT.NONE);
			textComposite.setBackground(background);

			layout = new GridLayout();
			textComposite.setLayout(layout);

			this.text = new StyledText(textComposite, SWT.MULTI | SWT.WRAP | SWT.READ_ONLY);
			this.text.setFont(parent.getFont());
			this.text.setText(item.getText());
			this.text.setCursor(null);
			this.text.setBackground(background);
			this.text.setForeground(foreground);

			this.aboutTextManager = new AboutTextManager(this.text);
			this.aboutTextManager.setItem(item);

			createTextMenu();

			final GridData gd = new GridData();
			gd.verticalAlignment = GridData.BEGINNING;
			gd.horizontalAlignment = GridData.FILL;
			gd.grabExcessHorizontalSpace = true;
			this.text.setLayoutData(gd);

			// Adjust the scrollbar increments
			scroller.getHorizontalBar().setIncrement(20);
			scroller.getVerticalBar().setIncrement(20);

			final boolean[] inresize = new boolean[1]; // flag to stop unneccesary
			// recursion
			textComposite.addControlListener(new ControlAdapter() {
				@Override
				public void controlResized(ControlEvent e) {
					if (inresize[0]) {
						return;
					}
					inresize[0] = true;
					// required because of bugzilla report 4579
					textComposite.layout(true);
					// required because you want to change the height that the
					// scrollbar will scroll over when the width changes.
					final int width = textComposite.getClientArea().width;
					final Point p = textComposite.computeSize(width, SWT.DEFAULT);
					scroller.setMinSize(minWidth, p.y);
					inresize[0] = false;
				}
			});

			scroller.setExpandHorizontal(true);
			scroller.setExpandVertical(true);
			final Point p = textComposite.computeSize(minWidth, SWT.DEFAULT);
			textComposite.setSize(p.x, p.y);
			scroller.setMinWidth(minWidth);
			scroller.setMinHeight(p.y);

			scroller.setContent(textComposite);
		}

		// horizontal bar
		Label bar = new Label(workArea, SWT.HORIZONTAL | SWT.SEPARATOR);
		data = new GridData();
		data.horizontalAlignment = GridData.FILL;
		bar.setLayoutData(data);

		// add image buttons for bundle groups that have them
		final Composite bottom = (Composite) super.createDialogArea(workArea);
		// override any layout inherited from createDialogArea
		layout = new GridLayout();
		bottom.setLayout(layout);
		data = new GridData();
		data.horizontalAlignment = SWT.FILL;
		data.verticalAlignment = SWT.FILL;
		data.grabExcessHorizontalSpace = true;

		bottom.setLayoutData(data);

		// spacer
		bar = new Label(bottom, SWT.NONE);
		data = new GridData();
		data.horizontalAlignment = GridData.FILL;
		bar.setLayoutData(data);

		return workArea;
	}

	private Control createThirdPartyArea(Composite parent) {
		final Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(GridLayoutFactory.fillDefaults().create());

		final Composite tableComposite = new Composite(composite, SWT.NONE);
		final TableColumnLayout layout = new TableColumnLayout();
		tableComposite.setLayout(layout);
		final Table table = new Table(tableComposite, SWT.FULL_SELECTION | SWT.H_SCROLL | SWT.V_SCROLL);
		table.setHeaderVisible(true);

		this.viewer = new TableViewer(table);
		this.viewer.setContentProvider(ArrayContentProvider.getInstance());
		this.viewer.setLabelProvider(new LabelProvider());
		this.viewer.addSelectionChangedListener(e -> openSelectedDependencyInBrowser());

		final TableViewerColumn titleColumn = createColumn(this.viewer, Messages.getString("DisplayName"));
		titleColumn.setLabelProvider(new FunctionLabelProvider(ThirdPartyDependency::getDisplayName));
		layout.setColumnData(titleColumn.getColumn(), new ColumnWeightData(150));

		final TableViewerColumn startTimeColumn = createColumn(this.viewer, Messages.getString("Url"));
		startTimeColumn.setLabelProvider(new FunctionLabelProvider(ThirdPartyDependency::getUrl));
		layout.setColumnData(startTimeColumn.getColumn(), new ColumnWeightData(50));

		final Button goToButton = new Button(composite, SWT.NONE);
		goToButton.setText(Messages.getString("GoTo"));
		goToButton.setLayoutData(GridDataFactory.swtDefaults().align(SWT.RIGHT, SWT.TOP).create());
		goToButton.addListener(SWT.Selection, e -> openSelectedDependencyExternally());

		this.browser = new Browser(composite, SWT.NONE);
		this.browser.setLayoutData(GridDataFactory.fillDefaults().grab(true, true).hint(100, 100).create());

		// do stuff with the actual dependencies

		final List<ThirdPartyDependency> thirdPartyDependencies = ThirdPartyDependency.readDefaults();
		tableComposite.setLayoutData(GridDataFactory.fillDefaults().grab(true, false)
				.hint(100, (thirdPartyDependencies.size() + 2) * table.getItemHeight()).create());
		this.viewer.setInput(thirdPartyDependencies);
		this.viewer.setSelection(new StructuredSelection(thirdPartyDependencies.get(0)));

		return composite;
	}

	private static TableViewerColumn createColumn(TableViewer tableViewer, String columnTitle) {
		final TableViewerColumn result = new TableViewerColumn(tableViewer, SWT.NONE);

		final TableColumn column = result.getColumn();
		column.setText(columnTitle);
		return result;
	}

	private void openSelectedDependencyExternally() {
		final ThirdPartyDependency dependency = getSelectedDependency();
		Program.launch(dependency.url);
	}

	private void openSelectedDependencyInBrowser() {
		try {
			final ThirdPartyDependency dependency = getSelectedDependency();
			this.browser.setUrl(dependency.licenseUrl);
		} catch (final Exception exception) {
			new StatusBuilder(exception.getMessage()).exception(exception).show();
		}
	}

	private ThirdPartyDependency getSelectedDependency() {
		return (ThirdPartyDependency) ((IStructuredSelection) this.viewer.getSelection()).getFirstElement();
	}

	/**
	 * Create the context menu for the text widget.
	 *
	 * @since 3.4
	 */
	private void createTextMenu() {
		final MenuManager textManager = new MenuManager();
		final IServiceLocator serviceLocator = PlatformUI.getWorkbench();
		final ICommandService commandService = serviceLocator.getService(ICommandService.class);
		textManager.add(new CommandContributionItem(new CommandContributionItemParameter(serviceLocator, null,
				IWorkbenchCommandConstants.EDIT_COPY, CommandContributionItem.STYLE_PUSH)));
		if (commandService.getCommand(COPY_BUILD_ID_COMMAND).isDefined()) {
			textManager.add(new CommandContributionItem(new CommandContributionItemParameter(serviceLocator, null,
					COPY_BUILD_ID_COMMAND, CommandContributionItem.STYLE_PUSH)));
		}
		textManager.add(new CommandContributionItem(new CommandContributionItemParameter(serviceLocator, null,
				IWorkbenchCommandConstants.EDIT_SELECT_ALL, CommandContributionItem.STYLE_PUSH)));
		this.text.setMenu(textManager.createContextMenu(this.text));
		this.text.addDisposeListener(e -> textManager.dispose());

	}

	@Override
	protected boolean isResizable() {
		return true;
	}

	/*
	 * Specific implementations
	 */

	static final class FunctionLabelProvider extends ColumnLabelProvider {

		private final Function<ThirdPartyDependency, String> toStringFunction;

		public FunctionLabelProvider(Function<ThirdPartyDependency, String> toStringFunction) {
			this.toStringFunction = toStringFunction;
		}

		@Override
		public String getText(Object element) {
			return this.toStringFunction.apply((ThirdPartyDependency) element);
		}
	}
}
