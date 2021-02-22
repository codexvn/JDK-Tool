import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import top.codexvn.po.JDK;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.LongAdder;

public class download {

     int longAdder;

    @Test
    synchronized public void add(int i) {
        this.longAdder += i;
    }

    @Test
    synchronized public String print(int fileLength) {
        return String.format("%.2f%%", ((double) longAdder * 100 )/ fileLength);
    }


    void getStream() throws IOException {
        int i = 0;
        URL url = new URL("https://d6.injdk.cn/openjdk/openjdk/14/openjdk-14.0.2_windows-x64_bin.zip");
        HttpsURLConnection httpsURLConnection = (HttpsURLConnection) url.openConnection();
        try (DataInputStream dataInputStream = new DataInputStream(httpsURLConnection.getInputStream());
             FileOutputStream fileOutputStream = new FileOutputStream(new File("d:/123.zip"))
        ) {
            double length = httpsURLConnection.getContentLength();
            for (int data = 0; i != length && data != -1; i++) {
                if (i % 100000 == 0)
                    System.out.println(String.format("%.2f%%", i * 100 / length));
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

    @Test
    void MultiThreadDownload() throws IOException, InterruptedException {
        URL url = new URL("https://d6.injdk.cn/openjdk/openjdk/16/openjdk-16-ea+12_windows-x64_bin.zip");
        HttpsURLConnection httpsURLConnection = (HttpsURLConnection) url.openConnection();
        InputStream inputStream = httpsURLConnection.getInputStream();
        File file = new File("d:/123.zip");
        RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
        int fileLength = httpsURLConnection.getContentLength();
        randomAccessFile.setLength(fileLength);
        randomAccessFile.close();
        ExecutorService executorService = Executors.newCachedThreadPool();
        int slip = fileLength/2;
        int j;
        for (j = 0; j + slip < fileLength; j += slip)
            executorService.submit(new DownloadRunnable(j, j + slip - 1, file, url));
        executorService.submit(new DownloadRunnable(j, fileLength, file, url));
        executorService.shutdown();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (true) {
                    int a=longAdder;
                    Thread.sleep(1000);
                    int b=longAdder;
                    System.out.println(String.format("%.2fMb", (double) (b - a) / 1024 / 1024));
                } }catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        int i = 0;
        for ( i = 0; executorService.isTerminated() == false; i++) {
            System.out.println(i + "S :" + print(fileLength));
            Thread.sleep(1000);
        }
        System.out.println(i + "S :" + print(fileLength));
    }
    class DownloadRunnable implements Runnable {
        private int start, end;
        private File file;
        private URL url;

        public DownloadRunnable(int start, int end, File file, URL url) {
            this.start = start;
            this.end = end;
            this.file = file;
            this.url = url;
        }
        @Override
        public void run() {
            try (FileChannel fileChannel = new RandomAccessFile(file, "rw").getChannel()
            ) {
                ByteBuffer byteBuffer = ByteBuffer.allocate(end-start+1);
                fileChannel.position(start);
                HttpsURLConnection httpsURLConnection = (HttpsURLConnection) url.openConnection();
                httpsURLConnection.setRequestProperty("Range", "bytes=" + start + "-" + end);
                InputStream inputStream = httpsURLConnection.getInputStream();
                int hadRead = 0;
                byte[] buffers = new byte[16 * 1024];
                while (true) {
                    hadRead = inputStream.read(buffers);
                    if (hadRead == -1) break;
                    byteBuffer.put(buffers, 0, hadRead);
                    if(byteBuffer.remaining()<buffers.length){
                        byteBuffer.flip();
                        fileChannel.write(byteBuffer);
                        byteBuffer.clear();
                    }
                    add(hadRead);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}


