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
    public void brokedWhitescapes() {
        URL url = this.getClass().getResource("/" + "test1.csv");
        File file = new File(url.getFile());
        CSVReader reader = null;
        try {
            reader = new CSVReader.Builder(file)
                    .strictSeparator(true)
                    .deleteWhitespaces(true)
                    .setRawMode(false)
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            String[] arr1 = new String[]{"test", "t", "t"};
            String[] arr2 = new String[]{"test", "3245", "345"};
            List<String> line1 = reader.getRow();
            List<String> line2 = reader.getRow();

            Assert.assertArrayEquals(line1.toArray(new String[line1.size()]), arr1);
            Assert.assertArrayEquals(line2.toArray(new String[line2.size()]), arr2);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void LFdelimiterTest() throws Exception {
        URL url = this.getClass().getResource("/" + "test2.csv");
        File file = new File(url.getFile());
        CSVReader reader1 =new CSVReader.Builder(file)
                .strictSeparator(true)
                .deleteWhitespaces(true)
                .setRawMode(false)
                .build();
        CSVReader reader2 =new CSVReader.Builder(file)
                .strictSeparator(false)
                .deleteWhitespaces(true)
                .setRawMode(false)
                .build();
        try {
            String[] arr1 = new String[]{"test2", "test2test2","test2"};
            String[] arr2 = new String[]{"test2", "test2"};
            List<String> line1 = reader1.getRow();
            List<String> line2 = reader2.getRow();

            Assert.assertArrayEquals(line1.toArray(new String[line1.size()]), arr1);
            Assert.assertArrayEquals(line2.toArray(new String[line2.size()]), arr2);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void deleteEscapedQuotes() throws Exception {
        URL url = this.getClass().getResource("/" + "test3.csv");
        File file = new File(url.getFile());
        CSVReader reader1 =new CSVReader.Builder(file)
                .strictSeparator(true)
                .deleteWhitespaces(true)
                .setRawMode(false)
                .build();

        try {


            String[] arr1 = new String[]{"test3", "tes\"\"t3"};
            String[] arr2 = new String[]{"test3", "tes\"t3"};
            List<String> line1 = reader1.getRow();
            List<String> line2 = reader1.getRow();

            Assert.assertArrayEquals(line1.toArray(new String[line1.size()]), arr1);
            Assert.assertArrayEquals(line2.toArray(new String[line2.size()]), arr2);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testRawMode() throws Exception {
        URL url = this.getClass().getResource("/" + "testraw.csv");
        File file = new File(url.getFile());
        CSVReader reader1 =new CSVReader.Builder(file)
                .strictSeparator(false)
                .setRawMode(true)
                .build();

        try {


            String[] arr1 = new String[]{"test raw  ", " \" test\" test"};
            String[] arr2 = new String[]{"\"test3\"", "  \"test\""};
            List<String> line1 = reader1.getRow();
            List<String> line2 = reader1.getRow();

            Assert.assertArrayEquals(line1.toArray(new String[line1.size()]), arr1);
            Assert.assertArrayEquals(line2.toArray(new String[line2.size()]), arr2);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
