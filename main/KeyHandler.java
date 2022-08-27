package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.security.Key;

import aeki.Order;

public class KeyHandler implements KeyListener {

  GamePanel gp;
  public boolean upPressed, downPressed, leftPressed, rightPressed, spacePressed;
  // Debug
  public boolean debugPressed = false;

  public KeyHandler(GamePanel gp) {
    this.gp = gp;
  }

  @Override
  public void keyPressed(KeyEvent e) {

    int code = e.getKeyCode();

    // MenuState
    if (gp.gameState == gp.menuState) {
      if (code == KeyEvent.VK_W) {
        gp.ui.menuCommandNum--;
        if (gp.ui.menuCommandNum < 0) {
          gp.ui.menuCommandNum = 2;
        }
      }
      if (code == KeyEvent.VK_S) {
        gp.ui.menuCommandNum++;
        if (gp.ui.menuCommandNum > 2) {
          gp.ui.menuCommandNum = 0;
        }
      }
      if (code == KeyEvent.VK_SPACE) {
        if (gp.ui.menuCommandNum == 0) {
          gp.gameState = gp.playState;
        }
        if (gp.ui.menuCommandNum == 1) {
          // TBD
        }
        if (gp.ui.menuCommandNum == 2) {
          System.exit(0);
        }
      }
    }
    // GameState
    if (gp.gameState == gp.playState) {
      if (code == KeyEvent.VK_W) {
        upPressed = true;
      }
      if (code == KeyEvent.VK_S) {
        downPressed = true;
      }
      if (code == KeyEvent.VK_A) {
        leftPressed = true;
      }
      if (code == KeyEvent.VK_D) {
        rightPressed = true;
      }
      if (code == KeyEvent.VK_P) {
        if (gp.gameState == gp.playState) {
          gp.gameState = gp.pauseState;
        } else if (gp.gameState == gp.pauseState) {
          gp.gameState = gp.playState;
        }
      }
      if (code == KeyEvent.VK_SPACE) {
        spacePressed = true;
      }

      // Debug
      if (code == KeyEvent.VK_T) {
        debugPressed ^= true;
      }
    }

    // PauseState
    else if (gp.gameState == gp.pauseState) {
      if (code == KeyEvent.VK_P) {
        gp.gameState = gp.playState;
      }
    }

    // InteractionState
    else if (gp.gameState == gp.interactionState) {
      if (code == KeyEvent.VK_SPACE) {
        gp.gameState = gp.playState;
      }
    }

    // SelectionState
    else if (gp.gameState == gp.selectionState) {
      int selectionsAvailable = gp.ui.currentSelection.split("\n").length - 1;

      // pressing esc will close the selection/interaction
      if (code == KeyEvent.VK_ESCAPE) {
        gp.gameState = gp.playState;
      }

      if (code == KeyEvent.VK_W) {
        if (gp.ui.selectionContext.equals("enterAmount")) {
          gp.ui.orderAmount++;
          gp.eHandler.cEvent.enterAmount();
        } else {
          gp.ui.selectionCommandNum--;
          if (gp.ui.selectionCommandNum < 1) {
            gp.ui.selectionCommandNum = selectionsAvailable;
          }
        }
      }
      if (code == KeyEvent.VK_S) {
        if (gp.ui.selectionContext.equals("enterAmount")) {
          gp.ui.orderAmount--;
          gp.eHandler.cEvent.enterAmount();
        } else {
          gp.ui.selectionCommandNum++;
          if (gp.ui.selectionCommandNum > selectionsAvailable) {
            gp.ui.selectionCommandNum = 1;
          }
        }
      }
      if (code == KeyEvent.VK_SPACE) {

        if (gp.ui.selectionContext.equals("checkout") || gp.ui.selectionContext.equals("makeOrder")
            || gp.ui.selectionContext.equals("displayProducts")) {
          System.out.println("space");
          handleSpaceForDynamicSelectionWindows(selectionsAvailable);
        } else {
          if (gp.ui.selectionCommandNum == 1) {
            switch (gp.ui.selectionContext) {
              case "customerPC":
                gp.eHandler.cEvent.displayProducts();
                break;
              case "enterAmount":
                gp.eHandler.cEvent.addToOrder();
                gp.eHandler.cEvent.makeOrder();
                break;
              case "adminPC":
                gp.eHandler.aEvent.displayPipeline();
                break;
            }
          }
          if (gp.ui.selectionCommandNum == 2) {
            switch (gp.ui.selectionContext) {
              case "customerPC":
                gp.eHandler.cEvent.makeOrder();
                break;
              case "adminPC":
                gp.eHandler.aEvent.displayOrders();
                break;
            }
          }
          if (gp.ui.selectionCommandNum == 3) {
            switch (gp.ui.selectionContext) {
              case "adminPC":
                gp.eHandler.aEvent.displayWarehouse();
                break;
            }
          }
        }
      }
    }

  }

  public void handleSpaceForDynamicSelectionWindows(int selectionsAvailable) {
    // Last option in the list the customer can select is always quit
    if (gp.ui.selectionCommandNum == selectionsAvailable) {
      gp.eHandler.quit();
    }
    // Second last option is always a special case
    if (gp.ui.selectionCommandNum == selectionsAvailable - 1) {
      // Make Order Screen
      if (gp.ui.selectionContext.equals("makeOrder") && gp.eHandler.cEvent.orderItems != null) {
        gp.eHandler.cEvent.proceedToCheckout();
      } else if (gp.ui.selectionContext.equals("checkout")) {
        Order newOrder = new Order(gp.eHandler.cEvent.orderItems);
        gp.aeki.backend.newOrderInSystem(newOrder);
        gp.eHandler.quit();
        gp.eHandler.cEvent.orderItems = null;
        gp.ui.currentInteraction = "Order Placed!";
        gp.gameState = gp.interactionState;
      }
    }

    if (gp.ui.selectionContext.equals("makeOrder")) {
      for (int i = 0; i < gp.aeki.backend.db.products.size(); i++) {
        if (gp.ui.selectionCommandNum == i + 1) {
          gp.eHandler.cEvent.getOrderProduct();
          gp.eHandler.cEvent.enterAmount();
        }
      }
    }
  }

  @Override
  public void keyReleased(KeyEvent e) {

    int code = e.getKeyCode();

    if (code == KeyEvent.VK_W) {
      upPressed = false;
    }
    if (code == KeyEvent.VK_S) {
      downPressed = false;
    }
    if (code == KeyEvent.VK_A) {
      leftPressed = false;
    }
    if (code == KeyEvent.VK_D) {
      rightPressed = false;
    }
  }

  @Override
  public void keyTyped(KeyEvent e) {
  }

}
