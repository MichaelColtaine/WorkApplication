package application.euromedia;

import java.util.prefs.Preferences;

public class EuroSettings {
	Preferences prefs;

	public EuroSettings() {
		prefs = Preferences.userNodeForPackage(EuroSettings.class);
	}

	public void saveLoginInfo(String id, String password) {
		prefs.put("id", id);
		prefs.put("password", password);
	}

	public void savePath(String path) {
		prefs.put("path", path);
	}

	public String getEuroPassword() {
		return prefs.get("password", "root");
	}

	public String getEuroId() {
		return prefs.get("id", "root");
	}

	public String getPath() {
		return prefs.get("path", "root");
	}

}
