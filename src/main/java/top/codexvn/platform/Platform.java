package top.codexvn.platform;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import top.codexvn.enums.ArchEnum;
import top.codexvn.enums.OSEnum;

import java.io.Serializable;


@Getter
@EqualsAndHashCode
public class Platform implements Serializable {

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
        String os = System.getProperty("os.name");
        if (os.contains("Windows")) {
            return OSEnum.WINDOWS;
        } else if (os.contains("Linux ")) {
            return OSEnum.LINUX;
        } else if (os.contains("Mac")) {
            return OSEnum.MACOS;
        } else {
            return OSEnum.UNKNOWN;
        }
    }

    public static ArchEnum getCurrentArch() {
        String arch = System.getProperty("os.arch");
        switch (arch) {
            case "x86":
            case "i386 ":
                return ArchEnum.X86;
            case "amd64":
                return ArchEnum.X64;
            case "aarch64":
                return ArchEnum.AARCH64;
            default:
                return ArchEnum.UNKNOWN;
        }
    }
}