import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.*;

public class Emulator {
    private static Robot robot;
    private static int sleep = 30;

    static {
        try {
            robot = new Robot();
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    private static void keyPress(String string) throws AWTException, InterruptedException {
        Robot robot = new Robot();
        if (string != null) {
            string = string.toLowerCase();

            for (int i = 0; i < string.length(); i++) {
                switch (string.charAt(i)) {
                    case '0':
                        robot.keyPress(KeyEvent.VK_0);
                        break;
                    case '1':
                        robot.keyPress(KeyEvent.VK_1);
                        break;
                    case '2':
                        robot.keyPress(KeyEvent.VK_2);
                        break;
                    case '3':
                        robot.keyPress(KeyEvent.VK_3);
                        break;
                    case '4':
                        robot.keyPress(KeyEvent.VK_4);
                        break;
                    case '5':
                        robot.keyPress(KeyEvent.VK_5);
                        break;
                    case '6':
                        robot.keyPress(KeyEvent.VK_6);
                        break;
                    case '7':
                        robot.keyPress(KeyEvent.VK_7);
                        break;
                    case '8':
                        robot.keyPress(KeyEvent.VK_8);
                        break;
                    case '9':
                        robot.keyPress(KeyEvent.VK_9);
                        break;
                    case 'a':
                        robot.keyPress(KeyEvent.VK_A);
                        break;
                    case 'b':
                        robot.keyPress(KeyEvent.VK_B);
                        break;
                    case 'c':
                        robot.keyPress(KeyEvent.VK_C);
                        break;
                    case 'd':
                        robot.keyPress(KeyEvent.VK_D);
                        break;
                    case 'e':
                        robot.keyPress(KeyEvent.VK_E);
                        break;
                    case 'f':
                        robot.keyPress(KeyEvent.VK_F);
                        break;

                    default:
                        break;
                }
                Thread.sleep(sleep);
            }
        }
    }

    static String[] readFile(File file) throws IOException {
        String splitter = "#";
        FileInputStream fileInputStream = new FileInputStream(file);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));

        StringBuilder stringBuilder = new StringBuilder();
        String tmp;
        while ((tmp = bufferedReader.readLine()) != null) {
            stringBuilder.append(tmp);
            stringBuilder.append(splitter);
        }

        return stringBuilder.toString().split(splitter);
    }

    private static void toAddr(int addr) throws InterruptedException, AWTException {
        robot.keyPress(KeyEvent.VK_F1);
        Thread.sleep(sleep);
        keyPress(String.valueOf(addr));
    }

    private static void setAddr(int addr, String value) throws InterruptedException, AWTException {
        robot.keyPress(KeyEvent.VK_F1);
        Thread.sleep(sleep);
        keyPress(String.valueOf(addr));
        robot.keyPress(KeyEvent.VK_F2);
        Thread.sleep(sleep);
        keyPress(value);
        robot.keyPress(KeyEvent.VK_SHIFT);
    }

    private static void setReg(String reg, String value) throws InterruptedException, AWTException {
        robot.keyPress(KeyEvent.VK_CONTROL);
        Thread.sleep(sleep);
        keyPress(reg);
        keyPress(value);
    }

    private static void setProgram(String[] string, int startAddr) throws AWTException, InterruptedException {
        Thread.sleep(sleep);

        toAddr(startAddr);

        robot.keyPress(KeyEvent.VK_F2);
        Thread.sleep(sleep);

        for (String aString : string) {
            keyPress(aString);
            robot.keyPress(KeyEvent.VK_SHIFT);
            Thread.sleep(sleep);
        }

        toAddr(startAddr);
    }

    public static void main(String[] args) throws IOException, InterruptedException, AWTException {
        //new File("F:\\BULAT\\#Projects\\Java\\Emulator\\Program.txt");
        File programFile = new File("Program.txt");


        String[] program = readFile(programFile);
        String[] script = new String[program.length];
        int startAddr = 8200;
        int n = 0;

        //Delete comments
        for (int i = 0; i < program.length; i++) {
            if (program[i] != null) {
                if (program[i].contains("-")) {
                    program[i] = null;
                }
            }
        }

        //Start addr
        for (int i = 0; i < program.length; i++) {
            if (program[i] != null) {
                if (program[i].toUpperCase().split(" ")[0].equals("START")) {
                    startAddr = Integer.parseInt(program[i].toUpperCase().split(" ")[1]);
                    program[i] = null;
                }
            }
        }

        //Set scripts
        for (int i = 0; i < program.length; i++) {
            if (program[i] != null) {
                if (program[i].toUpperCase().split(" ")[0].equals("ADDR") ||
                        program[i].toUpperCase().split(" ")[0].equals("REG")) {
                    script[n] = program[i].toUpperCase();
                    program[i] = null;
                    n++;
                }
            }
        }

        program = Interpreter.Translator(program);

        Thread.sleep(3000);
        setProgram(program, startAddr);

        //Release Scripts
        for (String aScript : script) {
            if (aScript != null) {
                if (aScript.split(" ")[0].equals("REG")) {
                    setReg(aScript.split(" ")[1], aScript.split(" ")[2]);
                }
                if (aScript.split(" ")[0].equals("ADDR")) {
                    setAddr(Integer.parseInt(aScript.split(" ")[1]), aScript.split(" ")[2]);
                }
            }
        }

        toAddr(startAddr);

        for (String aProgram : program) {
            System.out.println(aProgram);
        }

        robot.keyRelease(KeyEvent.VK_SHIFT);
        robot.keyRelease(KeyEvent.VK_CONTROL);
    }
}
