package top.codexvn.node;

import java.io.IOException;
import java.util.List;

public interface LocalStore {
    void storeJdkList(List<AbstractPackage> jdkList) throws IOException;

    List<AbstractPackage> loadJdkList() throws IOException;

    void storeLocalJdkList(List<LocalJdk> jdkList) throws IOException;

    List<LocalJdk> loadLocalJdkList() throws IOException;
}
