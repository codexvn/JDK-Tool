package top.codexvn.po;

import java.net.URL;

public class JRE extends Base {
    public JRE(String name,String version,String fileName, URL url) {
        super(url);
        this.name=name;
        this.version =version;
        this.fileName=fileName;
    }
}
