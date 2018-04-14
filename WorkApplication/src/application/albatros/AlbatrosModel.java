package application.albatros;

import java.io.File;
import java.util.ArrayList;

import javafx.stage.DirectoryChooser;
import javafx.stage.Window;

public class AlbatrosModel {
	private static AlbatrosModel INSTANCE;
	private Albatros albatros;
	private AlbatrosSettings settings;
	private ArrayList<String> rabatStrings;

	private AlbatrosModel() {
		this.albatros = new Albatros();
		this.settings = new AlbatrosSettings();
		this.rabatStrings = new ArrayList<>();

	}

	public static AlbatrosModel getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new AlbatrosModel();
		}
		return INSTANCE;
	}

	public void startImportEuromedia() {
		setLoginInfoEuro();
		albatros.start();
	}

	public boolean hasLoggedIn() {
		return albatros.hasLoggedIn();
	}

	private void setLoginInfoEuro() {
		albatros.setLoginInfo(settings.getId(), settings.getPassword());
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

	public AlbatrosSettings getSettigns() {
		return this.settings;
	}

	public ArrayList<String> getRabatStrings() {
		return this.rabatStrings;
	}

	public File chooseDirectory(Window owner) {
		DirectoryChooser dc = new DirectoryChooser();
		return dc.showDialog(owner);
	}

	public void setQuantityOfItemsToDownload(String selectedItem) {
		albatros.downloadAmount(Integer.parseInt(selectedItem));
	}

	public void tryLogin() {
		albatros.tryToLogin();
	}

	public void end() {
		albatros.end();
	}

	public void downloadFiles() {
		albatros.download();
	}
}
