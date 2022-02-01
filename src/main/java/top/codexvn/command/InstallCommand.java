package top.codexvn.command;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import top.codexvn.node.AbstractJdkInfo;
import top.codexvn.node.AbstractPackage;
import top.codexvn.node.LocalJdk;
import top.codexvn.node.LocalStore;
import top.codexvn.utils.DownloadUtil;
import top.codexvn.utils.FactoryUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.Callable;

@Command(name = "install", description = "安装JDK")
public class InstallCommand implements Callable<Integer> {
    private final static String INSTALL_LOCATION = System.getProperty("user.home") + "/.jdk/";

    private Integer code;
    @Option(names = {"-i", "--index"}, description = "指定待安装JDK的索引")
    private Integer index;

    @Override
    public Integer call() {
        try {
            AbstractPackage abstractPackage = loadJdkList().get(index);
            AbstractJdkInfo jdkInfo = abstractPackage.getJdkInfo();
            Path dest = Path.of(INSTALL_LOCATION, generateFileName(jdkInfo));
            if (!Files.isDirectory(dest)) {
                Files.createDirectories(Path.of(INSTALL_LOCATION));
            }
            abstractPackage.unpack(dest, DownloadUtil.getDownloaderProgressBarBuilder());
            LocalStore localStore = FactoryUtil.getLocalStore();
            List<LocalJdk> localJdks = localStore.loadLocalJdkList();
            localJdks.add(new LocalJdk(dest, jdkInfo));
            localStore.storeLocalJdkList(localJdks);
        } catch (IOException e) {
            e.printStackTrace();
            code = -1;
        }
        return code;
    }

    private List<AbstractPackage> loadJdkList() throws IOException {
        LocalStore localStore = FactoryUtil.getLocalStore();
        return localStore.loadJdkList();
    }

    private String generateFileName(AbstractJdkInfo jdkInfo) {
        return String.format("%s-%s", jdkInfo.getProduct(), jdkInfo.getJdkVersionMajor());
    }

}
