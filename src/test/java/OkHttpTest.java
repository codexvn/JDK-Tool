import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import top.codexvn.node.AbstractJdkInfo;
import top.codexvn.node.Resource;
import top.codexvn.node.impl.JetbrainsResource;
import top.codexvn.platform.JetbrainsPlatform;
import top.codexvn.platform.Platform;

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
        Resource resource = new JetbrainsResource();
        Map<Platform, List<AbstractJdkInfo>> infoListFromRemote = resource.getInfoListFromRemote();
        Platform currentPlatform = JetbrainsPlatform.getCurrentPlatform();
        List<AbstractJdkInfo> jdkInfos = infoListFromRemote.get(currentPlatform);

    }
}
