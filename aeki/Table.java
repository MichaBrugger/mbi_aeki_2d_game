package aeki;

import java.util.*;

public class Table {

  private int rowCounter;
  String tableName;
  private int colWidth;
  HashMap<Integer, ArrayList<String>> tableData = new HashMap<Integer, ArrayList<String>>();
  ArrayList<String> tableHeader = new ArrayList<String>();

  /**
   * Constructor for Table
   * A table is stored in the database as a HashMap of rows, each row is a HashMap
   * of columns
   * 
   * @param name    the name of the table
   * @param columns the columns of the table
   */
  public Table(String name, ArrayList<String> columns) {
    this.tableName = name;
    this.tableHeader = columns;
    this.colWidth = 0;
  }

  /**
   * getter for the table name
   * 
   * @return the name of the table
   */
  public String getTableName() {
    return this.tableName;
  }

  /**
   * getter for the table header
   * 
   * @return the header of the table
   */
  public ArrayList<String> getHeader() {
    return this.tableHeader;
  }

  /**
   * getter for the table data
   * 
   * @return the data of the table
   */
  public HashMap<Integer, ArrayList<String>> getRows() {
    return this.tableData;
  }

  /**
   * gets a specific row from the table by its index
   * 
   * @param index the index of the row
   * @return the row at the index
   */
  public ArrayList<String> getRow(int row) {
    return this.tableData.get(row);
  }

  /**
   * gets a specific column from the table by its index
   * 
   * @param index  the index of the column
   * @param column the column to get
   * @return the column at the index
   */
  public String getColumnByRowIndex(int index, int column) {
    return this.tableData.get(index).get(column);
  }

  /**
   * adds a row to the table
   * 
   * @param row the row to add (as an ArrayList of Strings)
   */
  public void addRow(ArrayList<String> row) {
    int diff = row.size() - this.tableHeader.size();
    String errorMsg = new String("Error (" + this.getTableName() + "): Your input is ");
    if (diff > 0) {
      System.out.println(errorMsg + diff + " column(s) too long.");
    } else if (diff < 0) {
      System.out.println(errorMsg + Math.abs(diff) + " column(s) too short.");
    } else {
      this.tableData.put(this.rowCounter, row);
      this.adjustColWidth(row);
      this.rowCounter++;
    }
  }

  /**
   * deletes a column from the table
   * 
   * @param columnName the name of the column to delete
   */
  public void deleteColumn(String columnName) {
    int index = this.tableHeader.indexOf(columnName);
    if (index != -1) {
      this.getHeader().remove(index);
      for (int i = 0; i < this.tableData.size(); i++) {
        this.tableData.get(i).remove(index);
      }
    }
  }

  /**
   * prints the table in a nicely formatted way
   */
  public void print() {
    System.out.println("\nTable: " + this.getTableName());
    this.printLines();
    this.printHeader();
    this.printRows();
    this.printLines();
  }

  /**
   * prints the header of the table
   * (part of the general print method)
   */
  private void printHeader() {
    this.printRow("index");
    for (String k : tableHeader) {
      // currently no need to print the data types since I'm not checking the inputs
      this.printRow(k.strip()); // + "(" + tableHeader.get(k) + ")");
    }
    System.out.println();
    this.printLines();
  }

  /**
   * prints the rows of the table
   * (part of the general print method)
   */
  private void printRows() {
    for (Integer i = 0; i < tableData.size(); i++) {
      this.printRow(i.toString());
      for (String row : tableData.get(i)) {
        this.printRow(row);
      }
      System.out.println();
    }
  }

  /**
   * prints a single row of the table
   * (part of the general print method)
   * 
   * @param row
   */
  private void printRow(String row) {
    System.out.printf("%-" + colWidth + "s", row);
  }

  /**
   * prints the "decorative" lines of the table
   * (part of the general print method)
   */
  private void printLines() {
    for (int i = -colWidth; i < this.tableHeader.size() * colWidth; i++) {
      System.out.print("-");
    }
    System.out.println();
  }

  /**
   * helper method to adjust the column width of the tables
   * this is used to make the table print out nicely
   * 
   * @param row the row to check for the longest string
   */
  private void adjustColWidth(ArrayList<String> row) {
    for (int i = 0; i < row.size(); i++) {
      if (row.get(i).length() >= this.colWidth) {
        this.colWidth = row.get(i).length() + 2;
      }
    }
  }

}
