package top.codexvn.node;

import top.codexvn.platform.Platform;

import java.util.List;
import java.util.Map;

public interface Depository {
    Map<Platform, List<AbstractPackage>> getJdkListFromRemote();

    Map<Platform, List<AbstractJdkInfo>> getInfoListFromRemote();
}
