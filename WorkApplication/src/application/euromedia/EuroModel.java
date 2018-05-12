package application.euromedia;

import java.io.File;
import java.util.ArrayList;

import application.RowRecord;
import javafx.stage.DirectoryChooser;
import javafx.stage.Window;

public class EuroModel {

	private static EuroModel INSTANCE;
	private Euromedia euromedia;
	private EuroSettings settings;
	private ArrayList<RowRecord> records;

	private EuroModel() {
		this.euromedia = new Euromedia();
		this.settings = new EuroSettings();
		this.records = new ArrayList<>();

	}

	public static EuroModel getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new EuroModel();
		}
		return INSTANCE;
	}

	public void startImportEuromedia() {
		setLoginInfoEuro();
		euromedia.start();
	}

	public boolean hasLoggedIn() {
		return euromedia.hasLoggedIn();
	}

	private void setLoginInfoEuro() {
		euromedia.setLoginInfo(settings.getId(), settings.getPassword());
	}

	public void deleteAllTempFiles() {
		File directory = new File(System.getProperty("user.dir") + "\\temp\\");
		createDirectoryifItDoesNotExists(directory);
		for (File f : directory.listFiles()) {
			f.delete();
		}
	}

	private void createDirectoryifItDoesNotExists(File directory) {
		if (!directory.exists()) {
			directory.mkdirs();
		}
	}

	public void saveSettings(String path, String email, String password) {
		settings.savePath(path);
		settings.saveLoginInfo(email, password);
		setLoginInfoEuro();
	}

	public EuroSettings getSettigns() {
		return this.settings;
	}

	public ArrayList<RowRecord> getRecords() {
		return this.records;
	}

	public File chooseDirectory(Window owner) {
		DirectoryChooser dc = new DirectoryChooser();
		return dc.showDialog(owner);
	}

	public void setQuantityOfItemsToDownload(String selectedItem) {
		euromedia.downloadAmount(Integer.parseInt(selectedItem));
	}

	public void tryLogin() {
		euromedia.tryToLogin();
	}

	public void end() {
		euromedia.end();
	}

	public void downloadFiles() {
		euromedia.download();
	}

}
