package top.codexvn.platform;

import top.codexvn.enums.ArchEnum;
import top.codexvn.enums.OSEnum;

public class JetbrainsPlatform extends Platform {

    public JetbrainsPlatform(OSEnum os, ArchEnum arch) {
        super(os, arch);
    }

    public JetbrainsPlatform(String os, String arch) {
        this(getOSByValue(os), getArchByValue(arch));
    }
    public static OSEnum getOSByValue(String value) {
        switch (value) {
            case "macOS":
                return OSEnum.MACOS;
            case "linux":
                return OSEnum.LINUX;
            case "windows":
                return OSEnum.WINDOWS;
            default:
                return OSEnum.UNKNOWN;
        }
    }

    public static ArchEnum getArchByValue(String value) {
        switch (value) {
            case "x86_64":
                return ArchEnum.X64;
            case "aarch64":
                return ArchEnum.AARCH64;
            default:
                return ArchEnum.UNKNOWN;
        }
    }
}
