package de.slothsoft.mp4spliterator.core;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;

/**
 * The video data that should be used to split a file.
 *
 * @author Stef Schulz
 * @since 1.1.0
 */

public class VideoSplit {

	private File sourceFile;
	private File targetFolder;
	private long videoLength = Long.MAX_VALUE;
	private List<VideoPart> chapters = new ArrayList<>();
	private VideoSplitterConfig config = new VideoSplitterConfig();
	private IProgressMonitor progressMonitor = new NullProgressMonitor();

	public VideoSplit(File sourceFile, File targetFolder, List<VideoPart> chapters) {
		this.sourceFile = Objects.requireNonNull(sourceFile);
		this.targetFolder = Objects.requireNonNull(targetFolder);
		this.chapters = Objects.requireNonNull(chapters);
	}

	public List<VideoPart> getChapters() {
		return this.chapters;
	}

	public VideoSplit chapters(List<VideoPart> newChapters) {
		setChapters(newChapters);
		return this;
	}

	public void setChapters(List<VideoPart> chapters) {
		this.chapters = Objects.requireNonNull(chapters);
	}

	public long getVideoLength() {
		return this.videoLength;
	}

	public VideoSplit videoLength(long newLength) {
		setVideoLength(newLength);
		return this;
	}

	public void setVideoLength(long length) {
		this.videoLength = length;
	}

	public File getSourceFile() {
		return this.sourceFile;
	}

	public VideoSplit sourceFile(File newSourceFile) {
		setSourceFile(newSourceFile);
		return this;
	}

	public void setSourceFile(File sourceFile) {
		this.sourceFile = Objects.requireNonNull(sourceFile);
	}

	public File getTargetFolder() {
		return this.targetFolder;
	}

	public VideoSplit targetFolder(File newTargetFolder) {
		setTargetFolder(newTargetFolder);
		return this;
	}

	public void setTargetFolder(File targetFolder) {
		this.targetFolder = Objects.requireNonNull(targetFolder);
	}

	public VideoSplitterConfig getConfig() {
		return this.config;
	}

	public VideoSplit config(VideoSplitterConfig newConfig) {
		setConfig(newConfig);
		return this;
	}

	public void setConfig(VideoSplitterConfig config) {
		this.config = Objects.requireNonNull(config);
	}

	public IProgressMonitor getProgressMonitor() {
		return this.progressMonitor;
	}

	public VideoSplit progressMonitor(IProgressMonitor newProgressMonitor) {
		setProgressMonitor(newProgressMonitor);
		return this;
	}

	public void setProgressMonitor(IProgressMonitor progressMonitor) {
		this.progressMonitor = Objects.requireNonNull(progressMonitor);
	}

	@Override
	public String toString() {
		return "VideoSplit [length=" + this.videoLength + ", chapters=" + this.chapters.size() + "]";
	}
}
