import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by Makarovaa on 06.02.18.
 */
public class Downloader implements Runnable {

    private String url;
    private HashSet<String> namesTosave;

    public Downloader(String url, HashSet<String> namesTosave) {
        this.url = url;
        this.namesTosave = namesTosave;
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

                        byte buffer[] = new byte[1024];
                        int bytes = 0;

                        while (inputStream.available()>0){
                            bytes = inputStream.read(buffer);

                        }


                        for (String fileName : namesTosave) {

                        }


                    }
                }
                }


            } catch (IOException i) {
            System.err.println("Ошибка чтения/записи.");
        }
    }
}