package top.codexvn.po;

import java.io.InputStream;
import java.net.URL;

public abstract class Base {
    protected String name;
    protected String version;
    protected String fileName;
    protected UrlInfo urlInfo;

    public Base(URL url) {
        this.urlInfo = new UrlInfo(url);
    }

    public String getVersion() {
        return version;
    }

    public String getFileName() {
        return fileName;
    }

    public String getType() {
        return version;
    }


    public String getName() {
        return name;
    }

    public UrlInfo getUrlInfo() {
        return urlInfo;
    }

    public int getFileLength() {
        return urlInfo.getLength();
    }

    public InputStream getInputStream() {
        return urlInfo.getInputStream();
    }
}
