package tests;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.logevents.SelenideLogger;
import helpers.Attach;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.util.Map;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;

public class BaseTest {

    @BeforeAll
    static void setup() {
        Configuration.baseUrl = "https://www.kufar.by";
        Configuration.browserSize = System.getProperty("browserSize", "1920x1080");
        Configuration.browser = System.getProperty("browser", "chrome");
        Configuration.browserVersion = System.getProperty("browser_version", "128.0");
        Configuration.timeout = 5000;

        Configuration.remote = System.getProperty("remote");

        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("selenoid:options", Map.<String, Object>of(
                "enableVNC", true,
                "enableVideo", true,
                "enableFileUploads", true
        ));
        capabilities.setCapability("goog:chromeOptions", Map.<String, Object>of(
                "args", java.util.List.of("--disable-notifications", "--disable-popup-blocking")
        ));
        Configuration.browserCapabilities = capabilities;
    }

    @BeforeEach
    void setupTest() {
        SelenideLogger.addListener("allure", new AllureSelenide()
                .screenshots(true)
                .savePageSource(false)
        );

        open("/");

        if ($(byText("Принять")).is(Condition.visible)) {
            $(byText("Принять")).click();
        }

        if ($("[data-testid='selection-close']").is(Condition.visible)) {
            $("[data-testid='selection-close']").click();
        }

        executeJavaScript(
                "document.querySelectorAll('[id^=\"google_ads\"], .adsbygoogle, [class*=\"banner\"], [class*=\"promo\"]').forEach(el => el.remove());"
        );

        sleep(500);
    }

    @AfterEach
    void tearDown() {
        Attach.screenshotAs("Last screenshot");
        Attach.pageSource();
        Attach.browserConsoleLogs();
        Attach.addVideo();

        Selenide.closeWebDriver();
    }
}
