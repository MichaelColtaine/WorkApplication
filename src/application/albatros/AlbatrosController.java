package application.albatros;

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

public class AlbatrosController {

	private ObservableList<RowRecord> data = FXCollections.observableArrayList();

	@FXML
	private TableView<RowRecord> tableView;

	@FXML
	private AnchorPane root;

	@FXML
	private JFXButton settingsButton;

	@FXML
	private RadioButton ssbButton;

	@FXML
	private ToggleGroup system;

	@FXML
	private JFXButton importButton;

	@FXML
	private ProgressIndicator progress;

	@FXML
	private ComboBox<String> comboBox;

	@FXML
	private Label messageLabel;

	@FXML
	void initialize() {
		comboBox.getItems().removeAll(comboBox.getItems());
		comboBox.getItems().addAll("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15",
				"16", "17", "18", "19", "20");
		tableView.setPlaceholder(new Label(""));
		fillListViewData();
	}

	@FXML
	void handleSettingsButtonAction(ActionEvent event) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("AlbatrosSettings.fxml"));
			Stage stage = new Stage(StageStyle.UTILITY);
			stage.setTitle("Nastavení Albatros");
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.showAndWait();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	void handleImportButtonAction(ActionEvent event) {
		if (Objects.nonNull(comboBox.getSelectionModel().getSelectedItem())) {
			AlbatrosModel.getInstance().setQuantityOfItemsToDownload(comboBox.getSelectionModel().getSelectedItem());
			clearListView();
			Thread t1 = new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						AlbatrosModel.getInstance().deleteAllTempFiles();
						progress.setVisible(true);
						if (ssbButton.isSelected()) {
							AlbatrosModel.getInstance().startAlbatrosImport();
							AlbatrosModel.getInstance().changeAndMoveFile();
						} else {
							ExcelUtils exel = new ExcelUtils();
							AlbatrosModel.getInstance().startAlbatrosImportFlores();
							exel.albatrosExcel();
						}
						progress.setVisible(false);
						InfoModel.getInstance().updateInfo("");
						fillListView();

					} catch (Exception e) {
						InfoModel.getInstance().updateInfo("Import se nezdařil");
						progress.setVisible(false);
						e.printStackTrace();
					}
				}
			});
			t1.start();
		} else {
			System.out.println("TEST");
		}
	}

	private void clearListView() {
		data.clear();
		AlbatrosModel.getInstance().getListOfNames().clear();
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				tableView.getItems().clear();
			}
		});
	}

	private void fillListView() {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				fillListViewData();
			}
		});
	}

	private void fillListViewData() {
		data = FXCollections.observableArrayList(AlbatrosModel.getInstance().getListOfNames());
		Collections.reverse(data);
		tableView.getItems().addAll(data);
		tableView.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("deliveryNote"));
		tableView.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("fileName"));
		tableView.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("rabat"));
	}
}
