package de.slothsoft.mp4spliterator.videos;

import java.io.File;
import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.program.Program;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.ui.handlers.HandlerUtil;

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
		final DirectoryDialog dialog = new DirectoryDialog(HandlerUtil.getActiveShell(event), SWT.SAVE);
		final String targetFolderString = dialog.open();
		if (targetFolderString != null) {

			final VideoEditor editor = (VideoEditor) HandlerUtil.getActiveEditor(event);
			// TODO: I think we can test the following part (to see the right config etc.
			// is used)
			final File file = editor.getEditorInput().getFile();
			final Video video = editor.getEditorInput().getVideo();
			final List<VideoPart> selectedChapters = editor.getCheckedChapters();
			final File targetFolder = new File(targetFolderString);
			try {
				final VideoSplitter splitter = VideoSplitter.createInstance();
				splitter.splitIntoChapters(new VideoSplit(file, targetFolder, selectedChapters)
						.videoLength(video.getLength()).config(VideoSplitterConfig.forPreferences()));
				Program.launch(targetFolderString);
			} catch (final VideoSplitterException e) {
				new StatusBuilder(e.getMessage()).exception(e).show();
			}
		}
		return null;
	}

}
