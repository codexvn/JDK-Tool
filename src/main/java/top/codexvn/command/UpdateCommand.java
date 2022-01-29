package top.codexvn.command;

import lombok.extern.slf4j.Slf4j;
import picocli.CommandLine.Command;
import top.codexvn.node.AbstractJdkInfo;
import top.codexvn.node.Resource;
import top.codexvn.node.impl.JetbrainsResource;
import top.codexvn.platform.JetbrainsPlatform;
import top.codexvn.platform.Platform;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import static top.codexvn.utils.ObjectUtil.jsonOf;

@Command(name = "update", description = "更新JDK数据")
@Slf4j
public class UpdateCommand implements Callable<Integer> {
    private Integer code = 0;

    @Override
    public Integer call() {
        Resource resource = new JetbrainsResource();
        Map<Platform, List<AbstractJdkInfo>> infoListFromRemote = resource.getInfoListFromRemote();
        Platform currentPlatform = JetbrainsPlatform.getCurrentPlatform();
        List<AbstractJdkInfo> jdkInfos = infoListFromRemote.get(currentPlatform);
        try (Writer writer = new FileWriter("jdk.json", StandardCharsets.UTF_8)) {
            String data = jsonOf(jdkInfos);
            log.info(data);
            writer.write(data);
        } catch (IOException e) {
            code = 1;
        }
        return code;
    }
}
