package top.codexvn.command;

import cn.hutool.core.io.FileUtil;
import lombok.EqualsAndHashCode;
import picocli.CommandLine.Command;
import top.codexvn.enums.ArchEnum;
import top.codexvn.enums.OSEnum;
import top.codexvn.node.AbstractJdkInfo;
import top.codexvn.node.AbstractPackage;
import top.codexvn.node.LocalJdk;
import top.codexvn.node.LocalStore;
import top.codexvn.utils.DownloadUtil;
import top.codexvn.utils.FactoryUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

@Command(name = "upgrade", description = "更新JDK")
public class UpgradeCommand implements Callable<Integer> {
    private Integer code = 0;

    /**
     * 保证文件夹存在且为空
     *
     * @param dir 目标文件夹
     */
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

    @Override
    public Integer call() {
        try {
            LocalStore localStore = FactoryUtil.getLocalStore();
            List<AbstractPackage> packages = localStore.loadJdkList();
            List<LocalJdk> localJdks = localStore.loadLocalJdkList();
            Map<LocalJdk, AbstractPackage> available = checkAvailableAndSetNewJdkInfo(packages, localJdks);
            for (var i : available.entrySet()) {
                LocalJdk localJdk = i.getKey();
                ensureDirExistAndClearDir(localJdk.locate);
                AbstractPackage abstractPackage = i.getValue();
                abstractPackage.unpack(localJdk.locate, DownloadUtil.getDownloaderProgressBarBuilder());
            }
            localStore.storeLocalJdkList(localJdks);
        } catch (IOException e) {
            code = 1;
        }

        return code;
    }

    /**
     * 检查可升级的JDK并更新localJdks中的JDK信息为新信息
     *
     * @return 可升级的JDK集合
     */
    private Map<LocalJdk, AbstractPackage> checkAvailableAndSetNewJdkInfo(List<AbstractPackage> packages, List<LocalJdk> localJdks) throws IOException {
        Map<LocalJdk, AbstractPackage> result = new HashMap<>();
        Map<Key, AbstractPackage> collect = packages.stream().collect(Collectors.toMap(Key::new, v -> v));
        for (LocalJdk localJdk : localJdks) {
            Key tmp = new Key(localJdk.jdkInfo);
            AbstractPackage abstractPackage = collect.get(tmp);
            if (abstractPackage != null
                && abstractPackage.getJdkInfo().compareTo(localJdk.jdkInfo) > 0) { // 且版本比大于本地版本
                localJdk.setJdkInfo(abstractPackage.getJdkInfo());
                result.put(localJdk, abstractPackage);
            }
        }
        return result;
    }


    @EqualsAndHashCode
    private static class Key {
        public String vendor;
        public String product;
        public OSEnum os;
        public ArchEnum arch;
        public String jdkVersionMajor;

        public Key(AbstractPackage jdkPackage) {
            this.vendor = jdkPackage.getJdkInfo().getVendor();
            this.product = jdkPackage.getJdkInfo().getProduct();
            this.os = jdkPackage.getJdkInfo().getOs();
            this.arch = jdkPackage.getJdkInfo().getArch();
            this.jdkVersionMajor = jdkPackage.getJdkInfo().getJdkVersionMajor();
        }

        public Key(AbstractJdkInfo jdkInfo) {
            this.vendor = jdkInfo.getVendor();
            this.product = jdkInfo.getProduct();
            this.os = jdkInfo.getOs();
            this.arch = jdkInfo.getArch();
            this.jdkVersionMajor = jdkInfo.getJdkVersionMajor();
        }
    }
}
