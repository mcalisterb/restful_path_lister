package hello;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.AccessDeniedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.*;
import java.util.stream.Stream;
import java.util.stream.Collectors;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
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
    public @ResponseBody List<Map<String,String>> listDir(@RequestParam("path") String path){
        List<Map<String,String>> filesMapList = new ArrayList<Map<String,String>>();
        try {
            String fullPath = "/mnt/localfs/" + path;
            System.out.println("Searching " + fullPath);
            Stream<Path> paths = Files.find(Paths.get(fullPath),
                                            Integer.MAX_VALUE,
                                            (filePath, fileAttr) -> fileAttr.isRegularFile());
            List<Path> list = paths.map(path_ -> Files.isDirectory(path_) ? path_: path_)
                                     .collect(Collectors.toList());

           
            for (Path pth: list){
                try {                    
                    BasicFileAttributes attrs = Files.readAttributes(pth, BasicFileAttributes.class);
                    HashMap<String, String> filemap = new HashMap<>();
                    filemap.put("Path", pth.toString().replace("/mnt/localfs",""));
                    filemap.put("Size",Long.toString(attrs.size()));
                    filemap.put("Creation time",attrs.creationTime().toString());
                    filemap.put("Last access time", attrs.lastAccessTime().toString());
                    filemap.put("Last modified time", attrs.lastModifiedTime().toString());
                    filesMapList.add(filemap);

                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
            
        }
        catch (UncheckedIOException e){
            System.out.println ("Access denied!");
            HashMap<String, String> errorMap = new HashMap<>();
            errorMap.put("Error","Access denied to a file in the specified path listing."
                         + "Please make sure to only provide paths with full read permissions.");
            filesMapList.add(errorMap);
            e.printStackTrace();
        }
        catch (Exception e){
            System.out.println ("Error occurred!");
            HashMap<String, String> errorMap = new HashMap<>();
            errorMap.put("Error","Please make sure to provide a path which exists.");
            filesMapList.add(errorMap);
            e.printStackTrace();
        }
        System.out.println(filesMapList.size());
        return filesMapList;
        
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}

