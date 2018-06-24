package application.kosmas;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import application.RowRecord;
import application.infobar.InfoModel;
import javafx.stage.DirectoryChooser;
import javafx.stage.Window;

public class KosmasModel {
	private static KosmasModel INSTANCE;
	private KosmasDownloader kosmas;
	private KosmasSettings settings;
	private KosmasFileMover fileMover;

	private KosmasModel() {
		this.kosmas = new KosmasDownloader();
		this.settings = new KosmasSettings();
		this.fileMover = new KosmasFileMover();

	}

	public static KosmasModel getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new KosmasModel();
		}
		return INSTANCE;
	}

	public void setDestinantionDirectory() {
		fileMover.setDestinantionDirectory(settings.getPath());
	}

	public ArrayList<RowRecord> getFileNames() {
		return fileMover.getListOfNames();

	}

	public void moveAndRenameFiles() {
		try {
			InfoModel.getInstance().updateInfo("Přesouvám  a přejmenovávám soubory");
			fileMover.move();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void start() {
		setLoginInfo();
		kosmas.start();
	}

	public void login() {
		kosmas.tryLogin();
	}

	public void downloadFilesSSB() {
		kosmas.downloadSSB();
	}

	public void downloadFilesFlores() {
		kosmas.downloadFlores();
	}

	public boolean hasSuccessfulyLoggedIn() {
		return kosmas.hasLoggedIn();
	}

	public void end() {
		kosmas.endDriver();
	}

	private void setLoginInfo() {
		kosmas.setLoginInfo(settings.getId(), settings.getPassword());
	}

	public void deleteAllTempFiles() {
		File directory = new File(System.getProperty("user.dir") + "\\temp\\");
		createDirectoryifItDoesNotExists(directory);
		for (File f : directory.listFiles()) {
			f.delete();
		}
	}

	private void createDirectoryifItDoesNotExists(File directory) {
		if (!directory.exists()) {
			directory.mkdirs();
		}
	}

	public File chooseDirectory(Window owner) {
		DirectoryChooser dc = new DirectoryChooser();
		return dc.showDialog(owner);
	}

	public void setQuantityOfItemsToDownload(String selectedItem) {
		kosmas.downloadAmount(Integer.parseInt(selectedItem));
	}

	public KosmasSettings getSettings() {
		return this.settings;
	}

	public void saveSettings(String path, String email, String password) {
		settings.savePath(path);
		settings.saveLoginInfo(email, password);
		setLoginInfo();
	}

}
