package application.albatros;

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
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import application.infobar.InfoModel;

public class Albatros {
	private String loginId, loginPassword, websiteUrl, downloadDirectory;
	private WebDriver driver;
	private ChromeOptions options;
	private int rowCount;
	private boolean hasLoggedIn;

	public Albatros() {
		this.websiteUrl = "https://www.distri.cz/Account/Login?ReturnUrl=%2F";
		this.rowCount = 0;
		this.downloadDirectory = System.getProperty("user.dir") + File.separator + "temp" + File.separator;
	}

	public void start() {
		InfoModel.getInstance().updateInfo("Otevírám prohlížeč");
		openBrowser();
		manageBrowser();
		fetchURL();
		tryToLogin();
		if (hasLoggedIn) {
			openMyDocuments();
			downloadFiles();
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
		hasLoggedIn = true;
		login();
		if (driver.getPageSource().contains("Neplatné přihlašovací údaje.")) {
			InfoModel.getInstance().updateInfo("Nepodařilo se přihlásit");
			hasLoggedIn = false;
			driver.quit();
		}
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
		InfoModel.getInstance().updateInfo("Přihlašuji se do portálu Distri");
		driver.findElement(By.xpath("//*[@id=\"Email\"]")).sendKeys(loginId);
		driver.findElement(By.xpath("//*[@id=\"Password\"]")).sendKeys(loginPassword);
		click(driver, By.xpath("/html/body/div[3]/div/div/div/div[2]/form/div[4]/div/input"));
	}

	private void openMyDocuments() {

		InfoModel.getInstance().updateInfo("Otevírám dokumenty");
		click(driver, By.xpath("/html/body/div[4]/div/div/div[2]/div/button/div/div/div/span[1]"));
		click(driver, By.xpath("/html/body/div[4]/div/div/div[2]/div/ul/li[2]/a"));

	}

	private void downloadFiles() {
		List<WebElement> elements = driver.findElements(By.cssSelector("[title^='Exportovat dle nastavení']"));
		Actions actions = new Actions(driver);
		agreeToCookies();
		for (int i = 0; i < rowCount; i++) {
			InfoModel.getInstance().updateInfo("Stahuji soubory.   ");
			// elements.get(i).click();

			actions.moveToElement(elements.get(i)).click().perform();
			InfoModel.getInstance().updateInfo("Stahuji soubory..  ");
			pause();

			// pause();
			InfoModel.getInstance().updateInfo("Stahuji soubory... ");
		}

	}

	private void agreeToCookies() {
		click(driver, By.xpath("/html/body/div[1]/button"));
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
