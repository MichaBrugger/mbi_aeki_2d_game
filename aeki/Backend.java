package aeki;

import java.io.File;
import java.util.*;

public class Backend {

  // the working directory is set as the current directory of the program
  String workingDir = System.getProperty("user.dir");
  ArrayList<File> storageFiles = new ArrayList<File>();
  private boolean foundFile;

  public Database db;
  Interpreter interpreter;
  public Pipeline pipeline;
  Warehouse warehouse = new Warehouse();

  /**
   * constants (set from the db)
   */
  public int deliveryTime;
  public int reorderTime;
  public int machineChangeTime;

  /**
   * constructor for the backend
   * 
   * @param db           the database instantiated in the software class
   * @param interpreter  the interpreter is used to read the database files found
   * @param deliveryTime the current delivery time
   * @param reorderTime  the current reorder time
   *                     by this.checkForExistingDatabases()
   * @param pipeline     the pipeline is instantiated with the current delivery
   *                     and reorder time
   * @param foundFile    boolean, true if a database file is found
   * @param workingDir   the current working directory
   * @param storageFiles the list of database files found that fulfill the
   *                     requirements given in this.checkForExistingDatabases()
   */
  public Backend() {
    this.checkForExistingDatabases();
    pipeline = new Pipeline(this);
  }

  /**
   * setter for the delivery time
   * 
   * @param deliveryTime the current delivery time
   */
  public void setDeliveryTime(int newDeliveryTime) {
    deliveryTime = newDeliveryTime;
  };

  /**
   * setter for the reorder time
   * 
   * @param reorderTime the current reorder time
   */
  public void setReorderTime(int newReorderTime) {
    reorderTime = newReorderTime;
  };

  /**
   * setter for the machine change time
   * 
   * @param machineChangeTime the new machine change time
   */
  public void setMachineChangeTime(int newMachineChangeTime) {
    this.machineChangeTime = newMachineChangeTime;
  };

  /**
   * getter for the delivery time
   * 
   * @return returns the current delivery time
   */
  public int getDeliveryTime() {
    return deliveryTime;
  };

  /**
   * getter for the reorder time
   * 
   * @return returns the current reorder time
   */
  public int getReorderTime() {
    return reorderTime;
  };

  /**
   * getter for the machine change time
   * 
   * @return returns the current machine change time
   */
  public int getMachineChangeTime() {
    return this.machineChangeTime;
  };

  /**
   * when a new order is created, this is where it "enters" the backend
   * from here on it is then handed over to the pipeline for further processing
   * 
   * @param order the order that was created
   */
  public void newOrderInSystem(Order newOrder) {
    pipeline.handleNewOrder(newOrder);
  }

  /**
   * checking for potential database files in the working directory
   * if a file it is added to this.storageFiles by this.findFilesByType() and
   * if it fulfills the requirements for a bootfile, a new database and
   * interpreter are instantiated for each file (currently there can only be one)
   */
  private void checkForExistingDatabases() {
    File rootDir = new File(workingDir);
    System.out.println(workingDir);
    // creates an array of all files in the working directory
    File[] files = rootDir.listFiles();
    // the fileType for database files
    String fileType = ".csv";
    // the name of the file that is used to boot the system (currently "db")
    String bootFileName = "db";

    // adds all files with the correct fileType to the storageFiles list
    this.findFilesByType(files, fileType);

    // if there are any files are found it is checked if it is the bootfile
    if (storageFiles.size() > 0) {
      File bootFile = findFileByName(storageFiles, bootFileName + fileType);
      if (bootFile != null) {
        this.db = new Database(this);
        interpreter = new Interpreter(db, bootFile.getName());
      } else {
        System.out.println("No bootfile found");
      }
    } else {
      System.out.println("No databases found");
    }
  }

  /**
   * finding all files in the working directory that fulfill the
   * requirements regarding their fileType (defined in
   * this.checkForExistingDatabases())
   * 
   * @param files    the list of files in the working directory
   * @param fileType the fileType for the database files (currently ".csv")
   */
  private void findFilesByType(File[] files, String fileType) {
    for (File file : files) {
      foundFile = file.getName().endsWith(fileType);
      if (foundFile) {
        storageFiles.add(file);
      }
    }
  }

  /**
   * checks if a file that has the correct fileType (see
   * this.findFilesByType()) is also fulfilling the requirements for a bootfile
   * (checking the name)
   * 
   * this is done because there could potentially be csv files in the directory
   * that are not database or bootfiles and also allows (in the future) to use the
   * same method to look for specific database files
   * 
   * @param files    the list of files to be checked
   * @param fileName the name of the file should have (defined in
   *                 this.checkForExistingDatabases())
   * @return returns the file if the check is successful, otherwise null
   */
  private File findFileByName(ArrayList<File> files, String fileName) {
    File bootFile = null;
    for (File file : files) {
      if (file.getName().equals(fileName)) {
        bootFile = file;
      }
    }
    return bootFile;
  }

}
