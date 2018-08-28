package application.analysis;

import java.util.prefs.Preferences;

public class AnalysisSettings {

	private Preferences prefs;

	public AnalysisSettings() {
		prefs = Preferences.userNodeForPackage(AnalysisSettings.class);
	}

	public String getPath() {
		return prefs.get("path", System.getProperty("user.dir"));
	}

	public void savePath(String path) {
		prefs.put("path", path);
	}

}
