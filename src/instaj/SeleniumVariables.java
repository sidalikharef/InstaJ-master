/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package instaj;

import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

/**
 *
 * @author Diego
 */
public class SeleniumVariables {

    WebDriver driver;

    public void setChromeDriver() {
        String exePath ="C:\\Program Files\\Google\\Chrome\\Application\\chromedriver.exe";
        System.setProperty("webdriver.chrome.driver", exePath);
        this.driver = new ChromeDriver();
        System.out.println("Chrome Driver loaded, using new profile.");
    }

    public WebDriver getDriver() {
        return driver;
    }

    public void setChromeProfile() {
        String exePath = "C:\\Program Files\\Google\\Chrome\\Application\\chromedriver.exe";
        System.setProperty("webdriver.chrome.driver", exePath); 
        ChromeOptions options = new ChromeOptions();
        Map<String, Object> prefs = new HashMap<String, Object>();
        prefs.put("profile.default_content_setting_values.notifications", 2);
        options.setExperimentalOption("prefs", prefs);
        this.driver = new ChromeDriver(options);
        System.out.println("Chrome Driver loaded, using local profile.");
    }
}