package application.controllers;

import java.util.Arrays;
import java.util.Objects;

import com.jfoenix.controls.JFXButton;

import application.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.AnchorPane;

public class KosmasController {

	private int[] numbers = new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };

	@FXML
	private AnchorPane root;

	@FXML
	private JFXButton importButton;

	@FXML
	private ProgressIndicator progress;

	@FXML
	private ComboBox<String> combobox;

	@FXML
	void handleImportButton(ActionEvent event) {
		if (Objects.nonNull(combobox.getSelectionModel().getSelectedItem())) {
			Model.getInstance().setQuantityOfItemsToDownloadKosmas(combobox.getSelectionModel().getSelectedItem());
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
	void initialize() {
		combobox.getItems().removeAll(combobox.getItems());
		combobox.getItems().addAll("1", "2", "3", "4", "5", "6", "7", "8", "9", "10");
	}

	private void start() {
		Model.getInstance().startImportKosmas();
	}

}
