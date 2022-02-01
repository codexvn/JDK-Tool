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
import me.tongfei.progressbar.ProgressBar;
import me.tongfei.progressbar.ProgressBarBuilder;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.compressors.CompressorStreamFactory;
import top.codexvn.enums.PackageTypeEnum;
import top.codexvn.exceptions.HashMismatchException;
import top.codexvn.exceptions.UnknownPackageTypeException;
import top.codexvn.node.AbstractPackage;
import top.codexvn.utils.DownloadUtil;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static cn.hutool.extra.compress.CompressUtil.getIn;
import static top.codexvn.enums.PackageTypeEnum.ZIP;
import static top.codexvn.utils.OtherUtil.createTempDirectory;

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
        try {
            this.packageType = Enum.valueOf(PackageTypeEnum.class, packageType);
        } catch (IllegalArgumentException e) {
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


    }

    @Override
    public File download(Path to, ProgressBarBuilder progressBarBuilder) {
        return download(to, archiveFileName, progressBarBuilder);
    }

    @Override
    public File download(Path to, String archiveFileName, ProgressBarBuilder progressBarBuilder) {
        ensureDirExist(to);
        File targetFile = to.resolve(archiveFileName).toFile();
        downloadToFile(targetFile, progressBarBuilder);
        return targetFile;
    }

    @SneakyThrows
    private void downloadToFile(File targetFile, ProgressBarBuilder progressBarBuilder) {
        URL source = new URL(url);
        if (DownloadUtil.acceptRangeRequest(source)) {
            fragmentDownload(source, targetFile, progressBarBuilder);
        } else {
            normalDownload(source, targetFile, progressBarBuilder);
        }
    }

    @SneakyThrows
    private void fragmentDownload(URL source, File targetFile, ProgressBarBuilder progressBarBuilder) {

        class DownloadTask implements Runnable {
            private final URL source;
            private final long begin, end;
            private final File targetFile;
            private final ProgressBar progressBar;
            private final CountDownLatch cdl;

            DownloadTask(URL source, long begin, long end, File targetFile, ProgressBar progressBar, CountDownLatch cdl) {
                this.source = source;
                this.begin = begin;
                this.end = end;
                this.targetFile = targetFile;
                this.progressBar = progressBar;
                this.cdl = cdl;
            }

            @SneakyThrows
            @Override
            public void run() {
                try {
                    URLConnection connection = source.openConnection();
                    connection.setRequestProperty("Range", String.format("bytes=%d-%d", begin, end));
                    connection.connect();
                    try (InputStream input = new BufferedInputStream(connection.getInputStream()); RandomAccessFile output = new RandomAccessFile(targetFile, "rw")) {
                        byte[] buffer = new byte[BUFFER_SIZE];
                        int read;
                        output.seek(begin);
                        while ((read = input.read(buffer)) != -1) {
                            output.write(buffer, 0, read);
                            progressBar.stepBy(read);
                        }
                    }
                } finally {
                    progressBar.close();
                    cdl.countDown();
                }
            }
        }
        CountDownLatch cdl = new CountDownLatch(8);
        ExecutorService executorService = Executors.newFixedThreadPool(8);
        long contentLength = DownloadUtil.getContentLength(source);
        progressBarBuilder.setInitialMax(contentLength);
        long fragmentSize = (long) Math.ceil(contentLength / 8.0);
        for (long i = 0, j = 1; i < contentLength; i += fragmentSize, j++) {
            progressBarBuilder.setTaskName("分片下载[" + j + "]")
                .setInitialMax(Math.min(i + fragmentSize, contentLength) - i);
            ProgressBar progressBar = progressBarBuilder.build();
            executorService.execute(new DownloadTask(source, i, Math.min(i + fragmentSize - 1, contentLength - 1), targetFile, progressBar, cdl));
        }
        executorService.shutdown();
        cdl.await();
    }

    @SneakyThrows
    private void normalDownload(URL source, File targetFile, ProgressBarBuilder progressBarBuilder) {
        URLConnection connection = source.openConnection();
        connection.connect();
        try (InputStream input = new BufferedInputStream(connection.getInputStream());
             OutputStream output = new FileOutputStream(targetFile);
             ProgressBar progressBar = progressBarBuilder.setInitialMax(connection.getContentLength()).build()) {
            doCopy(input, output, progressBar);
        }
    }


    private static void doCopy(InputStream input, OutputStream output, ProgressBar progressBar) throws IOException {
        byte[] buffer = new byte[BUFFER_SIZE];
        int read;
        while ((read = input.read(buffer)) != -1) {
            output.write(buffer, 0, read);
            progressBar.stepBy(read);
        }
    }

    /**
     * 下载JDK并解压到指定目录
     *
     * @param to                 解压到的目录
     * @param progressBarBuilder 进度条构建器
     */
    @SneakyThrows
    @Override
    public void unpack(Path to, ProgressBarBuilder progressBarBuilder) {
        Path downloadTempDir = createTempDirectory();
        download(downloadTempDir, archiveFileName, progressBarBuilder);
        checkSHA256(getSha256(), downloadTempDir.resolve(archiveFileName).toFile());
        Path unpackTempDir = unpackToTempDir(downloadTempDir.resolve(archiveFileName).toFile());
        moveFilesToDst(unpackTempDir, to);
    }

    /**
     * 解压到临时目录
     *
     * @param src 源文件
     * @return 解压后的目录
     */
    private Path unpackToTempDir(File src) throws IOException {
        Path tempDirectory = createTempDirectory();
        try (InputStream input = new FileInputStream(src)) {
            Extractor extractor = createExtractor(input);
            extractor.extract(tempDirectory.toFile());
            return tempDirectory;
        }
    }


    /**
     * 根据JDK打包格式创建解压器
     */
    private Extractor createExtractor(InputStream input) {
        switch (getPackageType()) {
            case ZIP:
                return CompressUtil.createExtractor(
                    CharsetUtil.CHARSET_UTF_8,
                    ArchiveStreamFactory.ZIP
                    , input
                );
            case TARGZ:
                return CompressUtil.createExtractor(
                    CharsetUtil.CHARSET_UTF_8,
                    ArchiveStreamFactory.TAR
                    , getIn(CompressorStreamFactory.GZIP, input)
                );
            default:
                throw new UnknownPackageTypeException();
        }
    }

    /**
     * 移动解压后的文件到目标目录,如果所需JDK文件在第一层目录中则直接移动目录中所需要的文件到目标目录
     *
     * @param src 解压后的文件所在目录
     * @param dst 目标目录
     */
    private void moveFilesToDst(Path src, Path dst) throws IOException {
        Path[] paths = Files.walk(src, 1).toArray(Path[]::new);
        ensureDirExistAndClearDir(dst);
        if (paths.length == 2) {
            Files.walk(paths[1], 1).skip(1).forEach(i -> FileUtil.copy(i, dst)); //skip(1)是为了去除path流中的根目录
        } else {
            Files.walk(src, 1).skip(1).forEach(i -> FileUtil.copy(i, dst));//skip(1)是为了去除path流中的根目录
        }
    }

    private static void checkSHA256(String expected, File file) {
        Digester digester = new Digester(DigestAlgorithm.SHA256);
        String sha256 = digester.digestHex(file);
        if (!expected.equals(sha256)) {
            throw new HashMismatchException("SHA256 mismatch,\nexpected: " + expected + ",\nactual: " + sha256);
        }
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
