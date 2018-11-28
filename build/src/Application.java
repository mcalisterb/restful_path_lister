package hello;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.AccessDeniedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.*;
import java.nio.file.FileVisitOption;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.FileVisitResult;
import java.nio.charset.StandardCharsets;
import java.util.stream.Stream;
import java.util.stream.Collectors;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import java.util.EnumSet;
import java.lang.StringBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class Application {

    @RequestMapping("/")
    public String home() {
        return "Path listing service alive.\n"
               + "USAGE: http://localhost:8080/listdir?path=PATH_INPUT\n"
               + "e.g http://localhost:8080/listdir?path=/home";
    }

    @RequestMapping(value="listdir", method = RequestMethod.GET)
    public @ResponseBody char [][] listDir(@RequestParam("path") String path){
        List<Map<String,String>> filesMapList = new ArrayList<Map<String,String>>();     
        int MAX_SIZE = 1000000;
        char [][] filesArr = new char[MAX_SIZE][];
        try {
            String fullPath = "/mnt/localfs/" + path;
            System.out.println("Searching " + fullPath);
            Files.walkFileTree(Paths.get(fullPath), EnumSet.of(FileVisitOption.FOLLOW_LINKS), Integer.MAX_VALUE, new SimpleFileVisitor<Path>() {
                public int i = 0;
                public StringBuilder sb;
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    try {
                        if (i < MAX_SIZE){

                            sb = new StringBuilder("");
                            sb.append("[Path: ").append(file.toString().replace("/mnt/localfs","")).append(",")
                            .append("Size: ").append(Long.toString(attrs.size())).append(",")
                            .append("Creation time: ").append(attrs.creationTime().toString()).append(",")
                            .append("Last access time: ").append(attrs.lastAccessTime().toString()).append(",")
                            .append("Last modified time: ").append(attrs.lastModifiedTime().toString()).append("]");
                            filesArr[i] = sb.toString().toCharArray();//s.toCharArray();
                            //System.out.println(i);
                        }
                        i++;
                    } catch (Exception e) {
                        System.out.println("Invalid file " + file.toString());
                    }                      
                    return FileVisitResult.CONTINUE;
                }
            }); 
        }
        catch (Exception e){
            System.out.println ("Error occurred!");
            e.printStackTrace();
            char [][] errArr = new char[1][];
            errArr[0] = "Error! Please make sure to provide a path which exists.".toCharArray();            
            return errArr;            
        }
        return filesArr;
        
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}

