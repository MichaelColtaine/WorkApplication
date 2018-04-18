package application.albatros;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import application.infobar.InfoModel;
import javafx.stage.DirectoryChooser;
import javafx.stage.Window;

public class AlbatrosModel {
	private static AlbatrosModel INSTANCE;
	private Albatros albatros;
	private AlbatrosSettings settings;
	private FileChanger fileChanger;
	private List<String> listOfNames = new ArrayList<>();

	private AlbatrosModel() {
		this.albatros = new Albatros();
		this.settings = new AlbatrosSettings();
		this.fileChanger = new FileChanger();

	}

	public static AlbatrosModel getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new AlbatrosModel();
		}
		return INSTANCE;
	}

	public void startAlbatrosImport() {
		// albatros.setDownloadDirecotry(AlbatrosModel.getInstance().getSettings().getPath());
		setLoginInfo();
		albatros.start();
	}

	public List<String> getListOfNames() {
		return this.listOfNames;
	}

	public void changeAndMoveFile() {
		String downloadPath = System.getProperty("user.dir") + File.separator + "temp" + File.separator;
		File directory = new File(downloadPath);
		fileChanger.setOuputDirectory(AlbatrosModel.getInstance().getSettings().getPath());
		for (File f : directory.listFiles()) {
			StringBuilder sb = new StringBuilder();
			InfoModel.getInstance().updateInfo("Přejmenovávám soubory");
			String name = f.getName().substring(f.getName().length() - 7);
			sb.append(f.getName().substring(6, 17)).append(" --> ").append(name);
			listOfNames.add(sb.toString());
			fileChanger.changeFile(f, name);
		}
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
