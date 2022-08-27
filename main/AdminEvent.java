package main;

public class AdminEvent extends EventHandler {

  public AdminEvent(GamePanel gp) {
    super(gp);
    productOptions = gp.aeki.backend.db.getProductsAsString();
  }

  public void useComputer(int gameState) {
    gp.ui.selectionCommandNum = 1;
    gp.gameState = gameState;
    gp.ui.selectionContext = "adminPC";
    gp.ui.currentSelection = "Welcome, dear Admin! How can I help?\nShow Pipeline\nShow all Orders\nShow Warehouse";
  }

  public void displayPipeline() {
    gp.ui.selectionContext = "pipeline";
    String pipelineString = gp.ui.getPipeline();
    if (pipelineString.contentEquals("")) {
      gp.ui.currentSelection = "The current Pipeline is empty.";
    } else {
      gp.ui.currentSelection = "ID: Step (Step Time Left / Total)" + pipelineString;
    }
  }

  public void displayOrders() {
    gp.ui.selectionContext = "orders";
    String orders = gp.ui.getOrderString();
    if (orders.contentEquals("")) {
      gp.ui.currentSelection = "There have not been any orders yet.";
    } else {
      gp.ui.currentSelection = "We received the following orders:" + orders;
    }
  }

  public void displayWarehouse() {
    gp.ui.selectionContext = "warehouse";
    String warehouse = gp.ui.getWarehouseString();
    if (warehouse.contentEquals("")) {
      gp.ui.currentSelection = "There is an error, please try again later.";
    } else {
      gp.ui.currentSelection = "Material: Available Stock (re-ordered)" + warehouse;
    }
  }

}
