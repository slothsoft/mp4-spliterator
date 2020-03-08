package de.slothsoft.mp4spliterator.videos;

import org.eclipse.jface.preference.PreferenceDialog;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.dialogs.PreferencesUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class VideoSplitterPreferencePageTest {

	private Shell shell;
	private PreferenceDialog dialog;
	private VideoSplitterPreferencePage page;

	@Before
	public void setUp() {
		this.shell = new Shell();
		this.shell.setLayout(new FillLayout());
		this.dialog = PreferencesUtil.createPreferenceDialogOn(this.shell, VideoSplitterPreferencePage.ID, null, null);
		this.dialog.setBlockOnOpen(false);
		this.dialog.open();

		this.page = (VideoSplitterPreferencePage) this.dialog.getSelectedPage();
	}

	@Test
	public void testConstruction() throws Exception {
		Assert.assertNotNull(this.page);
	}
}
