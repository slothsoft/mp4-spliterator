package de.slothsoft.mp4spliterator.videos;

import java.io.File;
import java.io.IOException;
import java.lang.ProcessBuilder.Redirect;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.ui.handlers.HandlerUtil;

import de.slothsoft.mp4spliterator.core.Chapter;
import de.slothsoft.mp4spliterator.core.StringifyUtil;
import de.slothsoft.mp4spliterator.core.Video;

public class ExportSplitHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		final DirectoryDialog dialog = new DirectoryDialog(HandlerUtil.getActiveShell(event), SWT.SAVE);
		final String targetFolder = dialog.open();
		if (targetFolder != null) {

			final VideoEditor editor = (VideoEditor) HandlerUtil.getActiveEditor(event);
			final File file = editor.getEditorInput().getFile();
			final Video video = editor.getEditorInput().getVideo();
			final List<Chapter> selectedChapters = editor.getSelectedChapters();

			int index = 0;
			for (final Chapter selectedChapter : selectedChapters) {
				final String startTime = StringifyUtil.stringifyTime(selectedChapter.getStartTime());
				final long endTimeSeconds = (index < selectedChapters.size() - 1
						? selectedChapters.get(index + 1).getStartTime()
						: video.getLength()) - selectedChapter.getStartTime();
				final String endTime = StringifyUtil.stringifyTime(endTimeSeconds);

				// Call ffmpeg to create this chunk of the video using a ffmpeg wrapper
				final String argv[] = {"\"S:\\Development\\Eclipse 2018-12\\ffmpeg\"", "-i", file.toString(), "-ss",
						startTime, "-t", endTime, "-c", "copy",
						'\"' + new File(targetFolder, selectedChapter.getTitle().replaceAll("[?:]", "") + ".mp4")
								.toString() + '\"'};

				try {
					final String arguments = Arrays.stream(argv).collect(Collectors.joining(" "));
					final ProcessBuilder pb = new ProcessBuilder(arguments);
					pb.redirectOutput(Redirect.INHERIT);
					pb.redirectError(Redirect.INHERIT);
					final Process p = pb.start();
				} catch (final IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				index++;
			}

		}

		return null;
	}

}
