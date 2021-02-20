import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;
import top.codexvn.po.JDK;
import top.codexvn.po.ResourceList;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.function.Consumer;

public class GetResourceList {
    @Test
    void name() throws IOException {
        ResourceList resourceList = new ResourceList();
        Document doc = Jsoup.connect("https://www.injdk.cn/").get();
        Element elementsByClass = doc.getElementsByClass("tab-content").first();
        Elements jdkList = elementsByClass.children();
        for (var i : jdkList) {
            String name = i.id();
            Element jdkOfName = i.getElementsByClass("grid-col").first();
            if(jdkOfName==null)
                continue;
            jdkOfName.children().forEach(new Consumer<Element>() {
                @Override
                public void accept(Element element) {
                    String version = element.selectFirst("span").text();
                    Elements fileList = element.select("a");
                    for (var j : fileList) {
                        String fileName = j.text();
                        if (fileName.indexOf("win") != -1)
                            try {
                                URL url = new URL(j.attr("href"));
                                resourceList.addJDK(name, version, new JDK(name, version, fileName, url));
                            } catch (MalformedURLException e) {
                                e.printStackTrace();
                            }
                    }
                }
            });

        }
        System.out.println(resourceList.toString());
    }
}
