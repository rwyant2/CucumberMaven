package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.By.ByXPath;
import pages.BasePage;

public class Html5Page extends BasePage {
	public void goToHTML5Page() {
		driver.findElement(By.xpath("//a[@href='/html5']")).click();
	}
}
