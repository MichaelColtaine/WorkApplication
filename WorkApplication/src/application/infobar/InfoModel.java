package application.infobar;

import javafx.application.Platform;
import javafx.scene.control.Label;

public class InfoModel {

	private static InfoModel INSTANCE = null;
	private InfoString info = new InfoString();

	private InfoModel() {

	}

	public static InfoModel getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new InfoModel();
		}
		return INSTANCE;
	}

	public void bindLabelToInfo(Label label) {
		label.textProperty().bind(info.getInfoProperty());
	}

	public void updateInfo(String text) {
		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				info.setInfo(text);
			}

		});

	}

}
