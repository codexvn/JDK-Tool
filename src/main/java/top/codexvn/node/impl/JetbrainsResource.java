package top.codexvn.node.impl;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import top.codexvn.node.AbstractJdkInfo;
import top.codexvn.node.AbstractPackage;
import top.codexvn.node.Resource;
import top.codexvn.platform.JetbrainsPlatform;
import top.codexvn.platform.Platform;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class JetbrainsResource implements Resource {
    private final ObjectMapper objectMapper = new ObjectMapper();

    {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    private JsonNode getDataFromRemote() {
        JsonNode readTree = null;
        try {
            readTree = objectMapper.readTree(new URL("https://download.jetbrains.com/jdk/feed/v1/jdks.json"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Objects.requireNonNull(readTree).get("jdks");
    }
    @SneakyThrows
    @Override
    public Map<Platform, List<AbstractPackage>> getJdkListFromRemote() {
        Map<Platform, List<AbstractPackage>> result = new HashMap<>();
        JsonNode data = getDataFromRemote();
        for (var i : data) {
            JetbrainsJdkInfo jdkInfo = objectMapper.readValue(i.toString(), JetbrainsJdkInfo.class);
            for (var j : i.get("packages")) {
                jdkInfo.setOs(j.get("os").textValue());
                jdkInfo.setArch(j.get("arch").textValue());
                JetbrainsPackage jetbrainsPackage = objectMapper.readValue(j.toString(), JetbrainsPackage.class);
                jetbrainsPackage.setJdkInfo(jdkInfo);
                Platform platform = new JetbrainsPlatform(j.get("os").textValue(), j.get("arch").textValue());
                result.merge(platform, new LinkedList<>(List.of(jetbrainsPackage)), (o, n) -> {
                    o.addAll(n);
                    return o;
                });
            }
        }
        return result;
    }
    @SneakyThrows
    @Override
    public Map<Platform, List<AbstractJdkInfo>> getInfoListFromRemote() {
        Map<Platform, List<AbstractJdkInfo>> result = new HashMap<>();
        JsonNode data = getDataFromRemote();
        for (var i : data) {
            JetbrainsJdkInfo jdkInfo = objectMapper.readValue(i.toString(), JetbrainsJdkInfo.class);
            for (var j : i.get("packages")) {
                jdkInfo.setOs(j.get("os").textValue());
                jdkInfo.setArch(j.get("arch").textValue());
                Platform platform = new JetbrainsPlatform(j.get("os").textValue(), j.get("arch").textValue());
                result.merge(platform, new LinkedList<>(List.of(jdkInfo)), (o, n) -> {
                    o.addAll(n);
                    return o;
                });
            }
        }
        return result;
    }
}
