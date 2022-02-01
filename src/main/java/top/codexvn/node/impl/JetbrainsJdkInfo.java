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
            this.arch = switch (arch) {
                case "x86_64" -> ArchEnum.X64;
                case "aarch64" -> ArchEnum.AARCH64;
                default -> ArchEnum.UNKNOWN;
            };
        }
    }

    public void isDcevm() {
        this.product = String.format("%s-%s", this.product, "dcevm");
    }
}
