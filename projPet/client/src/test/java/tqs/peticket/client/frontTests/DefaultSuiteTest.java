/* 
package tqs.peticket.client;

import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.JavascriptExecutor;
import java.util.*;

public class DefaultSuiteTest {
  private WebDriver driver;
  private Map<String, Object> vars;
  JavascriptExecutor js;
  @Before
  public void setUp() {
    driver = new ChromeDriver();
    js = (JavascriptExecutor) driver;
    vars = new HashMap<String, Object>();
  }
  @After
  public void tearDown() {
    driver.quit();
  }
  @Test
  public void tqspet() {
    driver.get("http://127.0.0.1:5500/projPet/frontend/html/login.html");
    driver.manage().window().setSize(new Dimension(1920, 1052));
    driver.findElement(By.id("login-email")).click();
    driver.findElement(By.id("login-email")).sendKeys("test@example.com");
    driver.findElement(By.id("login-password")).click();
    driver.findElement(By.id("login-password")).sendKeys("password123123");
    driver.findElement(By.cssSelector(".button-home-cards")).click();
    driver.findElement(By.linkText("Add")).click();
    driver.findElement(By.linkText("Pet")).click();
    driver.findElement(By.cssSelector(".user-box:nth-child(1)")).click();
    driver.findElement(By.id("name")).click();
    driver.findElement(By.id("name")).click();
    driver.findElement(By.id("name")).sendKeys("hope");
    driver.findElement(By.id("type")).click();
    driver.findElement(By.id("type")).sendKeys("dog");
    driver.findElement(By.id("breed")).click();
    driver.findElement(By.id("breed")).sendKeys("husky");
    driver.findElement(By.id("age")).click();
    driver.findElement(By.id("age")).sendKeys("3");
    driver.findElement(By.cssSelector(".user-box:nth-child(5)")).click();
    driver.findElement(By.id("color")).click();
    driver.findElement(By.id("color")).sendKeys("black");
    driver.findElement(By.cssSelector(".button-addpet")).click();
    driver.findElement(By.cssSelector(".large-nav > .nav-wrapper")).click();
    driver.findElement(By.cssSelector(".dropdown-item:nth-child(3) .fas")).click();
    driver.findElement(By.linkText("Appointment")).click();
    {
      WebElement dropdown = driver.findElement(By.id("pet-select"));
      dropdown.findElement(By.xpath("//option[. = 'hope']")).click();
    }
    {
      WebElement element = driver.findElement(By.id("pet-select"));
      Actions builder = new Actions(driver);
      builder.moveToElement(element).clickAndHold().perform();
    }
    {
      WebElement element = driver.findElement(By.id("pet-select"));
      Actions builder = new Actions(driver);
      builder.moveToElement(element).perform();
    }
    {
      WebElement element = driver.findElement(By.id("pet-select"));
      Actions builder = new Actions(driver);
      builder.moveToElement(element).release().perform();
    }
    driver.findElement(By.cssSelector(".button-adddateapp")).click();
    {
      WebElement element = driver.findElement(By.cssSelector(".button-adddateapp"));
      Actions builder = new Actions(driver);
      builder.moveToElement(element).perform();
    }
    driver.findElement(By.id("dateofbirth")).click();
    driver.findElement(By.id("dateofbirth")).sendKeys("2024-05-30");
    driver.findElement(By.cssSelector(".time-slot:nth-child(7)")).click();
    driver.findElement(By.name("observations")).click();
    {
      WebElement element = driver.findElement(By.cssSelector(".button-addapp"));
      Actions builder = new Actions(driver);
      builder.moveToElement(element).perform();
    }
    driver.findElement(By.name("observations")).sendKeys("sick");
    driver.findElement(By.cssSelector(".button-addapp")).click();
    {
      WebElement element = driver.findElement(By.tagName("body"));
      Actions builder = new Actions(driver);
      builder.moveToElement(element, 0, 0).perform();
    }
    driver.findElement(By.linkText("Profile")).click();
    driver.findElement(By.cssSelector(".fa-eye")).click();
    {
      WebElement element = driver.findElement(By.cssSelector(".button-updatepet"));
      Actions builder = new Actions(driver);
      builder.moveToElement(element).perform();
    }
    {
      WebElement element = driver.findElement(By.tagName("body"));
      Actions builder = new Actions(driver);
      builder.moveToElement(element, 0, 0).perform();
    }
    driver.findElement(By.cssSelector(".close-btn")).click();
    {
      WebElement element = driver.findElement(By.linkText("Appointments"));
      Actions builder = new Actions(driver);
      builder.moveToElement(element).perform();
    }
    driver.findElement(By.linkText("Appointments")).click();
    {
      WebElement element = driver.findElement(By.tagName("body"));
      Actions builder = new Actions(driver);
      builder.moveToElement(element, 0, 0).perform();
    }
    driver.findElement(By.cssSelector(".fa-qrcode")).click();
    driver.findElement(By.cssSelector(".close2")).click();
    {
      WebElement element = driver.findElement(By.linkText("Profile"));
      Actions builder = new Actions(driver);
      builder.moveToElement(element).perform();
    }
    {
      WebElement element = driver.findElement(By.tagName("body"));
      Actions builder = new Actions(driver);
      builder.moveToElement(element, 0, 0).perform();
    }
    driver.findElement(By.linkText("Profile")).click();
    driver.findElement(By.cssSelector(".fa-eye")).click();
    {
      WebElement element = driver.findElement(By.cssSelector(".button-updatepet"));
      Actions builder = new Actions(driver);
      builder.moveToElement(element).perform();
    }
    driver.findElement(By.cssSelector(".button-updatepet")).click();
    driver.findElement(By.id("update-color")).click();
    {
      WebElement element = driver.findElement(By.cssSelector(".button-save-update"));
      Actions builder = new Actions(driver);
      builder.moveToElement(element).perform();
    }
    driver.findElement(By.id("update-color")).sendKeys("black and white");
    driver.findElement(By.cssSelector(".button-save-update")).click();
    {
      WebElement element = driver.findElement(By.tagName("body"));
      Actions builder = new Actions(driver);
      builder.moveToElement(element, 0, 0).perform();
    }
    driver.findElement(By.cssSelector(".fa-eye")).click();
    driver.findElement(By.cssSelector(".close-btn")).click();
    driver.findElement(By.cssSelector(".fa-trash-alt")).click();
    driver.findElement(By.linkText("Logout")).click();
  }
}

*/
