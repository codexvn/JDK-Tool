package top.codexvn;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import top.codexvn.utils.FindSource;
import top.codexvn.vo.JdkList;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

public class Main {
    public static void main(String[] args){
        JdkList source = FindSource.getSource();
        ObjectMapper mapper = new ObjectMapper();
        try( FileWriter fileWriter = new FileWriter(new File("123.json"))){
            fileWriter.write(mapper.writeValueAsString(source));

        }catch (IOException e) {
            e.printStackTrace();
        }
    }
}
