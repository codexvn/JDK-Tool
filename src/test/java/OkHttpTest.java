import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import top.codexvn.ProgressBar;
import top.codexvn.node.AbstractPackage;
import top.codexvn.node.Resource;
import top.codexvn.node.impl.JetbrainsPackage;
import top.codexvn.node.impl.JetbrainsResource;
import top.codexvn.platform.Platform;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

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
        ProgressBar progressBar = new ProgressBar() {
            private final AtomicLong atomicLong = new AtomicLong();
            @Override
            public void start() {
                System.out.println("1下载开始");
                System.out.println(value.getSha256());
            }

            @Override
            public void progress(long totalSize, long progressSize) {
//                atomicLong.addAndGet(progressSize);
                    System.out.printf("共%d，已经下载%f%n",totalSize,atomicLong.addAndGet(progressSize)*100.0/totalSize);
            }

            @Override
            public void finish() {
                System.out.println("1下载结束");
                Assertions.assertEquals(atomicLong.get(), value.getArchiveSize());

            }
        };
        value.unpack(Path.of("D:/","12") ,progressBar);

    }
}
