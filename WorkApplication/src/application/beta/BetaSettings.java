package application.beta;

import java.io.File;
import java.util.prefs.Preferences;

import application.kosmas.KosmasSettings;

public class BetaSettings {
	Preferences betaPrefs;

	public BetaSettings() {
		betaPrefs = Preferences.userNodeForPackage(KosmasSettings.class);
	}

	public void savePaths(String from, String to) {
		savePathFrom(from);
		savePathTo(to);
	}

	private void savePathFrom(String path) {
		betaPrefs.put("from", path );
	}

	private void savePathTo(String path) {
		betaPrefs.put("to", path );
	}

	public String getFromPath() {
		return betaPrefs.get("from", "root");
	}

	public String getToPath() {
		return betaPrefs.get("to", "root");
	}

}