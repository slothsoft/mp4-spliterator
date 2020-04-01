package de.slothsoft.mp4spliterator.videos;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.SWT;
import org.eclipse.swt.program.Program;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.ui.handlers.HandlerUtil;

import de.slothsoft.mp4spliterator.common.GlobalConstants;
import de.slothsoft.mp4spliterator.common.StatusBuilder;
import de.slothsoft.mp4spliterator.core.Video;
import de.slothsoft.mp4spliterator.core.VideoPart;
import de.slothsoft.mp4spliterator.core.VideoSplit;
import de.slothsoft.mp4spliterator.core.VideoSplitter;
import de.slothsoft.mp4spliterator.core.VideoSplitterConfig;
import de.slothsoft.mp4spliterator.core.VideoSplitterException;

public class ExportSplitHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		final String targetFolderString = openDirectoryDialog(event);
		if (targetFolderString != null) {

			final VideoEditor editor = (VideoEditor) HandlerUtil.getActiveEditor(event);

			try {
				new ProgressMonitorDialog(HandlerUtil.getActiveShell(event)).run(true, true,
						new ExportSplitOperation(editor, targetFolderString));
			} catch (final InvocationTargetException e) {
				final Throwable targetException = e.getTargetException();
				if (targetException instanceof Exception) {
					new StatusBuilder(targetException.getLocalizedMessage()).exception((Exception) targetException)
							.show();
				} else {
					new StatusBuilder(targetException.getLocalizedMessage()).exception(e).show();
				}
			} catch (final InterruptedException e) {
				// user cancelled dialog
			}

		}
		return null;
	}

	private static String openDirectoryDialog(ExecutionEvent event) {
		return GlobalConstants.openNativeDialog(HandlerUtil.getActiveShell(event), shell -> {
			final DirectoryDialog dialog = new DirectoryDialog(shell, SWT.SAVE);
			return dialog.open();
		});
	}

	// TODO: I think we can test the following part (to see the right config etc.
	// is used)

	static class ExportSplitOperation implements IRunnableWithProgress {

		final File file;
		final Video video;
		final List<VideoPart> selectedChapters;
		final String targetFolderString;
		final boolean openTargetFolder;

		public ExportSplitOperation(VideoEditor editor, String targetFolderString) {
			this.file = editor.getEditorInput().getFile();
			this.video = editor.getEditorInput().getVideo();
			this.selectedChapters = editor.getCheckedChapters();
			this.targetFolderString = targetFolderString;
			this.openTargetFolder = !GlobalConstants.SWTBOT_MODE;
		}

		@Override
		public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
			final File targetFolder = new File(this.targetFolderString);
			try {
				final VideoSplitter splitter = VideoSplitter.createInstance();
				splitter.splitIntoChapters(new VideoSplit(this.file, targetFolder, this.selectedChapters)
						.videoLength(this.video.getLength()).config(VideoSplitterConfig.forPreferences())
						.progressMonitor(monitor));

				if (this.openTargetFolder) {
					Program.launch(this.targetFolderString);
				}
			} catch (final VideoSplitterException e) {
				new StatusBuilder(e.getMessage()).exception(e).show();
			}
		}
	}
}
