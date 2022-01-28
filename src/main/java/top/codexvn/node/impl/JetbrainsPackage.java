package top.codexvn.node.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.crypto.digest.DigestAlgorithm;
import cn.hutool.crypto.digest.Digester;
import cn.hutool.extra.compress.CompressUtil;
import cn.hutool.extra.compress.extractor.Extractor;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.SneakyThrows;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.compressors.CompressorStreamFactory;
import top.codexvn.ProgressBar;
import top.codexvn.enums.PackageTypeEnum;
import top.codexvn.node.AbstractPackage;
import top.codexvn.utils.DownloadUtil;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static cn.hutool.extra.compress.CompressUtil.getIn;
import static top.codexvn.enums.PackageTypeEnum.ZIP;

@Data
@EqualsAndHashCode(callSuper = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class JetbrainsPackage extends AbstractPackage {
    private static final int BUFFER_SIZE = 8 * 1024;
    private String archiveFileName;
    private String installFolderName;
    private long archiveSize;
    private long unpackedSize;
    private String sha256;

    public void setPackageType(String packageType) {
        switch (packageType) {
            case "targz":
                this.packageType = PackageTypeEnum.TARGZ;
                break;
            case "zip":
                this.packageType = ZIP;
                break;
            default:
                this.packageType = PackageTypeEnum.UNKNOWN;
                break;
        }
    }

    @Override
    public File download(Path to, ProgressBar progressBar) {
        return download(to, archiveFileName, progressBar);
    }

    @SneakyThrows
    @Override
    public File download(Path to, String archiveFileName, ProgressBar progressBar) {
        if (!Files.isDirectory(to)) {
            Files.createDirectories(to);
        }
        File targetFile = to.resolve(archiveFileName).toFile();
        downloadToFile(targetFile, progressBar);
        return targetFile;
    }

    @SneakyThrows
    private void downloadToFile(File targetFile, ProgressBar progressBar) {
        progressBar.start();
        URL source = new URL(url);
        if (DownloadUtil.acceptRangeRequest(source)) {
            fragmentDownload(source, targetFile, progressBar);
        } else {
            normalDownload(source, targetFile, progressBar);
        }
        progressBar.finish();
    }

    @SneakyThrows
    private void fragmentDownload(URL source, File targetFile, ProgressBar progressBar) {
        class DownloadTask implements Runnable {
            private final URL source;
            private final long begin, end, totalSize;
            private final File targetFile;
            private final ProgressBar progressBar;
            private final CountDownLatch cdl;

            DownloadTask(URL source, long begin, long end, long totalSize, File targetFile, ProgressBar progressBar, CountDownLatch cdl) {
                this.source = source;
                this.begin = begin;
                this.end = end;
                this.totalSize = totalSize;
                this.targetFile = targetFile;
                this.progressBar = progressBar;
                this.cdl = cdl;
            }

            @SneakyThrows
            @Override
            public void run() {
                URLConnection connection = source.openConnection();
                connection.setRequestProperty("Range", String.format("bytes=%d-%d", begin, end));
                connection.connect();
                try (InputStream input = new BufferedInputStream(connection.getInputStream()); RandomAccessFile output = new RandomAccessFile(targetFile, "rw")) {
                    byte[] buffer = new byte[BUFFER_SIZE];
                    int read;
                    output.seek(begin);
                    while ((read = input.read(buffer)) != -1) {
                        output.write(buffer, 0, read);
                        progressBar.progress(totalSize, read);
                    }
                }
                cdl.countDown();
            }
        }
        CountDownLatch cdl = new CountDownLatch(8);
        ExecutorService executorService = Executors.newFixedThreadPool(8);
        long contentLength = DownloadUtil.getContentLength(source);
        long fragmentSize = (long) Math.ceil(contentLength / 8.0);
        for (long i = 0; i < contentLength; i += fragmentSize) {
            executorService.execute(new DownloadTask(source, i, Math.min(i + fragmentSize - 1, contentLength - 1), contentLength, targetFile, progressBar, cdl));
        }
        executorService.shutdown();
        cdl.await();
    }

    @SneakyThrows
    private void normalDownload(URL source, File targetFile, ProgressBar progressBar) {
        progressBar.start();
        URLConnection connection = source.openConnection();
        connection.connect();
        try (InputStream input = new BufferedInputStream(connection.getInputStream()); OutputStream output = new FileOutputStream(targetFile)) {
            doCopy(input, output, connection.getContentLengthLong(), progressBar);
        }
        progressBar.finish();
    }


    private static void doCopy(InputStream input, OutputStream output, long totalSize, ProgressBar progressBar) throws IOException {
        byte[] buffer = new byte[BUFFER_SIZE];
        int read;
        while ((read = input.read(buffer)) != -1) {
            output.write(buffer, 0, read);
            progressBar.progress(totalSize, read);
        }
    }

    @SneakyThrows
    @Override
    public void unpack(Path to, ProgressBar progressBar) {
        ensureDirExist(to);
        Path tmpFile = generateTmpDir();
        download(tmpFile, archiveFileName, progressBar);
        checkSHA256(getSha256(), tmpFile.resolve(archiveFileName).toFile());
        try (InputStream input = Files.newInputStream(tmpFile.resolve(archiveFileName))) {
            Extractor extractor = null;
            switch (getPackageType()) {
                case ZIP:
                    extractor = CompressUtil.createExtractor(
                        CharsetUtil.CHARSET_UTF_8,
                        ArchiveStreamFactory.ZIP
                        , input
                    );
                    break;
                case TARGZ:
                    extractor = CompressUtil.createExtractor(
                        CharsetUtil.CHARSET_UTF_8,
                        ArchiveStreamFactory.TAR
                        , getIn(CompressorStreamFactory.GZIP, input)
                    );
                    break;
                case UNKNOWN:
                    throw new IllegalStateException("Unknown package type");
            }
            ensureDirExistAndClearDir(to);
            Objects.requireNonNull(extractor).extract(to.toFile());
        } finally {
            FileUtil.del(tmpFile);
        }
    }

    private static void checkSHA256(String expected, File file) {
        Digester digester = new Digester(DigestAlgorithm.SHA256);
        String sha256 = digester.digestHex(file);
        if (!expected.equals(sha256)) {
            throw new IllegalStateException("SHA256 mismatch,\nexpected: " + expected + ",\nactual: " + sha256);
        }
    }

    private static Path generateTmpDir() {
        String tmpdir = System.getProperty("java.io.tmpdir");
        return Path.of(Objects.requireNonNull(tmpdir), UUID.randomUUID().toString().replace("-", ""));
    }

    private static void ensureDirExist(Path dir) {
        try {
            if (!Files.isDirectory(dir)) Files.createDirectories(dir);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private static void ensureDirExistAndClearDir(Path dir) {
        try {
            if (Files.isDirectory(dir)) {
                FileUtil.del(dir);
            }
            Files.createDirectories(dir);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }

    }
}
