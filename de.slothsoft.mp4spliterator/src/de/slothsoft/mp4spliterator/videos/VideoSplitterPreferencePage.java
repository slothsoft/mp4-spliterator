package de.slothsoft.mp4spliterator.videos;

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import de.slothsoft.mp4spliterator.Mp4SpliteratorPlugin;
import de.slothsoft.mp4spliterator.Mp4SpliteratorPreferences;

public class VideoSplitterPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	public static final String ID = "de.slothsoft.mp4spliterator.videos.VideoSplitterPreferencePage";

	public VideoSplitterPreferencePage() {
		super(GRID);
		setPreferenceStore(Mp4SpliteratorPlugin.getDefault().getPreferenceStore());
	}

	@Override
	public void init(IWorkbench workbench) {
		// noop
	}

	@Override
	public void createFieldEditors() {
		StringFieldEditor fieldEditor = new StringFieldEditor(Mp4SpliteratorPreferences.PATTERN,
				Messages.getString("Pattern") + ':', getFieldEditorParent());
		fieldEditor.setEmptyStringAllowed(false);
		addField(fieldEditor);

		fieldEditor = new StringFieldEditor(Mp4SpliteratorPreferences.START_TIME_SHIFT,
				Messages.getString("StartTimeShift") + ':', getFieldEditorParent());
		fieldEditor.setEmptyStringAllowed(false);
		addField(fieldEditor);

		fieldEditor = new StringFieldEditor(Mp4SpliteratorPreferences.END_TIME_SHIFT,
				Messages.getString("EndTimeShift") + ':', getFieldEditorParent());
		fieldEditor.setEmptyStringAllowed(false);
		addField(fieldEditor);
	}
}