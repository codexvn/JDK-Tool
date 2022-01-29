package top.codexvn.node;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import top.codexvn.enums.ArchEnum;
import top.codexvn.enums.OSEnum;

import java.io.Serializable;
import java.util.Arrays;


@Getter
@EqualsAndHashCode

public abstract class AbstractJdkInfo implements Comparable<AbstractJdkInfo>, Serializable {
    private static final long serialVersionUID = 1L;
    protected String vendor;
    protected String product;
    protected OSEnum os;
    protected ArchEnum arch;
    protected String jdkVersionMajor;
    protected String jdkVersion;

    @Override
    public int compareTo(AbstractJdkInfo o) {
        if (compareVendor(o.getVendor()) != 0) {
            return compareVendor(o.getVendor());
        } else if (compareProduct(o.getProduct()) != 0) {
            return compareProduct(o.getProduct());
        } else {
            return compareJdkVersion(o.getJdkVersion());
        }
    }

    private int compareVendor(String o) {
        return this.vendor.compareTo(o);
    }

    private int compareProduct(String o) {
        return this.product.compareTo(o);
    }

    private int compareJdkVersion(String o) {
        return Arrays.compare(pauseJdkVersion(this.jdkVersion), pauseJdkVersion(o));
    }

    private Integer[] pauseJdkVersion(String o) {
        String version = o.replaceAll("[^0-9.]", "");
        return Arrays.stream(version.split("\\.")).map(Integer::parseInt).toArray(Integer[]::new);
    }

}
