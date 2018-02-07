import java.io.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Manager {

   private static int numberOfThreads = 0;
   private static int speedLimit = 0 ;
   private String pathToFile = "";
   private String outputFolder = "";
   private HashMap<String,HashSet<String>> urls;

    public Manager(int numberOfThreads, int speedLimit, String pathToFile, String outputFolder){
        this.numberOfThreads = numberOfThreads;
        this.speedLimit = speedLimit;
        this.pathToFile = pathToFile;
        this.outputFolder = outputFolder;
        parseUrls();
        startDownload();
    }

    public static int getSpeedLimit() {
        return speedLimit;
    }

    private void parseUrls ()  {
        urls = new HashMap<>();
        try ( BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(pathToFile)))) {
            while (bufferedReader.ready()){
                String temp = bufferedReader.readLine();
                String [] line = temp.split(" ");
                if (line.length<2){
                    System.out.println("Ошибка в формате файла со ссылками.");
                    System.exit(1);
                }
                String url = line[0];
                String filename = line[1];
                if (!urls.containsKey(url)){
                    HashSet<String> hashSet = new HashSet<>();
                    hashSet.add(outputFolder+File.separator+filename);
                    urls.put(url,hashSet);
                } else {
                    if (urls.get(url)!=null){
                        HashSet<String> tempHashSet = new HashSet<>();
                        tempHashSet.addAll((urls.get(url)));
                        tempHashSet.add(outputFolder+File.separator+filename);
                        urls.put(url,tempHashSet);
                    } else {
                        System.out.println("Ошибка в формате файла со ссылками.");
                        System.exit(1);
                    }

                }
            }
        }
        catch (IOException i){
            System.err.println("Ошибка чтения файла со ссылками.");
        }


    }
    private void startDownload(){
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        for (Map.Entry<String,HashSet<String>> entry: urls.entrySet()){
            String key = entry.getKey();
            HashSet<String> value = entry.getValue();
            executorService.submit(new Downloader(key,value));

        }

    }
}
