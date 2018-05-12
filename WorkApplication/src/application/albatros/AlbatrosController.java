package application.albatros;

import java.io.IOException;
import java.util.Collections;
import java.util.Objects;

import com.jfoenix.controls.JFXButton;

import application.RowRecord;
import application.infobar.InfoModel;
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
import javafx.scene.control.TableView;
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
		comboBox.getItems().addAll("1", "2", "3", "4", "5", "6", "7", "8", "9", "10");
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
						AlbatrosModel.getInstance().startAlbatrosImport();
						AlbatrosModel.getInstance().changeAndMoveFile();
						progress.setVisible(false);
						InfoModel.getInstance().updateInfo("");
						fillListView();
					} catch (Exception E) {
						InfoModel.getInstance().updateInfo("Import se nezdařil");
						progress.setVisible(false);
					}
				}
			});
			t1.start();
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
