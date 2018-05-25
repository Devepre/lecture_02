package myprojects.automation.assignment2.tests;

import myprojects.automation.assignment2.BaseScript;
import myprojects.automation.assignment2.utils.Properties;
import org.openqa.selenium.*;

/*
Скрипт А. Логин в Админ панель
1. Открыть страницу Админ панели
2. Ввести логин, пароль и нажать кнопку Логин.
3. После входа в систему нажать на пиктограмме пользователя в верхнем
правом углу и выбрать опцию «Выход.»
 */

public class LoginTest extends BaseScript {
    // Constants for searching web elements
    private static final String LOGIN_FIELD = "email";
    private static final String PASSWORD_FIELD = "passwd";
    private static final String SUBMIT_BUTTON = "submitLogin";
    private static final String USER_MENU = "employee_avatar_small";
    private static final String USER_MENU_LOGOUT = "header_logout";
    // Constants for web driver continuous search method
    private static final int FIND_INTERVAL_MILLISECONDS = 150;
    private static final int FIND_REPEATS = 7;
    private static WebDriver driver;

    public static void main(String[] args) {
        openPage();
        loginIn();
        logout();
    }

    /*
     * Navigate to the Admin login page
     */
    private static void openPage() {
        driver = getDriver();
        String openUrl = Properties.getBaseAdminUrl();
        driver.get(openUrl);
    }

    /*
     * Perform login to the Admin page
     */
    private static void loginIn() {
        // Filling the login field

        WebElement loginField = null;
        try {
            loginField = continuousFindWebElement(
                    () -> driver.findElement(By.id(LOGIN_FIELD)), FIND_INTERVAL_MILLISECONDS, FIND_REPEATS
            );
        } catch (Exception e) {
            e.printStackTrace();
        }

        loginField.sendKeys(Properties.getLogin());

        // Filling the password field
        WebElement passwordElement = driver.findElement(By.id(PASSWORD_FIELD));
        passwordElement.sendKeys(Properties.getPassword());

        // Submitting the filled form
        WebElement submitButton = driver.findElement(By.name(SUBMIT_BUTTON));
        submitButton.click();
    }

    /*
     * Perform logout from the Admin page
     */
    private static void logout() {
        try {
            WebElement menuElement = continuousFindWebElement(
                    () -> driver.findElement(By.className(USER_MENU)), FIND_INTERVAL_MILLISECONDS, FIND_REPEATS
            );
            menuElement.click();

            WebElement logoutElement = driver.findElement(By.id(USER_MENU_LOGOUT));
            logoutElement.click();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
