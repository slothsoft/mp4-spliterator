package de.slothsoft.mp4spliterator.about;

import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Shell;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class AboutDialogTest {

	private Shell shell;
	private AboutDialog aboutDialog;

	@Before
	public void setUp() {
		this.shell = new Shell();
		this.shell.setLayout(new FillLayout());

		this.aboutDialog = new AboutDialog(this.shell);
		this.aboutDialog.setBlockOnOpen(false);
	}

	@After
	public void tearDown() {
		this.aboutDialog.close();
		this.shell.dispose();
	}

	@Test
	public void testOpen() throws Exception {
		this.aboutDialog.open();

		Assert.assertNotNull(this.aboutDialog.productName);
	}
}
