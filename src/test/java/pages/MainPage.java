package pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import java.time.Duration;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;

public class MainPage {

    private final SelenideElement searchInput = $("[data-testid='searchbar-input']");
    private final SelenideElement regionModal = $("[data-testid='inview-testid']");
    private final SelenideElement suggestions = $("[class*='styles_suggestions']");
    private final SelenideElement headerTabs = $(".styles_top_header__PXNqm");

    private final SelenideElement regionSwitcher = $("[data-testid='kufar-region-switcher']");
    private final SelenideElement regionPopup = $("[data-testid='kufar-region-switcher-popup']");

    private final SelenideElement footer = $(".styles_footer__n7g2M");
    private final SelenideElement helpButton = $("#help");

    public void search(String text) {
        searchInput.shouldBe(Condition.visible).setValue(text);
        suggestions.shouldBe(Condition.visible, Duration.ofSeconds(8));
        searchInput.pressEnter();
        if (regionModal.is(Condition.visible)) {
            regionModal.click();
            regionModal.shouldNotBe(Condition.visible);
        }
    }


    public void searchWithoutSuggestions(String text) {
        searchInput.setValue(text);
        sleep(3000);
        searchInput.pressEnter();
    }

    public void clickHeaderTab(String tabName) {
        headerTabs.$(byText(tabName)).click();
    }

    public void switchLanguage(String lang) {
        footer.scrollTo();
        footer.$$("[role='button']").find(Condition.text(lang)).click();
    }

    public String getHelpButtonText() {
        return helpButton.getText();
    }

    public void openRegionSwitcher() {
        regionSwitcher.shouldBe(Condition.visible).click();
    }

    public void selectRegionInPopup(String regionName) {
        regionPopup.shouldBe(Condition.visible);
        regionPopup.$(byText(regionName)).click();
        regionPopup.$("button").shouldHave(Condition.text("Показать")).click();
    }

    public void checkSelectedRegion(String expectedRegion) {
        regionSwitcher.shouldHave(Condition.text(expectedRegion));
    }
}
