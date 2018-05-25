package myprojects.automation.assignment2.tests;

import myprojects.automation.assignment2.BaseScript;
import org.openqa.selenium.By;
import org.openqa.selenium.NotFoundException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

import static myprojects.automation.assignment2.tests.LoginTest.loginInAdminPage;
import static myprojects.automation.assignment2.tests.LoginTest.openAdminPage;

/*
Скрипт Б. Проверка работоспособности главного меню Админ панели
1. Войти в Админ панель (по аналогии с предыдущим скриптом)
2. Кликнуть на каждом видимом пункте главного меню (Dashboard, Заказы,
Каталог, Клиенты...) для открытия соответствующего раздела и выполнить следующее:
a. Вывести в консоль заголовок открытого раздела.
b. Выполнить обновление (рефреш) страницы и проверить, что
пользователь остается в том же разделе после перезагрузки страницы.
 */

public class CheckMainMenuTest extends BaseScript {
    // Constants for searching web elements
    private static final String NAVIGATION_SIDEBAR_MENU_ID_FIELD = "nav-sidebar";
    private static final String NAVIGATION_SIDEBAR_MENU_CLASS_FIELD = "nav-bar";
    private static final String PAGE_TITLE_CLASS = "page-title";
    private static final String PAGE_TITLE_CLASS_SIMPLE = "title";
    // Constants for web driver continuous search method
    private static final int FIND_INTERVAL_MILLISECONDS = 150;
    private static final int FIND_REPEATS = 10;
    // Check it true if need logging
    private static boolean logMode = false;

    public static void main(String[] args) throws Exception {
        // Getting driver
        WebDriver driver = getDriver();

        openAdminPage(driver);
        loginInAdminPage(driver);
        clickAndCheckAllVisibleMenuItems(driver);

        driver.quit();
    }

    /*
     * Task 2: click->print->refresh->check
     */
    private static void clickAndCheckAllVisibleMenuItems(WebDriver driver) throws Exception {
        int i = 0;
        List<WebElement> visibleLinks;
        do {
            // Need to get new list for each iteration
            // since after page refresh elements are changed
            visibleLinks = getVisibleMenuLinks(driver);
            visibleLinks.get(i).click();

            // Task 2a
            WebElement headerElement = getHeaderElement(driver);
            String initialHeaderTitle = headerElement.getText();
            System.out.println(initialHeaderTitle);

            // Task 2b
            driver.navigate().refresh();
            boolean headerWasChanged = checkTitleEquality(driver, initialHeaderTitle);
            if (headerWasChanged) {
                System.out.println("Header was changed after refresh!");
            }

            i++;
        } while (i < visibleLinks.size());

    }

    /**
     *
     * @return true if header was changed and false if doesn't.
     */
    private static boolean checkTitleEquality(WebDriver driver, String initialHeaderTitle) throws Exception {
        WebElement headerElement = getHeaderElement(driver);
        boolean headerWasChanged = !initialHeaderTitle.equals(headerElement.getText());
        return  headerWasChanged;
    }

    /*
     * Perform getting the header web element
     */
    private static WebElement getHeaderElement(WebDriver driver) throws Exception {
        WebElement headerElement;
        try {
            String xPathString = String.format("//h2[@ class='%s']", PAGE_TITLE_CLASS);
            headerElement = continuousFindWebElement(
                    () -> driver.findElement(By.xpath(xPathString)), FIND_INTERVAL_MILLISECONDS, FIND_REPEATS);
        } catch (NotFoundException e) {
            String xPathString = String.format("//h2[@ class='%s']", PAGE_TITLE_CLASS_SIMPLE);
            headerElement = continuousFindWebElement(
                    () -> driver.findElement(By.xpath(xPathString)), FIND_INTERVAL_MILLISECONDS, FIND_REPEATS);
        }

        return headerElement;
    }

    /*
     * @return List of the visible Main Menu links.
     */
    protected static List<WebElement> getVisibleMenuLinks(WebDriver driver) throws Exception {
        // Getting all links from the menu
        List<WebElement> links;
        WebElement navigationSideBarElement = null;
        try {
            navigationSideBarElement = continuousFindWebElement(
                    () -> driver.findElement(By.id(NAVIGATION_SIDEBAR_MENU_ID_FIELD)), FIND_INTERVAL_MILLISECONDS, FIND_REPEATS
            );
        } catch (NotFoundException e) {
            if (logMode) {
                String warningMessage = String.format("Unable to found element by ID: %s. Trying to find by Class name: %s",
                        NAVIGATION_SIDEBAR_MENU_ID_FIELD, NAVIGATION_SIDEBAR_MENU_CLASS_FIELD);
                BaseScript.getLogger().info(warningMessage);
            }
            try {
                navigationSideBarElement = continuousFindWebElement(
                        () -> driver.findElement(By.className(NAVIGATION_SIDEBAR_MENU_CLASS_FIELD)), FIND_INTERVAL_MILLISECONDS, FIND_REPEATS
                );
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        } finally {
            links = navigationSideBarElement.findElements(By.tagName("a"));
        }

        List<WebElement> visibleLinks = getVisibleWebElements(links);

        return visibleLinks;
    }

    /*
     * @return New List of the visible items.
     */
    private static List<WebElement> getVisibleWebElements(List<WebElement> items) {
        // Getting only visible links
        List<WebElement> visibleLinks = null;
        if (items != null) {
            visibleLinks = new ArrayList<>(items.size());
            for (WebElement element : items) {
                if (element.isDisplayed()) {
                    visibleLinks.add(element);
                }
            }
        }
        return visibleLinks;
    }
}
