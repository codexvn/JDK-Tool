package top.codexvn.po.JDKs;

import top.codexvn.po.JDK;
import top.codexvn.po.Original;

public class AdoptOpenJDK extends JDK {
    public AdoptOpenJDK(Original original) {
        super(original);
    }

    @Override
    public String getFileName() {
        String[] split = getUrl().getFile().split("/");
        return split[split.length-1];
    }
}
