package application.updater;

import com.jfoenix.controls.JFXButton;

import application.infobar.InfoModel;
import auth.Authentication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;

public class UpdaterController {

	@FXML
	private Label versionNumberLabel;

	@FXML
	private Label updateReadyInfoLabel;

	@FXML
	private JFXButton updateButton;

	@FXML
	private ProgressIndicator progress;

	@FXML
	private Label errorUpdate;

	@FXML
	void initialize() {
		int latestVersion = Authentication.getInstance().getLatestVersion();
		int currentVersion = UpdaterModel.getInstance().getCurrentVersion();
		if (latestVersion == 0) {
			latestVersion = currentVersion;
		}
		versionNumberLabel.setText(String.format("%d", currentVersion));
		if (latestVersion != UpdaterModel.getInstance().getCurrentVersion()) {
			updateReadyInfoLabel.setText("Je dostupná nová aktualizace. Verze " + latestVersion);
		}
	}

	@FXML
	void handleUpdateButton(ActionEvent event) {
		int currentVersion = UpdaterModel.getInstance().getCurrentVersion();
		try {
			int latestVersion = UpdaterModel.getInstance().getLastestVersion();
			handleDownloading(currentVersion, latestVersion);
		} catch (NullPointerException npe) {
			handleDownloading(currentVersion, (currentVersion + 1));
			npe.printStackTrace();
		}

	}

	private void handleDownloading(int currentVersion, int latestVersion) {
		if (currentVersion < latestVersion) {
			Thread t1 = new Thread(new Runnable() {
				@Override
				public void run() {
					progress.setVisible(true);
					InfoModel.getInstance().updateInfo("Stahuji..");
					UpdaterModel.getInstance().download();
					progress.setVisible(false);
					startUpdateApplication();
					System.exit(1);
				}
			});
			t1.start();
		} else {
			InfoModel.getInstance().updateInfo("Současná verze je aktuální!");
		}
	}

	public void startUpdateApplication() {
		try {
			Process proc = Runtime.getRuntime().exec("java -jar update/SpUpdater.jar");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
