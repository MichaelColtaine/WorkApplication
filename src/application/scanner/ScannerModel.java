package application.scanner;

public class ScannerModel {

	private ScannerSettings settings;

	public ScannerModel() {
		this.settings = new ScannerSettings();
	}

	private static class LazyHolder {
		static final ScannerModel INSTANCE = new ScannerModel();
	}

	public static ScannerModel getInstance() {
		return LazyHolder.INSTANCE;
	}

	public ScannerSettings getSettings() {
		return settings;
	}

}
