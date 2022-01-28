package top.codexvn.command;

import lombok.extern.slf4j.Slf4j;
import picocli.CommandLine.Command;
import top.codexvn.node.impl.JetbrainsJdkInfo;
import top.codexvn.node.impl.JetbrainsPackage;
import top.codexvn.platform.Platform;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

@Command(name="list",description="列出可用JDK")
@Slf4j
public class ListCommand implements Callable<Integer> {
    private Integer code = 0;
    @Override
    public Integer call()  {
        log.info("Listing JDKs...");
        try(ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream("jdks.dat"))) {
            Map<Platform, Map<JetbrainsJdkInfo, List<JetbrainsPackage>>> jdks =(Map<Platform, Map<JetbrainsJdkInfo, List<JetbrainsPackage>>>) inputStream.readObject();
            log.info("JDKs:{}",jdks);
        }catch (FileNotFoundException e){
            log.error("jdks.dat not found, please run 'upgrade' command first");
            code = 1;
        }catch (IOException | ClassNotFoundException ignored){}
        return code;
    }
}
