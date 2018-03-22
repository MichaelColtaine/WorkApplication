package application.euromedia;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;

import application.infobar.InfoModel;
import application.utils.FileChanger;
import application.utils.NumberFinder;
import application.utils.PdfWorker;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ProgressIndicator;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class EuromediaController implements Initializable {

	private ArrayList<String> rabatStrings = EuroModel.getInstance().getRabatStrings();

	@FXML
	private JFXListView<String> listView;

	@FXML
	private JFXButton settingsButton;

	@FXML
	private ProgressIndicator progress;

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
		comboBox.getItems().addAll("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15",
				"16", "17", "18", "19", "20");
	}

	@FXML
	void handleImportButtonAction(ActionEvent event) {
		if (Objects.nonNull(comboBox.getSelectionModel().getSelectedItem())) {
			EuroModel.getInstance().setQuantityOfItemsToDownload(comboBox.getSelectionModel().getSelectedItem());
			Thread t1 = new Thread(new Runnable() {
				@Override
				public void run() {
					startImport();
				}
			});
			t1.start();
		}

	}

	@FXML
	void handleSettingsButtonAction(ActionEvent event) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("EuromediaSettings.fxml"));
			Stage stage = new Stage(StageStyle.UTILITY);
			stage.setTitle("Nastavení Euromedia");
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.showAndWait();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void startImport() {
		EuroModel.getInstance().deleteAllTempFiles();
		progress.setVisible(true);
		clearListView();
		EuroModel.getInstance().startImportEuromedia();
		EuroModel.getInstance().tryLogin();
		if (EuroModel.getInstance().hasLoggedIn()) {
			EuroModel.getInstance().downloadFiles();
			changePdfToString();
			fillListView();
			FileChanger.changeAllEuroFiles(EuroModel.getInstance().getSettigns().getPath());
		}
		progress.setVisible(false);
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
		pause();
		for (File file : files.listFiles()) {
			if (isPdf(file)) {
				InfoModel.getInstance().updateInfo("Vypočítávám rabat pro " + file.getName());
				Double rabat = calculateRabat(finder.findNumbers(worker.getText(file.getAbsolutePath()))[1],
						finder.findNumbers(worker.getText(file.getAbsolutePath()))[0]);
				rabatStrings.add(getFilenameAndRabat(file, rabat));

			}
		}
		Collections.reverse(rabatStrings);
		InfoModel.getInstance().updateInfo("Hotovo!");
	}

	private void pause() {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
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
