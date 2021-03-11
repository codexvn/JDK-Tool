import org.junit.jupiter.api.Test;
import top.codexvn.po.JDK;
import top.codexvn.po.JDKs.AdoptOpenJDK;
import top.codexvn.po.JDKs.OpenJDK;
import top.codexvn.po.Original;

import java.net.MalformedURLException;
import java.net.URL;

public class MyTest {
    @Test
    void name() throws MalformedURLException {
        JDK jdk = new AdoptOpenJDK(new Original("AdoptOpenJDK","JDK15","win32.zip", new URL("https://d6.injdk.cn/openjdk/adoptopenjdk/15/OpenJDK15U-jdk_x86-32_windows_hotspot_15_36.zip")));
        System.out.println(jdk);

    }
}
