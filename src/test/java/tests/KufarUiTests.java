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

public class KufarUiTests extends BaseTest {

    MainPage mainPage = new MainPage();
    SearchResultPage searchResultPage = new SearchResultPage();


    @Test
    @DisplayName("Поиск товара и проверка текста")
    void searchIphoneTest() {
        String query = utils.PropertyReader.getProperty("search.query");
        mainPage.search(query);
        searchResultPage.getHeading()
                .shouldHave(Condition.text("Объявления по запросу «" + query + "»"), Duration.ofSeconds(10));
    }

    @Test
    @DisplayName("Переход по вкладке Авто и проверка URL")
    void tabNavigationTest() {
        mainPage.clickHeaderTab("Авто");
        switchTo().window(1);
        webdriver().shouldHave(WebDriverConditions.urlStartingWith("https://auto.kufar.by/"));
    }

    @Test
    @DisplayName("Поиск некорректных данных")
    void invalidSearchDataTest() {
        String invalidQuery = PropertyReader.getProperty("search.invalid");
        mainPage.searchWithoutSuggestions(invalidQuery);
        searchResultPage.getListings().shouldHave(Condition.text("Мы это не нашли"), Duration.ofSeconds(10));
    }

    @Test
    @DisplayName("Переключение языка на белорусский в футере")
    void languageSwitchTest() {
        mainPage.switchLanguage("BY");
        Assertions.assertEquals("Дапамога", mainPage.getHelpButtonText());
    }

    @Test
    @DisplayName("Выбор региона")
    void regionSelectionTest() {
        String region = PropertyReader.getProperty("region.name");

        mainPage.openRegionSwitcher();
        mainPage.selectRegionInPopup(region);
        mainPage.checkSelectedRegion(region);
    }
}
