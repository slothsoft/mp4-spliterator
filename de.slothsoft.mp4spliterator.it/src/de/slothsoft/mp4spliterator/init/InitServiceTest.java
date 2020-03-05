package de.slothsoft.mp4spliterator.init;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class InitServiceTest {

	@Test
	public void testConstruction() throws Exception {
		final List<InitService> initServices = InitService.fetchAll();

		Assert.assertNotNull(initServices);
		Assert.assertEquals(1, initServices.size());
	}
}
