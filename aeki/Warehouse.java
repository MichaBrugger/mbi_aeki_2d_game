package aeki;

import java.util.ArrayList;
import java.util.HashMap;

public class Warehouse {
  public ArrayList<StorageRoom> inventory = new ArrayList<StorageRoom>();
  public boolean ongoingOrder = false;

  public void addStorageRoom(Material mat, int curr, int max) {
    StorageRoom temp = new StorageRoom(mat, curr, max);
    inventory.add(temp);
  }

  public StorageRoom getStorageRoomByMaterial(Material mat) {
    StorageRoom temp = null;
    for (StorageRoom room : inventory) {
      if (mat.equals(room.getMaterial())) {
        temp = room;
        break;
      }
    }
    if (temp == null) {
      System.out.println("Storage for " + mat.getName() + " was not found!");
    }
    return temp;
  }

  /**
   * checks the warehouse for the material and returns the amount of the material
   * 
   * @param mat the material to check
   * @return the amount of the material in the warehouse
   */
  public int checkMaterialStock(Material mat) {
    StorageRoom temp = this.getStorageRoomByMaterial(mat);
    return temp.getCurrentStock();
  }

  /**
   * calculates the amount of material that can be stored in the warehouse,
   * what we have and what should be reordered
   * 
   * @return a hashmap with the material and the amount of material that can be
   *         stored
   */
  public HashMap<Material, Integer> calcReorder() {
    HashMap<Material, Integer> order = new HashMap<Material, Integer>();

    for (StorageRoom room : inventory) {
      Material mat = room.getMaterial();
      int curr = room.getCurrentStock();
      int max = room.getMaxStock();
      int matOrder = max - curr;
      order.put(mat, matOrder);
    }
    return order;
  }

  /**
   * stores the order in the warehouse (not to be confused with the orders that we
   * receive from our customers)
   * 
   * @param order the material ordered
   */
  public void storeOrder(HashMap<Material, Integer> supplierOrder) {
    for (Material mat : supplierOrder.keySet()) {
      int orderAmount = supplierOrder.get(mat);
      StorageRoom temp = this.getStorageRoomByMaterial(mat);
      temp.refillStock(orderAmount);
    }
    setOngoingOrder(false);
  }

  /**
   * setter for ongoingOrder, this is used to check if there is an order that the
   * we are waiting for to be delivered, by checking this, we want to avoid that
   * the warehouse is reordering the same order twice
   * 
   * @param ongoingOrder if true, there is an active order, if false there is none
   */

  public void setOngoingOrder(boolean ongoingOrder) {
    this.ongoingOrder = ongoingOrder;
  }

  /**
   * getter for ongoingOrder
   * 
   * @return returns true if there is an order that is to be delivered, false if
   *         there is no active order
   */
  public boolean getOngoingOrder() {
    return ongoingOrder;
  }
}
