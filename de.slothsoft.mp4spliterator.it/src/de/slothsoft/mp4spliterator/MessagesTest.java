package de.slothsoft.mp4spliterator;

import org.junit.Assert;
import org.junit.Test;

public class MessagesTest {

	@Test
	public void testValid() throws Exception {
		final String i18n = Messages.getString("ApplicationTitle");
		Assert.assertNotNull(i18n);
		Assert.assertFalse(i18n.startsWith("!"));
		Assert.assertFalse(i18n.endsWith("!"));
	}

	@Test
	public void testMissing() throws Exception {
		Assert.assertEquals("!unknown!", Messages.getString("unknown"));
	}
}
