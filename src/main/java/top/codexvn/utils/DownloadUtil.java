package top.codexvn.utils;

import lombok.SneakyThrows;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class DownloadUtil {

    @SneakyThrows
    public static boolean acceptRangeRequest(URL source) {
        HttpURLConnection connection = (HttpURLConnection) source.openConnection();
        connection.setRequestMethod("HEAD");
        connection.connect();
        return connection.getHeaderField("Accept-Ranges") != null;
    }

    @SneakyThrows
    public static long getContentLength(URL source) {
        URLConnection connection = source.openConnection();
        connection.connect();
        return connection.getContentLength();
    }
}
