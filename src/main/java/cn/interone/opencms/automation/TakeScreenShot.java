package cn.interone.opencms.automation;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.UUID;

import org.openqa.selenium.Cookie;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ByIdOrName;

public class TakeScreenShot {

	public static void main(final String[] args) {

		DesiredCapabilities.firefox().setJavascriptEnabled(true);

		FirefoxProfile profile = new FirefoxProfile();
		profile.setEnableNativeEvents(true);

		FirefoxDriver webDriver = new FirefoxDriver(profile);
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.YEAR, 1);
		Cookie acceptCookie = new Cookie("cookie_disclaimer", "accepted",
				"www.bmw.pt", "/", calendar.getTime());
		webDriver.get("http://www.bmw.pt/pt/pt/index.html");
		webDriver.manage().addCookie(acceptCookie);

		FileOutputStream fos = null;
		try {
			System.out.println("start waiting ...");
			Thread.sleep(20000);
			System.out.println("end of waiting ...");
			ByIdOrName byId = new ByIdOrName("bottomNavigation");
			WebElement webElement = webDriver.findElement(byId);
			((JavascriptExecutor) webDriver).executeScript(
					"arguments[0].style.visibility='hidden'", webElement);
			Thread.sleep(2000);
			ByIdOrName byIdCookie = new ByIdOrName("cookieWarningLayer");
			WebElement cookieWarningLayer = webDriver.findElement(byIdCookie);
			((JavascriptExecutor) webDriver).executeScript(
					"arguments[0].style.display='none'", cookieWarningLayer);
			Thread.sleep(2000);
			byte[] data = webDriver.getScreenshotAs(OutputType.BYTES);
			String fileName = UUID.randomUUID().toString() + ".png";
			File file = new File("G:/" + fileName);
			fos = new FileOutputStream(file);
			fos.write(data);

		} catch (RuntimeException e) {
			throw e;
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {

			try {
				if (fos != null)
					fos.close();
			} catch (RuntimeException e) {
				throw new RuntimeException(e);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
			webDriver.close();

		}

	}
}
