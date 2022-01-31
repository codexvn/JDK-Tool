package top.codexvn.command;

import lombok.extern.slf4j.Slf4j;
import picocli.CommandLine.Command;
import top.codexvn.node.AbstractPackage;
import top.codexvn.node.LocalStore;
import top.codexvn.utils.FactoryUtil;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Callable;

import static top.codexvn.utils.OtherUtil.printJdkInfoList;

@Command(name = "list", description = "列出可用JDK")
@Slf4j
public class ListCommand implements Callable<Integer> {
    private Integer code = 0;

    @Override
    public Integer call() throws IOException {
        LocalStore localStore = FactoryUtil.getLocalStore();
        List<AbstractPackage> jdkList = localStore.loadJdkList();
        printJdkInfoList(jdkList.stream().map(AbstractPackage::getJdkInfo).toList());
        return code;
    }

}
