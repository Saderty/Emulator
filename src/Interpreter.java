import java.io.File;
import java.io.IOException;

class Interpreter {
    private static String[][] commandMatrix = new String[16][16];

    private static String Translator(String string) throws IOException {
        if (string != null) {
            Matrix();

            string = string.toUpperCase();
            String[] tmp = string.split(" ");

            String arg0 = "";
            String arg1 = "";

            switch (tmp[0]) {
                case "MVI":
                    arg0 = tmp[0] + " " + tmp[1];
                    arg1 = tmp[2];
                    break;
                case "LDA":
                    arg0 = tmp[0];
                    arg1 = tmp[1].substring(2, 4) + " "
                            + tmp[1].substring(0, 2);
                    break;
                case "LXI":
                    arg0 = tmp[0] + " " + tmp[1];
                    arg1 = tmp[2].substring(2, 4) + " "
                            + tmp[2].substring(0, 2);
                    break;
                case "JZ":
                    arg0 = tmp[0];
                    arg1 = tmp[1].substring(2, 4) + " "
                            + tmp[1].substring(0, 2);
                    break;
                case "JMP":
                    arg0 = tmp[0];
                    arg1 = tmp[1].substring(2, 4) + " "
                            + tmp[1].substring(0, 2);
                    break;
                case "ADDR":
                    break;
                case "REG":
                    break;
                default:
                    arg0 = string;
                    break;
            }

            for (int i = 0; i < 15; i++) {
                for (int j = 0; j < 15; j++) {
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
                } else if(aResult.split(" ").length > 0){
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

    public static void main(String... args) throws IOException {
        System.out.println(Translator("end"));
    }
}
