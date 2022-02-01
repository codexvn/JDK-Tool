package top.codexvn.node.impl;


import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import top.codexvn.enums.ArchEnum;
import top.codexvn.enums.OSEnum;
import top.codexvn.node.AbstractJdkInfo;


@Getter
@EqualsAndHashCode(callSuper = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class JetbrainsJdkInfo extends AbstractJdkInfo {
    public void setOs(String os) {
        try {
            this.os = Enum.valueOf(OSEnum.class, os);
        } catch (IllegalArgumentException e) {
            switch (os) {
                case "macOS":
                    this.os = OSEnum.MACOS;
                    break;
                case "linux":
                    this.os = OSEnum.LINUX;
                    break;
                case "windows":
                    this.os = OSEnum.WINDOWS;
                    break;
                default:
                    this.os = OSEnum.UNKNOWN;
                    break;
            }
        }
    }

    public void setArch(String arch) {
        try {
            this.arch = Enum.valueOf(ArchEnum.class, arch);
        } catch (IllegalArgumentException e) {
            switch (arch) {
                case "x86_64":
                case "X64":
                    this.arch = ArchEnum.X64;
                    break;
                case "aarch64":
                case "AARCH64":
                    this.arch = ArchEnum.AARCH64;
                    break;
                default:
                    this.arch = ArchEnum.UNKNOWN;
                    break;
            }
        }
    }

    public void isDcevm() {
        this.product = String.format("%s-%s", this.product, "dcevm");
    }
}
