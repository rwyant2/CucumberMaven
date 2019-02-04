package pages;

import java.io.File;


import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import org.openqa.selenium.chrome.ChromeDriver;

public class BasePage {

	protected static WebDriver driver;
	protected static WebDriverWait wait;
	protected static String landingPageURL;

	static {
//		// TODO: figure out how to replicate auto-driver selection here
//		// TODO: when I start running on windows, this will have to be smarter
		String absPath = new File("").getAbsolutePath();
		System.setProperty("webdriver.chrome.driver", absPath + "/src/main/resources/webdrivers/chromedriver");
		driver = new ChromeDriver();
		wait = new WebDriverWait(driver, 10);
		landingPageURL = "localhost:8080";
		driver.get(landingPageURL);
	}

	//protected WebDriver getDriver() { return driver; }
	//protected WebDriverWait getWait() { return wait; }
	protected void waitUntil(By by) {
		wait.until(ExpectedConditions.visibilityOfElementLocated(by));
	}
	
	public void click(String label)
	{
		WebElement e = driver.findElement(By.xpath("//*[@value=\"" + label + "\"]"));
		e.click();
	}
	
	//I'm assuming each text field in this app will be implemented the same way
	public void enterTextInField(String text,String label) {
		// Ideally, you'd have an id, ngmodel, ngbind, or something unique to grab on to.
		// Why I'm doing it this way is to make a generic method for this
		// That would mimic what an end-user would actualy see.
		label = cleanUpForXPath(label);
		By by = By.xpath("//p[contains(.,\"" + label + "\")]/input");
		wait.until(ExpectedConditions.visibilityOfElementLocated(by));
		driver.findElement(by).sendKeys(text);
	}
	
	private String cleanUpForXPath(String string) {
		string.replace("'", "\\'");
		return string;
	}

	public void verifyTextField(String id, String value) {
		WebElement e = driver.findElement(By.id(id));
		explicitWait(e);
		Assert.assertEquals("Mesaage",value,e.getText());
	}

	public void explicitWait(WebElement e) {
		wait.until(ExpectedConditions.visibilityOf(e));
	}
}
