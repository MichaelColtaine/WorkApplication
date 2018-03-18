package application.kosmas;

import java.io.File;
import java.util.HashMap;
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

public class Kosmas {

	private String loginId, loginPassword, websiteUrl, downloadDirectory;
	private WebDriver driver;
	private ChromeOptions options;
	private int rowCount;

	public Kosmas() {
		this.websiteUrl = "https://firma.kosmas.cz/";
		this.downloadDirectory = System.getProperty("user.dir") + File.separator + "temp" + File.separator;
		this.rowCount = 1;

	}

	public void start() {
		openBrowser();
		manageBrowser();
		fetchURL();
		login();
		openDocuments();
		downloadFiles();

		pause();
		endDriver();
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

	private void login() {
		driver.findElement(By.xpath("//*[@id=\"login_id\"]")).sendKeys(loginId);
		driver.findElement(By.xpath("//*[@id=\"login_pwd\"]")).sendKeys(loginPassword);
		click(driver, By.xpath("/html/body/div[3]/div[1]/form/table/tbody/tr[4]/td[2]/input"));
	}

	private void openDocuments() {
		click(driver, By.xpath("/html/body/div[1]/div[1]/ul/li[8]/a"));
	}

	private void downloadFiles() {

		for (int i = 1; i < rowCount; i++) {
			click(driver, By.xpath("//*[@id=\"tbl_seznam\"]/tbody/tr[" + i + "]/td[1]/a"));
			WebElement fullText = driver
					.findElement(By.xpath("/html/body/div[3]/table[2]/tbody/tr/td[1]/table/tbody/tr[2]/td/table"));
			if (!containsTwoFiles(fullText)) {
				downloadOneFile();
			} else {
				downloadTwoFiles();
			}
		}
	}

	private boolean containsTwoFiles(WebElement fullText) {
		return fullText.getText().contains("Zboží do konsignace");
	}

	private void downloadTwoFiles() {
		click(driver, By
				.xpath("/html/body/div[3]/table[2]/tbody/tr/td[1]/table/tbody/tr[2]/td/table/tbody/tr[9]/td[2]/a[4]"));
		click(driver, By
				.xpath("/html/body/div[3]/table[2]/tbody/tr/td[1]/table/tbody/tr[2]/td/table/tbody/tr[10]/td[2]/a[4]"));
		driver.navigate().back();
	}

	private void downloadOneFile() {
		click(driver, By
				.xpath("/html/body/div[3]/table[2]/tbody/tr/td[1]/table/tbody/tr[2]/td/table/tbody/tr[8]/td[2]/a[4]"));
		driver.navigate().back();
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

	private void endDriver() {
		driver.close();
		driver.quit();
	}

	public void setLoginInfo(String loginId, String loginPassword) {
		this.loginId = loginId;
		this.loginPassword = loginPassword;
	}

	public void downloadAmount(int amount) {
		this.rowCount = amount + 1;
	}

}