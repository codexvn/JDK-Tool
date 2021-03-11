package top.codexvn.po;

import java.net.URL;

public abstract class JDK {

    public static enum ARCHITECTURE{
            X86,X64
        }

    protected Original original;
    private ARCHITECTURE architecture=ARCHITECTURE.X64;

    public JDK(Original original) {
        this.original = original;
    }

    public URL getUrl() {
        return original.getUrl();
    }

    public String getVersion() {
        return original.getVersion();
    }

    public String getClassName() {
        return this.getClass().getSimpleName();
    }

    public String getOriginalFileName() {
        return original.getFileName();
    }

    abstract public String getFileName();

    public ARCHITECTURE getArchitecture() {
        return architecture;
    }

    public void setArchitecture(ARCHITECTURE architecture) {
        this.architecture = architecture;
    }

    @Override
    public String toString() {
        return getClassName() + "{" +
                "original=" + original.toString() + '\'' +
                ", version='" + getVersion() + '\'' +
                ", className='" + getClassName() + '\'' +
                ", originalFileName='" + getOriginalFileName() + '\'' +
                ", fileName='" + getFileName() + '\'' +
                ", url=" + getUrl() +
                '}';
    }
}