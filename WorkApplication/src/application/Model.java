package application;

import java.io.File;
import java.util.ArrayList;

import Pref.Settings;
import application.calculator.RabatCalculator;
import javafx.stage.DirectoryChooser;
import javafx.stage.Window;

public class Model {

	private static Model model;
	private RabatCalculator rabat;
	private Euromedia euromedia;
	private Settings settings;
	private ArrayList<String> rabatStrings;

	public Model() {

		this.euromedia = new Euromedia();
		this.settings = new Settings();
		this.rabatStrings = new ArrayList<>();
	}

	public static Model getInstance() {
		if (model == null) {
			model = new Model();
		}
		return model;
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

	public void saveEuroSettings(String path, String email, String password) {
		settings.savePath(path);
		settings.saveLoginInfo(email, password);
		setLoginInfoEuro();
	}

	public Settings getSettigns() {
		return this.settings;
	}

	public ArrayList<String> getRabatStrings() {
		return this.rabatStrings;
	}

	public File chooseDirectory(Window owner) {
		DirectoryChooser dc = new DirectoryChooser();
		return dc.showDialog(owner);
	}

	public void setAmountOfItemsToDownload(String selectedItem) {
		euromedia.downloadAmount(Integer.parseInt(selectedItem));
	}

}
