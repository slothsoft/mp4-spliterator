package de.slothsoft.mp4spliterator.impl.ffmpeg;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import de.slothsoft.mp4spliterator.Application;
import de.slothsoft.mp4spliterator.Mp4SpliteratorImages;
import de.slothsoft.mp4spliterator.Mp4SpliteratorPlugin;
import de.slothsoft.mp4spliterator.common.StatusBuilder;
import de.slothsoft.mp4spliterator.init.InitWizardPage;

public class FfmpegWizardPage extends WizardPage implements InitWizardPage {

	static final String FFMPEG_FILE = "ffmpeg.exe";
	static final String FFMPEG_PATH = "ext/" + FFMPEG_FILE;
	static final String FFMPEG_URL = "https://ffmpeg.zeranoe.com/builds/win64/static/ffmpeg-4.2.2-win64-static.zip";
	private static final int BUFFER_SIZE = 4096;

	private Text urlText;
	private Text fileText;

	public FfmpegWizardPage() {
		super("FfmpegWizardPage");
		setTitle(Messages.getString("FfmpegWizardPageTitle"));
		setDescription(Messages.getString("FfmpegWizardPageDescription"));
		setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(Mp4SpliteratorPlugin.ID,
				Mp4SpliteratorImages.WIZARD_FFMPEG));
	}

	@Override
	public void createControl(Composite parent) {
		final Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(GridLayoutFactory.fillDefaults().numColumns(3).create());

		createDescriptionLabel(container, Messages.getString("DownloadDesciption"));

		this.urlText = createTextButton(container, Messages.getString("Url"), Messages.getString("Download"),
				this::download);
		this.urlText.setText(FFMPEG_URL);

		createDescriptionLabel(container, Messages.getString("SelectDesciption"));

		this.fileText = createTextButton(container, Messages.getString("File") + '*',
				Messages.getString("Select") + '\u2026', this::select);
		this.fileText.addModifyListener(e -> updatePageComplete());

		setControl(container);
		setPageComplete(false);
	}

	private static Label createDescriptionLabel(Composite parent, String text) {
		final Label result = new Label(parent, SWT.WRAP);
		result.setText(text);
		result.setLayoutData(GridDataFactory.fillDefaults().span(3, 1).create());
		return result;
	}

	private static Text createTextButton(Composite parent, String labelText, String buttonText, Runnable action) {
		final Label label = new Label(parent, SWT.NONE);
		label.setText(labelText + ':');

		final Text text = new Text(parent, SWT.BORDER);
		text.setLayoutData(GridDataFactory.fillDefaults().grab(true, false).create());

		final Button button = new Button(parent, SWT.NONE);
		button.setText(buttonText);
		button.setLayoutData(GridDataFactory.fillDefaults().create());
		button.addListener(SWT.Selection, e -> action.run());
		return text;
	}

	private void download() {
		try {
			final String url = this.urlText.getText();
			getContainer().run(true, false, monitor -> {
				try {
					monitor.beginTask(Messages.getString("Download") + '\u2026', IProgressMonitor.UNKNOWN);
					downloadAsync(url);
					monitor.done();
				} catch (final IOException e) {
					throw new InvocationTargetException(e);
				}
			});

			final File ffmpegFile = new File(Application.FOLDER, FfmpegWizardPage.FFMPEG_PATH);
			if (ffmpegFile.exists()) {
				this.fileText.setText(FFMPEG_PATH);
				updatePageComplete();
			}
		} catch (final InvocationTargetException e) {
			new StatusBuilder(Messages.getString("CouldNotDownload")).exception((Exception) e.getTargetException())
					.show();
		} catch (final InterruptedException e) {
			// nothing to do?
		}
	}

	private static void downloadAsync(String urlString) throws IOException {
		final File ffmpegFile = new File(Application.FOLDER, FfmpegWizardPage.FFMPEG_PATH);
		ffmpegFile.getParentFile().mkdirs();

		final URL url = new URL(urlString);
		final String ffmpegFileName = '/' + ffmpegFile.getName();

		try (final ZipInputStream zipIn = new ZipInputStream(url.openStream())) {
			ZipEntry entry = zipIn.getNextEntry();
			// iterates over entries in the zip file
			while (entry != null) {
				if (!entry.isDirectory() && entry.getName().endsWith(ffmpegFileName)) {
					// if the entry is THE file, extracts it
					extractFile(zipIn, ffmpegFile);
					break;
				}
				zipIn.closeEntry();
				entry = zipIn.getNextEntry();
			}
		}
	}

	private static void extractFile(ZipInputStream zipIn, File ffmpegFile) throws IOException {
		try (final BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(ffmpegFile))) {
			final byte[] bytesIn = new byte[BUFFER_SIZE];
			int read = 0;
			while ((read = zipIn.read(bytesIn)) != -1) {
				outputStream.write(bytesIn, 0, read);
			}
		}
	}

	private void select() {
		final FileDialog dialog = new FileDialog(this.fileText.getShell(), SWT.OPEN);
		dialog.setFilterExtensions(new String[]{FFMPEG_FILE});
		final String file = dialog.open();
		if (file != null) {
			this.fileText.setText(file);
			updatePageComplete();
		}
	}

	private void updatePageComplete() {
		String errorMessage = null;

		if (this.fileText.getText().isEmpty()) {
			errorMessage = Messages.getString("FileTextMandatory");
		}

		setErrorMessage(errorMessage);
		setPageComplete(errorMessage == null);
	}

	@Override
	public void performFinish() {
		final String file = this.fileText.getText();
		final IPreferenceStore preferences = Mp4SpliteratorPlugin.getDefault().getPreferenceStore();
		preferences.setValue(FfmpegPreferencePage.FFMPEG_PATH_DEFAULT, file);
		preferences.setDefault(FfmpegPreferencePage.FFMPEG_PATH, file);
	}

}