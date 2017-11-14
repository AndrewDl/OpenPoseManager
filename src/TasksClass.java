import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Laimi on 13.11.2017.
 */
public class TasksClass {
    private static final String TASKLIST = "tasklist";
    private static final String KILL = "taskkill /F /IM";
    /**
     * This method searching given task in windows tasklist
     * @param serviceName
     * @return
     * @throws Exception
     */
    public static boolean isProcessRunning(String serviceName)throws Exception{
        Process p = Runtime.getRuntime().exec(TASKLIST);
        BufferedReader reader = new BufferedReader( new InputStreamReader(
                p.getInputStream()));
        String line;
        while ((line = reader.readLine())!=null){
            // System.out.println(line); //
            if(line.contains(serviceName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * method starts process using CMDLine
     */
    public void startTask(String cmdLine)throws Exception{
        Runtime rt = Runtime.getRuntime();
       // Process pr =
        rt.exec(cmdLine);
    }

    /**
     * I guess programm need more permissions to kill other tasks
     */
//    private static void killProcess(String serviceName) throws Exception {
//        Runtime.getRuntime().exec(KILL + serviceName);
//    }


}
