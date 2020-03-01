package de.slothsoft.mp4spliterator.init;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import org.eclipse.jface.preference.IPreferenceStore;
import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;

import de.slothsoft.mp4spliterator.Mp4SpliteratorPlugin;
import de.slothsoft.mp4spliterator.common.StatusBuilder;

/**
 * A service to init specific parts of the application. The part can:
 * <ul>
 * <li>{@link #initializePreferences(IPreferenceStore)} - decouple preference
 * initialization</li>
 * <li>{@link #createInitWizardPage()} - hook to {@link InitWizard}</li>
 * <li>{@link #initialize()} - general initialization</li>
 * </ul>
 *
 * @author Stef Schulz
 * @since 1.1.0
 */

public interface InitService {

	static List<InitService> fetchAll() {
		final List<InitService> result = new ArrayList<>();
		doToAllServices(result::add);
		return result;
	}

	static void doToAllServices(Consumer<InitService> function) {
		try {
			final BundleContext bundleContext = Mp4SpliteratorPlugin.getDefault().getBundle().getBundleContext();
			for (final ServiceReference<?> service : bundleContext.getAllServiceReferences(InitService.class.getName(),
					null)) {
				function.accept((InitService) bundleContext.getService(service));
				bundleContext.ungetService(service);
			}
		} catch (final InvalidSyntaxException e) {
			// cannot happen
			new StatusBuilder("🤯").exception(e).show();
		}
	}

	@SuppressWarnings("unused")
	default void initializePreferences(IPreferenceStore preferences) {
		// do nothing on default
	}

	default void initialize() {
		// do nothing on default
	}

	default InitWizardPage createInitWizardPage() {
		return null;
	}
}
