package aeki;

import java.util.*;

/**
 * 
 * This class is currently heavily under construction.
 * We received the feedback yesterday on how to change the
 * logic of the program. But literally haven't had time to implement
 * everything today. I hope you understand that it's currently a work in
 * progress as it will be cleaned up before the next deadline.
 * 
 * Please check the comment at the interpretCommands() method in the interpreter
 * class for more details
 */
public class Database {

  private ArrayList<Table> storedTables = new ArrayList<Table>();
  public ArrayList<Material> materials = new ArrayList<Material>();
  public ArrayList<Product> products = new ArrayList<Product>();
  public ArrayList<Machine> machines = new ArrayList<Machine>();

  public Warehouse warehouse;
  Backend backend;

  /**
   * Constructor for Database
   * 
   * @param backend the backend instantiated in the software class
   */
  public Database(Backend backend) {
    this.backend = backend;
    System.out.println("New database initialized");

  }

  /**
   * gets a stored table by name
   * 
   * @param tableName the name of the table
   * @return the table object
   */
  public Table getTableByName(String tableName) {
    for (Table table : storedTables) {
      boolean tableFound = table.getTableName().equals(tableName);
      if (tableFound) {
        return table;
      }
    }
    return null;
  }

  /**
   * returns the warehouse object
   * 
   * @return
   */
  public Warehouse getWarehouse() {
    return warehouse;
  }

  public ArrayList<Material> getMaterials() {
    return materials;
  }

  /**
   * return products as a string for game-computer output
   */
  public String getProductsAsString() {
    String productsString = "";
    for (Product product : products) {
      productsString += "\n" + product.getName();
    }
    return productsString;
  }

  public Machine getMachineByType(String machineType) {
    for (Machine machine : machines) {
      if (machine.getType().equals(machineType)) {
        return machine;
      }
    }
    return null;
  }

  /**
   * prints all the tables
   * (for debugging purposes)
   */
  public void printAllTables() {
    for (Table t : storedTables) {
      t.print();
    }
  }

  /**
   * inserts a row into a table
   * 
   * @param tableName the name of the table
   * @param data      the data to be inserted as an array of strings
   */
  public void insertRows(String tableName, String[] data) {
    ArrayList<String> row = new ArrayList<String>();
    for (String s : data) {
      row.add(s.strip());
    }
    for (Table t : storedTables) {
      if (t.getTableName().equals(tableName)) {
        t.addRow(row);
      }
    }
  }

  /**
   * creates a table
   * 
   * @param tableName the name of the table
   * @param data      the data to be inserted as an array of strings
   */
  public void createTable(String tableName, String[] data) {
    ArrayList<String> columns = new ArrayList<String>();
    for (String col : data) {
      col = col.trim();
      String columnName = col.split(" ", 0)[0];
      columns.add(columnName);
    }

    Table table = new Table(tableName, columns);
    this.storedTables.add(table);
  }

  /**
   * this is very much under construction but should at some point return the
   * correct values from a table (currently only working for simple table where we
   * have to look up a single column value - not working for tables where we have
   * to check for combined values like matRequirements)
   * 
   * @param tableName  the name of the table
   * @param columnName the name of the column
   * @param rowName    the name of the row
   * @return
   */
  private String getValueFromTable(String tableName, String columnName, String rowName) {
    for (Table t : storedTables) {
      if (t.getTableName().equals(tableName)) {
        int columnIndex = findColumnIndexByName(t, columnName);
        int rowIndex = findRowIndexByName(t, rowName);
        String value = t.getColumnByRowIndex(rowIndex, columnIndex);
        return value;
      }
    }
    return null;
  }

  private int findColumnIndexByName(Table table, String columnName) {
    // if columnName in table header then return index of that column
    for (int i = 0; i < table.getHeader().size(); i++) {
      if (table.getHeader().get(i).strip().equals(columnName)) {
        return i;
      }
    }
    return -1;
  }

  private int findRowIndexByName(Table table, String rowName) {
    // if rowName in table rows then return index of that row
    for (int i = 0; i < table.getRows().size(); i++) {
      if (table.getRows().get(i).get(0).strip().equals(rowName)) {
        return i;
      }
    }
    return -1;
  }

  /**
   * Please forgive us this dirty method. It's really been a short time since
   * yesterday and we gave our best effort to get it working
   * 
   * I will write a proper comment for the next deadline
   * its basically just a switch statement that allocates the correct table to the
   * respective objects
   * 
   * @param tableName the name of the table
   * @param data
   */
  public void altInsertRows(String tableName, String[] data) {
    switch (tableName) {
      case "constants":
        setConstValuesFromDB(tableName, data);
        break;
      case "materials":
        String materialName = data[0].strip();
        this.addMaterial(materialName);
        break;
      case "matStorage":
        String materialToBeStored = data[0].strip();
        String currStockString = this.getValueFromTable(tableName, "currStock", materialToBeStored);
        int currStock = Integer.parseInt(currStockString);
        String maxStockString = this.getValueFromTable(tableName, "maxStock", materialToBeStored);
        int maxStock = Integer.parseInt(maxStockString);
        this.addStorage(materialToBeStored, currStock, maxStock);
        break;
      case "products":
        String productName = data[0].strip();
        this.addProduct(productName);
        break;
      case "matRequirements":
        String productTobeUpdated = data[0].strip();
        String materialTobeUpdated = data[1].strip();
        Table table = this.getTableByName(tableName);
        if (table != null) {
          for (int i = 0; i < table.getRows().size(); i++) {
            if (table.getRow(i).get(0).strip().equals(productTobeUpdated)) {
              if (table.getRow(i).get(1).strip().equals(materialTobeUpdated)) {
                String amountString = data[2].strip();
                int amount = Integer.parseInt(amountString);
                this.addProductRequirement(productTobeUpdated, materialTobeUpdated, amount);
              }
            }
          }
        }
        break;
      case "machines":
        String machineType = data[0].strip();
        String amountString = data[1].strip();
        int amount = Integer.parseInt(amountString);
        for (int i = 0; i < amount; i++) {
          this.addMachine(machineType);
        }
        break;
      case "timeRequirements":
        String productTBD = data[0].strip();
        String machineToBeAdded = data[1].strip();
        Table tableTime = this.getTableByName(tableName);
        if (tableTime != null) {
          for (int i = 0; i < tableTime.getRows().size(); i++) {
            if (tableTime.getRow(i).get(0).strip().equals(productTBD)) {
              if (tableTime.getRow(i).get(1).strip().equals(machineToBeAdded)) {
                String timeReqString = data[2].strip();
                int timeReq = Integer.parseInt(timeReqString);
                addProductTimeRequirement(productTBD, machineToBeAdded, timeReq);
              }
            }
          }
        }
        break;
      default:
        break;
    }
  }

  private void addProductTimeRequirement(String productTBD, String machineToBeAdded, int amount1) {
    for (Product p : products) {
      if (p.getName().equals(productTBD)) {
        for (Machine m : machines) {
          if (m.getType().equals(machineToBeAdded)) {
            p.addMachineRequirement(m, amount1);
          }
        }
      }
    }
  }

  /**
   * sets the constants values (deliveryTime, reorderTime, etc) from the database
   * 
   * @param tableName the name of the table
   * @param data      the const value from the database
   */
  private void setConstValuesFromDB(String tableName, String[] data) {
    String constName = data[0].strip();
    String constValueString = data[1].strip();
    int constValue = Integer.parseInt(constValueString);
    switch (constName) {
      case "deliveryTime":
        backend.setDeliveryTime(constValue);
        break;
      case "reorderTime":
        backend.setReorderTime(constValue);
        break;
      case "machineChangeTime":
        backend.setMachineChangeTime(constValue);
        break;
    }
  }

  /**
   * adds a material to the material table
   * 
   * @param materialName the name of the material
   */
  private void addMaterial(String name) {
    Material temp = new Material(name);
    materials.add(temp);
  }

  /**
   * adds a storage room to the storage table
   * 
   * @param materialName the name of the material
   * @param currStock    the current stock of the material
   * @param maxStock     the maximum stock of the material
   */
  private void addStorage(String materialName, int currStock, int maxStock) {
    System.out.println("Adding storage for material: " + materialName);
    for (Material m : materials) {
      if (m.getName().equals(materialName)) {
        backend.warehouse.addStorageRoom(m, currStock, maxStock);
        System.out.println("Storage room for " + materialName + " is now available and filled with " + currStock + "/"
            + maxStock + " units!");
      }
    }
  }

  /**
   * adds a product to the product table
   * when initialized a product requires 0 materials, they are added later
   * 
   * @param name the name of the product
   */
  private void addProduct(String name) {
    HashMap<Material, Integer> emptyReqList = new HashMap<Material, Integer>();
    for (Material M : materials) {
      emptyReqList.put(M, 0);
    }
    Product temp = new Product(name, emptyReqList);
    products.add(temp);
  }

  /**
   * adds the machines to the system
   */
  private void addMachine(String machineType) {
    Machine temp = new Machine(machineType, this.backend.getMachineChangeTime(), this);
    temp.start();
    machines.add(temp);
  }

  /**
   * adds the product requirement to the product
   * 
   * @param productName  the name of the product
   * @param materialName the name of the material
   * @param amount       the amount of the material required
   */
  private void addProductRequirement(String productName, String materialName, Integer amount) {
    for (Product p : products) {
      if (p.getName().equals(productName)) {
        for (Material m : materials) {
          if (m.getName().equals(materialName)) {
            p.addMaterialRequirement(m, amount);
          }
        }
      }
    }
  }

}