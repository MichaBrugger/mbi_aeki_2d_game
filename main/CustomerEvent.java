package main;

import java.util.HashMap;
import java.util.Map;

import aeki.Product;

public class CustomerEvent extends EventHandler {

  Product selectedProduct;
  HashMap<Product, Integer> orderItems;

  public CustomerEvent(GamePanel gp) {
    super(gp);
  }

  public void useComputer(int gameState) {
    this.productOptions = gp.aeki.backend.db.getProductsAsString();
    gp.ui.selectionCommandNum = 1;
    gp.gameState = gameState;
    gp.ui.selectionContext = "customerPC";
    gp.ui.currentSelection = "Welcome, dear Customer! How can I help?\nProduct Catalogue\nOrder a Product";
  }

  public void makeOrder() {
    gp.ui.selectionContext = "makeOrder";
    String checkOrder = "";
    if (orderItems != null) {
      checkOrder = "\nProceed to checkout";
    }
    gp.ui.selectionCommandNum = 1;
    gp.ui.currentSelection = "What are you interested in?" + productOptions + checkOrder + "\nQuit";
  }

  public void getOrderProduct() {
    Product product = gp.aeki.backend.db.products.get(gp.ui.selectionCommandNum - 1);
    gp.ui.selectionCommandNum = 1;
    selectedProduct = product;
  }

  public void addToOrder() {
    if (orderItems == null) {
      orderItems = new HashMap<Product, Integer>();
    }
    orderItems.put(selectedProduct, gp.ui.orderAmount);
    gp.ui.orderAmount = 0;
  }

  public void displayProducts() {
    gp.ui.selectionContext = "displayProducts";
    gp.ui.currentSelection = "Our Products:" + productOptions + "\nQuit";
  }

  public void enterAmount() {
    gp.ui.selectionContext = "enterAmount";
    gp.ui.currentSelection = "How many " + selectedProduct.getName() + " would you like?\n" + gp.ui.orderAmount;
  }

  public void proceedToCheckout() {
    gp.ui.selectionContext = "checkout";
    String orderString = "";
    for (Map.Entry<Product, Integer> entry : orderItems.entrySet()) {
      if (entry.getValue() > 0) {
        orderString += "\n" + entry.getKey().getName() + " x" + entry.getValue();
      }
    }
    gp.ui.currentSelection = "Your order:" + orderString + "\nConfirm Order\nQuit";
  }

  public void orderConfirmation() {
    gp.ui.selectionContext = "confirmation";
    gp.ui.currentSelection = "Thank you for your order!\nYour order will be delivered in the next few days.";
  }

}
