import org.jsoup.Jsoup;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.function.Consumer;

public class main {
    public static void main(String[] args) throws IOException {
        Document doc = Jsoup.connect("https://www.injdk.cn/").get();
        Element elementsByClass = doc.getElementsByClass("tab-content").first();
        Elements children = elementsByClass.children();
        children.forEach(a -> System.out.println(a.id()));
        children.first().getElementsByClass("grid-col").forEach(new Consumer<Element>() {
            @Override
            public void accept(Element element) {
                System.out.println(element.selectFirst("span").text());
                element.select("li").forEach(new Consumer<Element>() {
                    @Override
                    public void accept(Element element) {
                        System.out.println(element.selectFirst("a").attr("href"));
                    }
                });
            }
        });
    }
}