package myprojects.automation.assignment2;

import myprojects.automation.assignment2.utils.Properties;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.NotFoundException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.BrowserType;

import java.net.URL;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * Base script functionality, can be used for all Selenium scripts.
 */
public abstract class BaseScript {
    public static final Logger logger = Logger.getLogger(BaseScript.class.getName());
    // Check it true if need logging
    private static boolean logMode = false;

    /**
     *
     * @return New instance of {@link WebDriver} object.
     */
    public static WebDriver getDriver() {
        registerDrivers();

        WebDriver driver;
        switch (Properties.getBrowser()) {
            case BrowserType.CHROME:
                driver = new ChromeDriver();
                break;
            case BrowserType.FIREFOX:
                driver = new FirefoxDriver();
                break;
            case BrowserType.IE:
                driver = new InternetExplorerDriver();
                break;
            default:
                throw new UnsupportedOperationException("Method doesn't return WebDriver instance");
        }

        return driver;
    }

    /*
     * Perform registering web driver depending on current OS
     * Default is Mac OS
     * Secondary supported is MS Windows
     */
    private static void registerDrivers() {
        String driverChrome = "chromedriver";
        String driverFirefox = "geckodriver";
        String driverIE = "IEDriverServer.exe";
        String currentOS = System.getProperty("os.name");
        if (logMode) {
            logger.info(String.format("Current OS: %s", currentOS));
        }
        if (currentOS.toLowerCase().contains("windows")) {
            driverChrome+= ".exe";
            driverFirefox+= ".exe";
        }
        try {
            URL chromeUrl = BaseScript.class.getClassLoader().getResource(driverChrome);
            System.setProperty("webdriver.chrome.driver", chromeUrl.getPath());

            URL firefoxUrl = BaseScript.class.getClassLoader().getResource(driverFirefox);
            System.setProperty("webdriver.firefox.driver", firefoxUrl.getPath());

            URL IEUrl = BaseScript.class.getClassLoader().getResource(driverIE);
            System.setProperty("webdriver.ie.driver", IEUrl.getPath());
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    /*
     * Perform continuous search for the web element
     */
    public static WebElement continuousFindWebElement(Callable<WebElement> func, long intervalMilliseconds, long repeats) throws Exception {
        WebElement foundElement;
        if (repeats == 0) {
            if (logMode) {
                logger.info("Couldn't find element");
            }
            throw new NotFoundException();
        }
        try {
            foundElement = func.call();
        } catch (NoSuchElementException e) {
            if (logMode) {
                logger.info(String.format("Unable to find web element. Tries left: %d", repeats));
            }
            try {
                TimeUnit.MILLISECONDS.sleep(intervalMilliseconds);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
            foundElement = continuousFindWebElement(func, intervalMilliseconds, --repeats);
        }
        return foundElement;
    }

    public static Logger getLogger() {
        return logger;
    }
}
