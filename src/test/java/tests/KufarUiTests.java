package tests;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.WebDriverConditions;
import org.junit.jupiter.api.*;
import pages.MainPage;
import pages.SearchResultPage;
import utils.PropertyReader;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.switchTo;
import static com.codeborne.selenide.Selenide.webdriver;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class KufarUiTests extends BaseTest {

    MainPage mainPage = new MainPage();
    SearchResultPage searchResultPage = new SearchResultPage();


    @Test
    @Order(1)
    @DisplayName("1. Поиск товара и проверка текста")
    void searchIphoneTest() {
        String query = utils.PropertyReader.getProperty("search.query");
        mainPage.search(query);

        searchResultPage.getHeading()
                .shouldHave(Condition.text("Объявления по запросу «" + query + "»"), Duration.ofSeconds(20));
    }

    @Test
    @Order(2)
    @DisplayName("2. Переход по вкладке Авто и проверка URL")
    void tabNavigationTest() {
        mainPage.clickHeaderTab("Авто");
        switchTo().window(1);
        webdriver().shouldHave(WebDriverConditions.urlStartingWith("https://auto.kufar.by/"));
    }

    @Test
    @Order(3)
    @DisplayName("3. Поиск некорректных данных")
    void invalidSearchDataTest() {
        String invalidQuery = PropertyReader.getProperty("search.invalid");

        mainPage.searchWithoutSuggestions(invalidQuery);
        searchResultPage.getListings().shouldHave(Condition.text("Мы это не нашли"));
    }

    @Test
    @Order(4)
    @DisplayName("4. Переключение языка на белорусский в футере")
    void languageSwitchTest() {
        mainPage.switchLanguage("BY");
        Assertions.assertEquals("Дапамога", mainPage.getHelpButtonText());
    }

    @Test
    @Order(5)
    @DisplayName("5. Выбор региона")
    void regionSelectionTest() {
        String region = PropertyReader.getProperty("region.name");

        mainPage.openRegionSwitcher();
        mainPage.selectRegionInPopup(region);
        mainPage.checkSelectedRegion(region);
    }
}
