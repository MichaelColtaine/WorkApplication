package application.scanner;

import java.util.ArrayList;

public class ScannerModel {

	private ScannerSettings settings;
	private ArrayList<Article> articles = new ArrayList<>();

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

	public ArrayList<Article> getArticles() {
		return articles;
	}

}
