package de.slothsoft.mp4spliterator.common;

import java.util.UUID;

import org.eclipse.core.runtime.IStatus;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class StatusBuilderTest {

	private StatusBuilder builder;

	@Before
	public void setUp() {
		this.builder = new StatusBuilder("message");
	}

	@Test
	public void testException() {
		final Exception exception = new Exception();
		this.builder.exception(exception);

		Assert.assertSame(exception, this.builder.getException());
		Assert.assertSame(exception, this.builder.build().getException());
	}

	@Test
	public void testSetException() {
		final Exception exception = new Exception();
		this.builder.setException(exception);

		Assert.assertSame(exception, this.builder.getException());
		Assert.assertSame(exception, this.builder.build().getException());
	}

	@Test
	public void testMessage() {
		final String message = UUID.randomUUID().toString();
		this.builder.message(message);

		Assert.assertSame(message, this.builder.getMessage());
		Assert.assertSame(message, this.builder.build().getMessage());
	}

	@Test
	public void testSetMessage() {
		final String message = UUID.randomUUID().toString();
		this.builder.setMessage(message);

		Assert.assertSame(message, this.builder.getMessage());
		Assert.assertSame(message, this.builder.build().getMessage());
	}

	@Test
	public void testMessageFromConstructor() {
		final String message = UUID.randomUUID().toString();
		this.builder = new StatusBuilder(message);

		Assert.assertSame(message, this.builder.getMessage());
		Assert.assertSame(message, this.builder.build().getMessage());
	}

	@Test
	public void testPluginId() {
		final String pluginId = UUID.randomUUID().toString();
		this.builder.pluginId(pluginId);

		Assert.assertSame(pluginId, this.builder.getPluginId());
		Assert.assertSame(pluginId, this.builder.build().getPlugin());
	}

	@Test
	public void testSetPluginId() {
		final String pluginId = UUID.randomUUID().toString();
		this.builder.setPluginId(pluginId);

		Assert.assertSame(pluginId, this.builder.getPluginId());
		Assert.assertSame(pluginId, this.builder.build().getPlugin());
	}

	@Test
	public void testSeverity() {
		final int severity = IStatus.ERROR;
		this.builder.severity(severity);

		Assert.assertEquals(severity, this.builder.getSeverity());
		Assert.assertEquals(severity, this.builder.build().getSeverity());
	}

	@Test
	public void testSetSeverity() {
		final int severity = IStatus.CANCEL;
		this.builder.setSeverity(severity);

		Assert.assertEquals(severity, this.builder.getSeverity());
		Assert.assertEquals(severity, this.builder.build().getSeverity());
	}

}