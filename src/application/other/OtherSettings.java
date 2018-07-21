package application.other;

import java.util.prefs.Preferences;

public class OtherSettings {
	Preferences gradaPrefs;

	public OtherSettings() {
		gradaPrefs = Preferences.userNodeForPackage(OtherSettings.class);
	}

	public void savePaths(String from, String to) {
		savePathFrom(from);
		savePathTo(to);
	}

	private void savePathFrom(String path) {
		gradaPrefs.put("from", path);
	}

	private void savePathTo(String path) {
		gradaPrefs.put("to", path);
	}

	public String getFromPath() {
		return gradaPrefs.get("from", "");
	}

	public String getToPath() {
		return gradaPrefs.get("to", "");
	}

}
