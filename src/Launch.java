import java.io.File;

public class Launch {

    private static long time;

    public static long getTime() {
        return time;
    }

    public static void main(String[] args)  {
        System.setProperty("java.net.useSystemProxies", "true");
         int numberOfThreads = 0;
         int speedLimit = 0 ;
         String pathToFile = "";
         String outputFolder = "";

        if (args.length>1){

           for (int i=0;i<args.length;i++){
               String arg = args[i];
                try {
                    if (arg.charAt(0) == '-'){
                        switch (arg.charAt(1)){
                            case 'n':
                                if (Integer.parseInt(args[i+1])<=0){
                                    numberOfThreads = 1;
                                } else numberOfThreads = Integer.parseInt(args[i+1]);

                                break;
                            case 'l':
                                speedLimit = parseSpeedLimit(args[i+1]);
                                break;
                            case 'f':
                                pathToFile = args[i+1];
                                break;
                            case 'o':
                                outputFolder = args[i+1];
                                break;
                            default:
                                break;
                        }
                    }
                }
                catch (IllegalArgumentException e){
                    System.err.println("Указаны неверные параметры.");
                    return;
                }
           }
        } else {
            System.out.println("Не указаны необходимые параметры.");
            return;
        }
        File InputFile = new File(pathToFile);
        File OutputFile = new File(outputFolder);
        if (!InputFile.isFile()){
            System.out.println("Неправильно указан путь к файлу со ссылками.");
            return;
        }
        if (!OutputFile.isDirectory()){
            System.out.println("Неправильно указан путь к папке для загруженных файлов.");
            return;
        }
        time = System.currentTimeMillis();
        new Manager(numberOfThreads,speedLimit,pathToFile,outputFolder).startDownload();
    }

    private static int parseSpeedLimit (String s){
        int result = 0;
        if (s!=null){
            switch (s.charAt(s.length()-1)){
                case 'k':
                    result = 1024*Integer.parseInt(s.substring(0,s.length()-1));
                    break;
                case 'm':
                    result = 1024*1024*Integer.parseInt(s.substring(0,s.length()-1));
                    break;
                default:
                    result = Integer.parseInt(s);
                    break;
            }
        }
        return result;
    }
}


