package application.kosmas;

import java.util.prefs.Preferences;

public class KosmasSettings {
	Preferences kosmasPref;

	public KosmasSettings() {
		kosmasPref = Preferences.userNodeForPackage(KosmasSettings.class);
	}

	public void saveLoginInfo(String id, String password) {
		kosmasPref.put("id", id);
		kosmasPref.put("password", password);
	}

	public void savePath(String path) {
		kosmasPref.put("path", path);
	}

	public String getPassword() {
		return kosmasPref.get("password", "root");
	}

	public String getId() {
		return kosmasPref.get("id", "root");
	}

	public String getPath() {
		return kosmasPref.get("path", "root");
	}

}
