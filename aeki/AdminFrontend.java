package aeki;

public class AdminFrontend extends Frontend {
  /**
   * Constructor for AdminFrontend.
   * the constructor calls the super constructor from the parent class Frontend
   */
  public AdminFrontend() {
    super();
  }

  /**
   * this method is used to display the menu for the admin
   * it overrides the normal frontend menu
   */
  @Override
  public void setUserInputOptions() {
    this.userInputOptions = "\nHi there, admin!\n" +
        "You have the following options:\n\n" +
        "(1) Show all Database Tables\n" +
        "(0) Exit\n";
  }

  /**
   * this method handles the user input for the admin
   * it overrides the normal frontend commands
   */
  @Override
  public void handleUserInput(int userInput) {
    switch (userInput) {
      case 1:
        this.aekiBackend.db.printAllTables();
        break;
      case 0:
        this.stopFrontend();
        break;
      default:
        System.out.println("This option is not available. Please try again.");
        break;
    }
  }
}
