package pages;

import com.codeborne.selenide.SelenideElement;
import static com.codeborne.selenide.Selenide.$;

public class SearchResultPage {

    private final SelenideElement mainHeading = $("h1");
    private final SelenideElement listingsContainer = $("[data-name='listings']");

    public SelenideElement getHeading() {
        return mainHeading;
    }

    public SelenideElement getListings() {
        return listingsContainer;
    }
}
