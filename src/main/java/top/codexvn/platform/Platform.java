package top.codexvn.platform;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import top.codexvn.enums.ArchEnum;
import top.codexvn.enums.OSEnum;

import java.io.Serializable;


@Getter
@EqualsAndHashCode
public  class Platform implements Serializable {

    private static final long serialVersionUID = 1L;
    private final OSEnum os;
    private final ArchEnum arch;

    protected Platform(OSEnum os, ArchEnum arch) {
        this.os = os;
        this.arch = arch;
    }


    public static Platform getCurrentPlatform() {
        return new Platform(getCurrentOS(), getCurrentArch());
    }

    public static OSEnum getCurrentOS() {
        String os = System.getProperty("os.name").toLowerCase();
        throw  new UnsupportedOperationException("Not implemented yet");
    }

    public static ArchEnum getCurrentArch() {
        String arch = System.getProperty("os.arch").toLowerCase();
        throw  new UnsupportedOperationException("Not implemented yet");
    }
}