package application.controllers;

import java.io.IOException;

import com.jfoenix.controls.JFXButton;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class HomeController {

	@FXML
	private BorderPane borderpane;

	@FXML
	private ImageView close;

	@FXML
	private ImageView minimize;

	@FXML
	private JFXButton rabatButton;

	@FXML
	private JFXButton euromediaButton;

	@FXML
	private JFXButton kosmasButton;

	@FXML
	private JFXButton settingsButton;

	@FXML
	private void initialize() {
		close.setOnMouseClicked(event -> closeWindow());
		minimize.setOnMouseClicked(event -> minimizeWindow());
	}

	private void closeWindow() {
		Stage root = (Stage) borderpane.getScene().getWindow();
		root.close();
	}

	private void minimizeWindow() {
		Stage root = (Stage) borderpane.getScene().getWindow();
		root.setIconified(true);
	}

	@FXML
	void handleEuromediaButton(ActionEvent event) {
		loadScene("/application/ui/Euromedia.fxml");
	}

	@FXML
	void handleRabatButton(ActionEvent event) {
		loadScene("/application/ui/Calc.fxml");
	}

	@FXML
	void handleSettingsButton(ActionEvent event) {
		loadScene("/application/ui/Settings.fxml");
	}

	@FXML
	void handleKosmasButton(ActionEvent event) {
		loadScene("/application/ui/Kosmas.fxml");
	}
	
	@FXML
	void handleBetaButton(ActionEvent event) {
		loadScene("/application/ui/Beta.fxml");
	}

	private void loadScene(String sceneName) {
		Parent root = null;
		try {
			root = FXMLLoader.load(getClass().getResource(sceneName));
		} catch (IOException ex) {
			System.out.println(ex);
		}
		borderpane.setCenter(root);
	}
	
	

}