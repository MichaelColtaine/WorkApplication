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

public class UpdaterModel {

	private static UpdaterModel INSTANCE;
	private UpdateInfo updater = new UpdateInfo();
	private int currentVersion = 0;
	private static final String ACCESS_TOKEN = "Mvam_rwrPU8AAAAAAAB2WWFrcxCL6L33QPqqZTfDwubtEQ8ixHArsw7sfjgF_qKU";
	private String downloadUpdateDirectory = System.getProperty("user.dir") + File.separator + "update" + File.separator
			+ "update.7z";

	public UpdaterModel() {

	}

	public static UpdaterModel getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new UpdaterModel();
		}
		return INSTANCE;
	}

	public String getLastestVersion() {
		return updater.getLatestVersion();
	}

	public int getCurrentVersion() {
		return this.currentVersion;
	}

	public String getDownloadUrl() {
		return updater.getDownloadUrl();
	}

	public void download() {
		DbxRequestConfig config = new DbxRequestConfig("dropbox/java-tutorial", "en_US");
		DbxClientV2 client = new DbxClientV2(config, ACCESS_TOKEN);

		try {
			downloadFile(client, "/startFlores.7z", downloadUpdateDirectory);
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

		}));

	}

}
