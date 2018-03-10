package application.controllers;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;

import application.FileChanger;
import application.Model;
import application.NumberFinder;
import application.PdfWorker;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ProgressBar;

public class EuromediaController implements Initializable {

	private ArrayList<String> rabatStrings = Model.getInstance().getRabatStrings();

	@FXML
	private JFXListView<String> listView;
	
	@FXML
	private ProgressBar progress;

	@FXML
	private JFXButton importButton;

	@FXML
	private ComboBox<String> comboBox;

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		fillCombobox();

		listView.getItems().addAll(rabatStrings);

	}

	private void fillCombobox() {
		comboBox.getItems().removeAll(comboBox.getItems());
		comboBox.getItems().addAll("1", "2", "3", "4", "5", "6", "7", "8", " 9", "10", "11", "12", "13", "14", "15",
				"16", "17", "18", "19", "20");
		comboBox.getSelectionModel().select("1");

	}

	@FXML
	void handleImportButtonAction(ActionEvent event) {
		Model.getInstance().setAmountOfItemsToDownload(comboBox.getSelectionModel().getSelectedItem());
		Thread t1 = new Thread(new Runnable() {
			@Override
			public void run() {
				progress.setProgress(0.1);
				clearListView();
				Model.getInstance().startImportEuromedia();
				progress.setProgress(0.4);
				changePdfToString();
				fillListView();
				progress.setProgress(0.6);
				FileChanger.changeAllFiles(Model.getInstance().getSettigns().getPath());
				progress.setProgress(0.8);
				Model.getInstance().deleteAllTempFiles();
				progress.setProgress(1);
			}
		});
		t1.start();
	}

	private void fillListView() {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				listView.getItems().addAll(rabatStrings);
			}
		});
	}

	private void clearListView() {
		rabatStrings.clear();
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				listView.getItems().clear();
			}
		});
	}

	private void changePdfToString() {
		PdfWorker worker = new PdfWorker();
		NumberFinder finder = new NumberFinder();
		File files = new File(System.getProperty("user.dir") + File.separator + "temp" + File.separator);
		System.out.println("Length of files " + files.listFiles().length);
		try {
			Thread.sleep(1000);
			System.out.println("Length of files " + files.listFiles().length);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (File file : files.listFiles()) {
			System.out.println(file);
			if (isPdf(file)) {

				Double rabat = calculateRabat(finder.findNumbers(worker.getText(file.getAbsolutePath()))[1],
						finder.findNumbers(worker.getText(file.getAbsolutePath()))[0]);
				rabatStrings.add(getFilenameAndRabat(file, rabat));

			}
		}

		Collections.reverse(rabatStrings);

	}

	private boolean isPdf(File f) {
		return f.getName().substring(f.getName().length() - 4, f.getName().length()).equalsIgnoreCase(".pdf");
	}

	private double calculateRabat(double first, double second) {
		return (first - second) / (first * 0.01);
	}

	private String getFilenameAndRabat(File file, Double rabat) {
		return String.format("%s    RABAT: -%.2f", getFileName(file), rabat);

	}

	private String getFileName(File file) {
		return file.getName().substring(0, file.getName().length() - 4);
	}

}
