package aeki;

import java.util.*;

public class Order {
  private String id;
  public HashMap<Product, Integer> items;
  private static int counter;

  /**
   * Constructor for Order
   * 
   * @param id    the id of the order
   * @param items the items in the order (product, quantity)
   */
  public Order(HashMap<Product, Integer> newOrderItems) {
    Order.counter++;
    this.id = "OR" + counter;
    this.items = newOrderItems;
  }

  /**
   * Getter for the order id
   * 
   * @return the id of the order
   */
  public String getId() {
    return id;
  }

  /**
   * Getter for the items in the order
   * 
   * @return the items in the order (product, quantity)
   */
  public HashMap<Product, Integer> getItems() {
    return items;
  }

  /**
   * Prints the order in a readable format (id, product, quantity)
   * (we added this for convenience)
   */
  public void print() {
    System.out.println("-----\n" + this.id + ":");
    for (Product prod : this.items.keySet()) {
      System.out.println(prod.name + " (" + this.items.get(prod) + ")");
    }
    System.out.println("-----");
  }
}