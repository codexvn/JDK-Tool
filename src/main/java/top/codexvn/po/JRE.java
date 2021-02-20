package top.codexvn.po;

import java.net.URL;

public class JRE extends Base {
    public JRE() {
    }

    public JRE(String name, String version, String fileName, URL url) {
        this.url = url;
        this.name = name;
        this.version = version;
        this.fileName = fileName;
    }
}
