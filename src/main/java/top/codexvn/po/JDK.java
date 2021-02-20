package top.codexvn.po;

import java.net.URL;

public class JDK extends Base {
    public JDK() {
    }

    public JDK(String name, String version, String fileName, URL url) {
        this.url = url;
        this.name = name;
        this.version = version;
        this.fileName = fileName;
    }
}
