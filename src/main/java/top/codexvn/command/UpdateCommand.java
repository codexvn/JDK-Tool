package top.codexvn.command;

import lombok.extern.slf4j.Slf4j;
import picocli.CommandLine.Command;

import java.util.concurrent.Callable;

@Command(name="update",description="更新JDK")
@Slf4j
public class UpdateCommand implements Callable<Integer> {
    private Integer code;

    @Override
    public Integer call()  {
        log.error(String.valueOf(code));
        return code;
    }
}
