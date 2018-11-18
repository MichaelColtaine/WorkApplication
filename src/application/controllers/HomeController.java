package application.controllers;

import java.io.IOException;

import application.analysis.AnalysisSender;
import application.analysis.ExportReceiver;
import application.infobar.InfoModel;
import application.scanner.ScannerServer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
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
			ScannerServer.closeAll();
			AnalysisSender.closeAll();
			ExportReceiver.closeAll();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	private void minimizeWindow() {
		Stage root = (Stage) borderpane.getScene().getWindow();
		root.setIconified(true);
	}

	@FXML
	void handleButtons(ActionEvent event) {
		String text = ((Button) event.getSource()).getText();
		closeConnection();
		switch (text) {
		case "Kalkulačka Rabatu":
			handleRabatButton();
			break;
		case "Euromedia":
			handleEuromediaButton();
			break;
		case "Distri/Albatros":
			handleAlbatrosButton();
			break;
		case "Kosmas":
			handleKosmasButton();
			break;
		case "Beta":
			handleBetaButton();
			break;
		case "Analýza":
			handleAnalysisButton();
			break;
		case "Ostatní Flores":
			handleOtherButton();
			break;
		case "Flores čtečka":
			handleScannerButton();
			break;
		case "O Programu":
			handleInfoButton();
			break;
		}
	}

	private void handleEuromediaButton() {
		loadScene("/application/ui/Euromedia.fxml");
	}

	private void handleAlbatrosButton() {
		loadScene("/application/ui/Albatros.fxml");
	}

	private void handleRabatButton() {
		loadScene("/application/ui/Calc.fxml");
	}

	private void handleKosmasButton() {
		loadScene("/application/ui/Kosmas.fxml");
	}

	private void handleBetaButton() {
		loadScene("/application/ui/Beta.fxml");
	}

	private void handleInfoButton() {
		loadScene("/application/ui/Updater.fxml");
	}

	private void handleOtherButton() {
		loadScene("/application/ui/Other.fxml");
	}

	private void handleScannerButton() {
		loadScene("/application/ui/Scanner.fxml");
	}

	private void handleAnalysisButton() {
		loadScene("/application/ui/Analysis.fxml");
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