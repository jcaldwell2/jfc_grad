import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
/**
 * @Author James Caldwell
 * @Version 11/09/2016
 * ITEC 541 Java Algebra Project
 *
 * ttable Class creates a new ttable from file or by String[][]
 */

/**
 * The type ttable.
 */
public class ttable {

    private File file;
    /**
     * The Table.
     */
    public final String[][] table;
    /**
     * The Parser.
     */
    public Scanner parser;

    /**
     * Instantiates a new ttable from file.
     *
     * @param _file takes in a file and creates a table in memory
     */
    public ttable(String _file) {

        this.file = new File("src/" + _file);

        table = new String[getRowNumFromFile()][getColumnNum()];
        populateTableFromFile();

    }

    /**
     * Instantiates a new ttable from a 2d Array.
     *
     * @param _table the table takes in a String[][] and returns a ttable
     */
    public ttable(String[][] _table) {

        this.table = _table;
    }


    private void populateTableFromFile() {

        try {

            int row = 0;

            Scanner scanner = new Scanner(file);

            // scanner.useDelimiter("~");
            while (scanner.hasNextLine()) {

                int col = 0;
                String line = scanner.nextLine();
                //  System.out.println(line);

                parser = new Scanner(line);
                parser.useDelimiter("~ *");
                while (parser.hasNext()) {

                    table[row][col] = parser.next();
                    col += 1;
                }

                row += 1;
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    /**
     * Gets column number from file.
     *
     * @return the column number
     */
    public int getColumnNum() {

        int count = 0;

        try {

            Scanner scanner = new Scanner(file);

            String line = scanner.nextLine();

            parser = new Scanner(line);
            parser.useDelimiter("~");
            while (parser.hasNext()) {
                parser.next();
                count += 1;

            }
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return count;
    }

    /**
     * Gets row numbers from file.
     *
     * @return the row number from file
     */
    public int getRowNumFromFile() {

        int count = 0;

        try {

            Scanner scanner = new Scanner(file);


            while (scanner.hasNextLine()) {


                String line = scanner.nextLine();

                //Must check for empty lines in the file
                if (line.length() > 0) {
                    count += 1;
                }


            }
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        return count;
    }

    /**
     * Gets amount of rows in the table by the length.
     *
     * @return the row number
     */
    public int getRowNum() {

        return table.length;
    }


    /**
     * Gets the length of the table as the amount of columns.
     *
     * @return the number of columns
     */
    public int getCol() {

        return this.table[0].length;
    }

    /**
     * Gets the total amount of columns from the parsed column string.
     *
     * @param column the column
     * @return the col num
     */
    public int getColNum(String column) {

        for (int k = 0; k < table[0].length; k++) {
            if (table[0][k].equalsIgnoreCase(column)) {
                return k;
            }
        }
        return -1;
    }

    /**
     * Return an array of columns from the table.
     *
     * @return the string [ ]
     */
    public String[] getColArray() {

        String[] returnArray = new String[table[0].length];

        for (int i = 0; i < table[0].length; i++) {
            returnArray[i] = table[0][i];
        }

        return returnArray;

    }

}
