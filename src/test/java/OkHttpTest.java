import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import me.tongfei.progressbar.ProgressBarBuilder;
import me.tongfei.progressbar.ProgressBarStyle;
import org.junit.jupiter.api.Test;
import top.codexvn.node.AbstractPackage;
import top.codexvn.node.Resource;
import top.codexvn.node.impl.JetbrainsPackage;
import top.codexvn.node.impl.JetbrainsResource;
import top.codexvn.platform.Platform;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;

@Slf4j
public class OkHttpTest {

    static {
        System.setProperty("socksProxyHost", "127.0.0.1");
        System.setProperty("socksProxyPort", "10808");
    }

    @Test
    @SneakyThrows
    void okHttp() {
        TimeInterval timer = DateUtil.timer();
        Resource resource = new JetbrainsResource();
        var jdks = resource.getJdkListFromRemote();
        Map.Entry<Platform, List<AbstractPackage>> next = jdks.entrySet().iterator().next();
        var value = (JetbrainsPackage) next.getValue().get(0);
        ProgressBarBuilder pbb = new ProgressBarBuilder()
            .setTaskName("下载")
            .setStyle(ProgressBarStyle.ASCII);
        value.unpack(Path.of("D:/","12") ,pbb);

    }
}
