package top.codexvn.node;

import lombok.Data;
import lombok.EqualsAndHashCode;
import me.tongfei.progressbar.ProgressBarBuilder;
import top.codexvn.enums.PackageTypeEnum;

import java.io.File;
import java.io.Serializable;
import java.nio.file.Path;

@Data
@EqualsAndHashCode
public abstract class AbstractPackage implements Serializable, Comparable<AbstractPackage> {
    private static final long serialVersionUID = 1L;
    protected AbstractJdkInfo jdkInfo;
    protected String url;
    protected PackageTypeEnum packageType;

    public abstract File download(Path to, ProgressBarBuilder progressBarBuilder);

    public abstract File download(Path to, String archiveFileName, ProgressBarBuilder progressBarBuilder);

    public abstract void unpack(Path to, ProgressBarBuilder progressBarBuilder);

    public int compareTo(AbstractPackage o) {
        return this.jdkInfo.compareTo(o.jdkInfo);
    }
}
