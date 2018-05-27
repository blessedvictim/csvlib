import csvlib.CSVReader;

import java.io.File;
import java.io.IOException;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {
        CSVReader reader = new CSVReader.Builder(new File("F:\\example1.csv"))
                .strictSeparator(false)
                .deleteWhitespaces(true)
                .setRawMode(false)
                .build();
        try {
            String s;
            int i = 0;
            while (reader.hasNextRow() && i < 3) {
                System.out.print(++i + ":");
                for (String str : reader.getRow()) {
                    System.out.println(str);
                }
                System.out.println("---new line---");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
