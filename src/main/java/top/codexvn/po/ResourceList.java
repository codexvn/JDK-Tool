package top.codexvn.po;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.BiFunction;

public class ResourceList implements Serializable {
    private HashMap<String, HashMap<String, JDK>> jdkList = new HashMap<String, HashMap<String, JDK>>();
    private HashMap<String, HashMap<String, JRE>> jreList = new HashMap<String, HashMap<String, JRE>>();

    public void addJDK(String name, String version, JDK jdk) {
        HashMap<String, JDK> jdkListByName = jdkList.get(name);
        if (jdkListByName == null) jdkList.put(name, new HashMap<String, JDK>() {{
            put(version, jdk);
        }});
        else jdkListByName.put(version, jdk);
    }

    public void addJRE(String name, String version, JRE jre) {
        HashMap<String, JRE> jreListByName = jreList.get(name);
        if (jreListByName == null) jreList.put(name, new HashMap<String, JRE>() {{
            put(version, jre);
        }});
        else jreListByName.put(version, jre);
    }

    public HashMap<String, HashMap<String, JDK>> getJdkList() {
        return jdkList;
    }

    public HashMap<String, HashMap<String, JRE>> getJreList() {
        return jreList;
    }
}
