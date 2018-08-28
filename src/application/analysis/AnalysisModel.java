package application.analysis;

public class AnalysisModel {

	private AnalysisSettings settings;
	private String dataAsString = "";
	private String importedData = "";
	private String books = "";

	public AnalysisModel() {
		this.settings = new AnalysisSettings();
	}

	private static class LazyHolder {
		static final AnalysisModel INSTANCE = new AnalysisModel();
	}

	public static AnalysisModel getInstance() {
		return LazyHolder.INSTANCE;
	}

	public AnalysisSettings getSettings() {
		return settings;
	}

	public void setData(String data) {
		this.dataAsString = data;
	}

	// holds analys data
	public String getData() {
		return dataAsString;
	}

	// holds all books data
	public void setDatabase(String data) {
		this.books = data;
	}

	public String getDatabase() {
		return this.books;
	}

	public String getImportedData() {
		return this.importedData;
	}

	public void setImportedData(String importedData) {
		this.importedData = importedData;
	}
}
