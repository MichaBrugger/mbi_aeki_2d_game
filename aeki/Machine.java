package aeki;

import java.util.*;

import entity.NPC_Sofa;
import entity.NPC_Stuhl;
import main.GamePanel;

public class Machine extends Thread {
  String type;
  int runTimeRequired;
  int changingTime;
  String currentProductType = "";
  Item currentItem;
  Database db;
  public GamePanel gp;

  public Queue<Item> queue = new LinkedList<Item>();

  public Machine(String type, int changingTime, Database db) {
    // this.queue = new LinkedList<Item>();
    this.changingTime = changingTime;
    this.type = type;
    this.db = db;
  }

  /**
   * Once machine.start() is called when initializing the machine in the Database
   * class, the machine will constantly run and check if there is an item in the
   * queue for it to process. When the queue is empty, we slowed down the request
   * to every 0.1 real-life seconds
   */
  @Override
  public void run() {
    while (true) {
      if (this.queue.size() > 0) {
        try {
          getNextItemFromQueue();
          adaptMachineToProductType();
          runMachine();
          forwardItemToNextStage();
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      } else {
        slowDownRequests();
      }
    }
  }

  /**
   * After finishing the production process, the item is forwarded to the next
   * stage in the process, or, if it is the last stage, it is delivered.
   */
  private void forwardItemToNextStage() {
    Machine nextStage = currentItem.getNextStage();
    if (nextStage != null) {
      nextStage.addItem(currentItem);
    } else {

      currentItem.setStatus("delivered");
      System.out.println("Item " + currentItem.getItemId() + " delivered");
      // TODO: refactor workingQueue.remove() into its own method
      db.backend.pipeline.workingQueue.remove(currentItem);
    }
  }

  /**
   * Adding the item to the queue of the machine
   * 
   * @param item the item to be added to the queue
   */
  public void addItem(Item item) {
    queue.add(item);
  }

  /**
   * Getting type of the machine
   */
  public String getType() {
    return type;
  }

  /**
   * Setting the current product type of the machine
   * 
   * @param productType the product type that needs to be processed from now on
   */
  private void setCurrentProductType(String currentProductType) {
    this.currentProductType = currentProductType;
  }

  /**
   * Get the next item to process by removing the head of the queue
   */
  private void getNextItemFromQueue() {
    currentItem = queue.poll();
  }

  /**
   * After the item is chosen to be the next in line for the machine, the machine
   * checks if it has to adapt the production process, if yes, it will sleep the
   * required amount of time and change the status of the item to machineType +
   * "changing" so it's visible in the frontend
   * 
   * @throws InterruptedException if the thread is interrupted (needs to be there
   *                              so thread can be extracted into its own method)
   */
  private void adaptMachineToProductType() throws InterruptedException {
    if (!currentProductType.equals(currentItem.getProductName())) {
      currentItem.setStatus(type + " adapting");
      setCurrentProductType(currentItem.getProductName());
      Thread.sleep(changingTime);
    }
    currentItem.setStatus(type);
    if (currentItem.getStatus().equals("HolzCNC")) {
      gp.aSetter.displayProductInProduction(currentItem.getProductName());
    }
  }

  /**
   * Getting the runtime needed for the current item and "running" the machine.
   * Every runtimeMinute the remaining time of the item is adjusted so it can be
   * live monitored in the frontend
   * 
   * @throws InterruptedException if the thread is interrupted (needs to be there
   *                              so thread can be extracted into its own method)
   */
  private void runMachine() throws InterruptedException {
    int runtimeMinutes = currentItem.getCurrentStepRuntime();

    for (int i = 1; i <= runtimeMinutes; i++) {
      Thread.sleep(1000);
      currentItem.setCurrentStepRuntime(runtimeMinutes - i);
    }
  }

  /**
   * Slowing down the requests to the machine, so it "doesn't get overloaded"
   */
  private void slowDownRequests() {
    try {
      Thread.sleep(100);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
