package top.codexvn.po.JDKs;

import top.codexvn.po.JDK;
import top.codexvn.po.Original;

public class GraalVM extends JDK {
    public GraalVM(Original original) {
        super(original);
    }
    @Override
    public String getFileName() {
        return original.getFileName();
    }
}
