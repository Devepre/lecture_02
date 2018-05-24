package myprojects.automation.assignment2;

import org.openqa.selenium.WebDriver;

/**
 * Base script functionality, can be used for all Selenium scripts.
 */
public abstract class BaseScript {

    /**
     *
     * @return New instance of {@link WebDriver} object.
     */
    public static WebDriver getDriver() {
        // TODO return  WebDriver instance
        throw new UnsupportedOperationException("Method doesn't return WebDriver instance");
    }
}
