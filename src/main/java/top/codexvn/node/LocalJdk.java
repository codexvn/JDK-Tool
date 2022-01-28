package top.codexvn.node;

import java.nio.file.Path;

public class LocalJdk {
    public final Path locate;
    public final AbstractJdkInfo jdkInfo;

    public LocalJdk(Path locate, AbstractJdkInfo jdkInfo) {
        this.locate = locate;
        this.jdkInfo = jdkInfo;
    }
    public static LocalJdk of(Path locate, AbstractJdkInfo jdkInfo){
        return new LocalJdk(locate,jdkInfo);
    }
}
