package application.euromedia;

import java.util.prefs.Preferences;

public class EuroSettings {
	Preferences euroPrefs;

	public EuroSettings() {
		euroPrefs = Preferences.userNodeForPackage(EuroSettings.class);
	}

	public void saveLoginInfo(String id, String password) {
		euroPrefs.put("id", id);
		euroPrefs.put("password", password);
	}

	public void savePath(String path) {
		euroPrefs.put("path", path);
	}

	public String getPassword() {
		return euroPrefs.get("password", "root");
	}

	public String getId() {
		return euroPrefs.get("id", "root");
	}

	public String getPath() {
		return euroPrefs.get("path", "root");
	}

}
