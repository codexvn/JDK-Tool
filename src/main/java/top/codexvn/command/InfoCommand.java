package top.codexvn.command;

import lombok.extern.slf4j.Slf4j;
import picocli.CommandLine.Command;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

@Command(name = "info", description = "显示系统信息")
@Slf4j
public class InfoCommand implements Runnable {
    @Override
    public void run() {
        log.info("System info");
        log.info("OS: {}", System.getProperty("os.name"));
        log.info("OS: {}", System.getProperty("os.arch"));
        log.info("Java version: {}", System.getProperty("java.version"));
        log.info("Java vendor: {}", System.getProperty("java.vendor"));
        log.info("Java home: {}", System.getProperty("java.home"));
        log.info("Java class path: {}", System.getProperty("java.class.path"));
        log.info("Java library path: {}", System.getProperty("java.library.path"));
        log.info("Java tmp dir: {}", System.getProperty("java.io.tmpdir"));
        log.info("Java max memory: {}", Runtime.getRuntime().maxMemory());
        log.info("Java free memory: {}", Runtime.getRuntime().freeMemory());
        log.info("Java total memory: {}", Runtime.getRuntime().totalMemory());
        log.info("Java available processors: {}", Runtime.getRuntime().availableProcessors());
        log.info("User home: {}", System.getProperty("user.home"));
        String s = null;
        try (InputStream inputStream = new FileInputStream(s = Objects.requireNonNull(this.getClass().getClassLoader().getResource("123.txt")).getPath())) {
            log.info(new String(inputStream.readAllBytes()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
