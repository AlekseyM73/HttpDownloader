import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashSet;

/**
 * Created by Makarovaa on 06.02.18.
 */
public class Downloader implements Runnable {

    private String url;
    private HashSet<String> namesTosave;
    private static volatile long totalBytesRead = 0;
    private long bytesRead = 0;
    private int speedLimit;
    private static volatile int elapsedTime;

    public Downloader(String url, HashSet<String> namesTosave) {
        this.url = url;
        this.namesTosave = namesTosave;
        speedLimit = Manager.getSpeedLimit();
    }

    public static long getTotalBytesRead() {
        return totalBytesRead;
    }

    public static int getElapsedTime() {
        return elapsedTime;
    }

    @Override
    public void run() {
        try {
            URL u = new URL(url);
            HttpURLConnection connect = (HttpURLConnection) u.openConnection();
            connect.setReadTimeout(0);
            int response = connect.getResponseCode();

            if (response == HttpURLConnection.HTTP_OK) {
                try (InputStream inputStream = connect.getInputStream()) {
                    try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {

                        long initTime = System.currentTimeMillis();
                        byte buffer[] = new byte[1024];
                        int bytes;

                        while ((bytes = inputStream.read(buffer))>=0) {
                            int currentSpeed;

                            long now = System.currentTimeMillis();
                            totalBytesRead += bytes;
                            bytesRead += bytes;
                            currentSpeed = Math.round(bytesRead / ((float) (now - initTime ) / 1000));

                            if (speedLimit > 0 && currentSpeed > speedLimit) {
                                int timeSleep = Math.round((float) (currentSpeed - speedLimit) / speedLimit * (now - initTime));
                                try {
                                    Thread.sleep(timeSleep);
                                } catch (InterruptedException i){
                                    i.printStackTrace();
                                }
                            }
                            byteArrayOutputStream.write(buffer,0,bytes);
                        }
                        for (String fileName : namesTosave) {
                            saveFiles(byteArrayOutputStream,fileName);
                        }
                    }
                }
            }
        } catch (IOException i) {
            System.err.println("Ошибка загрузки файла. "+ url);
        }
        elapsedTime =(int)(System.currentTimeMillis()- Launch.getTime())/1000;
    }

    private void saveFiles (ByteArrayOutputStream b, String fileName){
            File file = new File(fileName);
        try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
            b.writeTo(fileOutputStream);
            System.out.println("Файл "+file.getName()+" загружен.");
        } catch (Exception e) {
            System.out.println("Ошибка при записи скачанного файла " + fileName);
        }
    }
}