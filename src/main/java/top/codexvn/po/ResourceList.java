package top.codexvn.po;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.function.BiFunction;

public class ResourceList implements Serializable {
    private TreeMap<String,TreeMap<String, JDK>> jdkList = new TreeMap<String, TreeMap<String, JDK>>();
    private TreeMap<String, TreeMap<String, JRE>> jreList = new TreeMap<String, TreeMap<String, JRE>>();

    public void addJDK(String name, String version, JDK jdk) {
        TreeMap<String, JDK> jdkListByName = jdkList.get(name);
        if (jdkListByName == null) jdkList.put(name, new TreeMap<String, JDK>() {{
            put(version, jdk);
        }});
        else jdkListByName.put(version, jdk);
    }

    public void addJRE(String name, String version, JRE jre) {
        TreeMap<String, JRE> jreListByName = jreList.get(name);
        if (jreListByName == null) jreList.put(name, new TreeMap<String, JRE>() {{
            put(version, jre);
        }});
        else jreListByName.put(version, jre);
    }

    public TreeMap<String, TreeMap<String, JDK>> getJdkList() {
        return jdkList;
    }

    public TreeMap<String, TreeMap<String, JRE>> getJreList() {
        return jreList;
    }
}
