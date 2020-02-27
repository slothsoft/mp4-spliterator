package de.slothsoft.mp4spliterator.about;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import org.junit.Assert;
import org.junit.Test;

public class ThirdPartyDependencyTest {

	@Test
	public void testReadDefaults() throws Exception {
		final List<ThirdPartyDependency> result = ThirdPartyDependency.readDefaults();

		Assert.assertNotNull(result);
		Assert.assertTrue(result.size() > 0);
	}

	@Test
	public void testReadFromResourceBundleEmpty() throws Exception {
		final ResourceBundle bundle = createResourceBundle("");

		final List<ThirdPartyDependency> result = ThirdPartyDependency.readFromResourceBundle(bundle);

		Assert.assertNotNull(result);
		Assert.assertEquals(0, result.size());
	}

	private static PropertyResourceBundle createResourceBundle(String string) throws IOException {
		try (InputStream input = new ByteArrayInputStream(string.getBytes())) {
			return new PropertyResourceBundle(input);
		}
	}

	@Test
	public void testReadFromResourceBundleSize0() throws Exception {
		final ResourceBundle bundle = createResourceBundle("size=0");

		final List<ThirdPartyDependency> result = ThirdPartyDependency.readFromResourceBundle(bundle);

		Assert.assertNotNull(result);
		Assert.assertEquals(0, result.size());
	}

	@Test
	public void testReadFromResourceBundleSize1() throws Exception {
		final ResourceBundle bundle = createResourceBundle("size=1\n0.displayName=A\n0.url=B\n0.licenseUrl=C");

		final List<ThirdPartyDependency> result = ThirdPartyDependency.readFromResourceBundle(bundle);

		Assert.assertNotNull(result);
		Assert.assertEquals(1, result.size());

		final ThirdPartyDependency dep = result.get(0);
		Assert.assertEquals("A", dep.getDisplayName());
		Assert.assertEquals("B", dep.getUrl());
		Assert.assertEquals("C", dep.getLicenseUrl());
	}

	@Test
	public void testReadFromResourceBundleSize2() throws Exception {
		final ResourceBundle bundle = createResourceBundle(
				"size=2\n0.displayName=A\n0.url=B\n0.licenseUrl=C\n1.displayName=D");

		final List<ThirdPartyDependency> result = ThirdPartyDependency.readFromResourceBundle(bundle);

		Assert.assertNotNull(result);
		Assert.assertEquals(2, result.size());

		ThirdPartyDependency dep = result.get(0);
		Assert.assertEquals("A", dep.getDisplayName());
		Assert.assertEquals("B", dep.getUrl());
		Assert.assertEquals("C", dep.getLicenseUrl());

		dep = result.get(1);
		Assert.assertEquals("D", dep.getDisplayName());
		Assert.assertEquals(null, dep.getUrl());
		Assert.assertEquals(null, dep.getLicenseUrl());
	}

	@Test
	public void testReadFromResourceBundleSize3() throws Exception {
		final ResourceBundle bundle = createResourceBundle(
				"size=3\n0.displayName=A\n0.url=B\n0.licenseUrl=C\n1.displayName=D\n2.displayName=E\n2.url=F\n2.licenseUrl=G");

		final List<ThirdPartyDependency> result = ThirdPartyDependency.readFromResourceBundle(bundle);

		Assert.assertNotNull(result);
		Assert.assertEquals(3, result.size());

		ThirdPartyDependency dep = result.get(0);
		Assert.assertEquals("A", dep.getDisplayName());
		Assert.assertEquals("B", dep.getUrl());
		Assert.assertEquals("C", dep.getLicenseUrl());

		dep = result.get(1);
		Assert.assertEquals("D", dep.getDisplayName());
		Assert.assertEquals(null, dep.getUrl());
		Assert.assertEquals(null, dep.getLicenseUrl());

		dep = result.get(2);
		Assert.assertEquals("E", dep.getDisplayName());
		Assert.assertEquals("F", dep.getUrl());
		Assert.assertEquals("G", dep.getLicenseUrl());
	}

}
