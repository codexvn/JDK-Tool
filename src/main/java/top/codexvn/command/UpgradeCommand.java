package top.codexvn.command;

import lombok.extern.slf4j.Slf4j;
import picocli.CommandLine.Command;

import java.util.concurrent.Callable;

@Command(name = "upgrade", description = "更新指定JDK")
@Slf4j
public class UpgradeCommand implements Callable<Integer> {
    @Override
    public Integer call()  {

        return 0;
    }
}
