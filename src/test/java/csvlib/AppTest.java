package csvlib;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Unit test for simple App.
 */
public class AppTest {

    @Test
    public void shouldAnswerWithTrue() {
        assertTrue(true);
    }

    @Test
    public void brokedWhitescaped() {
        URL url = this.getClass().getResource("/" + "test1.csv");
        File file = new File(url.getFile());
        CSVReader reader =new CSVReader.Builder(new File("F:\\test1.csv"))
                .strictSeparator(true)
                .deleteWhitespaces(true)
                .setRawMode(false)
                .build();
        try {
            String[] arr1 = new String[]{"test", "\"t\"", "\"t\""};

            List<String> line1 = reader.getRow();
            List<String> line2 = reader.getRow();
            for(String s : line1){
                System.out.println(s);
            }

            Assert.assertArrayEquals(line1.toArray(new String[line1.size()]), arr1);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
