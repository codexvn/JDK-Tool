package top.codexvn.utils;

import cn.hutool.core.io.FileUtil;
import top.codexvn.node.AbstractJdkInfo;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class OtherUtil {
    public static void printJdkInfoList(List<AbstractJdkInfo> jdkInfoList) {
        printJdkInfoList(jdkInfoList, System.out);
    }

    public static void printJdkInfoList(List<AbstractJdkInfo> jdkInfoList, PrintStream printStream) {
        int index = 0;
        for (AbstractJdkInfo i : jdkInfoList) {
            // TODO: 2022/1/30 0030 下午 4:05 美化输出，对齐表格
            printStream.printf("[%d] %s %s %s %s\n", index++, i.getVendor(), i.getProduct(), i.getJdkVersionMajor(), i.getJdkVersion());
        }
    }

    public static Path createTempDirectory() throws IOException {
        Path file = Files.createTempDirectory(null);
//        file.toFile().deleteOnExit();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            FileUtil.del(file);
        }));
        return file;
    }
}
