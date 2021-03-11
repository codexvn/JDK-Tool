package top.codexvn.po.JDKs;

import top.codexvn.po.JDK;
import top.codexvn.po.Original;

public class OpenJDK extends JDK {

    public OpenJDK(Original original) {
        super(original);
    }

    @Override
    public String getFileName() {
        return getClassName()+'-' + getOriginalFileName();
    }
}