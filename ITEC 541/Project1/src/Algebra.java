import java.util.Scanner;

/**
 * @Author James Caldwell
 * @Version 11 /09/2016 ITEC 541 Java Algebra Project
 *
 * Algebra Class contains the Algebra operations to perform operations on ttables
 */
public class Algebra {


    /**
     * Restrict the table by a string.
     *
     * @param table the table to restrict
     * @param string the string contains the restriction
     * @return the ttable
     */
    public static ttable Restrict(ttable table, String string) {

        String column = getColumn(string);
        String restriction = getRestriction(string);
        String operation = getOperator(string);

        switch (operation) {
            case "=":
                return new ttable(equalsTable(table, column, restriction));

            case ">":
                return new ttable(compareTable(table, column, restriction, operation));

            case "<":
                return new ttable(compareTable(table, column, restriction, operation));

            case ">=":
                return new ttable(compareTable(table, column, restriction, operation));

            case "<=":
                return new ttable(compareTable(table, column, restriction, operation));

            case "!=":
                return new ttable(notEqualsTable(table, column, restriction));

            default:
                System.out.println("Invalid operation!");
        }

        return table;
    }


    /**
     * Project the specified columns in a string of a table.
     *
     * @param _table   the table to project from
     * @param _columns the columns to project
     * @return the ttable
     */
    public static ttable Project(ttable _table, String _columns) {

        int numColumns = getNumCols(_columns);
        int numRows = _table.getRowNum();
        int count = 0;


        String[] columnArray = populateColumns(numColumns, _columns);
        // String[] testArray = {"Price","Make","Model"};

        String[][] returnTable = new String[numRows][numColumns];


        int i = 0;
        while (i < columnArray.length) {
            for (int l = 0; l < _table.table[0].length; l++) {

                if (columnArray[i].equalsIgnoreCase(_table.table[0][l])) {

                    for (int j = 0; j < _table.table.length; j++) {

                        returnTable[j][count] = _table.table[j][l];
                    }
                    count += 1;
                }
            }
            i++;
        }
        return new ttable(returnTable);
    }

    /**
     * Display the contents of the table formatted
     *
     * @param table the table
     */
    public static void Display(ttable table) {

        String[][] temp = table.table;
        String columns = "";

        for (int l = 0; l < temp[0].length; l++) {

            columns = temp[0][l];
            System.out.printf("%-16s", columns);
        }
        System.out.println();

        for (int k = 0; k < temp[0].length; k++)
            System.out.print("--------------- ");

        for (int i = 1; i < temp.length; i++) {

            System.out.println(" ");

            for (int j = 0; j < temp[0].length; j++) {

                System.out.printf("%-16s", temp[i][j]);
            }

        }

        System.out.println("\n");
    }

    /**
     * Join two tables together.
     *
     * @param _first  the first table
     * @param _second the second table
     * @return the ttable
     */
    public static ttable Join(ttable _first, ttable _second) {

        String[][] joinArray = new String[getNumRows(_first, _second)][getNumCols(_first.getColArray(),
                _second.getColArray())];

        String[] commonCol = getCommonCol(_first, _second);


        return combineTable(_first, _second, commonCol, joinArray);
    }

    /**
     * Perform that actual combing of two tables based on a common column
     * @param _firstTbl the first table of the join
     * @param _secTbl the second table of the join
     * @param _comCol contains the common column, but is setup if there are more than one column, but currently only
     *                works with 1 common column
     * @param _newArray is the new array of the two tables
     * @return
     */
    private static ttable combineTable(ttable _firstTbl, ttable _secTbl, String[] _comCol, String[][] _newArray) {

        //Copy Largest Table array to new array first
        if (_firstTbl.table.length > _secTbl.table.length) {

            //Copy Array 1 to the return array
            for (int i = 0; i < _firstTbl.table.length; i++) {

                System.arraycopy(_firstTbl.table[i], 0, _newArray[i], 0, _firstTbl.table[0].length);
            }

            //Copy rest of columns to be joined starting at firstTable.length

            int index = 0;
            for (int j = _firstTbl.table[0].length - 1; j < _newArray[0].length; j++) {

                //commonCols is set to index 0 to work with only 1 matching column for now
                //Skip copying where you find the common column
                if (_comCol[0].equalsIgnoreCase(_secTbl.table[0][index])) {
                    //do nothing if a match is found
                } else {
                    _newArray[0][j] = _secTbl.table[0][index];
                }

                index += 1;
            }


            //Get the index of the join column from the first table in order to make joins
            int comIndexT1 = getComColIndex(_firstTbl, _comCol);
            index = _firstTbl.table[0].length;

            //Copy second table to matching join column to rest of the return table
            //For now assumes the matching column from second table is the first column


            for (int k = 1; k < _newArray.length; k++) {
                for (int n = 1; n < _secTbl.table.length; n++) {

                    if (_newArray[k][comIndexT1].equalsIgnoreCase(_secTbl.table[n][0])) {
                        System.arraycopy(_secTbl.table[n], 1, _newArray[k], index, _secTbl.table[0].length - 1);


                    }
                }
            }

        }


        if (_secTbl.table.length > _firstTbl.table.length) {

            //Copy Array 1 to the return array

            for (int i = 0; i < _secTbl.table.length; i++) {

                System.arraycopy(_secTbl.table[i], 0, _newArray[i], 0, _secTbl.table[0].length);
            }

            //Copy rest of columns to be joined starting at firstTable.length

            int index = 0;
            for (int j = _secTbl.table[0].length - 1; j < _newArray[0].length; j++) {

                //commonCols is set to index 0 to work with only 1 matching column for now
                //Skip copying where you find the common column
                if (_comCol[0].equalsIgnoreCase(_firstTbl.table[0][index])) {
                    //do nothing if a match is found
                } else {
                    _newArray[0][j] = _firstTbl.table[0][index];
                }

                index += 1;
            }


            //Get the index of the join column from the second table in order to make joins
            int comIndexT1 = getComColIndex(_secTbl, _comCol);
            index = _secTbl.table[0].length;

            //Copy first table to matching join column to rest of the return table
            //For now assumes the matching column from first table is the first column

            for (int k = 1; k < _newArray.length; k++) {
                for (int n = 1; n < _firstTbl.table.length; n++) {

                    if (_newArray[k][comIndexT1].equalsIgnoreCase(_firstTbl.table[n][0])) {
                        System.arraycopy(_firstTbl.table[n], 1, _newArray[k], index, _firstTbl.table[0].length - 1);


                    }
                }

            }

        }
        return new ttable(_newArray);
    }

    /**
     * uses the common column in the array and finds the column index of the join column from the table
     * @param table contains a matching column
     * @param commonCols contains what the matching column is
     * @return
     */
    private static int getComColIndex(ttable table, String[] commonCols) {

        int index = 0;
        for (int i = 0; i < table.table[0].length; i++) {
            if (table.table[0][i].equalsIgnoreCase(commonCols[0])) {
                index = i;
            }
        }

        return index;
    }

    /**
     * Return number of rows based on the largest of the join tables
     *
     * @param firstTable
     * @param secondTable
     * @return
     */
    private static int getNumRows(ttable firstTable, ttable secondTable) {

        int count = 0;

        if (firstTable.table.length > secondTable.table.length) {

            count = firstTable.table.length;
        }

        if (firstTable.table.length < secondTable.table.length) {

            count = secondTable.table.length;
        }
        return count;

    }

    /**
     * Determines what the common columns are. Will store 1 instance of each matching column, but currently the join
     * only works with 1 matching column
     *
     * @param firstTable
     * @param secondTable
     * @return
     */
    private static String[] getCommonCol(ttable firstTable, ttable secondTable) {

        int count = 0;

        for (int i = 0; i < firstTable.table[0].length; i++) {

            for (int j = 0; j < secondTable.table[0].length; j++) {
                if (firstTable.table[0][i].equalsIgnoreCase(secondTable.table[0][j])) {
                    count += 1;
                }
            }
        }

        String[] returnArray = new String[count];
        count = 0;

        for (int i = 0; i < firstTable.table[0].length; i++) {

            for (int j = 0; j < secondTable.table[0].length; j++) {
                if (firstTable.table[0][i].equalsIgnoreCase(secondTable.table[0][j])) {
                    returnArray[count] = firstTable.table[0][i];
                    count += 1;
                }

            }
        }

        return returnArray;
    }

    /**
     * Takes the determined number of columns and extracts those columns from a string into a String array
     * @param _numColumns the number of columns that should be extracted from the string
     * @param _columns the string containing the columns
     * @return
     */
    private static String[] populateColumns(int _numColumns, String _columns) {
        String[] temp = new String[_numColumns];

        int count = 0;

        Scanner parser = new Scanner(_columns);
        parser.useDelimiter(", *");

        while (parser.hasNext()) {

            temp[count] = parser.next();
            count += 1;


        }
        return temp;

    }

    /**
     * returns number of columns contained in a string
     *
     * @param _columns is a string containing columns
     * @return
     */
    private static int getNumCols(String _columns) {

        int count = 0;

        Scanner parser = new Scanner(_columns);
        parser.useDelimiter(", *");

        while (parser.hasNext()) {
            parser.next();
            count += 1;
        }
        return count;
    }



    /**
     * return the count of columns and only counts duplicates once
     * @param cols1 is a String array
     * @param cols2 is a String array
     * @return
     */
    private static int getNumCols(String[] cols1, String[] cols2) {

        int count = 0;

        for (int i = 0; i < cols1.length; i++) {

            for (int j = 0; j < cols2.length; j++) {

                if (cols1[i].equalsIgnoreCase(cols2[j])) {
                    count += 1;
                }
            }
        }
        return cols1.length + cols2.length - count;
    }


    /**
     * returns the column from a restriction string
     * @param _resString is a string containing a table restriction
     * @return
     */
    private static String getColumn(String _resString) {
        Scanner scanner = new Scanner(_resString);
        scanner.useDelimiter("=|>|<|>=|<=|!=|'");

        return scanner.next();
    }

    /**
     * returns the restriction from the restriction string
     * @param _resString is a string containg a table restriction
     * @return
     */
    private static String getRestriction(String _resString) {
        String returnString = "";
        Scanner scanner = new Scanner(_resString);
        scanner.useDelimiter("=|>|<|>=|<=|!=|'");
        scanner.next();

        while (scanner.hasNext()) {
            String temp = scanner.next();
            if (!temp.contains(" ")) {
                returnString = temp;
            }
        }
        return returnString;
    }

    /**
     * returns the operator of a restriction string
     * @param _resString is a string containg a table restriction
     * @return
     */
    private static String getOperator(String _resString) {
        return _resString.replaceAll("[^=<>!]", "");
    }

    /**
     * performs the equals operation in a restiction
     *
     * @param _table the table the operation is performed on
     * @param _column the column to the value is equal to
     * @param _restriction the value that is being restricted
     * @return
     */
    private static String[][] equalsTable(ttable _table, String _column, String _restriction) {
        int colNum = _table.getColNum(_column);
        int startRowNum = 1;

        String[][] returnArray = new String[getRows(_table, _column, _restriction)][_table.getCol()];

        System.arraycopy(_table.table[0], 0, returnArray[0], 0, _table.table[0].length);

        for (int j = 1; j < _table.table.length; j++) {
            if (_table.table[j][colNum].equalsIgnoreCase(_restriction)) {

                System.arraycopy(_table.table[j], 0, returnArray[startRowNum], 0, _table.table[0].length);

                startRowNum += 1;
            }
        }
        return returnArray;
    }


    /**
     * performs the not equals openration
     * @param _table the table the operation is performed on
     * @param _column the column to the value is not equal to
     * @param _restriction the value that is being restricted
     * @return
     */
    private static String[][] notEqualsTable(ttable _table, String _column, String _restriction) {
        int colNum = _table.getColNum(_column);
        int startRowNum = 1;

        String[][] returnArray = new String[getRows(_table, _column, _restriction, "!=")][_table.getCol()];

        System.arraycopy(_table.table[0], 0, returnArray[0], 0, _table.table[0].length);


        for (int j = 1; j < _table.table.length; j++) {



            if (!_table.table[j][colNum].equalsIgnoreCase(_restriction)) {


                System.arraycopy(_table.table[j], 0, returnArray[startRowNum], 0, _table.table[0].length);

                startRowNum += 1;
            }
        }
        return returnArray;
    }

    /**
     * compares tables based upon > < >= <=
     * @param _table the table the operation is performed on
     * @param _column the column to the value is equal to
     * @param _restriction the value that is being restricted
     * @param _operation is the operation being performed
     * @return
     */
    private static String[][] compareTable(ttable _table, String _column, String _restriction, String _operation) {

        int colNum = _table.getColNum(_column);
        int startRowNum = 1;

        String[][] returnArray = new String[getRows(_table, _column, _restriction, _operation)][_table.getCol()];

        // Load Columns
        System.arraycopy(_table.table[0], 0, returnArray[0], 0, _table.table[0].length);

        if (_operation.equals(">")) {
            for (int j = 1; j < _table.table.length; j++) {
                if (Integer.parseInt(_table.table[j][colNum]) > Integer.parseInt(_restriction)) {
                    System.arraycopy(_table.table[j], 0, returnArray[startRowNum], 0, _table.table[0].length);

                    startRowNum += 1;
                }
            }
        }

        if (_operation.equals("<")) {
            for (int j = 1; j < _table.table.length; j++) {
                if (Integer.parseInt(_table.table[j][colNum]) < Integer.parseInt(_restriction)) {
                    System.arraycopy(_table.table[j], 0, returnArray[startRowNum], 0, _table.table[0].length);

                    startRowNum += 1;
                }
            }
        }

        if (_operation.equals("<=")) {
            for (int j = 1; j < _table.table.length; j++) {
                if (Integer.parseInt(_table.table[j][colNum]) <= Integer.parseInt(_restriction)) {
                    System.arraycopy(_table.table[j], 0, returnArray[startRowNum], 0, _table.table[0].length);

                    startRowNum += 1;
                }
            }
        }

        if (_operation.equals(">=")) {
            for (int j = 1; j < _table.table.length; j++) {
                if (Integer.parseInt(_table.table[j][colNum]) >= Integer.parseInt(_restriction)) {
                    System.arraycopy(_table.table[j], 0, returnArray[startRowNum], 0, _table.table[0].length);

                    startRowNum += 1;
                }
            }
        }

        return returnArray;
    }


    /**
     * returns the number of rows for a new table array
     * @param table the table containing the rows
     * @param _column the column
     * @param _restriction the restriction
     * @return
     */
    private static int getRows(ttable table, String _column, String _restriction) {

        int colNum = table.getColNum(_column);
        int rowNum = 0;
        if (colNum < 0) {
            System.out.println("No column match was found");
            return -1;
        }

        for (int i = 1; i < table.table.length; i++) {
            if (table.table[i][colNum].equalsIgnoreCase(_restriction))
                rowNum += 1;
        }
        return rowNum + 1;  // Have to include the row for the columns as well with + 1
    }

    /**
     * returns the number of rows after a particular operation has occured i.e. > < >= <=
     * @param table the table containing the rows
     * @param _column the column in the restriction
     * @param _restriction the restriction
     * @param _operation the operation to be performed
     * @return
     */
    private static int getRows(ttable table, String _column, String _restriction, String _operation) {

        int colNum = table.getColNum(_column);
        int rowNum = 0;


        if (colNum < 0) {
            System.out.println("No column match was found");
            return -1;
        }


        if (_operation.equals(">")) {
            for (int i = 1; i < table.table.length; i++) {
                if (Integer.parseInt(table.table[i][colNum]) > Integer.parseInt(_restriction))
                    rowNum += 1;

            }
        }

        if (_operation.equals("<")) {
            for (int i = 1; i < table.table.length; i++) {
                if (Integer.parseInt(table.table[i][colNum]) < Integer.parseInt(_restriction))
                    rowNum += 1;

            }
        }

        if (_operation.equals(">=")) {
            for (int i = 1; i < table.table.length; i++) {
                if (Integer.parseInt(table.table[i][colNum]) >= Integer.parseInt(_restriction))
                    rowNum += 1;

            }
        }

        if (_operation.equals("<=")) {
            for (int i = 1; i < table.table.length; i++) {
                if (Integer.parseInt(table.table[i][colNum]) <= Integer.parseInt(_restriction))
                    rowNum += 1;

            }
        }

        if (_operation.equals("!=")){
            for (int i = 1; i < table.table.length; i++) {
                if (!table.table[i][colNum].equalsIgnoreCase(_restriction))
                    rowNum += 1;
            }

        }

        return rowNum + 1;  // Have to include the row for the columns with + 1
    }


}
