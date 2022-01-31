package top.codexvn.utils;

import top.codexvn.node.AbstractJdkInfo;

import java.io.PrintStream;
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
}
