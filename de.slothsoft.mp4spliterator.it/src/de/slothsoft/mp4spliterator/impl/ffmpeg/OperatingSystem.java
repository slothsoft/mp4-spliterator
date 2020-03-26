package de.slothsoft.mp4spliterator.impl.ffmpeg;

public enum OperatingSystem {
	WINDOWS, LINUX, MAC, SOLARIS;

	private static OperatingSystem current;

	public static OperatingSystem getCurrent() {
		if (current == null) {
			final String osName = System.getProperty("os.name").toLowerCase();
			if (osName.contains("win")) {
				current = OperatingSystem.WINDOWS;
			} else if (osName.contains("nix") || osName.contains("nux") || osName.contains("aix")) {
				current = OperatingSystem.LINUX;
			} else if (osName.contains("mac")) {
				current = OperatingSystem.MAC;
			} else if (osName.contains("sunos")) {
				current = OperatingSystem.SOLARIS;
			} else {
				throw new UnsupportedOperationException("Cannot handle OS " + osName + "!");
			}
		}
		return current;
	}

}