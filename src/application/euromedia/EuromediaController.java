package application.euromedia;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.Objects;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;

import application.RowRecord;
import application.infobar.InfoModel;
import application.utils.FileUnziper;
import application.utils.NumberFinder;
import application.utils.PdfWorker;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class EuromediaController implements Initializable {

	private ObservableList<RowRecord> data = FXCollections.observableArrayList();

	@FXML
	private TableView<RowRecord> tableView;

	@FXML
	private JFXButton settingsButton;

	@FXML
	private ProgressIndicator progress;

	@FXML
	private JFXButton importButton;

	@FXML
	private ComboBox<String> comboBox;

	@FXML
	private RadioButton ssbButton;

	@FXML
	private ToggleGroup system;

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		fillCombobox();
		tableView.setPlaceholder(new Label(" "));
		refreshData();
	}

	private void refreshData() {
		data.addAll(EuroModel.getInstance().getRecords());
		Collections.reverse(data);
		tableView.getItems().addAll(data);
		tableView.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("deliveryNote"));
		tableView.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("fileName"));
		tableView.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("rabat"));
	}

	private void fillCombobox() {
		comboBox.getItems().removeAll(comboBox.getItems());
		comboBox.getItems().addAll("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15",
				"16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32",
				"33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49",
				"50", "51", "52", "53", "54", "55", "56", "57", "58", "59", "60");
	}

	@FXML
	void handleImportButtonAction(ActionEvent event) {
		if (Objects.nonNull(comboBox.getSelectionModel().getSelectedItem())) {
			Thread t1 = new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						startImport();
					} catch (Exception e) {
						InfoModel.getInstance().updateInfo("Import se nezdařil");
						e.printStackTrace();
						progress.setVisible(false);
					}
				}
			});
			t1.start();
		} else {
			convertTempFilesWithoutDownloading();
		}

	}

	private void startImport() {
		prepareForImport();
		int amount = Integer.parseInt(comboBox.getSelectionModel().getSelectedItem());
		InfoModel.getInstance().updateInfo("Začínám stahovat");
		if (ssbButton.isSelected()) {
			startDownloading("SSB", amount);
			changePdfToString();
			EuroModel.getInstance().changeAllEuroFilesSSB(EuroModel.getInstance().getSettings().getPath());
		} else {
			startDownloading("Flores", amount);
			waitForFilesToBeDownloaded();
			EuroModel.getInstance().changeAllEuroFilesFlores(EuroModel.getInstance().getSettings().getPath());
		}
		cleanUp();
	}

	private void prepareForImport() {
		EuroModel.getInstance().deleteAllTempFiles();
		progress.setVisible(true);
		clearListView();
	}

	private void startDownloading(String type, int amount) {
		EuromediaWebScraper scraper;
		scraper = new EuromediaWebScraper(type);
		scraper.setAmount(amount);
		scraper.startDownloading();

	}

	private void cleanUp() {
		fillListView();
		progress.setVisible(false);
		EuroModel.getInstance().deleteAllTempFiles();
		InfoModel.getInstance().updateInfo("Hotovo!");
	}

	private void fillListView() {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				refreshData();
			}
		});
	}

	private void waitForFilesToBeDownloaded() {
		if (new File(System.getProperty("user.dir") + File.separator + "temp" + File.separator)
				.listFiles().length == 0) {
			System.out.println("Prazdno");
			InfoModel.getInstance().updateInfo("Čekám na soubor!");
			pause();
		}
	}

	private void convertTempFilesWithoutDownloading() {
		Thread t1 = new Thread(new Runnable() {
			@Override
			public void run() {
				EuroModel.getInstance().changeAllEuroFilesFlores(EuroModel.getInstance().getSettings().getPath());
			}
		});
		t1.start();

	}

	private void clearListView() {
		EuroModel.getInstance().getRecords().clear();
		data.clear();
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				tableView.getItems().clear();
			}
		});
	}

	private void changePdfToString() {
		PdfWorker worker = new PdfWorker();
		NumberFinder finder = new NumberFinder();
		pause();
		File files = new File(System.getProperty("user.dir") + File.separator + "temp" + File.separator);
		waitForFilesToBeDownloaded();
		pause();
		for (File file : files.listFiles()) {
			if (isPdf(file)) {
				InfoModel.getInstance().updateInfo("Vypočítávám rabat pro " + file.getName());

				Double rabat = calculateRabat(finder.findNumbers(worker.getText(file.getAbsolutePath()))[1],
						finder.findNumbers(worker.getText(file.getAbsolutePath()))[0]);

				EuroModel.getInstance().getRecords().add(getFilenameAndRabat(file, rabat));

			}
		}
	}

	private void pause() {
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private boolean isPdf(File f) {
		return f.getName().substring(f.getName().length() - 4, f.getName().length()).equalsIgnoreCase(".pdf");
	}

	private double calculateRabat(double first, double second) {
		System.out.println(first + " " + second);
		return (first - second) / (first * 0.01);
	}

	private RowRecord getFilenameAndRabat(File file, Double rabat) {
		return new RowRecord(getFileName(file), getFileName(file).substring(getFileName(file).length() - 5) + ".txt",
				String.format("-%.2f", rabat) + "%");
	}

	private String getFileName(File file) {
		return file.getName().substring(0, file.getName().length() - 4).replace("_cnf", "");
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

}
