package application.euromedia;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

public class XlsDownloader extends Downloader implements Runnable {

	public XlsDownloader(Element element, String USER_AGENT, String loginFormUrl, String tempPath,
			HashMap<String, String> cookies) {
		super(element, USER_AGENT, loginFormUrl, tempPath, cookies);
	}

	@Override
	public void run() {
		try {
			if (element.text().contains("XLS")) {
				String linkUrl = (element.attr("abs:href"));
				byte[] bytes = Jsoup.connect(linkUrl).cookies(cookies).userAgent(USER_AGENT)
						.referrer("https://vo.knizniweb.cz/moje-dokumenty/").ignoreContentType(true).maxBodySize(0)
						.timeout(600000).execute().bodyAsBytes();
				FileOutputStream fos = new FileOutputStream(new File(
						tempPath + linkUrl.substring(linkUrl.indexOf("_") + 1, linkUrl.lastIndexOf("zip") + 3)));
				fos.write(bytes);
				fos.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
