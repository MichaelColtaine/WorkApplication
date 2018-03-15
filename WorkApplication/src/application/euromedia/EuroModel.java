package application.euromedia;

import java.io.File;
import java.util.ArrayList;

import application.calculator.RabatCalculator;
import javafx.stage.DirectoryChooser;
import javafx.stage.Window;

public class EuroModel {

	private static EuroModel INSTANCE;
	private RabatCalculator rabat;
	private Euromedia euromedia;
	private EuroSettings settings;
	private ArrayList<String> rabatStrings;

	private EuroModel() {

		this.euromedia = new Euromedia();
		this.settings = new EuroSettings();
		this.rabatStrings = new ArrayList<>();

	}

	public static EuroModel getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new EuroModel();
		}
		return INSTANCE;
	}

	public String calculate(String first, String second) {
		rabat = new RabatCalculator();
		return rabat.getResult(first, second);
	}

	public void startImportEuromedia() {
		setLoginInfoEuro();
		euromedia.start();
	}

	private void setLoginInfoEuro() {
		euromedia.setLoginInfo(settings.getId(), settings.getPassword());
	}

	public void deleteAllTempFiles() {
		File directory = new File(System.getProperty("user.dir") + "\\temp\\");
		for (File f : directory.listFiles()) {
			f.delete();
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

	public ArrayList<String> getRabatStrings() {
		return this.rabatStrings;
	}

	public File chooseDirectory(Window owner) {
		DirectoryChooser dc = new DirectoryChooser();
		return dc.showDialog(owner);
	}

	public void setQuantityOfItemsToDownload(String selectedItem) {
		euromedia.downloadAmount(Integer.parseInt(selectedItem));
	}

}
