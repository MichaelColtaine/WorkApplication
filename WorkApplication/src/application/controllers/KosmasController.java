package application.controllers;

import com.jfoenix.controls.JFXButton;

import application.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.AnchorPane;

public class KosmasController {

	@FXML
	private AnchorPane root;

	@FXML
	private JFXButton importButton;

	@FXML
	private ProgressIndicator progress;

	@FXML
	void handleImportButton(ActionEvent event) {
		Thread t1 = new Thread(new Runnable() {
			@Override
			public void run() {
				start();
			}
		});
		t1.start();
	}

	private void start() {
		Model.getInstance().startImportKosmas();
	}

}
