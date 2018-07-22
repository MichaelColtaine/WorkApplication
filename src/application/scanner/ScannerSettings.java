package application.scanner;

import java.util.prefs.Preferences;

public class ScannerSettings {

	private Preferences prefs;

	public ScannerSettings() {
		prefs = Preferences.userNodeForPackage(ScannerSettings.class);
	}

	public String getPath() {
		return prefs.get("path", System.getProperty("user.dir"));
	}

	public void savePath(String path) {
		prefs.put("path", path);
	}

}
