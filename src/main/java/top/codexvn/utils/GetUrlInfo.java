package top.codexvn.utils;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class GetUrlInfo {
    public static int getFileLength(URL url) {
        try {
            URI uri = url.toURI();
            if (uri.getScheme().equals("https")) {
                HttpsURLConnection httpsURLConnection = (HttpsURLConnection) url.openConnection();
                return httpsURLConnection.getContentLength();
            } else if (uri.getScheme().equals("http")) {
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                return httpURLConnection.getContentLength();
            } else
                return -1;
        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
            return -1;
        }

    }
    public static InputStream getFileStream(URL url) {
        try {
            URI uri = url.toURI();
            if (uri.getScheme().equals("https")) {
                HttpsURLConnection httpsURLConnection = (HttpsURLConnection) url.openConnection();
                return httpsURLConnection.getInputStream();
            } else if (uri.getScheme().equals("http")) {
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                return httpURLConnection.getInputStream();
            } else
                return InputStream.nullInputStream();
        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
            return InputStream.nullInputStream();
        }
    }
}