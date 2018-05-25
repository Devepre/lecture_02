package myprojects.automation.assignment2;

import myprojects.automation.assignment2.utils.Properties;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.NotFoundException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.BrowserType;

import java.net.URL;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * Base script functionality, can be used for all Selenium scripts.
 */
public abstract class BaseScript {
    private static final Logger logger = Logger.getLogger(BaseScript.class.getName());

    /**
     *
     * @return New instance of {@link WebDriver} object.
     */
    public static WebDriver getDriver() {
        try {
            URL chromeUrl = BaseScript.class.getClassLoader().getResource("chromedriver");
            System.setProperty("webdriver.chrome.driver", chromeUrl.getPath());

            URL firefoxUrl = BaseScript.class.getClassLoader().getResource("geckodriver");
            System.setProperty("webdriver.firefox.driver", firefoxUrl.getPath());
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        WebDriver driver;
        switch (Properties.getBrowser()) {
            case BrowserType.CHROME:
                driver = new ChromeDriver();
                break;
            case BrowserType.FIREFOX:
                driver = new FirefoxDriver();
                break;
            default:
                throw new UnsupportedOperationException("Method doesn't return WebDriver instance");
        }

        return driver;
    }

    /*
     * Perform continuous search for the web element
     */
    public static WebElement continuousFindWebElement(Callable<WebElement> func, long intervalMilliseconds, long repeats) throws Exception {
        WebElement foundElement;
        if (repeats == 0) {
            throw new NotFoundException();
        }
        try {
            foundElement = func.call();
        } catch (NoSuchElementException e) {
            logger.info(String.format("Unable to find web element. Tries left: %d", repeats));
            TimeUnit.MILLISECONDS.sleep(intervalMilliseconds);
            foundElement = continuousFindWebElement(func, intervalMilliseconds, --repeats);
        }
        return foundElement;
    }
}
