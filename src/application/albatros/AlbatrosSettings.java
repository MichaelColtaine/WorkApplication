package application.albatros;

import java.util.prefs.Preferences;

public class AlbatrosSettings {
	Preferences albatrosPrefs;

	public AlbatrosSettings() {
		albatrosPrefs = Preferences.userNodeForPackage(AlbatrosSettings.class);
	}

	public void saveLoginInfo(String id, String password) {
		albatrosPrefs.put("id", id);
		albatrosPrefs.put("password", password);
	}

	public void savePath(String path) {
		albatrosPrefs.put("path", path);
	}

	public String getPassword() {
		return albatrosPrefs.get("password", "root");
	}

	public String getId() {
		return albatrosPrefs.get("id", "root");
	}

	public String getPath() {
		return albatrosPrefs.get("path", "root");
	}
}
