package utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

public class PropertyReader {
    private static final Properties properties = new Properties();

    static {
        try (FileInputStream fis = new FileInputStream("src/test/resources/testdata.properties")) {
            properties.load(new InputStreamReader(fis, StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new RuntimeException("Не удалось найти файл testdata.properties в src/test/resources/");
        }
    }

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }
}
