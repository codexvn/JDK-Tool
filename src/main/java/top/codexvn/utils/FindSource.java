package top.codexvn.utils;

import lombok.AllArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import top.codexvn.po.JDK;
import top.codexvn.po.JdkFactory;
import top.codexvn.po.Original;
import top.codexvn.vo.JdkList;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FindSource {
    public static JdkList getSource() {
        JdkList jdkList = new JdkList();
        try {
            Document doc = Jsoup.connect("https://www.injdk.cn/").get();
            Element elementsByClass = doc.getElementsByClass("tab-content").first();
            Elements sourceList = elementsByClass.children();
            ExecutorService executorService = Executors.newCachedThreadPool();
            for (var i : sourceList) {
                executorService.submit(new MyThread(jdkList, i));
            }
            executorService.shutdown();
            while (executorService.isTerminated() == false) ;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jdkList;
    }

}

@AllArgsConstructor
class MyThread implements Runnable {
    private JdkList jdkList;
    private Element element;

    private void addJdkFromChildren(Element source, String jdkName) {

        String version = source.selectFirst("span").text();
        Elements fileList = source.select("a");
        for (var j : fileList) {
            try {
                URL url = new URL(j.attr("href"));
                String fileName = Paths.get(url.getPath()).getFileName().toString();
                if (fileName.endsWith(".zip") && fileName.indexOf("win") != -1) {
                    Original original = new Original(jdkName, version, fileName, url);
                    JDK jdk = JdkFactory.builder(original);
                    jdkList.add(jdk);
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void run() {
        String jdkName = element.id();
        Element jdkOfName = element.getElementsByClass("grid-col").first();
        if (jdkOfName == null)
            return;
        else
            for (var i : jdkOfName.children()) {
                addJdkFromChildren(i, jdkName);
            }

    }
}