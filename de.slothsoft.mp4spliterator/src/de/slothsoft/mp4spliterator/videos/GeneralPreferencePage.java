package de.slothsoft.mp4spliterator.videos;

import org.eclipse.jface.preference.DirectoryFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import de.slothsoft.mp4spliterator.Mp4SpliteratorPlugin;
import de.slothsoft.mp4spliterator.Mp4SpliteratorPreferences;

public class GeneralPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	public static final String ID = "de.slothsoft.mp4spliterator.videos.GeneralPreferencePage";

	public GeneralPreferencePage() {
		super(GRID);
		setPreferenceStore(Mp4SpliteratorPlugin.getDefault().getPreferenceStore());
	}
	@Override
	public void init(IWorkbench workbench) {
		// noop
	}

	@Override
	public void createFieldEditors() {
		final DirectoryFieldEditor fieldEditor = new DirectoryFieldEditor(Mp4SpliteratorPreferences.VIDEO_FOLDER,
				Messages.getString("VideoFolder") + ':', getFieldEditorParent());
		fieldEditor.setEmptyStringAllowed(false);
		addField(fieldEditor);
	}

}