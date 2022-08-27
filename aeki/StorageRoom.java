package aeki;

public class StorageRoom {
  public String name;
  public Material material;
  public int currentStock;
  public int maxStock;

  private static int counter;

  public StorageRoom(Material mat, int curr, int max) {
    StorageRoom.counter++;
    this.name = "RO" + counter;
    this.material = mat;
    this.currentStock = curr;
    this.maxStock = max;
  }

  /**
   * Setters and Getters
   * 
   * These are all rather straight forward methods, for the sake of readability
   * I'll be describing them all in the same comment.
   * Setter Methods: setName, setMaterial, setCurrentStock, setMaxStock
   * Getter Methods: getName, getMaterial, getCurrentStock, getMaxStock
   * 
   * @param name         name of the StorageRoom
   * @param material     describes the stored Material, only one type of Material
   *                     per StorageRoom
   * @param currentStock maximal storage possible, as defined in Warehouse
   * @param maxStock     maximal storage possible, as defined in Warehouse
   */

  public void setName(String name) {
    this.name = name;
  };

  public String getName() {
    return name;
  };

  public void setMaterial(Material mat) {
    this.material = mat;
  };

  public Material getMaterial() {
    return material;
  };

  public void setCurrentStock(int curr) {
    this.currentStock = curr;
  };

  public int getCurrentStock() {
    return currentStock;
  };

  public void setMaxStock(int max) {
    this.maxStock = max;
  };

  public int getMaxStock() {
    return maxStock;
  };

  public void useStock(int neededMat) {
    int temp = currentStock - neededMat;
    this.setCurrentStock(temp);
  };

  public void refillStock(int addedMat) {
    int temp = currentStock + addedMat;
    this.setCurrentStock(temp);
  };

}
