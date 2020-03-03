package de.slothsoft.mp4spliterator.common;

import java.util.Objects;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.ui.statushandlers.StatusManager;

import de.slothsoft.mp4spliterator.Mp4SpliteratorPlugin;

public class StatusBuilder {

	public static void showStatus(IStatus status) {
		StatusManager.getManager().handle(status, StatusManager.SHOW | StatusManager.LOG);
	}

	private int severity = IStatus.ERROR;
	private String pluginId = Mp4SpliteratorPlugin.ID;
	private String message;
	private Exception exception;

	public StatusBuilder(String message) {
		this.message = Objects.requireNonNull(message);
	}

	public void show() {
		showStatus(build());
	}

	public IStatus build() {
		return new Status(this.severity, this.pluginId, this.message, this.exception);
	}

	public Exception getException() {
		return this.exception;
	}

	public StatusBuilder exception(Exception newException) {
		setException(newException);
		return this;
	}

	public void setException(Exception exception) {
		this.exception = exception;
	}

	public String getMessage() {
		return this.message;
	}

	public StatusBuilder message(String newMessage) {
		setMessage(newMessage);
		return this;
	}

	public void setMessage(String message) {
		this.message = Objects.requireNonNull(message);
	}

	public String getPluginId() {
		return this.pluginId;
	}

	public StatusBuilder pluginId(String newPluginId) {
		setPluginId(newPluginId);
		return this;
	}

	public void setPluginId(String pluginId) {
		this.pluginId = pluginId;
	}

	public int getSeverity() {
		return this.severity;
	}

	public StatusBuilder severity(int newSeverity) {
		setSeverity(newSeverity);
		return this;
	}

	public void setSeverity(int severity) {
		this.severity = severity;
	}

}