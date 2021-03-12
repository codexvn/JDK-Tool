package top.codexvn.po;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class JDK implements Comparable<JDK> {
    Logger logger = Logger.getLogger(this.getClassName());
     {
        logger.setLevel(Level.OFF);
    }
    @Override
    public int compareTo(JDK o) {
        if (getClassName().equals(o.getClassName()) == false){
            logger.warning(getClassName()+':'+o.getClassName()+':'+getClassName().compareTo(o.getClassName()));
            return getClassName().compareTo(o.getClassName());
        }
        else if (getVersion().equals(o.getVersion()) == false){
            logger.warning(getVersion()+':'+o.getVersion()+':'+getVersion().compareTo(o.getVersion()));
            return getVersion().compareTo(o.getVersion());
        }
        else{
            logger.warning(getArchitecture().toString()+':'+o.getArchitecture().toString()+':'+getArchitecture().compareTo(o.getArchitecture()));
            return getArchitecture().compareTo(o.getArchitecture());
        }
    }

    public static enum ARCHITECTURE {
        X86, X64
    }

    protected Original original;
    private ARCHITECTURE architecture = ARCHITECTURE.X64;
    private final Pattern pattern = Pattern.compile("\\bJDK\\d+\\w*(?<!\\()");

    public JDK(Original original) {
        this.original = original;
    }

    public URL getUrl() {
        return original.getUrl();
    }

    public String getVersion() {
        Matcher matcher = pattern.matcher(original.getVersion());

        if(matcher.find())
            return matcher.group();
        else
            return "";
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