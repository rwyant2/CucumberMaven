package pages;

import java.io.File;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import cucumber.api.Scenario;
import cucumber.api.java.Before;

import org.openqa.selenium.chrome.ChromeDriver;

public class BasePage {

	protected static WebDriver driver;
	protected static WebDriverWait wait;
	protected static String landingPageURL;
	
	public static void basePage() {
		// TODO: figure out how to replicate auto-driver selection here
		// TODO: when I start running on windows, this will have to be smarter
		System.out.println("basepage constructor");
//			String absPath = new File("").getAbsolutePath();		
//			System.setProperty("webdriver.chrome.driver", absPath + "//webdrivers//chromedriver");
//			wait = new WebDriverWait(driver, 10);
//			driver = new ChromeDriver();
//			landingPageURL = "localhost:8080";
	}
	
	public void driverSetup(Scenario scenario) {
		System.out.println("driverSetup method");
		String absPath = new File("").getAbsolutePath();		
		System.setProperty("webdriver.chrome.driver", absPath + "//webdrivers//chromedriver");
		driver = new ChromeDriver();
		wait = new WebDriverWait(driver, 10);
		landingPageURL = "localhost:8080";
	}
	
	//protected WebDriver getDriver() { return driver; }
	//protected WebDriverWait getWait() { return wait; }
	protected void waitUntil(By by) {
		wait.until(ExpectedConditions.visibilityOfElementLocated(by));
	}
	
	protected void click(By by) {
		driver.findElement(by).click();	
	}
	
	//I'm assuming each text field in this app will be implemented the same way
	public void enterTextInField(String text,String label) {
		// Ideally, you'd have an id, ngmodel, ngbind, or something unique to grab on to.
		// Why I'm doing it this way is to make a generic method for this
		// That would mimic what an end-user would actualy see.
		label = cleanUpForXPath(label);
		By by = By.xpath("//input/parent::p[contains(.,'" + label + "')]");
		wait.until(ExpectedConditions.visibilityOfElementLocated(by));
		driver.findElement(by).sendKeys(text);
	}
	
	private String cleanUpForXPath(String string) {
		string.replace("'", "\\'");
		System.out.println("beark");
		return string;
	}
}
