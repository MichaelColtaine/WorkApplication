package application.controllers;

import java.io.IOException;

import application.infobar.InfoModel;
import application.scanner.Server;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
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
	private Label infoLabel;

	@FXML
	private void initialize() {
		close.setOnMouseClicked(event -> closeWindow());
		minimize.setOnMouseClicked(event -> minimizeWindow());
		InfoModel.getInstance().bindLabelToInfo(infoLabel);
	}

	private void closeWindow() {
		closeConnection();
		Stage root = (Stage) borderpane.getScene().getWindow();
		root.close();
	}

	private void closeConnection() {
		try {
			Server.closeAll();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	private void minimizeWindow() {
		Stage root = (Stage) borderpane.getScene().getWindow();
		root.setIconified(true);
	}

	@FXML
	void handleButtons(ActionEvent event) {
//		String text = ((Button) event.getSource()ev
	}

	@FXML
	void handleEuromediaButton(ActionEvent event) {
		closeConnection();
		loadScene("/application/ui/Euromedia.fxml");
	}

	@FXML
	void handleAlbatrosButton(ActionEvent event) {
		closeConnection();
		loadScene("/application/ui/Albatros.fxml");
	}

	@FXML
	void handleRabatButton(ActionEvent event) {
		closeConnection();
		loadScene("/application/ui/Calc.fxml");
	}

	@FXML
	void handleKosmasButton(ActionEvent event) {
		closeConnection();
		loadScene("/application/ui/Kosmas.fxml");
	}

	@FXML
	void handleBetaButton(ActionEvent event) {
		closeConnection();
		loadScene("/application/ui/Beta.fxml");
	}

	@FXML
	void handleInfoButton(ActionEvent event) {
		closeConnection();
		loadScene("/application/ui/Updater.fxml");
	}

	@FXML
	void handleOtherButton(ActionEvent event) {
		closeConnection();
		loadScene("/application/ui/Other.fxml");
		// System.out.println("TEST");
	}

	@FXML
	void handleScannerButton(ActionEvent event) {
		closeConnection();
		loadScene("/application/ui/Scanner.fxml");
	}

	private void loadScene(String sceneName) {
		InfoModel.getInstance().updateInfo("");
		Parent root = null;
		try {
			root = FXMLLoader.load(getClass().getResource(sceneName));
		} catch (IOException ex) {
			System.out.println(ex);
		}
		borderpane.setCenter(root);
	}

}