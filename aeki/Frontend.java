package aeki;

import java.util.*;
import java.util.Scanner;

public class Frontend {

  Scanner scanner = new Scanner(System.in);
  public Backend aekiBackend;
  private boolean isRunning = false;
  public String userInputOptions;

  /**
   * constructor for the frontend class
   * 
   * @param aekiBackend      the aeki backend instantiated in the Software class
   * @param isRunning        the boolean value for the isRunning variable, this is
   *                         only true if the frontend successfully connected to
   *                         the backend -> check this.connectToBackend()
   * @param userInputOptions the string value for the userInputOptions that are
   *                         displayed in the command line, it can be set by
   *                         this.setUserInputOptions()
   *                         the reason its handled like this is because the
   *                         setUserInputOptions() method can be (and is)
   *                         overridden in the child-class AdminFrontend
   */
  public Frontend() {
    setUserInputOptions();
    System.out.println("Frontend booting up...");
  }

  /**
   * sets the userInputOptions, check the class constructor for more details
   */
  public void setUserInputOptions() {
    this.userInputOptions = "\nWelcome to AEKI!\n" +
        "You have the following options:\n\n" +
        "(1) Show all available Products\n" +
        "(2) Create a new Order\n" +
        "(3) Show all my Orders\n" +
        "(0) Exit\n";
  }

  /**
   * purpose: used to connect the frontend to the backend
   * (for more details, check the constructor and the software class)
   * it sets isRunning to true if the connection was successful
   * 
   * -> calls this.displayFrontend() that displays the frontend
   *
   * @param aekiBackend the aeki backend instantiated in the Software class
   */
  public void connectToBackend(Backend backend) {
    this.aekiBackend = backend;
    System.out.println("Connection to Backend established...");
    isRunning = true;
    // displayFrontend();
  }

  /**
   * purpose: starts the process to display the frontend and handle the users
   * input, for that, it creates a while loop that runs as long as the isRunning
   * variable is true
   */
  private void displayFrontend() {
    while (isRunning) {
      this.userInput(userInputOptions);
    }
  }

  public void stopFrontend() {
    this.isRunning = false;
    scanner.close();
  }

  /**
   * this method displays the userInputOptions to the user
   * and receives the
   *
   * @param userInputOptions the string value for the userInputOptions that are
   *                         displayed in the command line, it can be set by
   *                         this.setUserInputOptions()
   */
  public void userInput(String inputOptions) {
    this.printInputOptions(inputOptions);
    int input = returnUserInput();
    this.handleUserInput(input);
  }

  private int returnUserInput() {
    try {
      int command = scanner.nextInt();
      return command;
    } catch (Exception e) {
      System.out.println("Unfortunately, your input is not valid. Please try again.");
      return -1;
    }
  }

  /**
   * handles the user input and calls the appropriate method
   * 
   * @param userInput the user input that is received from the user
   */
  public void handleUserInput(int userInput) {
    switch (userInput) {
      case 1:
        this.displayAllProducts();
        break;
      case 2:
        this.createNewOrder();
        break;
      case 3:
        this.showMyOrders();
        break;
      case 0:
        this.stopFrontend();
        break;
      default:
        System.out.println("This option is not available. Please try again.");
        break;
    }
  }

  /**
   * prints the input options received from this.userInput() to the console
   * 
   * @param options the string value for the userInputOptions that are displayed
   */
  public void printInputOptions(String options) {
    System.out.println(options);
  }

  private void displayAllProducts() {
    this.aekiBackend.db.getTableByName("products").print();
  }

  private void createNewOrder() {
    // Table products = this.aekiBackend.db.getTableByName("products");
    HashMap<Product, Integer> orderList = new HashMap<>();

    for (Product product : this.aekiBackend.db.products) {
      System.out.println("How many " + product.getName() + " would you like to order?");
      try {
        Integer userinput = scanner.nextInt();
        if (userinput > 0) {
          orderList.put(product, userinput);
        }
      } catch (Exception e) {
        System.out.println("This product is not available. Please try again.");
      }

    }
    // if the orderList is not empty a new order is created
    if (orderList.size() > 0) {
      Order newOrder = new Order(orderList);
      aekiBackend.newOrderInSystem(newOrder);
    } else {
      System.out.println("No products were ordered. Please try again.");
    }
  }

  /**
   * shows all the orders that are in the system from the user perspective
   */
  private void showMyOrders() {
    this.aekiBackend.pipeline.printLog();
  }

}
