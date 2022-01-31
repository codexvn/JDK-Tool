package top.codexvn.command;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import picocli.CommandLine.Command;
import top.codexvn.utils.FactoryUtil;

@Command(name = "test", description = "测试")
@Slf4j
public class TestCommand implements Runnable {
    @Override
    @SneakyThrows
    public void run() {
        log.info("test");
        log.info(FactoryUtil.getSettings().toString());
    }
}
