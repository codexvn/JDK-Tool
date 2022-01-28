package top.codexvn.node;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import top.codexvn.enums.ArchEnum;
import top.codexvn.enums.OSEnum;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;


@Getter
@EqualsAndHashCode

public abstract class AbstractJdkInfo implements Comparable<AbstractJdkInfo>, Serializable {
    private static final long serialVersionUID = 1L;
    protected  String vendor;
    protected  String product;
    protected OSEnum os;
    protected ArchEnum arch;
    protected  String jdkVersionMajor;
    protected  String jdkVersion;
    @Override
    public int compareTo( AbstractJdkInfo o) {
        Integer[] thisJdkVersion = Arrays.stream(this.jdkVersion.split("\\.")).map(Integer::parseInt).toArray(Integer[]::new);
        Integer[] OJdkVersion = Arrays.stream(o.jdkVersion.split("\\.")).map(Integer::parseInt).toArray(Integer[]::new);
        if (!Objects.equals(thisJdkVersion[0], OJdkVersion[0])) {
            return thisJdkVersion[0] - OJdkVersion[0];
        }else if (!Objects.equals(thisJdkVersion[1], OJdkVersion[1])) {
            return thisJdkVersion[1] - OJdkVersion[1];
        }
        return thisJdkVersion[2] - OJdkVersion[2];
    }

}
