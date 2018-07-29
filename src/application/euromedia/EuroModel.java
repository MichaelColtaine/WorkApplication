package application.euromedia;

import java.io.File;
import java.util.ArrayList;

import application.RowRecord;
import application.utils.FileUnziper;
import javafx.stage.DirectoryChooser;
import javafx.stage.Window;

public class EuroModel {

	private static EuroModel INSTANCE;
	private final EuroSettings settings;
	private final ArrayList<RowRecord> records;
	private final String TEMP_DIRECOTORY = System.getProperty("user.dir") + File.separator + "temp" + File.separator;
	private final File tempDirectory = new File(TEMP_DIRECOTORY);

	private EuroModel() {

		this.settings = new EuroSettings();
		this.records = new ArrayList<>();

	}

	public static EuroModel getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new EuroModel();
		}
		return INSTANCE;
	}

	public void deleteAllTempFiles() {
		File directory = new File(System.getProperty("user.dir") + "\\temp\\");
		createDirectoryifItDoesNotExists(directory);
		for (File f : directory.listFiles()) {

			f.delete();
		}
		for (File f : directory.listFiles()) {
			if (f.getName().contains(".zip")) {
				f.delete();
			}
		}
	}

	private void createDirectoryifItDoesNotExists(File directory) {
		if (!directory.exists()) {
			directory.mkdirs();
		}
	}

	public void saveSettings(String path, String email, String password) {
		settings.savePath(path);
		settings.saveLoginInfo(email, password);
	}

	public EuroSettings getSettings() {
		return this.settings;
	}

	public ArrayList<RowRecord> getRecords() {
		return this.records;
	}

	public File chooseDirectory(Window owner) {
		DirectoryChooser dc = new DirectoryChooser();
		return dc.showDialog(owner);
	}

	public void changeAllEuroFilesSSB(String toDirectory) {
		StringBuilder sb = new StringBuilder();
		for (File source : tempDirectory.listFiles()) {
			sb.delete(0, sb.length());
			sb.append(toDirectory).append(File.separator)
					.append(source.getName().replaceAll(".zip", ".txt").substring(source.getName().length() - 7));
			File destination = new File(sb.toString());
			FileUnziper.unzip(source, destination);
		}
	}

	public void changeAllEuroFilesFlores(String toDirectory) {
		StringBuilder sb = new StringBuilder();
		for (File source : tempDirectory.listFiles()) {
			sb.delete(0, sb.length());
			sb.append(TEMP_DIRECOTORY).append(File.separator).append(source.getName().replaceAll("zip", "xls"));
			File destination = new File(sb.toString());
			FileUnziper.unzip(source, destination);
		}
		removeZipFiles(tempDirectory);
		createFinalExcelFile();
	}

	private void removeZipFiles(File directory) {
		for (File f : directory.listFiles()) {
			if (f.getName().contains(".zip")) {
				f.delete();
			}
		}
	}

	private void createFinalExcelFile() {
		EuromediaExcelConverter converter = new EuromediaExcelConverter();
		converter.euromediaExcel(TEMP_DIRECOTORY, EuroModel.getInstance().getSettings().getPath());
	}

}
