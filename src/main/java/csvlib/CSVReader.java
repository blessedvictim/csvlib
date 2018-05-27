package csvlib;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CSVReader {

    static public class Builder{
        boolean deletePreFieldWhitescapes=true;
        boolean rawMode=false;
        boolean strictLineDelim=true;
        File file;

        public Builder(File file){
            this.file=file;
        }

        public Builder deleteWhitespaces(boolean deletePreFieldWhitescapes) {
            this.deletePreFieldWhitescapes = deletePreFieldWhitescapes;
            return this;
        }

        public Builder strictSeparator(boolean strictLineDelim) {
            this.strictLineDelim = strictLineDelim;
            return this;
        }



        public Builder setRawMode(boolean rawMode) {
            if(rawMode)deletePreFieldWhitescapes=false;
            this.rawMode = rawMode;
            return this;
        }

        public CSVReader build() throws Exception {
            return new CSVReader(this);
        }

    }

    PushbackReader reader;
    private final char C_QUOTE = '"';
    private final char C_CR = '\r';
    private final char C_LF = '\n';
    private final char C_COMMA = ',';
    private final char C_WHITESPACE = ' ';
    List<String> currentRow = null;

    // default values
    private boolean deletePreFieldWhitescapes=true;
    private boolean rawMode=false;
    private boolean strictLineDelim=true;


    private boolean hasCached = false;



    /**
     * Return count of readed records(lines)
     *
     * @return cout of lines
     */
    public int getCount() {
        return linenum;
    }

    private int linenum = 0;


    private CSVReader(Builder builder)throws Exception {
        this.rawMode=builder.rawMode;
        this.deletePreFieldWhitescapes=builder.deletePreFieldWhitescapes;
        this.strictLineDelim = builder.strictLineDelim;

            this.reader = new PushbackReader(new InputStreamReader(new FileInputStream(builder.file), "utf-8"));

    }

    /**
     * reads the string until find the line separator
     * in strict mode line separator maybe only CRLF \r\n
     * in non-strict mode line separator maybe also LF \n
     * @return
     * @throws IOException
     */
    String readLine() throws IOException {
        StringBuilder builder = new StringBuilder();
        int c;
        boolean quotedRun = false;
        while ((c = reader.read()) != -1) {
            //System.out.println(Integer.toHexString(c));
            if (c == C_CR) {
                if ((c = reader.read()) == C_LF) {
                    if (quotedRun) continue;
                    linenum++;
                    return builder.toString();
                } else reader.unread(c);
                continue;
            }
            if (c == C_LF) {
                if (!strictLineDelim) {
                    linenum++;
                    return builder.toString();
                }
                continue;
            }
            if (c == C_QUOTE) {
                quotedRun = !quotedRun;
            }
            builder.append((char) c);
        }
        if (builder.length() == 0) return null;
        else return builder.toString();
    }

    /**
     * Divides the line into fields
     *
     * @param line
     * @return list of fields
     */
    List<String> parseLine(String line) {
        if (line == null) return null;
        try (
                StringReader reader1 = new StringReader(line);
                PushbackReader reader = new PushbackReader(reader1)) {
            boolean quoteRun = false;
            StringBuilder builder = new StringBuilder();
            List<String> list = new ArrayList<>();
            int c;
            boolean prevQuote = false;
            while ((c = reader.read()) != -1) {
                //System.out.println((char)(c));
                //System.out.println(builder.toString());

                if (c == C_QUOTE) {
                    quoteRun = !quoteRun;
                }

                if (c == C_COMMA) {
                    if (quoteRun) {
                        builder.append((char) c);
                        continue;
                    } else {
                        list.add(builder.toString());
                        builder.setLength(0);
                        continue;
                    }
                }

                builder.append((char) c);
            }

            list.add(builder.toString());
            if(rawMode){
                return list;
            }else{
                if(deletePreFieldWhitescapes)list = list.stream().map((s)->s.trim()).collect(Collectors.toList());
                return list.stream().map((s) -> deleteEscapedQuote(s)).takeWhile((s) -> s != null).collect(Collectors.toList());
            }

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * @param str string which must be clered from first and last quote and escaped quotes
     * @return raw string from quoted field
     */
    private String deleteEscapedQuote(String str) {
        //System.out.println(str);
        if (str == null || str.isEmpty()) return null;
        StringBuilder builder = new StringBuilder(str);
        if (builder.charAt(0) == '"' && builder.charAt(builder.length() - 1) == '"') {
            builder.deleteCharAt(0);
            builder.deleteCharAt(builder.length() - 1);
            int i;
            while ((i = builder.lastIndexOf("\"\"")) != -1) {
                builder.deleteCharAt(i);
            }
        }
        return builder.toString();
    }

    /*private String deleteDelimQuotes(String str){

    } */

    public List<String> getRow() throws IOException {
        if (hasCached) {
            hasCached = false;
            return this.currentRow;
        } else {
            String line = readLine();
            return parseLine(line);
        }
    }

    public boolean hasNextRow() {
        if (hasCached) return true;
        else {
            try {
                this.currentRow = getRow();
                if (this.currentRow != null) {
                    this.hasCached = true;
                    return true;
                } else {
                    hasCached = false;
                    return false;
                }
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
    }
}