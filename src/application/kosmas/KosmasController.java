package application.kosmas;

import java.io.IOException;
import java.util.Collections;
import java.util.Objects;

import com.jfoenix.controls.JFXButton;

import application.RowRecord;
import application.infobar.InfoModel;
import application.utils.ExcelUtils;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class KosmasController {

	private ObservableList<RowRecord> data = FXCollections
			.observableArrayList(KosmasModel.getInstance().getFileNames());

	@FXML
	private AnchorPane root;

	@FXML
	private Label messageLabel;

	@FXML
	private ComboBox<String> comboBox;

	@FXML
	private JFXButton importButton;

	@FXML
	private JFXButton settingsButton;

	@FXML
	private RadioButton ssbButton;

	@FXML
	private ToggleGroup system;

	@FXML
	private ProgressIndicator progress;

	@FXML
	private TableView<RowRecord> tableView;

	@FXML
	void initialize() {
		fillComboboxAndTableview();
		refreshData();
	}

	private void fillComboboxAndTableview() {
		comboBox.getItems().removeAll(comboBox.getItems());
		comboBox.getItems().addAll("2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16",
				"17", "18", "19", "20");
		tableView.setPlaceholder(new Label(""));
	}

	private void refreshData() {
		data = FXCollections.observableArrayList(KosmasModel.getInstance().getFileNames());
		Collections.reverse(data);
		tableView.getItems().addAll(data);
		tableView.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("deliveryNote"));
		tableView.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("fileName"));
		tableView.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("rabat"));
	}

	@FXML
	void handleImportButtonAction(ActionEvent event) {
		if (Objects.nonNull(comboBox.getSelectionModel().getSelectedItem())) {
			KosmasModel.getInstance().setQuantityOfItemsToDownload(comboBox.getSelectionModel().getSelectedItem());
			Thread t1 = new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						clearListView();
						KosmasModel.getInstance().setDestinantionDirectory();
						progress.setVisible(true);
						startImport();
						KosmasModel.getInstance().login();
						if (KosmasModel.getInstance().hasSuccessfulyLoggedIn()) {
							if (ssbButton.isSelected()) {
								startDownloadingMovingAndRenamingSSB();
							} else {
								startDownloadingMovingAndRenamingFlores();
							}
							fillListView();
							progress.setVisible(false);
							KosmasModel.getInstance().end();
						} else {
							progress.setVisible(false);
							KosmasModel.getInstance().end();
						}
					} catch (Exception e) {
						progress.setVisible(false);
						e.printStackTrace();
						InfoModel.getInstance().updateInfo("Import se nezdařil");
					}
				}
			});
			t1.start();
		} else {
			Thread t1 = new Thread(new Runnable() {
				@Override
				public void run() {
					moveFilesFlores();
				}
			});
			t1.start();
		}
	}

	private void startImport() {
		KosmasModel.getInstance().deleteAllTempFiles();
		KosmasModel.getInstance().start();
	}

	private void clearListView() {
		// listViewItems.clear();
		KosmasModel.getInstance().getFileNames().clear();
		data.clear();
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				// listView.getItems().clear();
				tableView.getItems().clear();
			}
		});
	}

	private void startDownloadingMovingAndRenamingSSB() {
		KosmasModel.getInstance().downloadFilesSSB();
		KosmasModel.getInstance().moveAndRenameFiles();
		InfoModel.getInstance().updateInfo("Hotovo");
	}

	private void startDownloadingMovingAndRenamingFlores() {
		KosmasModel.getInstance().downloadFilesFlores();
		moveFilesFlores();
		InfoModel.getInstance().updateInfo("Hotovo");
	}

	private void moveFilesFlores() {
		InfoModel.getInstance().updateInfo("Přesouvám  a přejmenovávám soubory");
		ExcelUtils excel = new ExcelUtils();
		excel.kosmasExcel();
	}

	private void fillListView() {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				refreshData();
			}
		});
	}

	@FXML
	void handleSettingsButtonAction(ActionEvent event) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("KosmasSettings.fxml"));
			Stage stage = new Stage(StageStyle.UTILITY);
			stage.setTitle("Nastavení Kosmas");
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.showAndWait();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
