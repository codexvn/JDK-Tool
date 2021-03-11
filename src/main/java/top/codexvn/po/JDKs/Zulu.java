package top.codexvn.po.JDKs;

import top.codexvn.po.JDK;
import top.codexvn.po.Original;

public class Zulu extends JDK {
    public Zulu(Original original) {
        super(original);
    }

    @Override
    public String getFileName() {
        return getClassName()+'_'+getOriginalFileName();
    }
}
