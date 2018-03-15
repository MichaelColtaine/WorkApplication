package application.kosmas;

import java.io.IOException;
import java.util.Objects;

import com.jfoenix.controls.JFXButton;

import application.Model;
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

public class KosmasController {

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
	private ProgressIndicator progress;

	@FXML
	void initialize() {
		comboBox.getItems().removeAll(comboBox.getItems());
		comboBox.getItems().addAll("1", "2", "3", "4", "5", "6", "7", "8", "9", "10");
	}

	@FXML
	void handleImportButtonAction(ActionEvent event) {
		if (Objects.nonNull(comboBox.getSelectionModel().getSelectedItem())) {
			Model.getInstance().setQuantityOfItemsToDownloadKosmas(comboBox.getSelectionModel().getSelectedItem());
			Thread t1 = new Thread(new Runnable() {
				@Override
				public void run() {
					progress.setVisible(true);
					start();
					progress.setVisible(false);
				}
			});
			t1.start();
		}
	}

	@FXML
	void handleSettingsButtonAction(ActionEvent event) {
		System.out.println("TEST");
		try {
			Parent root = FXMLLoader.load(getClass().getResource("KosmasSettings.fxml"));
			Stage stage = new Stage(StageStyle.UTILITY);
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.showAndWait();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void start() {
		Model.getInstance().startImportKosmas();
	}

}
