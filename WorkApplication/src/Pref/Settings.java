package Pref;

import java.util.prefs.Preferences;

public class Settings {
	Preferences prefs;

	public Settings() {
		prefs = Preferences.userNodeForPackage(Settings.class);
	}

	public void saveLoginInfo(String id, String password) {
		prefs.put("id", id);
		prefs.put("password", password);
	}

	public void savePath(String path) {
		prefs.put("path", path);
	}

	public String getPassword() {
		return prefs.get("password", "root");
	}

	public String getId() {
		return prefs.get("id", "root");
	}

	public String getPath() {
		return prefs.get("path", "root");
	}

}
