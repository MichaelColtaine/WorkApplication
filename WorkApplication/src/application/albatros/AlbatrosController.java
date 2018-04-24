package application.albatros;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;

import application.infobar.InfoModel;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class AlbatrosController {

	private List<String> listOfNames = AlbatrosModel.getInstance().getListOfNames();

	@FXML
	private JFXListView<String> listView;

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
		comboBox.getItems().addAll("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15",
				"16", "17", "18", "19", "20");
		listView.getItems().addAll(listOfNames);
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
					AlbatrosModel.getInstance().deleteAllTempFiles();
					progress.setVisible(true);
					AlbatrosModel.getInstance().startAlbatrosImport();
					AlbatrosModel.getInstance().changeAndMoveFile();
					progress.setVisible(false);
					InfoModel.getInstance().updateInfo("");
					Collections.reverse(listOfNames);
					fillListView();
				}
			});
			t1.start();
		}
	}

	private void clearListView() {
		listOfNames.clear();
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				listView.getItems().clear();
			}
		});
	}

	private void fillListView() {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				listView.getItems().addAll(listOfNames);
			}
		});
	}

}
