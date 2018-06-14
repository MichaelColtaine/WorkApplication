package application.kosmas;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import application.infobar.InfoModel;

public class KosmasDownloader {

	private String loginId, loginPassword, websiteUrl, downloadDirectory;
	private WebDriver driver;
	private ChromeOptions options;
	private int rowCount = 1;
	private boolean success = true;

	public KosmasDownloader() {
		this.websiteUrl = "https://firma.kosmas.cz/";
		this.downloadDirectory = System.getProperty("user.dir") + File.separator + "temp" + File.separator;
	}

	public void start() {
		InfoModel.getInstance().updateInfo("Otevírám prohlížeč");
		openBrowser();
		manageBrowser();
		fetchURL();
	}

	private void openBrowser() {
		String chromeDirectory = "chromedriver/chromedriver.exe";
		System.setProperty("webdriver.chrome.driver", chromeDirectory);
		changeOptions();
		driver = new ChromeDriver(options);
		driver.manage().window().setPosition(new Point(-2000, 0));
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

	private void manageBrowser() {
		driver.manage().deleteAllCookies();
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
	}

	private void fetchURL() {
		driver.get(websiteUrl);

	}

	public void tryLogin() {
		InfoModel.getInstance().updateInfo("Přihlašuji se do portálu Kosmas");
		login();
	}

	private void login() {
		insertLoginInformationAndPressOk();
		success = true;
		if (!driver.getTitle().contains("Kosmas s.r.o. - objednávkový systém pro knihkupce")) {
			success = false;
			InfoModel.getInstance().updateInfo("Nepodařilo se zalogovat");
		}

	}

	private void insertLoginInformationAndPressOk() {
		driver.findElement(By.xpath("//*[@id=\"login_id\"]")).sendKeys(loginId);
		driver.findElement(By.xpath("//*[@id=\"login_pwd\"]")).sendKeys(loginPassword);
		click(driver, By.xpath("/html/body/div[3]/div[1]/form/table/tbody/tr[4]/td[2]/input"));
	}

	public void downloadSSB() {
		openDocuments();
		InfoModel.getInstance().updateInfo("Otevírám dodací listy");
		downloadFilesSSB();
		pause();
	}
	
	public void downloadFlores() {
		openDocuments();
		InfoModel.getInstance().updateInfo("Otevírám dodací listy");
		downloadFilesFlores();
		pause();
	}
	
	private void openDocuments() {
		click(driver, By.xpath("/html/body/div[1]/div[1]/ul/li[8]/a"));
	}
	private void downloadFilesFlores() {
		for (int i = 1; i < rowCount; i++) {
			openDeliveryNote(i);
			waitUntilVisible();
			List<WebElement> elements = getSourceText().findElements(By.linkText("CSV"));
			
			if (elements.size() == 3) {
				downloadTwoFiles(elements);
			} else {
				downloadOneFile(elements);
			}
			goBack();
		}
	}

	private void downloadFilesSSB() {
		for (int i = 1; i < rowCount; i++) {
			openDeliveryNote(i);
			waitUntilVisible();
			List<WebElement> elements = getSourceText().findElements(By.linkText("SBK"));
			if (elements.size() == 3) {
				downloadTwoFiles(elements);
			} else {
				downloadOneFile(elements);
			}
			goBack();
		}
	}

	private void openDeliveryNote(int index) {
		click(driver, By.xpath("//*[@id=\"tbl_seznam\"]/tbody/tr[" + index + "]/td[1]/a"));
	}

	private WebElement getSourceText() {
		return driver.findElement(By.xpath("/html/body/div[3]/table[2]/tbody/tr/td[1]/table/tbody/tr[2]/td/table"));
	}

	private void waitUntilVisible() {
		InfoModel.getInstance().updateInfo("Stahuji dodací listy.   ");
		new WebDriverWait(driver, 10).until(ExpectedConditions.visibilityOfElementLocated(By.linkText("SBK")));
	}

	private void downloadTwoFiles(List<WebElement> elements) {
		InfoModel.getInstance().updateInfo("Stahuji dodací listy..  ");
		elements.get(1).click();
		elements.get(2).click();
	}

	private void downloadOneFile(List<WebElement> elements) {
		InfoModel.getInstance().updateInfo("Stahuji dodací listy..  ");
		elements.get(0).click();
	}

	private void goBack() {
		driver.navigate().back();
		InfoModel.getInstance().updateInfo("Stahuji dodací listy....");
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
			System.out.println(e);
		}
	}

	public void endDriver() {
		driver.quit();
	}

	public void setLoginInfo(String loginId, String loginPassword) {
		this.loginId = loginId;
		this.loginPassword = loginPassword;
	}

	public void downloadAmount(int amount) {
		this.rowCount = amount + 1;
	}

	public boolean hasLoggedIn() {
		return this.success;
	}
}
