import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Test {
    private boolean checkProcess(String CmdProcess) {
        StringBuilder fullprocess = new StringBuilder();
        boolean inputcmd;
        try {
            Process p = Runtime.getRuntime().exec(System.getenv("windir") + "\\system32\\" + "tasklist.exe");
            BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            while ((line = input.readLine()) != null) {
                fullprocess.append(line).append("\n");
            }
            input.close();
        } catch (IOException ignored) {
        }
        String process = fullprocess.toString();
        inputcmd = process.contains(CmdProcess);
        return inputcmd;
    }

    public static void main(String[] args) {
        System.out.println(new Test().checkProcess("idea64"));
    }
}