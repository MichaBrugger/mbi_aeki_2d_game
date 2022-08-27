package aeki;

import java.util.*;

public class Item {

  Pipeline pipeline;
  private static int counter;
  private String itemId;
  private Product product;
  public String status;
  int totalRuntimeMinutes;
  int currentStepRuntimeMinutes;

  private LinkedHashMap<Machine, Integer> productionSteps;

  /**
   * Constructor for Item. An item is created for every product that is ordered.
   * Since there is currently no concept of time, we have not yet implemented the
   * logic of a item "status" (like "ordered", "delivered", "cancelled", etc).
   * However, as soon as time will be implemented with the next task we will add
   * the logic in this class.
   * 
   * The item id is a unique identifier for each item and created by combining the
   * order id, the product id and a counter.
   * 
   * @param product  The product that is ordered
   * @param orderId  The orderId of the order that the item belongs to
   * @param pipeline The pipeline object
   * @param db       The database object
   * 
   */
  public Item(Product product, String orderId) {
    Item.counter++;
    this.itemId = orderId + "-" + product.getProductId() + "-IT" + Item.counter;
    this.product = product;
    this.status = "ordered";
    this.productionSteps = product.getMachineRequirements();

    setTotalRuntime();
  }

  /**
   * Getter for the itemId
   * 
   * @return The itemId
   */
  public String getItemId() {
    return this.itemId;
  }

  /**
   * Getter for the product name
   * 
   * @return returns the product name as a String
   */
  public String getProductName() {
    return this.product.getName();
  }

  /**
   * Getter for the product
   * 
   * @return returns the product of the item (the actual Object)
   */
  public Product getProduct() {
    return this.product;
  }

  /**
   * Setter for the status
   * 
   * @param status the updated status of the item
   */
  public void setStatus(String status) {
    this.status = status;
  }

  /**
   * Getter for the status
   * 
   * @return returns the current status of the item
   */
  public String getStatus() {
    return this.status;
  }

  /**
   * Setter for the runtimeMinutes of the current processing step
   * 
   * @param runtime runtime in (simulation) minutes
   */
  public void setCurrentStepRuntime(int runtime) {
    currentStepRuntimeMinutes = runtime;
  }

  /**
   * Getter for the current step runtime of the item
   * 
   * @return runtime of the current step in (simulation) minutes
   */
  public int getCurrentStepRuntime() {
    return currentStepRuntimeMinutes;
  }

  /**
   * Total expected runtime of the item (neither including potential changetime of
   * machine nor potential wait time if machine is blocked)
   */
  public void setTotalRuntime() {
    int total = 0;
    for (Machine m : productionSteps.keySet()) {
      total += productionSteps.get(m);
    }
    totalRuntimeMinutes = total;
  }

  /**
   * Getter for the total runtime of the item
   * 
   * @return returns the total runtime of the item
   */
  public int getTotalRuntime() {
    return totalRuntimeMinutes;
  }

  /**
   * Checks the next Machine for the item, removes it from the queue and returns
   * it to caller of this method (either current Machine or Pipeline (if its the
   * first step))
   * 
   * @return returns the next Machine in the production process or null if if the
   *         process is finished
   */
  public Machine getNextStage() {
    // guard clause
    if (productionSteps.isEmpty()) {
      return null;
    }

    // TODO: refactor to not work with variables directly (create actual methods)
    Machine nextMachine = productionSteps.keySet().iterator().next();
    currentStepRuntimeMinutes = productionSteps.get(nextMachine);
    setStatus(nextMachine.getType() + " queue");
    productionSteps.remove(nextMachine);
    return nextMachine;
  }
}
