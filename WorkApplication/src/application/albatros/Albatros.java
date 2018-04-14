package application.albatros;

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
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Albatros {
	private String loginId, loginPassword, websiteUrl, downloadDirectory;
	private WebDriver driver;
	private ChromeOptions options;
	private int rowCount;
	private boolean hasLoggedIn;

	public Albatros() {
		this.websiteUrl = "https://www.distri.cz/Account/Login?ReturnUrl=%2F";
		this.downloadDirectory = System.getProperty("user.dir") + File.separator + "temp" + File.separator;
		this.rowCount = 0;

	}

	public static void main(String args[]) {
		new Albatros().start();
	}

	public void start() {
		// InfoModel.getInstance().updateInfo("Otevírám prohlížeč");
		openBrowser();
		manageBrowser();
		fetchURL();

		try {
			tryToLogin();
			openMyDocuments();
			downloadFiles();
		} catch (Exception e) {
			System.out.println("Failed to log in");
			System.out.println(e);
			hasLoggedIn = false;
			driver.quit();
		}
		driver.quit();

	}

	public boolean hasLoggedIn() {
		return hasLoggedIn;
	}

	public void tryToLogin() {
		hasLoggedIn = true;
		login();
	}

	public void download() {
		// InfoModel.getInstance().updateInfo("Otevírám dokumenty");
		openMyDocuments();

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
		driver.findElement(By.xpath("//*[@id=\"Email\"]")).sendKeys("loginId");
		driver.findElement(By.xpath("//*[@id=\"Password\"]")).sendKeys("loginPassword");
		click(driver, By.xpath("/html/body/div[3]/div/div/div/div[2]/form/div[4]/div/input"));
	}

	private void openMyDocuments() {
		// click(driver,
		// By.xpath("/html/body/div[4]/div/div/div[2]/div[2]/button/div/div/div/span[1]"));
		// click(driver,
		// By.xpath("/html/body/div[4]/div/div/div[2]/div[2]/ul/li[2]/a"));
		click(driver, By.xpath("/html/body/div[4]/div/div/div[2]/div/button/div/div/div/span[1]"));
		click(driver, By.xpath("/html/body/div[4]/div/div/div[2]/div/ul/li[2]/a"));

	}

	private void downloadFiles() {
		WebElement table = driver.findElement(By.xpath("//*[@id=\"deliveryNotes\"]/tbody"));
		int lowerBound = 1, upperBound = 5;
		for (WebElement row : table.findElements(By.xpath(".//tr"))) {
			// System.out.println(row.getAttribute("id"));
			if (lowerBound > upperBound) {
				break;
			}
			String id = row.getAttribute("id");
			StringBuilder sb = new StringBuilder();

			sb.append("//*[@id=").append("\"").append(id).append("\"").append("]/td[1]/a[1]");
			System.out.println(sb.toString());
			// System.out.println("//*[@id=\"deliveryNotes_row_47784255\"]/td[1]/a[1]");
			Actions actions = new Actions(driver);

			actions.moveToElement(row.findElement(By.xpath(sb.toString()))).click().perform();
			// row.findElement(By.xpath(sb.toString())).click();
			// row.findElement(By.xpath("//*[@id=\"deliveryNotes_row_47784255\"]/td[1]/a[1]")).click();
			lowerBound++;
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
