package application.euromedia;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.poi.ss.formula.functions.T;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class EuromediaWebScraper {

	private final String tempPath;
	private final String USER_AGENT = "\"Mozilla/5.0 (Windows NT\" +\n"
			+ "          \" 6.1; WOW64) AppleWebKit/535.2 (KHTML, like Gecko) Chrome/15.0.874.120 Safari/535.2\"";
	private final String loginFormUrl = "https://vo.knizniweb.cz/prihlaseni/?url=https%3A%2F%2Fvo.knizniweb.cz%2Fmoje-dokumenty%2Fdodaci-listy%2F";
	private final String loginActionUrl = "https://vo.knizniweb.cz/prihlaseni/index$204-login.html";
	private final String username = EuroModel.getInstance().getSettings().getId();
	private final String password = EuroModel.getInstance().getSettings().getPassword();
	private final String filetype;
	private final ArrayList<Element> listOfLinksForPdf = new ArrayList<>();
	private final ArrayList<Element> listOfLinksForSbk = new ArrayList<>();
	private final ArrayList<Element> listOfLinksForXls = new ArrayList<>();
	private int amount;

	private final HashMap<String, String> cookies = new HashMap<>();
	private final HashMap<String, String> formData = new HashMap<>();

	public EuromediaWebScraper(String filetype) {
		tempPath = System.getProperty("user.dir") + File.separator + "temp" + File.separator;
		this.filetype = filetype;
	}

	public void startDownloading() {
		try {
			Connection.Response loginForm = Jsoup.connect(loginFormUrl).method(Connection.Method.GET)
					.userAgent(USER_AGENT).execute();
			Document loginDoc = loginForm.parse();
			cookies.putAll(loginForm.cookies());
			formData.put("loginId", username);
			formData.put("passwd", password);
			formData.put("url", "https://vo.knizniweb.cz/moje-dokumenty/dodaci-listy/");
			Connection.Response homepage = Jsoup.connect(loginActionUrl).cookies(cookies).data(formData)
					.method(Connection.Method.POST).userAgent(USER_AGENT).execute();
			Elements links = homepage.parse().select("a[href]");
			sortData(links);
			handleDownloading();
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void handleDownloading() throws InterruptedException {
		if ("SSB".equalsIgnoreCase(filetype)) {
			downloadSbkFiles(amount);
			downloadPdfFiles(amount);
		} else {
			downloadXlsFiles(amount);
		}

	}

	private void downloadSbkFiles(int amount) {
		for (int i = 0; i < amount; i++) {
			new Thread(new SbkDownloader(listOfLinksForSbk.get(i), USER_AGENT, loginFormUrl, tempPath, cookies))
					.start();
		}
	}

	private void downloadPdfFiles(int amount) {
		for (int i = 0; i < amount; i++) {
			new Thread(new PdfDownloader(listOfLinksForPdf.get(i), USER_AGENT, loginFormUrl, tempPath, cookies))
					.start();
		}
	}

	private void downloadXlsFiles(int amount) throws InterruptedException {
		for (int i = 0; i < amount; i++) {
			Thread t1 = new Thread(
					new XlsDownloader(listOfLinksForXls.get(i), USER_AGENT, loginFormUrl, tempPath, cookies));
			t1.join();
			t1.start();
		}
	}

	private ArrayList<Element> sortData(Elements elements) {
		for (Element e : elements) {
			if (e.text().contentEquals("PDF")) {
				listOfLinksForPdf.add(e);
			} else if (e.text().contentEquals("SBK")) {
				listOfLinksForSbk.add(e);
			} else if (e.text().contentEquals("XLS")) {
				listOfLinksForXls.add(e);
			}
			continue;
		}
		return elements;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}
}
