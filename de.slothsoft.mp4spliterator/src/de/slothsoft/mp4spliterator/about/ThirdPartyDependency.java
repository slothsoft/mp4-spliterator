package de.slothsoft.mp4spliterator.about;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.MissingResourceException;
import java.util.Objects;
import java.util.ResourceBundle;

public class ThirdPartyDependency {

	static final String SIZE = "size";
	static final String DISPLAY_NAME = ".displayName";
	static final String URL = ".url";
	static final String LICENSE_URL = ".licenseUrl";

	public static List<ThirdPartyDependency> readDefaults() {
		return readFromResourceBundle(Messages.DEPENDENCIES);
	}

	static List<ThirdPartyDependency> readFromResourceBundle(ResourceBundle bundle) {
		final List<ThirdPartyDependency> result = new ArrayList<>();

		final String sizeString = getString(bundle, SIZE);
		final int size = sizeString == null ? 0 : Integer.parseInt(sizeString);
		for (int i = 0; i < size; i++) {
			result.add(new ThirdPartyDependency(getString(bundle, i + DISPLAY_NAME)).url(getString(bundle, i + URL))
					.licenseUrl(getString(bundle, i + LICENSE_URL)));
		}
		result.sort(Comparator.comparing(ThirdPartyDependency::getDisplayName));
		return result;
	}

	private static String getString(ResourceBundle bundle, String key) {
		try {
			return bundle.getString(key);
		} catch (final MissingResourceException e) {
			return null;
		}
	}

	final String displayName;
	String url;
	String licenseUrl;

	public ThirdPartyDependency(String displayName) {
		this.displayName = Objects.requireNonNull(displayName);
	}

	public String getDisplayName() {
		return this.displayName;
	}

	public String getLicenseUrl() {
		return this.licenseUrl;
	}

	public ThirdPartyDependency licenseUrl(String newLicenseUrl) {
		setLicenseUrl(newLicenseUrl);
		return this;
	}

	public void setLicenseUrl(String licenseUrl) {
		this.licenseUrl = licenseUrl;
	}

	public String getUrl() {
		return this.url;
	}

	public ThirdPartyDependency url(String newUrl) {
		setUrl(newUrl);
		return this;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
