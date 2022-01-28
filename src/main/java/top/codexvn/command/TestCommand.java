package top.codexvn.command;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import picocli.CommandLine.Command;
import top.codexvn.node.AbstractPackage;
import top.codexvn.node.Resource;
import top.codexvn.node.impl.JetbrainsPackage;
import top.codexvn.node.impl.JetbrainsResource;
import top.codexvn.platform.Platform;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import static top.codexvn.utils.DownloadUtil.getDownloaderProgressBarBuilder;

@Command(name = "test", description = "测试")
@Slf4j
public class TestCommand implements Runnable {
    @Override
    @SneakyThrows
    public void run() {
        Resource resource = new JetbrainsResource();
        var jdks = resource.getJdkListFromRemote();
        Map.Entry<Platform, List<AbstractPackage>> next = jdks.entrySet().iterator().next();
        var value = (JetbrainsPackage) next.getValue().get(0);
        value.unpack(Path.of("D:/", "test"), getDownloaderProgressBarBuilder());
    }
}
