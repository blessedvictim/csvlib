package csvlib;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CSVWriter {
    private final char C_QUOTE = '"';
    private final char C_CR = '\r';
    private final char C_LF = '\n';
    private final char C_COMMA = ',';

    private boolean deleteEscaped;

    private boolean strictLineDelim;
    FileWriter writer;

    public CSVWriter(File file, boolean deleteEscapedQuotes, boolean strictLineDelimiter) {
        this.deleteEscaped = deleteEscapedQuotes;
        this.strictLineDelim = strictLineDelimiter;
        try {
            this.writer = new FileWriter(file);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeRow(List<String> row) {
        for (int i = 0; i < row.size(); i++) {
            try {
                if (i < row.size() - 1) {
                    writer.write(processString(row.get(i)));
                    writer.write(C_COMMA);
                } else {
                    writer.write(processString(row.get(i)));
                    writer.write(C_CR);
                    writer.write(C_LF);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String processString(String s) {
        StringBuilder builder = new StringBuilder(s);
        int i;
        while ( (i=builder.indexOf("\"\"")) != -1) {
            builder.deleteCharAt(i);
        }
        builder.insert(0,'"');
        builder.insert(builder.length()-1,'"');
        return builder.toString();
    }
}
