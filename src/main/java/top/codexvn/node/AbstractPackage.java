package top.codexvn.node;

import lombok.Data;
import lombok.EqualsAndHashCode;
import top.codexvn.ProgressBar;
import top.codexvn.enums.PackageTypeEnum;

import java.io.File;
import java.io.Serializable;
import java.nio.file.Path;

@Data
@EqualsAndHashCode
public abstract class AbstractPackage  implements Serializable {
    private static final long serialVersionUID = 1L;
    protected AbstractJdkInfo jdkInfo;
    protected  String url;
    protected PackageTypeEnum packageType;
    public abstract File download(Path to, ProgressBar progressBar);
    public abstract  File download(Path to,String archiveFileName, ProgressBar progressBar);
    public abstract  void  unpack(Path to, ProgressBar progressBar);
}
