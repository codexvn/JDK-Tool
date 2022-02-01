package top.codexvn.command;

import lombok.SneakyThrows;
import picocli.CommandLine.Command;

@Command(name = "test", description = "测试")
public class TestCommand implements Runnable {
    @Override
    public void run() {

    }
}
