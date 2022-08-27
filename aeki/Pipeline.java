package aeki;

import java.util.*;

import main.GamePanel;
import java.sql.Date;

public class Pipeline implements Runnable {
  private ArrayList<Order> orderLogbook = new ArrayList<Order>();
  private ArrayList<String> orderConfirmation = new ArrayList<String>();
  public Queue<Item> waitingQueue = new LinkedList<Item>();
  public Queue<Item> workingQueue = new LinkedList<Item>();
  public ArrayList<Item> deliveredItems = new ArrayList<Item>();

  Supplier supplier;
  Database db;
  Backend backend;
  public GamePanel gp;
  Warehouse warehouse;

  /**
   * constructor for the pipeline class
   * 
   * explanation: since there is no "time" yet we currently just assume that when
   * the next order comes in the previous orders have been dealt with, this means:
   * the waitingQueue is empty again, the workingQueue is empty again and, in case
   * material was reordered, the reorderTime has passed
   * 
   * @param db                the aeki database created in the backend class
   * @param deliveryTime      the time it takes to deliver an order when it can be
   *                          delivered directly
   * @param reorderTime       the time it takes to deliver an order when its
   *                          material has to be reordered
   * @param orderLogbook      the log of all orders that have been placed so far
   * @param orderConfirmation holds all the different string "parts" of the order
   *                          confirmation message that then put together and sent
   *                          to the user
   * @param gp                the game panel, needed for interactions with the
   *                          frontend
   * @param warehouse         the warehouse
   * @param supplier          we initialize the supplier in the class itself,
   *                          since we want to avoid having multiple suppliers
   *                          without any reason
   */
  public Pipeline(Backend backend) {
    this.backend = backend;

    this.db = backend.db;
    this.warehouse = backend.warehouse;
    this.supplier = new Supplier(backend.reorderTime);

    Thread pipelineThread = new Thread(this);
    pipelineThread.start();
  }

  /**
   * This method is called when the pipeline thread is started.
   * 
   * It has two main functions:
   * 1. checking if there are any items in the waitingQueue that should be added
   * to the workingQueue (if new material arrived)
   * 2. updating the "game" frontend in case the player uses the "admin" computer
   */
  @Override
  public void run() {
    while (true) {
      slowDownRequests();
      checkWaitingQueueForItems();
      updateAdminFrontend();
    }
  }

  /**
   * slowing down the checks made, since there is no point in doing it more than
   * 10x a second
   */
  private void slowDownRequests() {
    try {
      Thread.sleep(1000);

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * checks if there are items in the waiting queue and if there are, it adds them
   */
  private void checkWaitingQueueForItems() {
    if (!waitingQueue.isEmpty()) {
      // TODO: check if there is enough material for an item to be processed
      Item item = waitingQueue.remove();
      workingQueue.add(item);
    }
  }

  /**
   * receives the new order and builds the order confirmation message depending on
   * various factors that are determined by the successive methods (availability,
   * potential errors, etc.)
   * 
   * each order is added to the logbook and when it ran through the full process a
   * confirmation message is sent to the user
   * 
   * @param order the order that has been placed
   * 
   */
  public void handleNewOrder(Order order) {
    addOrderToLogbook(order);

    try {
      orderConfirmation.add("Thank you for your order! You can pick up your items as follows:\n");
      queueAllocator(order);
    } catch (Exception e) {
      orderConfirmation.add("Due to technical issues, we are currently not available, please try again later.\n");
    }

    // after allocating the items to the queue, we check if a material reorder is
    // needed (we currently only reorder when the storage of one material is empty)
    checkIfReorderIsNecessary();
    sendOrderConfirmationInConsole();
  }

  /**
   * adding the order to the logbook and initializing the first line of the
   * console message that is sent to the customer
   * 
   * @param order the order that has been placed
   */
  private void addOrderToLogbook(Order order) {
    orderLogbook.add(order);
    orderConfirmation.add("\n---\nYour order ID: " + order.getId() + "\n---\n\nDear customer,");
  }

  /**
   * since there can only be items in the waitingQueue when there was not enough
   * material, we can simply check that.
   * 
   * to avoid that this is triggered every
   * time an order is added (which could happen if the supplier order hasn't
   * arrived yet) we added a boolean check to see if there is currently an active
   * order, if there is, we don't trigger another one.
   * 
   * if not, we trigger a new order and set the ongoingOrder boolean to true
   */
  private void checkIfReorderIsNecessary() {
    if (waitingQueue.size() > 0 && warehouse.getOngoingOrder() == false) {
      warehouse.setOngoingOrder(true);
      reorderMaterial();
    }
  }

  /**
   * sends console confirmation to the user and resets it
   */
  private void sendOrderConfirmationInConsole() {
    orderConfirmation.add("\nBest regards,\nyour AEKI team\n---\n");
    for (String s : orderConfirmation) {
      System.out.println(s);
    }
    orderConfirmation.clear();
  }

  /**
   * there are currently no queues, but later there will be and this method will
   * be used to allocate the order to the correct queue (depending on
   * optimization-criteria and the availability of materials)
   * 
   * @param order the order to be allocated to a queue
   */
  public void queueAllocator(Order order) {
    for (Product prod : order.getItems().keySet()) {
      // checking the material requirements for this kind of product
      HashMap<Material, Integer> matReq = prod.getMaterialRequirements();
      int amountOrdered = order.items.get(prod);
      String orderId = order.getId();

      // checking for each item individually if it can be produced at the moment
      // if one material is not available, we add all the remaining items of this
      // product directly to the waiting queue
      for (int i = 0; i < amountOrdered; i++) {
        if (checkMaterialAvailability(prod, matReq)) {
          createItem(prod, orderId, matReq, true);
        } else {
          for (int j = i; j < amountOrdered; j++) {
            createItem(prod, orderId, matReq, false);
          }
          break;
        }
      }
    }
  }

  /**
   * creates an item and adds it to the correct queue
   * 
   * @param prod      the product that is being produced
   * @param orderId   the id of the order that is being produced
   * @param matReq    the material requirements for this product
   * @param available if the material is available or not
   */
  private void createItem(Product prod, String orderId, HashMap<Material, Integer> matReq, boolean available) {
    Item item = new Item(prod, orderId);
    if (available) {
      addToWorkingQueue(item, matReq);
    } else {
      addToWaitingQueue(item);
    }
  }

  /**
   * adds the item to the working queue, updates the database and adds a
   * confirmation for the item
   * 
   * @param item   the item to be added to the working queue
   * @param matReq the material requirements for this item
   */
  private void addToWorkingQueue(Item item, HashMap<Material, Integer> matReq) {
    workingQueue.add(item);
    removeMaterialFromStorage(matReq);

    // adding the item to the machine
    item.getNextStage().addItem(item);
    addConfirmationPerItem(item, true);

  }

  /**
   * removes the material from the warehouse and updates the database
   * 
   * @param matReq the material requirements for this item
   */
  private void removeMaterialFromStorage(HashMap<Material, Integer> matReq) {
    for (Material mat : matReq.keySet()) {
      StorageRoom room = warehouse.getStorageRoomByMaterial(mat);
      room.useStock(matReq.get(mat));
    }
  }

  /**
   * adds an item to the waiting queue
   * 
   * @param item the item to be added to the waiting queue
   */
  private void addToWaitingQueue(Item item) {
    item.setStatus("waiting");
    waitingQueue.add(item);
    addConfirmationPerItem(item, false);
  }

  /**
   * checks how much material needs to be reordered and the makes an order request
   * at the supplier
   */
  private void reorderMaterial() {
    HashMap<Material, Integer> neededMat = warehouse.calcReorder();
    supplier.requestMaterials(neededMat, warehouse);
  }

  /**
   * this method is used to add the order confirmation message for each item to
   * the order confirmation message created in this.handleNewOrder()
   * 
   * @param item      the item that is added to the order confirmation message
   * @param available boolean that indicates if the item is available,
   *                  if true the item has the standard delivery time, if false
   *                  the item has the reorder time
   */
  private void addConfirmationPerItem(Item item, boolean available) {
    String itemInfo = "-" + item.getProductName() + " (" + item.getItemId() + ")" + ": ";
    if (available) {
      orderConfirmation.add(itemInfo + today(backend.getDeliveryTime()));
    } else {
      orderConfirmation.add(itemInfo + today(backend.getDeliveryTime() + backend.getReorderTime()) + " (delayed)");
    }
  }

  /**
   * this method checks the availability of the materials for a certain product
   * 
   * @param product the product that is checked
   * @param matReq  the material requirements for the product
   * @return true if the materials are available, false if not
   */
  public boolean checkMaterialAvailability(Product product, HashMap<Material, Integer> matReq) {
    boolean available = true;

    for (Material mat : matReq.keySet()) {
      int currStock = warehouse.checkMaterialStock(mat);
      if (currStock < matReq.get(mat)) {
        available = false;
        break;
      }
    }
    return available;
  }

  /**
   * Prints all the orders that have been placed so far
   * 
   */
  public void printLog() {
    System.out.println("Total orders: " + orderLogbook.size());
    for (Order order : orderLogbook) {
      System.out.println(order.getId() + "(" + order.getItems().size() + " items)");
    }
  }

  /**
   * returns a string with the current date and time, with the given time offset
   * the return represents the time when the item will be delivered
   * 
   * @param addedDays the number of days to add to the current date
   * @return a string with the current date and time, with the given time offset
   */
  public String today(int addedDays) {
    Date date = new Date(System.currentTimeMillis() + (addedDays * 24 * 60 * 60 * 1000));
    return date.toString();
  }

  /**
   * this method selectively updates the values that are displayed in the frontend
   * (depending on which value is requested)
   */
  private void updateAdminFrontend() {
    if (gp.gameState == gp.selectionState) {

      switch (gp.ui.selectionContext) {
        case "pipeline":
          setWaitingQueueForUI();
          setWorkingQueueForUI();
          gp.eHandler.aEvent.displayPipeline();
          break;
        case "orders":
          setOrderStringForUI();
          gp.eHandler.aEvent.displayOrders();
          break;
        case "warehouse":
          setWarehouseStringForUI();
          gp.eHandler.aEvent.displayWarehouse();
          break;
      }
    }
  }

  /**
   * this method sets the working queue for the frontend
   */
  // TODO: should be refactored/combined with method below
  public void setWorkingQueueForUI() {
    String queue = "";

    for (Item item : workingQueue) {
      queue += "\n" + item.getItemId() + ": " + item.getStatus() + " (" + item.getCurrentStepRuntime() + "/"
          + item.getTotalRuntime() + ")";
    }

    gp.ui.workingQueue = queue;
  }

  /**
   * this method sets the waiting queue for the frontend
   */
  // TODO: should be refactored/combined method above
  public void setWaitingQueueForUI() {
    String queue = "";

    for (Item item : waitingQueue) {
      queue += "\n" + item.getItemId() + ": " + item.getStatus() + " (" + item.getCurrentStepRuntime() + ")";
    }

    gp.ui.waitingQueue = queue;
  }

  /**
   * this method sets the order string for the frontend, that lets admin see all
   * previous orders
   */
  public void setOrderStringForUI() {
    String orders = "";

    for (Order order : orderLogbook) {
      for (Product prod : order.getItems().keySet()) {
        orders += "\n" + order.getId() + ": " + prod.getName() + " (" + order.getItems().get(prod) + ")";
      }
    }

    gp.ui.setOrderString(orders);
  }

  /**
   * this method sets the warehouse string for the frontend, that lets admin see
   * the current stock of the warehouse
   * 
   * one could make the argument that this method be executed from the database,
   * and I would tend to agree, but since all other methods updating the frontend
   * are here, I decided to keep it here too
   */
  public void setWarehouseStringForUI() {
    String warehouseString = "";

    for (Material mat : backend.db.getMaterials()) {
      warehouseString += "\n" + mat.getName() + ": " + warehouse.checkMaterialStock(mat);
    }

    gp.ui.setWarehouseString(warehouseString);
  }
}
