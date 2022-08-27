package aeki;

import java.io.*;

public class Interpreter {

  BufferedReader reader = null;
  String line = "";
  Database db;

  /**
   * the interpreter reads the input from the csv files and writes the values to
   * the database
   * currently this is primarily used to set up the database but we plan on using
   * it more for in terms of writing values that should be stored (like previous
   * orders, logins for admins, etc.)
   * 
   * @param database the aeki database
   * @param filePath the filepath to the csv file that is to be interpreted
   */
  public Interpreter(Database database, String filePath) {
    this.db = database;
    this.readCSV(filePath);
  }

  /**
   * reads the csv file line by line. it ignores lines that have a # in front of
   * it and will stop as soon as it encounters an empty line, in case of an error,
   * it is printed to the console
   *
   * 
   * @param filePath the filepath to the csv file that is to be interpreted
   */
  public void readCSV(String inputFile) {
    String file = inputFile;
    BufferedReader reader = null;
    try {
      reader = new BufferedReader(new FileReader(file));
      while ((line = reader.readLine()) != null) {
        if (line.charAt(0) == '#') {
          continue;
        }
        String action = line.split(":", 0)[0];
        String content = line.split(":", 0)[1];
        this.interpretCommands(action, content);
      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      try {
        reader.close();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  /**
   * the writer is currently not used, but we plan on using it in the future
   * and have already written the code for it
   */
  // public void writeCSV(String outputFile, String command, String[] content) {
  // String file = outputFile + ".csv";
  // String str = String.join(",", content);
  // BufferedWriter writer = null;
  // try {
  // writer = new BufferedWriter(new FileWriter(file, true));
  // writer.write(command + ":" + str + "\n");
  // } catch (Exception e) {
  // e.printStackTrace();
  // } finally {
  // try {
  // writer.close();
  // } catch (Exception e) {
  // e.printStackTrace();
  // }
  // }
  // }

  /**
   * the interpreter interprets the commands and calls the appropriate methods
   * in the database
   * 
   * @param action  the command that is to be interpreted
   * @param content the content of the command
   */
  private void interpretCommands(String action, String content) {
    String command = action.split(" ")[0];
    String tableName = action.split(" ", 0)[1];
    String[] data = content.split(",");
    switch (command) {
      case "create":
        db.createTable(tableName, data);
        break;
      case "insert":
        /**
         * We are currently in a transition phase. Yesterday, we got the great input
         * that we should look into Serialization to store our objects. But since there
         * was only a day since that feedback we haven't managed to fully implement this
         * logic yet. thats why there is currently two different tables where the data
         * is stored.
         * 
         * db.insertRows() stores the data still in the String format we used to use
         * db.altInsertRows() stores the data properly in their respective Object
         * formats
         * 
         * We hope you understand why this is currently the case. We are currently still
         * trying to wrap our head around Serialization and how it works. But we will
         * give our best to implement it asap.
         */
        db.insertRows(tableName, data);
        db.altInsertRows(tableName, data);
        break;
      default:
        System.out.println("Invalid command");
        break;
    }
  }
}
