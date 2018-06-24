package application.updater;

import com.jfoenix.controls.JFXButton;

import application.infobar.InfoModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;

public class UpdaterController {

	@FXML
	private Label versionNumberLabel;

	@FXML
	private JFXButton updateButton;

	@FXML
	private ProgressIndicator progress;

	@FXML
	void initialize() {
		versionNumberLabel.setText(String.format("%d", UpdaterModel.getInstance().getCurrentVersion()));

	}

	@FXML
	void handleUpdateButton(ActionEvent event) {
		int currentVersion = UpdaterModel.getInstance().getCurrentVersion();
		int latestVersion = Integer.parseInt(UpdaterModel.getInstance().getLastestVersion());
		System.out.println("currentVersion: " + currentVersion + " latestVersion: " + latestVersion);

		if (currentVersion < latestVersion) {
			Thread t1 = new Thread(new Runnable() {
				@Override
				public void run() {
					progress.setVisible(true);
					InfoModel.getInstance().updateInfo("Stahuju..");
					UpdaterModel.getInstance().download();
					progress.setVisible(false);
					startApp();
					System.exit(1);
				}
			});
			t1.start();
		} else {
			InfoModel.getInstance().updateInfo("Současná verze je aktuální!");
		}
		InfoModel.getInstance().updateInfo("Aktualizace ještě nebyla implementována.");
	}

	public void startApp() {
		try {
			Process proc = Runtime.getRuntime().exec("java -jar update/SpUpdater.jar");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
