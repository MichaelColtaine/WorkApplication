package application.updater;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.dropbox.core.DbxDownloader;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.DownloadErrorException;
import com.dropbox.core.v2.files.FileMetadata;

import application.infobar.InfoModel;
import auth.Authentication;
import javafx.application.Platform;

public class UpdaterModel {

	private static UpdaterModel INSTANCE;
	private int currentVersion = 42;
	private static final String ACCESS_TOKEN = "Mvam_rwrPU8AAAAAAAB2YbsIiwwCyzBA1Cu73A1LZaNetAzMXtz454qSf9aYf70X";
	private String downloadUpdateDirectory = System.getProperty("user.dir") + File.separator + "update" + File.separator
			+ "update.zip";

	public UpdaterModel() {

	}

	public static UpdaterModel getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new UpdaterModel();
		}
		return INSTANCE;
	}

	public int getLastestVersion() {
		return Authentication.getInstance().getLatestVersion();
	}

	public int getCurrentVersion() {
		return this.currentVersion;
	}

	public void download() {
		DbxRequestConfig config = new DbxRequestConfig("dropbox/java-tutorial", "en_US");
		DbxClientV2 client = new DbxClientV2(config, ACCESS_TOKEN);

		try {
			downloadFile(client, "/update.zip", downloadUpdateDirectory);
		} catch (DbxException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		;

	}

	public static void downloadFile(DbxClientV2 client, String dropBoxFilePath, String localFileAbsolutePath)
			throws DownloadErrorException, DbxException, IOException {

		// Create DbxDownloader
		DbxDownloader<FileMetadata> dl = client.files().download(dropBoxFilePath);

		// FileOutputStream
		FileOutputStream fOut = new FileOutputStream(localFileAbsolutePath);
		System.out.println("Downloading .... " + dropBoxFilePath);

		// Add a progress Listener
		dl.download(new ProgressOutputStream(fOut, dl.getResult().getSize(), (long completed, long totalSize) -> {
			System.out.println((completed * 100) / totalSize + " %");
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					InfoModel.getInstance().updateInfo((completed * 100) / totalSize + "%");
				}
			});

		}));

	}

}
