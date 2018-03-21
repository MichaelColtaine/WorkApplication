package application.kosmas;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
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

public class KosmasController {

	private ArrayList<String> listViewItems = KosmasModel.getInstance().getFileNames();

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
	private JFXListView<String> listView;

	@FXML
	void initialize() {
		fillCombobox();
		listView.getItems().addAll(listViewItems);
	}

	private void fillListView() {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				Collections.reverse(listViewItems);
				listView.getItems().addAll(listViewItems);
			}
		});
	}

	private void clearListView() {
		listViewItems.clear();
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				listView.getItems().clear();
			}
		});
	}

	private void fillCombobox() {
		comboBox.getItems().removeAll(comboBox.getItems());
		comboBox.getItems().addAll("1", "2", "3", "4", "5", "6", "7", "8", "9", "10");
	}

	@FXML
	void handleImportButtonAction(ActionEvent event) {
		if (Objects.nonNull(comboBox.getSelectionModel().getSelectedItem())) {
			KosmasModel.getInstance().setQuantityOfItemsToDownload(comboBox.getSelectionModel().getSelectedItem());
			Thread t1 = new Thread(new Runnable() {
				@Override
				public void run() {
					clearListView();
					KosmasModel.getInstance().setDestinantionDirectory();
					progress.setVisible(true);
					startImport();
					KosmasModel.getInstance().login();
					System.out.println(KosmasModel.getInstance().hasSuccessfulyLoggedIn());
					if (KosmasModel.getInstance().hasSuccessfulyLoggedIn()) {
						KosmasModel.getInstance().downloadFiles();
						KosmasModel.getInstance().moveAndRenameFiles();
						InfoModel.getInstance().updateInfo("Hotovo");
						fillListView();
					} else {
						KosmasModel.getInstance().end();
					}
					progress.setVisible(false);
				}
			});
			t1.start();
		}
	}

	private void startImport() {
		KosmasModel.getInstance().deleteAllTempFiles();
		progress.setVisible(true);
		KosmasModel.getInstance().start();
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
