import java.io.File;
import java.io.IOException;

class Interpreter {
     static String[][] commandMatrix = new String[16][16];

    private static String[] commandsList00 = {"ADDR", "REG"};
    private static String[] commandsList10 = {"ADI", "ACI", "ANI", "CPI", "ORI", "SUI", "SBI", "XRI"};
    private static String[] commandsList11 = {"MVI"};
    private static String[] commandsList20 =
            {"CALL", "CZ", "CNZ", "CP", "CM", "CC", "CNC", "CPE", "CPO", "JMP", "JZ", "JNZ", "JP",
                    "JM", "JC", "JNC", "JPE", "JPO", "LDA", "LHLD", "SHLD", "STA"};
    private static String[] commandsList21 = {"LXI"};

     static String Translator(String string) throws IOException {
        if (string != null) {
            Matrix();

            string = string.toUpperCase();
            String[] tmp = string.split(" ");

            String arg0;
            String arg1 = "";

            //scripts commands
            if (arrayContains(tmp[0], commandsList00)) {
                arg0 = "";
            }
            //d8
            else if (arrayContains(tmp[0], commandsList10)) {
                arg0 = tmp[0];
                arg1 = tmp[1];
            }
            //reg <- d8
            else if (arrayContains(tmp[0], commandsList11)) {
                arg0 = tmp[0] + " " + tmp[1];
                arg1 = tmp[2];
            }
            //reg <- a16
            else if (arrayContains(tmp[0], commandsList20)) {
                arg0 = tmp[0];
                arg1 = tmp[1].substring(2, 4) + " " + tmp[1].substring(0, 2);
            }
            //reg <- d16
            else if (arrayContains(tmp[0], commandsList21)) {
                arg0 = tmp[0] + " " + tmp[1];
                arg1 = tmp[2].substring(2, 4) + " " + tmp[2].substring(0, 2);
            } else {
                arg0 = string;
            }

            for (int i = 0; i < 16; i++) {
                for (int j = 0; j < 16; j++) {
                    if (commandMatrix[i][j] != null) {
                        if (commandMatrix[i][j].equals(arg0)) {
                            arg0 = Integer.toHexString(j) + Integer.toHexString(i);
                            break;
                        }
                    }
                }
            }

            return arg0 + " " + arg1;
        }
        return null;
    }

    static String[] Translator(String[] strings) throws IOException {
        String splitter = "#";
        String[] result = new String[strings.length];

        for (int i = 0; i < strings.length; i++) {
            result[i] = Translator(strings[i]);
        }

        StringBuilder stringBuilder = new StringBuilder();
        for (String aResult : result) {
            if (aResult != null) {
                if (aResult.split(" ").length > 2) {
                    stringBuilder.append(aResult.split(" ")[0]);
                    stringBuilder.append(splitter);
                    stringBuilder.append(aResult.split(" ")[1]);
                    stringBuilder.append(splitter);
                    stringBuilder.append(aResult.split(" ")[2]);
                    stringBuilder.append(splitter);
                } else if (aResult.split(" ").length > 1) {
                    stringBuilder.append(aResult.split(" ")[0]);
                    stringBuilder.append(splitter);
                    stringBuilder.append(aResult.split(" ")[1]);
                    stringBuilder.append(splitter);
                } else if (aResult.split(" ").length > 0) {
                    stringBuilder.append(aResult.split(" ")[0]);
                    stringBuilder.append(splitter);
                }
            }
        }

        return stringBuilder.toString().split(splitter);
    }

    private static void Matrix() throws IOException {
        /*
        setMatrix(new String[]{"MVI A", "3E"});
        setMatrix(new String[]{"MVI C", "0E"});
        setMatrix(new String[]{"MVI D", "16"});
        setMatrix(new String[]{"ADD C", "81"});
        setMatrix(new String[]{"ADD D", "82"});
        setMatrix(new String[]{"END", "E7"});
        setMatrix(new String[]{"LDA", "3A"});
        setMatrix(new String[]{"MOV B A", "47"});
        setMatrix(new String[]{"SUB A", "97"});
        setMatrix(new String[]{"LXI", "21"});
        setMatrix(new String[]{"ADD M", "86"});
        setMatrix(new String[]{"DCR B", "05"});
        setMatrix(new String[]{"JZ", "CA"});
        setMatrix(new String[]{"INX H", "23"});
        setMatrix(new String[]{"JMP", "C3"});
        setMatrix(new String[]{"MVI B", "06"});
        */
        //                    setMatrix(new String[]{"",""});

        //File commandFile = new File("F:\\BULAT\\#Projects\\Java\\Emulator\\Command.txt");
        File commandFile = new File("Command.txt");
        String[] command = Emulator.readFile(commandFile);

        for (String aCommand : command) {
            String[] commands = aCommand.split(" ");
            if (commands.length > 3) {
                setMatrix(new String[]{commands[0] + " " + commands[1] + " " + commands[2], commands[3]});
            } else if (commands.length > 2) {
                setMatrix(new String[]{commands[0] + " " + commands[1], commands[2]});
            } else {
                setMatrix(new String[]{commands[0], commands[1]});
            }
        }
    }

    private static void setMatrix(String[] strings) {
        commandMatrix[Integer.parseInt(strings[1].split("")[1], 16)]
                [Integer.parseInt(strings[1].split("")[0], 16)] = strings[0];
    }

    private static boolean arrayContains(String value, String[] strings) {
        for (String string : strings) {
            if (string.equals(value)) {
                return true;
            }
        }
        return false;
    }

    public static void main(String... args) throws IOException {
        System.out.println(Translator("end"));
    }
}
