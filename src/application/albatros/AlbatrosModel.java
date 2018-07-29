package application.albatros;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import application.RowRecord;
import application.infobar.InfoModel;
import javafx.stage.DirectoryChooser;
import javafx.stage.Window;

public class AlbatrosModel {
	private static AlbatrosModel INSTANCE;
	private AlbatrosDownloader albatros;
	private AlbatrosSettings settings;
	private FileChanger fileChanger;
	private List<RowRecord> listOfNames = new ArrayList<>();

	private AlbatrosModel() {
		this.albatros = new AlbatrosDownloader();
		this.settings = new AlbatrosSettings();
		this.fileChanger = new FileChanger();

	}

	public static AlbatrosModel getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new AlbatrosModel();
		}
		return INSTANCE;
	}

	public void startAlbatrosImport() throws InterruptedException {
		setLoginInfo();
		Thread t1 = new Thread(new Runnable() {
			@Override
			public void run() {
				albatros.startSSB();
			}
		});
		t1.start();
		t1.join();

	}

	public void startAlbatrosImportFlores() throws InterruptedException {
		setLoginInfo();
		Thread t1 = new Thread(new Runnable() {
			@Override
			public void run() {
				albatros.startFlores();
			}
		});
		t1.start();
		t1.join();
	}

	public List<RowRecord> getListOfNames() {

		return listOfNames;
	}

	public void changeAndMoveFile() {
		String downloadPath = System.getProperty("user.dir") + File.separator + "temp" + File.separator;
		File directory = new File(downloadPath);
		fileChanger.setOuputDirectory(AlbatrosModel.getInstance().getSettings().getPath());
		for (File f : directory.listFiles()) {

			InfoModel.getInstance().updateInfo(f.getName());
			String fileName = f.getName().substring(f.getName().length() - 7);
			String name = f.getName().substring(6, f.getName().length() - 4);
			listOfNames.add(new RowRecord(name, fileName, "-38.00%"));
			fileChanger.changeFile(f, fileName);
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
