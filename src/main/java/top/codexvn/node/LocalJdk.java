package top.codexvn.node;

import java.nio.file.Path;

public class LocalJdk {
    public Path locate;
    public AbstractJdkInfo jdkInfo;

    public LocalJdk(Path locate, AbstractJdkInfo jdkInfo) {
        this.locate = locate;
        this.jdkInfo = jdkInfo;
    }

    public LocalJdk() {
    }

    public static LocalJdk of(Path locate, AbstractJdkInfo jdkInfo) {
        return new LocalJdk(locate, jdkInfo);
    }

    public void setLocate(Path locate) {
        this.locate = locate;
    }

    public void setJdkInfo(AbstractJdkInfo jdkInfo) {
        this.jdkInfo = jdkInfo;
    }
}
