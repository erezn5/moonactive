package com.moonactive.automation.utils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.moonactive.automation.logger.LoggerFactory.LOG;

public class FileUtil {

    private FileUtil() { }

    public static Properties createPropertiesFromResource(Class clazz , String relativePath) {
        try(InputStream ips = clazz.getClassLoader().getResourceAsStream(relativePath)){
            Properties properties = new Properties();
            properties.load(ips);
            return properties;
        }catch (IOException e){
            System.err.println("Failed to convert resource'" + relativePath + "'stream to properties, cause: " + e.getMessage());
            return null;
        }
    }

    public static void writeToFile(String filePath , HashMap<String,String> map) {
        try(FileWriter fileWriter = new FileWriter(filePath);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)){
            for(Map.Entry<String,String> entry : map.entrySet()){
                bufferedWriter.write(entry.getKey() + "=" + entry.getValue());
                bufferedWriter.write('\n');
            }
            bufferedWriter.flush();
        }catch (Exception e){
            LOG.e(e ,"failed write to file=[%s]" , filePath);
        }
    }


    public static void createFolder(File folder , boolean recursive){
        if(folder.exists() && folder.isDirectory()){
            LOG.i(folder.getName() + " directory already exist");
        }else if((recursive ? folder.mkdirs() : folder.mkdir())){
            LOG.i(folder.getName() + " directory created successfully");
        }else{
            LOG.error("failed to create '" + folder.getName() + "' directory");
        }
    }

    public static boolean delete(File file){
        boolean success = file.delete();
        LOG.i("File was deleted =[%b]", success);
        return success;
    }

    public static void printFileContent(String filePath) {
        try (Stream<String> lines = Files.lines(Paths.get(filePath))) {
            String content = lines.collect(Collectors.joining(System.lineSeparator()));
            System.out.println(content);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

