package de.slothsoft.mp4spliterator.impl.ffmpeg;

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.FileFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import de.slothsoft.mp4spliterator.Mp4SpliteratorPlugin;

public class FfmpegPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	public static final String FFMPEG_PATH = "ffmpegPath";
	public static final String FFMPEG_PATH_DEFAULT = "ffmpegPathDefault";

	public FfmpegPreferencePage() {
		super(GRID);
		setPreferenceStore(Mp4SpliteratorPlugin.getDefault().getPreferenceStore());
	}
	@Override
	public void init(IWorkbench workbench) {
		// noop
	}

	@Override
	public void createFieldEditors() {
		final FileFieldEditor fieldEditor = new FileFieldEditor(FFMPEG_PATH, Messages.getString("FfmpegPath") + ':',
				getFieldEditorParent());
		fieldEditor.setEmptyStringAllowed(false);
		addField(fieldEditor);
	}

}