import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import picocli.CommandLine;
import top.codexvn.command.UpgradeCommand;

public class OkHttpTest {

    static {
        System.setProperty("socksProxyHost", "127.0.0.1");
        System.setProperty("socksProxyPort", "10808");
    }

    @Test
    @SneakyThrows
    void okHttp() {
        new CommandLine(new UpgradeCommand()).execute();


    }

}
