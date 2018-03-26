package application.euromedia;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import application.infobar.InfoModel;

public class Euromedia {
	private String loginId, loginPassword, websiteUrl, downloadDirectory;
	private WebDriver driver;
	private ChromeOptions options;
	private int rowCount;
	private boolean hasLoggedIn;

	public Euromedia() {
		this.websiteUrl = "https://vo.knizniweb.cz/";
		this.downloadDirectory = System.getProperty("user.dir") + File.separator + "temp" + File.separator;
		this.rowCount = 0;

	}

	public void start() {
		InfoModel.getInstance().updateInfo("Otevírám prohlížeč");
		openBrowser();
		manageBrowser();
		fetchURL();
	}

	public boolean hasLoggedIn() {
		return hasLoggedIn;
	}

	public void tryToLogin() {
		hasLoggedIn = true;
		login();
		if (!driver.getTitle().contains("Knižní web | Úvodní stránka")) {
			hasLoggedIn = false;
			InfoModel.getInstance().updateInfo("Nepodařilo se zalogovat");
			driver.quit();
		}
	}

	public void download() {
		InfoModel.getInstance().updateInfo("Otevírám dokumenty");
		openMyDocuments();
		downloadFiles();
	}

	public void end() {
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
		click(driver, By.xpath("//*[@id=\"login-link\"]/a"));
		driver.findElement(By.xpath("//*[@id=\"login-id\"]")).sendKeys(loginId);
		driver.findElement(By.xpath("//*[@id=\"passwd\"]")).sendKeys(loginPassword);
		click(driver, By.xpath("//*[@id=\"content-main\"]/form/table/tbody/tr[3]/td[2]/button"));
	}

	private void openMyDocuments() {
		driver.findElement(By.id("nav-dokumenty")).click();
	}

	private void downloadFiles() {
		for (int i = 2; i <= rowCount; i++) {
			InfoModel.getInstance().updateInfo("Stahuji dodací listy.    ");
			click(driver, By.xpath("//*[@id=\"content-main\"]/table/tbody/tr[" + i + "]/td[9]"));
			InfoModel.getInstance().updateInfo("Stahuji dodací listy...  ");
			click(driver, By.xpath("//*[@id=\"content-main\"]/table/tbody/tr[" + i + "]/td[11]"));
			InfoModel.getInstance().updateInfo("Stahuji dodací listy.....");
		}
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
		// driver.close();
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
