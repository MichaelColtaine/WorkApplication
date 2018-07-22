package application.updater;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class UpdateInfo {

	private String versionURL = "http://spupdater.wz.cz/index.html";

	private String data = "";

	public UpdateInfo() {
		try {
			getData(versionURL);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getLatestVersion() {
		return data.substring(data.indexOf("[version]") + 9, data.indexOf("[/version]"));
	}

	private void getData(String adress) throws IOException {
		URL url = new URL(adress);
		InputStream html = null;
		html = url.openStream();
		int c = 0;
		StringBuffer buffer = new StringBuffer("");
		while (c != -1) {
			c = html.read();
			buffer.append((char) c);
		}
		this.data = buffer.toString();
	}

}
