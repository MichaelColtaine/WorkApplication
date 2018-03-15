package application.kosmas;

import java.io.File;

import application.calculator.RabatCalculator;
import application.euromedia.EuroSettings;
import javafx.stage.DirectoryChooser;
import javafx.stage.Window;

public class KosmasModel {
	private static KosmasModel INSTANCE;
	private RabatCalculator rabat;
	private Kosmas kosmas;
	private KosmasSettings settings;

	private KosmasModel() {

		this.kosmas = new Kosmas();
		this.settings = new KosmasSettings();

	}

	public static KosmasModel getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new KosmasModel();
		}
		return INSTANCE;
	}

	public String calculate(String first, String second) {
		rabat = new RabatCalculator();
		return rabat.getResult(first, second);
	}

	public void startImportEuromedia() {
		setLoginInfoEuro();
		kosmas.start();
	}

	private void setLoginInfoEuro() {
		kosmas.setLoginInfo(settings.getId(), settings.getPassword());
	}

	public void deleteAllTempFiles() {
		File directory = new File(System.getProperty("user.dir") + "\\temp\\");
		for (File f : directory.listFiles()) {
			f.delete();
		}
	}

	public File chooseDirectory(Window owner) {
		DirectoryChooser dc = new DirectoryChooser();
		return dc.showDialog(owner);
	}

	public void setQuantityOfItemsToDownload(String selectedItem) {
		kosmas.downloadAmount(Integer.parseInt(selectedItem));
	}

	public KosmasSettings getSettigns() {
		return this.settings;
	}

	public void saveSettings(String path, String email, String password) {
		settings.savePath(path);
		settings.saveLoginInfo(email, password);
		setLoginInfoEuro();
	}

}
