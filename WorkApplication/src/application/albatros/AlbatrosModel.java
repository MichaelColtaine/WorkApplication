package application.albatros;

import java.io.File;

import javafx.stage.DirectoryChooser;
import javafx.stage.Window;

public class AlbatrosModel {
	private static AlbatrosModel INSTANCE;
	private Albatros albatros;
	private AlbatrosSettings settings;

	private AlbatrosModel() {
		this.albatros = new Albatros();
		this.settings = new AlbatrosSettings();

	}

	public static AlbatrosModel getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new AlbatrosModel();
		}
		return INSTANCE;
	}

	public void startAlbatrosImport() {
		albatros.setDownloadDirecotry(AlbatrosModel.getInstance().getSettings().getPath());
		setLoginInfo();
		albatros.start();
	}

	private void setLoginInfo() {
		albatros.setLoginInfo(settings.getId(), settings.getPassword());
	}

	public void saveSettings(String path, String email, String password) {
		settings.savePath(path);
		settings.saveLoginInfo(email, password);
	}

	public AlbatrosSettings getSettings() {
		return this.settings;
	}

	public File chooseDirectory(Window owner) {
		DirectoryChooser dc = new DirectoryChooser();
		return dc.showDialog(owner);
	}

	public void setQuantityOfItemsToDownload(String selectedItem) {
		albatros.downloadAmount(Integer.parseInt(selectedItem));
	}

}
