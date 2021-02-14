package application.albatros;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.javafx.image.impl.BaseByteToIntConverter;

import application.infobar.InfoModel;

public class AlbatrosDownloader {
	private String loginId = "", loginPassword = "", websiteUrl, downloadDirectory;
	private WebDriver driver;
	private ChromeOptions options;
	private int rowCount;
	private boolean hasLoggedIn;

	public AlbatrosDownloader() {
		this.websiteUrl = "https://www.distri.cz/Account/Login?ReturnUrl=%2F";
		this.rowCount = 0;
		this.downloadDirectory = System.getProperty("user.dir") + File.separator + "temp" + File.separator;
	}

	public void startSSB() {
		openBrowser();
		manageBrowser();
		fetchURL();
		tryToLogin();
		if (hasLoggedIn) {
			openMyDocuments();
			downloadFilesSSB();
			driver.quit();
		}
	}

	public String getPageSource() {
		return driver.getPageSource();
	}

	public void quit() {
		driver.quit();
	}

	public void startFlores() {
		openBrowser();
		manageBrowser();
		fetchURL();
		tryToLogin();
		if (hasLoggedIn) {
			openMyDocuments();
		 downloadFilesFlores();
			driver.quit();
		}

	}

	public void setDownloadDirecotry(String path) {
		this.downloadDirectory = path;
	}

	public boolean hasLoggedIn() {
		return hasLoggedIn;
	}

	public void tryToLogin() {
		InfoModel.getInstance().updateInfo("Přihlašuji se.");
		hasLoggedIn = true;
		login();
		if (driver.getPageSource().contains("Neplatné přihlašovací údaje.")) {
			hasLoggedIn = false;
			driver.quit();
		}
	}

	public void end() {
		pause();
		endDriver();
	}

	public void openBrowser() {
		InfoModel.getInstance().updateInfo("Otevírám prohlížeč.");
		String chromeDirectory = "chromedriver/chromedriver.exe";
		System.setProperty("webdriver.chrome.driver", chromeDirectory);
		changeOptions();
		driver = new ChromeDriver(options);
		driver.manage().window().setPosition(new Point(2000, 0));
	}

	private void changeOptions() {
		Map<String, Object> prefs = createPreferences();
		this.options = new ChromeOptions();
		createDownloadDirectoryIfItDoesNotExists();
		options.setExperimentalOption("prefs", prefs);
	}

	private Map<String, Object> createPreferences() {
		Map<String, Object> prefs = new HashMap<String, Object>();
		prefs.put("download.default_directory", downloadDirectory);
		prefs.put("plugins.always_open_pdf_externally", true);
		prefs.put("download.prompt_for_download", false);
		return prefs;
	}

	private void createDownloadDirectoryIfItDoesNotExists() {
		File f = new File(downloadDirectory);
		if (!f.exists()) {
			f.mkdirs();
		}
	}

	public void manageBrowser() {
		driver.manage().deleteAllCookies();
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
	}

	public void fetchURL() {
		driver.get(websiteUrl);
	}

	private void login() {
		driver.findElement(By.xpath("//*[@id=\"Email\"]")).sendKeys(loginId);
		driver.findElement(By.xpath("//*[@id=\"Password\"]")).sendKeys(loginPassword);
		click(driver, By.xpath("/html/body/div[3]/div/div/div/div[2]/form/div[4]/div/input"));
	}

	public void openMyDocuments() {
		 driver.get("https://www.distri.cz/Customer/Detail");
		//test();
	}
	// http://distri.cz/DeliveryNote/Download/55515463/?custom=True
	// https://www.distri.cz/DeliveryNote/Download/72695546/?custom=True
	// https://www.distri.cz/DeliveryNote/Download/72695520/?custom=True
	// https://www.distri.cz/DeliveryNote/Download/72657073/?custom=True

	private void test() {
		driver.get("https://www.distri.cz/odata/DeliveryNote");
		String pageSource = driver.getPageSource();

		String[] object = pageSource.split(",");
		List<String> ids = new ArrayList<>();
		for (String s : object) {

			if (s.contains("packages") || s.contains("deliveryType") || s.contains("\"value\":")) {
				continue;
			}
			if (s.contains("\"id\":")) {
				s = s.replace("{\"id\":", "");
			
				ids.add(s);
			}
		}

		List<String> links = new ArrayList<>();
		for (String id : ids) {
			links.add(String.format("https://www.distri.cz/DeliveryNote/Download/%s/?custom=True", id));
		}
		
		
		List<String> downloadLinks = new ArrayList<>();
		int last = links.size()-10;
		
		for(int i = last; i < links.size(); i++){
			downloadLinks.add(links.get(i));
		}
		
		System.out.println(downloadLinks.size());
		
		
		
		int number = 0;
		for (String link : downloadLinks) {
			System.out.println(link);
			try {
				URL url = new URL(link);
				HttpURLConnection con = (HttpURLConnection) url.openConnection();
				con.setRequestProperty("Cookie", getCookies());
				String deliveryNoteName = number + "s";
				number++;
				InfoModel.getInstance().updateInfo("Stahuji " + deliveryNoteName);
				Files.copy(con.getInputStream(), Paths.get(downloadDirectory + deliveryNoteName + ".xlsx"));
			} catch (IOException ef) {
				ef.printStackTrace();
			}
		}

	}

	private void scrollDown() {

		Actions action = new Actions(driver);

		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {

			e.printStackTrace();
		}

		action.sendKeys(Keys.PAGE_DOWN).build().perform();

	}

	private void setDropDownMenu() {

		// click(driver,
		// By.xpath("//*[@id=\"deliveryNotes_length\"]/label/span/span[1]/span"));
		WebElement element = driver.findElement(By.xpath("//*[@id=\"deliveryNotes_length\"]/label/span/span[1]/span"));
		Actions actions = new Actions(driver);
		actions.moveToElement(element).click().perform();

		WebElement element2 = driver
				.findElement(By.xpath("//*[@id=\"select2-deliveryNotes_length-gs-result-ndyg-100\"]"));
		Actions actions2 = new Actions(driver);
		actions2.click(element2).perform();
	}

	public WebDriver getDriver() {
		return driver;
	}

	private void downloadFilesSSB() {
		for (int i = 0; i < rowCount; i++) {

			try {
				URL url = new URL(listOfDownloadLinksSSB().get(i).getAttribute("href"));
				System.out.println(url);
				HttpURLConnection con = (HttpURLConnection) url.openConnection();
				con.setRequestProperty("Cookie", getCookies());
				String deliveryNoteName = getListOfAllDeliveryNoteNames().get(i);
				InfoModel.getInstance().updateInfo("Stahuji " + deliveryNoteName);
				Files.copy(con.getInputStream(), Paths.get(downloadDirectory + deliveryNoteName + ".txt"));
			} catch (IOException ef) {
				ef.printStackTrace();
			}
		}
	}

	private ArrayList<String> getListOfAllDeliveryNoteNames() {
		ArrayList<String> names = new ArrayList<>();
		String fullpage = driver.getPageSource();
		String[] test = fullpage.split("</a></td><td class=\" text-right\">");
		for (String s : test) {
			String name = s.substring(s.length() - 14);
			if (name.contains("VPT")) {
				names.add(name);
			}

		}
		return names;
	}

	private List<WebElement> listOfDownloadLinksSSB() {
		return driver.findElements(By.cssSelector("[title^='Exportovat dle nastavení']"));
	}

	private void downloadFilesFlores() {
		for (int i = 0; i < rowCount; i++) {
			try {
				URL url = new URL(listOfDownloadLinksFlores().get(i).getAttribute("href"));
				HttpURLConnection con = (HttpURLConnection) url.openConnection();
				con.setRequestProperty("Cookie", getCookies());
				String deliveryNoteName = getListOfAllDeliveryNoteNames().get(i);
				InfoModel.getInstance().updateInfo("Stahuji " + deliveryNoteName);
				Files.copy(con.getInputStream(), Paths.get(downloadDirectory + deliveryNoteName + ".xlsx"));
			} catch (IOException ef) {
				ef.printStackTrace();
			}
		}
	}

	private List<WebElement> listOfDownloadLinksFlores() {
		return driver.findElements(By.cssSelector("[title^='Exportovat do Excelu']"));
	}

	private String getCookies() {
		StringBuilder sb = new StringBuilder();
		for (Cookie c : driver.manage().getCookies()) {
			sb.append(c);
		}
		return sb.toString();
	}

	private void click(WebDriver driver, By location) {
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(location));
		driver.findElement(location).click();
	}

	private void pause() {
		try {
			Thread.sleep(1000);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void endDriver() {
		driver.quit();
	}

	public void setLoginInfo(String loginId, String loginPassword) {
		this.loginId = loginId;
		this.loginPassword = loginPassword;
	}

	public void downloadAmount(int amount) {
		this.rowCount = amount;
	}

}
