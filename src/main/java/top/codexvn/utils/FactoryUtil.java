package top.codexvn.utils;

import top.codexvn.exceptions.UnknownSettingException;
import top.codexvn.node.Depository;
import top.codexvn.node.LocalStore;
import top.codexvn.node.impl.JetbrainsDepository;
import top.codexvn.node.impl.LocalStoreByJson;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Properties;

public class FactoryUtil {
    private static final String CONFIG_FILE = "settings.properties";

    static {
        init();
    }

    private static void init() {
        if (!Files.exists(Path.of(CONFIG_FILE))) {
            Properties properties = new Properties();
            properties.put("source", "jetbrains");
            properties.put("db", "json");
            try (Writer writer = new FileWriter(CONFIG_FILE, StandardCharsets.UTF_8)) {
                properties.store(writer, "Settings");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static Properties getSettings() {
        Properties properties = new Properties();
        try (Reader reader = new FileReader("settings.properties", StandardCharsets.UTF_8)) {
            properties.load(reader);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return Objects.requireNonNull(properties);
    }

    public static Depository getDepository() {
        String source = (String) getSettings().get("source");
        switch (source) {
            case "jetbrains":
                return new JetbrainsDepository();
            default:
                throw new UnknownSettingException("source", CONFIG_FILE);
        }
    }

    public static LocalStore getLocalStore() {
        String db = (String) getSettings().get("db");
        switch (db) {
            case "json":
                return new LocalStoreByJson();
            default:
                throw new UnknownSettingException("db", CONFIG_FILE);
        }
    }
}
