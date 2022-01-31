import org.junit.jupiter.api.Test;
import top.codexvn.utils.FactoryUtil;

import java.io.IOException;
import java.util.Properties;

public class PropertiesTest {
    @Test
    void name() throws IOException {
        Properties properties = FactoryUtil.getSettings();
        System.err.println(properties.hashCode());
    }
}
