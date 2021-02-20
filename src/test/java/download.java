import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import top.codexvn.po.JDK;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.file.Paths;

public class download {
    volatile int i=0;

    @Test
    void getStream() throws IOException {
        URL url = new URL("https://d6.injdk.cn/openjdk/openjdk/14/openjdk-14.0.2_windows-x64_bin.zip");
        HttpsURLConnection httpsURLConnection = (HttpsURLConnection)url.openConnection();
        try (DataInputStream dataInputStream = new DataInputStream(httpsURLConnection.getInputStream());
             FileOutputStream fileOutputStream = new FileOutputStream(new File("d:/123.zip"))
        ) {
            double length =httpsURLConnection.getContentLength();
            for ( int data=0; i !=length  && data!=-1; i++) {
                if(i%100000==0)
                    System.out.println(String.format("%.2f%%",i*100/length));
                fileOutputStream.write(data);
            }
        }
    }

    @Test
    void test() throws MalformedURLException, URISyntaxException {
        URL url = new URL("https://d6.injdk.cn/openjdk/openjdk/14/openjdk-14.0.2_windows-x64_bin.zip");
        System.out.println(url.toURI().getScheme());
        System.out.println();

    }
}
