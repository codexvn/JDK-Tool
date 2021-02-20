package top.codexvn.po;

import com.fasterxml.jackson.annotation.JsonIgnore;
import top.codexvn.utils.GetUrlInfo;

import java.io.InputStream;
import java.net.URL;

public abstract class Base {
    protected String name;
    protected String version;
    protected String fileName;
    protected URL url;

    public String getName() {
        return name;
    }

    public URL getUrl() {
        return url;
    }

    public String getVersion() {
        return version;
    }

    public String getFileName() {
        return fileName;
    }


    @JsonIgnore
    public int getFileLength() {
         return GetUrlInfo.getFileLength(url);
    }

    @JsonIgnore
    public InputStream getInputStream() {
        return GetUrlInfo.getFileStream(url);
    }
}
