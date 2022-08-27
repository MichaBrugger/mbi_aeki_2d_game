package aeki;

import java.util.Scanner;

public class Software implements Runnable {
  Scanner scanner = new Scanner(System.in);
  public Backend backend;
  public Frontend frontend;
  boolean isAdmin = false;

  /**
   * constructor method for the aeki software
   * 
   * @param aekiBackend  the backend of the software
   * @param aekiFrontend the frontend of the software
   * @param isAdmin      whether the user is an admin or not
   *                     - see this.checkIfAdmin()
   */
  public Software() {
    // System.out.println("Would you like to run Smart Pipeline as an admin?
    // (y/n)");

    this.startSoftware();
    // try {
    // String input = scanner.nextLine();
    // if (input.equals("y")) {
    // isAdmin = true;
    // }
    // } catch (Exception e) {
    // System.out.println("Software failed to start up...");
    // e.printStackTrace();
    // }
  }

  @Override
  public void run() {
    // TODO Auto-generated method stub
    this.startSoftware();
  }

  /**
   * starts the software by creating an instance of the backend and frontend
   * this serves as an abstraction layer for the software-startup process,
   * for the frontend there are additional steps executed in in lower level
   * methods
   */
  private void startSoftware() {
    backend = new Backend();
    // this.startFrontend();
  }

  /**
   * abstraction of lower level methods to start the frontend:
   * checks if the user is admin, creates the respective frontend and
   * connects the backend to it
   */
  private void startFrontend() {
    this.checkIfAdmin();
    this.createCorrectFrontend();
    this.connectFrontendToBackend();
  }

  /**
   * since AdminFrontend is a subclass of Frontend, we can use dynamic
   * polymorphism to create the correct frontend for the respective user-type
   */
  private void createCorrectFrontend() {
    if (isAdmin) {
      frontend = new AdminFrontend();
    } else {
      frontend = new Frontend();
    }
  }

  /**
   * checks if the user is an admin
   * currently this is done by asking the user in the UI if they are admin (y/n)
   * but we're looking to implement a login functionality in the future
   * 
   * @return true if the user is an admin
   */
  private boolean checkIfAdmin() {
    return isAdmin;
  }

  /**
   * the frontend needs access to the backend in order to display the right data
   * to the user
   */
  private void connectFrontendToBackend() {
    this.frontend.connectToBackend(this.backend);
  }

}
