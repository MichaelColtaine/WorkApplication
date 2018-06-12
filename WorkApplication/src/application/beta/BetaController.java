package application.beta;

import java.io.File;
import java.io.IOException;

import com.jfoenix.controls.JFXButton;

import application.infobar.InfoModel;
import application.utils.ExcelUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class BetaController {

	@FXML
	private AnchorPane root;

	@FXML
	private JFXButton settingsButton;

	@FXML
	private JFXButton moveButton;

	@FXML
	private ProgressIndicator progress;

	@FXML
	private RadioButton ssbButton;

	@FXML
	private ToggleGroup system;

	@FXML
	void handleSettingsButtonAction(ActionEvent event) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("BetaSettings.fxml"));
			Stage stage = new Stage(StageStyle.UTILITY);
			stage.setTitle("Nastaven� Beta");
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.showAndWait();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML

	void handleMoveButton() {

		Thread t1 = new Thread(new Runnable() {
			@Override
			public void run() {
				ExcelUtils exel = new ExcelUtils();

				if (isFolderEmpty()) {
					InfoModel.getInstance().updateInfo("Slo�ka je pr�zdn�!");
				} else {
					removeAllPdf();
					progress.setVisible(true);
					if (ssbButton.isSelected()) {
						BetaModel.getInstance().moveAndRename();
					} else {
						exel.betaExcel();
					}
					progress.setVisible(false);
					InfoModel.getInstance().updateInfo("Hotovo!");
				}
			}
		});
		t1.start();
	}

	private void removeAllPdf() {
		File fromDirectory = new File(BetaModel.getInstance().getFromPath());
		for (File f : fromDirectory.listFiles()) {
			InfoModel.getInstance().updateInfo("Ma�u PDFka.");
			if (f.getName().toLowerCase().contains(".pdf")) {
				f.delete();
				InfoModel.getInstance().updateInfo("Ma�u PDFka..");
			}
			InfoModel.getInstance().updateInfo("Ma�u PDFka....");
		}
	}

	public boolean isFolderEmpty() {
		File file = new File(BetaModel.getInstance().getFromPath());
		return file.listFiles().length == 0;
	}

}
