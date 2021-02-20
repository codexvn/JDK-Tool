package top.codexvn.po;

import top.codexvn.utils.GetUrlInfo;

import java.io.InputStream;
import java.net.URL;

public class UrlInfo {
    private URL url;
    private InputStream inputStream;
    private int length=-2;

    public URL getUrl() {
        return url;
    }

    public int getLength() {
        if(this.length==-2)
            this.length = GetUrlInfo.getFileLength(url);
        return length;
    }

    public InputStream getInputStream() {
        if (this.inputStream == null)
            this.inputStream = GetUrlInfo.getFileStream(url);
        return inputStream;
    }

    public UrlInfo(URL url) {
        this.url = url;
    }
}
