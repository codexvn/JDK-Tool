package top.codexvn.command;

import picocli.CommandLine.Command;
import top.codexvn.node.AbstractPackage;
import top.codexvn.node.Depository;
import top.codexvn.node.LocalStore;
import top.codexvn.platform.JetbrainsPlatform;
import top.codexvn.platform.Platform;
import top.codexvn.utils.FactoryUtil;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import static top.codexvn.utils.FactoryUtil.getDepository;
import static top.codexvn.utils.OtherUtil.printJdkInfoList;

@Command(name = "update", description = "更新JDK数据")
public class UpdateCommand implements Callable<Integer> {
    private Integer code = 0;

    @Override
    public Integer call() {
        List<AbstractPackage> jdkList = getJdkListFromRemote();
        try {
            LocalStore localStore = FactoryUtil.getLocalStore();
            localStore.storeJdkList(jdkList);
            printJdkInfoList(jdkList.stream().map(AbstractPackage::getJdkInfo).toList());
        } catch (IOException e) {
            code = 1;
        }
        return code;
    }

    /**
     * 从接口获得适用于当前平台的JDK信息
     */
    private List<AbstractPackage> getJdkListFromRemote() {
        Depository depository = getDepository();
        Map<Platform, List<AbstractPackage>> jdkList = depository.getJdkListFromRemote();
        Platform currentPlatform = JetbrainsPlatform.getCurrentPlatform();
        return jdkList.get(currentPlatform);
    }
}
