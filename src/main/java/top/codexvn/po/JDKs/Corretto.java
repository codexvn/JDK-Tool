package top.codexvn.po.JDKs;

import top.codexvn.po.JDK;
import top.codexvn.po.Original;

public class Corretto extends JDK {
    public Corretto(Original original) {
        super(original);
    }

    @Override
    public String getFileName() {
        return getOriginalFileName();
    }
}
