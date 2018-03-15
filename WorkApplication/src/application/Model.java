package application;

import java.io.File;
import java.util.ArrayList;

import application.calculator.RabatCalculator;
import application.euromedia.Euromedia;
import application.euromedia.EuroSettings;
import javafx.stage.DirectoryChooser;
import javafx.stage.Window;

public class Model {

	private static Model model;
	private RabatCalculator rabat;
	private Euromedia euromedia;
	private EuroSettings settings;
	private ArrayList<String> rabatStrings;
	private Kosmas kosmas;

	public Model() {

		this.euromedia = new Euromedia();
		this.settings = new EuroSettings();
		this.rabatStrings = new ArrayList<>();
		this.kosmas = new Kosmas();
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
		euromedia.setLoginInfo(settings.getEuroId(), settings.getEuroPassword());
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
	
	public void setQuantityOfItemsToDownloadKosmas(String selectedItem) {
		kosmas.downloadAmount(Integer.parseInt(selectedItem));
	}

	public void startImportKosmas() {
		kosmas.start();
	}

}
