package tests;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.logevents.SelenideLogger;
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
        Configuration.browser = System.getProperty("browser", "chrome");
        Configuration.browserVersion = System.getProperty("browserVersion", "122.0");
        Configuration.browserSize = System.getProperty("browserSize", "1920x1080");
        Configuration.baseUrl = "https://www.kufar.by";
        Configuration.timeout = 5000;

        String remoteUrl = System.getProperty("remoteUrl"); // Передаем из Jenkins, напр. http://localhost:4444/wd/hub
        if (remoteUrl != null) {
            Configuration.remote = remoteUrl;

            // Настройки для того, чтобы в Selenoid UI можно было смотреть экран (VNC) и записывать видео
            DesiredCapabilities capabilities = new DesiredCapabilities();
            capabilities.setCapability("selenoid:options", Map.<String, Object>of(
                    "enableVNC", true,
                    "enableVideo", true
            ));
            Configuration.browserCapabilities = capabilities;
        }
        SelenideLogger.addListener("allure", new AllureSelenide()
                .screenshots(true)
                .savePageSource(false)
        );

        Configuration.browserCapabilities.setCapability("goog:chromeOptions",
                java.util.Map.of("args", java.util.List.of("--disable-notifications", "--disable-popup-blocking")));
    }

    @BeforeEach
    void setupTest() {
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

        sleep(500); // Небольшая пауза для завершения анимаций
    }

    @AfterEach
    void tearDown() {
        Selenide.closeWebDriver();
    }
}
