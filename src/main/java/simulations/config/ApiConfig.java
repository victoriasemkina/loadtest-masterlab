package simulations.config;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ApiConfig {

    private static final Properties properties = new Properties();
    private static boolean loaded = false;

    private static synchronized void load() {
        if (loaded) {
            return;
        }

        try (InputStream input = ApiConfig.class.getClassLoader()
                .getResourceAsStream("config/gatling-config.properties")) {

            if (input == null) {
                log.error("Не удалось найти файл config/gatling-config.properties");
                throw new RuntimeException("Не удалось найти файл config/gatling-config.properties");
            }

            properties.load(input);
            loaded = true;
            log.info("Конфигурация API успешно загружена");

        } catch (IOException e) {
            log.error("Ошибка загрузки конфигурации API", e);
            throw new RuntimeException("Ошибка загрузки конфигурации API", e);
        }
    }

    public static String getPropertyByKey(String key) {
        load();
        return properties.getProperty(key);
    }

    public static String getPropertyByKey(String key, String defaultValue) {
        load();
        return properties.getProperty(key, defaultValue);
    }

    public static int getIntPropertyByKey(String key) {
        load();
        try {
            return Integer.parseInt(properties.getProperty(key));
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public static int getIntPropertyByKey(String key, int defaultValue) {
        load();
        try {
            return Integer.parseInt(properties.getProperty(key));
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public static long getLongPropertyByKey(String key) {
        load();
        try {
            return Long.parseLong(properties.getProperty(key));
        } catch (NumberFormatException e) {
            return 0L;
        }
    }

    public static boolean getBooleanPropertyByKey(String key) {
        load();
        return Boolean.parseBoolean(properties.getProperty(key));
    }

    public static boolean getBooleanPropertyByKey(String key, boolean defaultValue) {
        load();
        String value = properties.getProperty(key);
        return value != null ? Boolean.parseBoolean(value) : defaultValue;
    }
}