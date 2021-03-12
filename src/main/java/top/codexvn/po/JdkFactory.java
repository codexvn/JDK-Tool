package top.codexvn.po;

import lombok.Data;
import top.codexvn.po.JDKs.*;

import java.net.URL;


public class JdkFactory {
    public static JDK builder(Original original){
        switch (original.getName()){
            case "oracle-jdk": return new OracleJDK(original);
            case "SapMachine": return new SapMachine(original);
            case "RedHat-JDK": return new RedHatOpenJDK(original);
            case "Zulu-JDK": return new Zulu(original);
            case "adopt-JDK": return new AdoptOpenJDK(original);
            case "Amazon-JDK": return new Corretto(original);
            case "OpenJDK": return new OpenJDK(original);
            case "Liberica-JDK": return new Liberica(original);
            default:
                System.out.println(original.getName()); return null;
        }
    }
}


