package top.codexvn.utils;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import me.tongfei.progressbar.ProgressBarBuilder;
import me.tongfei.progressbar.ProgressBarStyle;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

@Slf4j
public class DownloadUtil {

    @SneakyThrows
    public static boolean acceptRangeRequest(URL source) {
        log.info("Checking if server supports range request{}", source.toString());
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

    public static ProgressBarBuilder getDownloaderProgressBarBuilder() {
        return new ProgressBarBuilder()
            .setTaskName("下载")
            .setStyle(ProgressBarStyle.ASCII)
            .setUpdateIntervalMillis(100)
            .setUnit(" Mb", 1024 * 1024)
            .showSpeed();
    }
}
