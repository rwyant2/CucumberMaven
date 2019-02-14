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

	public void clickButton(String label) { driver.findElement(By.xpath("//*[@value=\"" + label + "\"]")).click(); }
	public void clickLink(String label) { driver.findElement(By.xpath("//a[text()=\"" + label + "\"]")).click(); }
	public void clickWebElementWithId(String id) { driver.findElement(By.id(id)).click(); }

	//I'm assuming each text field in this app will be implemented the same way
	public void enterTextInField(String text,String label) {
		// Ideally, you'd have an id, ng-model, ng-bind, or something unique to grab on to.
		// Why I'm doing it this way is to make a generic method for this
		// That would mimic what an end-user would actually see.
		label = cleanUpForXPath(label);
		WebElement e = driver.findElement(By.xpath("//p[contains(.,\"" + label + "\")]/input"));
		explicitWait(e);
		e.sendKeys(text);
	}
	
	private String cleanUpForXPath(String string) {
		string.replace("'", "\\'");
		return string;
	}

	public void verifyTextField(String id, String value) {
		WebElement e = driver.findElement(By.id(id));
		explicitWait(e);
		Assert.assertEquals("Failed on verifying " + id + ": ",value,e.getText());
	}

	public void verifyTextFieldIsBlank(String id) {
		wait.until(ExpectedConditions.presenceOfElementLocated(By.id(id)));
		Assert.assertEquals(driver.findElement(By.id(id)).getText(),"");
	}

	private void explicitWait(WebElement e) {
		wait.until(ExpectedConditions.visibilityOf(e));
	}

	public String getPageTitle() {
		//todo: in SpringBootProject, give the page title element it's own id
		WebElement e = driver.findElement(By.xpath("//h1"));
		String header = e.getText();
		switch(header) {
			case "This is the index page! Hooray!": return "index";
			case "HTML5 fun times": return "html5";
			case "Result": return "results";
			default: return "i dunno lol";
		}

	}

}
