package top.codexvn.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.net.URL;

@Data
@AllArgsConstructor
public  class Original {
    private String name;
    private String version;
    private String fileName;

    private URL url;


}
