package top.codexvn.vo;

import top.codexvn.po.JDK;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.ConcurrentSkipListSet;

public class JdkList {
    //    new HashMap<String,TreeMap<String,TreeSet<JDK>>>()
    ConcurrentHashMap<String, ConcurrentSkipListMap<String, ConcurrentSkipListSet<JDK>>> jdkList = new ConcurrentHashMap<>();

    public void add(JDK jdk) {
        if(jdk==null)return;
        if (jdkList.containsKey(jdk.getClassName()) == false) {
            ConcurrentSkipListSet<JDK> set = new ConcurrentSkipListSet<>();
            set.add(jdk);

            ConcurrentSkipListMap<String, ConcurrentSkipListSet<JDK>> map = new ConcurrentSkipListMap<>();
            map.put(jdk.getVersion(), set);

            jdkList.put(jdk.getClassName(), map);

        } else if (jdkList.get(jdk.getClassName()).containsKey(jdk.getVersion()) == false) {
            ConcurrentSkipListSet<JDK> set = new ConcurrentSkipListSet<>();
            set.add(jdk);
            jdkList.get(jdk.getClassName()).put(jdk.getVersion(), set);
        } else
            jdkList.get(jdk.getClassName()).get(jdk.getVersion()).add(jdk);
    }

    public ConcurrentHashMap<String, ConcurrentSkipListMap<String, ConcurrentSkipListSet<JDK>>> getJdkList() {
        return jdkList;
    }
}
