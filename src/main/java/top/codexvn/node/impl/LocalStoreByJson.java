package top.codexvn.node.impl;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.type.TypeFactory;
import lombok.SneakyThrows;
import top.codexvn.node.AbstractJdkInfo;
import top.codexvn.node.AbstractPackage;
import top.codexvn.node.LocalJdk;
import top.codexvn.node.LocalStore;
import top.codexvn.utils.JsonUtil;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LocalStoreByJson implements LocalStore {
    private final static String DB_FILE = "db.json";
    private final static String JDK_LIST_FILED_NAME = "jdkList";
    private final static String LOCAL_JDK_FILED_NAME = "localJdk";
    private final static ObjectMapper objectMapper;

    static {
        objectMapper = JsonUtil.objectMapper.copy();
        objectMapper.registerModules(new SimpleModule().addAbstractTypeMapping(AbstractJdkInfo.class, JetbrainsJdkInfo.class));
        init();
    }

    @SneakyThrows
    private static void init() {
        if (!Files.exists(Path.of(DB_FILE)) || Files.size(Path.of(DB_FILE)) == 0) {
            try (Writer writer = new FileWriter(DB_FILE, StandardCharsets.UTF_8)) {
                objectMapper.writeValue(writer, Map.of(JDK_LIST_FILED_NAME, Collections.emptyList(), LOCAL_JDK_FILED_NAME, Collections.emptyList()));
            }
        }
    }

    @Override
    public void storeJdkList(List<AbstractPackage> jdkList) throws IOException {
        Map<String, List<?>> dbData = new HashMap<>(Map.of(LOCAL_JDK_FILED_NAME, loadLocalJdkList()));
        dbData.put(JDK_LIST_FILED_NAME, jdkList);
        storeAll(dbData);
    }

    @Override
    public List<AbstractPackage> loadJdkList() throws IOException {
        JsonNode jsonNode = loadAll().get(JDK_LIST_FILED_NAME);
        TypeFactory typeFactory = objectMapper.getTypeFactory();
        JavaType javaType = typeFactory.constructCollectionType(List.class, JetbrainsPackage.class);
        return objectMapper.readValue(jsonNode.toString(), javaType);
    }

    @Override
    public void storeLocalJdkList(List<LocalJdk> localJdkList) throws IOException {
        Map<String, List<?>> dbData = new HashMap<>(Map.of(JDK_LIST_FILED_NAME, loadJdkList()));
        dbData.put(LOCAL_JDK_FILED_NAME, localJdkList);
        storeAll(dbData);
    }

    @Override
    public List<LocalJdk> loadLocalJdkList() throws IOException {
        JsonNode jsonNode = loadAll().get(LOCAL_JDK_FILED_NAME);
        TypeFactory typeFactory = objectMapper.getTypeFactory();
        JavaType javaType = typeFactory.constructCollectionType(List.class, LocalJdk.class);
        return objectMapper.readValue(jsonNode.toString(), javaType);
    }

    private JsonNode loadAll() throws IOException {
        return objectMapper.readTree(new File(DB_FILE));
    }

    private void storeAll(Map<String, List<?>> data) throws IOException {
        objectMapper.writeValue(new File(DB_FILE), data);
    }
}
