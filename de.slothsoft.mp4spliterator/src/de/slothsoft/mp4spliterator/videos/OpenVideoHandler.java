package de.slothsoft.mp4spliterator.videos;

import java.io.File;
import java.io.IOException;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;

import de.slothsoft.mp4spliterator.common.GlobalConstants;
import de.slothsoft.mp4spliterator.common.StatusBuilder;
import de.slothsoft.mp4spliterator.core.Video;
import de.slothsoft.mp4spliterator.core.VideoReader;

public class OpenVideoHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		final String videoFileString = GlobalConstants.openNativeDialog(HandlerUtil.getActiveShell(event), shell -> {
			final FileDialog dialog = new FileDialog(HandlerUtil.getActiveShell(event), SWT.OPEN);
			dialog.setFilterExtensions(
					VideoReader.getAllSupportedExtensions().stream().map(ext -> "*." + ext).toArray(String[]::new));
			return dialog.open();
		});
		if (videoFileString != null) {
			openVideoFile(new File(videoFileString));
		}
		return null;
	}

	static VideoEditor openVideoFile(File file) {
		try {
			final Video video = VideoReader.readVideo(file);
			final VideoEditorInput input = new VideoEditorInput(file, video);
			final IEditorPart editor = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
					.openEditor(input, VideoEditor.ID);
			if (editor instanceof VideoEditor) {
				return (VideoEditor) editor;
			}
			return null;
		} catch (final IOException e) {
			new StatusBuilder(Messages.getString("CannotParseVideo")).exception(e).show();
			return null;
		} catch (final PartInitException e) {
			new StatusBuilder(Messages.getString("CannotOpenEditor")).exception(e).show();
			return null;
		}
	}

}
