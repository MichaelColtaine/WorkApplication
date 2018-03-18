package application;

import java.io.File;
import java.util.ArrayList;

import application.calculator.RabatCalculator;
import application.euromedia.Euromedia;
import application.kosmas.Kosmas;
import application.euromedia.EuroSettings;
import javafx.stage.DirectoryChooser;
import javafx.stage.Window;

public class Model {

	private static Model model;
	private RabatCalculator rabat;
	private ArrayList<String> rabatStrings;

	public Model() {
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

	public void deleteAllTempFiles() {
		File directory = new File(System.getProperty("user.dir") + "\\temp\\");
		for (File f : directory.listFiles()) {
			f.delete();
		}
	}

	public ArrayList<String> getRabatStrings() {
		return this.rabatStrings;
	}

	public File chooseDirectory(Window owner) {
		DirectoryChooser dc = new DirectoryChooser();
		return dc.showDialog(owner);
	}

	

}
