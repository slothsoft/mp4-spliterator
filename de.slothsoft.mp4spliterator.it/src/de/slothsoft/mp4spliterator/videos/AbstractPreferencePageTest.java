package de.slothsoft.mp4spliterator.videos;

import org.eclipse.jface.preference.IPreferencePage;
import org.eclipse.jface.preference.PreferenceDialog;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.dialogs.PreferencesUtil;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public abstract class AbstractPreferencePageTest<P extends IPreferencePage> {

	private final String id;

	private Shell shell;
	private PreferenceDialog dialog;
	protected P page;

	public AbstractPreferencePageTest(String id) {
		this.id = id;
	}

	@Before
	@SuppressWarnings("unchecked")
	public void setUp() {
		this.shell = new Shell();
		this.shell.setLayout(new FillLayout());
		this.dialog = PreferencesUtil.createPreferenceDialogOn(this.shell, this.id, new String[]{this.id}, null);
		this.dialog.setBlockOnOpen(false);
		this.dialog.open();

		this.page = (P) this.dialog.getSelectedPage();
	}

	@After
	public void tearDown() {
		this.dialog.close();
		this.shell.close();
	}

	@Test
	public void testConstruction() throws Exception {
		Assert.assertNotNull(this.page);
	}
}
