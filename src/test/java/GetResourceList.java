import com.fasterxml.jackson.databind.ObjectMapper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;
import top.codexvn.po.JDK;
import top.codexvn.po.ResourceList;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.function.Consumer;

public class GetResourceList {
    @Test
    ResourceList getResourceList() throws IOException {
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
                            try {
                                URL url = new URL(j.attr("href"));
                                String fileName = Paths.get(url.getPath()).getFileName().toString();
                                if(fileName.endsWith(".zip") && fileName.indexOf("win")!=-1 && fileName.indexOf("x64")!=-1)
                                    resourceList.addJDK(name, version, new JDK(name, version, fileName, url));
                            } catch (MalformedURLException e) {
                                e.printStackTrace();
                            }
                    }
                }
            });

        }
        return resourceList;
    }
    @Test
    void listTOJson() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        FileWriter fileWriter = new FileWriter(new File("d:/2.json"));
        ResourceList resourceList = getResourceList();
        fileWriter.write(mapper.writeValueAsString(resourceList));
        fileWriter.close();
    }
@Test
    void jsonToList() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ResourceList resourceList = mapper.readValue(new File("d:/2.json"),ResourceList.class);
    System.out.println(2121);
    }
}
