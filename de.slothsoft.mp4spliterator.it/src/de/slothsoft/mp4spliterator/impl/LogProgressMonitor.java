package de.slothsoft.mp4spliterator.impl;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;

public class LogProgressMonitor implements IProgressMonitor {

	private final List<String> log = new ArrayList<>();

	public List<String> getLog() {
		return this.log;
	}

	@Override
	public void beginTask(String name, int totalWork) {
		this.log.add("TASK: " + name + " (" + totalWork + ')');
	}

	@Override
	public void setTaskName(String name) {
		this.log.add("TASK: " + name);
	}

	@Override
	public void subTask(String name) {
		this.log.add("SUBTASK: " + name);
	}

	@Override
	public void worked(int work) {
		this.log.add("WORKED: " + work);
	}

	@Override
	public void done() {
		this.log.add("DONE.");
	}

	@Override
	public void internalWorked(double work) {
		// not used?
	}

	@Override
	public boolean isCanceled() {
		return false; // not used?
	}

	@Override
	public void setCanceled(boolean value) {
		// not used?
	}

}
