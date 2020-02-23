package de.slothsoft.example;

import org.eclipse.ui.IViewPart;
import org.eclipse.ui.PlatformUI;
import org.junit.Assert;
import org.junit.Test;

public class ExampleViewTest {

	@Test
	public void testOpen() throws Exception {
		final IViewPart view = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
				.showView(ExampleView.ID);

		Assert.assertTrue("Error opening view: " + view, view instanceof ExampleView);
	}
}
