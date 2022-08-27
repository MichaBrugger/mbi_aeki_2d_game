package aeki;

import java.util.*;

public class Supplier implements Runnable {

  public Queue<HashMap<Material, Integer>> ordersReceived = new LinkedList<HashMap<Material, Integer>>();

  private static int counter;
  public String supplierId;
  public int reorderTime;

  Warehouse warehouse;

  /**
   * Constructor for Supplier.
   * 
   * @param supplierId  The supplier id as a string.
   * @param warehouse   The warehouse he needs to deliver to
   * @param reorderTime The time it takes for the order to arrive
   *
   */
  public Supplier(int reorderTime) {
    Supplier.counter++;
    supplierId = "SU" + "-" + counter;
    this.reorderTime = reorderTime;

    Thread supplierThread = new Thread(this);
    supplierThread.start();
  }

  /**
   * This method is called when the supplier thread is started.
   * It permanently checks if there are any orders in the queue and starts the
   * delivery process if it can find one.
   */
  @Override
  public void run() {
    while (true) {
      try {
        if (ordersReceived.size() > 0) {
          Thread.sleep(reorderTime * 1000);
          HashMap<Material, Integer> order = ordersReceived.poll();
          warehouse.storeOrder(order);
          System.out.println("Our material order has been delivered to the warehouse");
        } else {
          // slowing down the requests
          Thread.sleep(100);
        }
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }

  /**
   * getter for supplierId
   * 
   * @return returns the supplierId as a string
   */
  public String getSupplierId() {
    return supplierId;
  }

  /**
   * the request that the supplier receives from the warehouse, after receiving
   * it, the supplier adds it to their queue and delivers after the reorderTime
   * 
   * @param requestedMaterial the material that the supplier should deliver
   * @param warehouse         the warehouse that the supplier should deliver to
   */
  public void requestMaterials(HashMap<Material, Integer> requestedMaterial, Warehouse warehouse) {
    this.ordersReceived.add(requestedMaterial);
    this.warehouse = warehouse;
  }
}
