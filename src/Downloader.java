import java.io.*;
import java.util.HashMap;
import java.util.HashSet;

public class Downloader {

   private int numberOfThreads = 0;
   private int speedLimit = 0 ;
   private String pathToFile = "";
   private String outputFolder = "";
   private HashMap<String,HashSet<String>> urls;

    public Downloader (int numberOfThreads, int speedLimit, String pathToFile, String outputFolder){
        this.numberOfThreads = numberOfThreads;
        this.speedLimit = speedLimit;
        this.pathToFile = pathToFile;
        this.outputFolder = outputFolder;
    }

    private void parseUrls ()  {
        urls = new HashMap<>();
        try ( BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(pathToFile)))) {
            while (bufferedReader.ready()){
                String temp = bufferedReader.readLine();
                String [] line = temp.split(" ");
                String url = line[0];
                String filename = line[1];
                if (!urls.containsKey(url)){
                    HashSet<String> hashSet = new HashSet<>();
                    hashSet.add(filename);
                    urls.put(url,hashSet);
                } else {
                    if (urls.get(url)!=null){
                        HashSet<String> tempHashSet = new HashSet<>();
                        tempHashSet.addAll((urls.get(url)));
                        tempHashSet.add(filename);
                        urls.put(url,tempHashSet);
                    } else throw new Exception("Ошибка в формате файла со ссылками.") ;
                }


            }
        }
        catch (Exception e){
            System.out.println("Ошибка чтения файла со ссылками.");
        }


    }
}
