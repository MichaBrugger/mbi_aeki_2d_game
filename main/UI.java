package main;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import java.awt.image.BufferedImage;

public class UI {

  GamePanel gp;
  Graphics2D g2;
  Font maruMonica;

  public String currentInteraction = "";
  public String currentSelection = "";
  public String selectionContext = "";

  public int menuCommandNum = 0;
  public int selectionCommandNum = 1;
  public int orderAmount = 0;
  int overflowStartY;

  // admin interace variables that can be updated from the backend
  public String waitingQueue = "";
  public String workingQueue = "";
  String orderString = "";
  String warehouseString = "";

  public UI(GamePanel gp) {
    this.gp = gp;
    this.overflowStartY = gp.tileSize;

    importFont();
  }

  /**
   * Puts the current and waiting and working queue together to show the whole
   * pipeline
   * 
   * @return the pipeline as a string
   */
  public String getPipeline() {
    return workingQueue + waitingQueue;
  }

  public void setOrderString(String orderString) {
    this.orderString = orderString;
  }

  public void setWarehouseString(String materialsString) {
    this.warehouseString = materialsString;
  }

  public String getOrderString() {
    return orderString;
  }

  public String getWarehouseString() {
    return warehouseString;
  }

  public void draw(Graphics2D g2) {
    this.g2 = g2;

    g2.setFont(maruMonica);
    g2.setColor(Color.WHITE);

    // MenuState
    if (gp.gameState == gp.menuState) {
      drawMenuScreen();
    }

    // PlayState
    if (gp.gameState == gp.playState) {
      // playState things
    }

    // PauseState
    if (gp.gameState == gp.pauseState) {
      drawPauseScreen();
    }

    // InteractionState
    if (gp.gameState == gp.interactionState) {
      drawInteractionScreen();
    }
    if (gp.gameState == gp.selectionState) {
      drawSelectionScreen();
    }
  }

  // Menu Screen
  public void drawMenuScreen() {

    // Menu Title
    g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 96F));
    String text = "AEKI - A Game";
    int x = getXValueToCenter(text);
    int y = gp.tileSize * 3;

    // Shadow
    g2.setColor(Color.gray);
    g2.drawString(text, x + 3, y + 3);
    // Main Color
    g2.setColor(Color.white);
    g2.drawString(text, x, y);

    // Image
    x = gp.screenWidth / 2 - gp.tileSize * 2 / 2;
    y += gp.tileSize * 1.5;
    g2.drawImage(gp.player.down1, x, y, gp.tileSize * 2, gp.tileSize * 2, null);

    // Menu
    g2.setFont(g2.getFont().deriveFont(Font.BOLD, 48F));

    // Play
    text = "New Game";
    x = getXValueToCenter(text);
    y += gp.tileSize * 4;
    g2.drawString(text, x, y);

    if (menuCommandNum == 0) {
      g2.drawString(">", x - gp.tileSize / 4 * 3, y);
    }

    // Options
    text = "Options";
    x = getXValueToCenter(text);
    y += gp.tileSize;
    g2.drawString(text, x, y);

    if (menuCommandNum == 1) {
      g2.drawString(">", x - gp.tileSize / 4 * 3, y);
    }

    // Quit
    text = "Quit";
    x = getXValueToCenter(text);
    y += gp.tileSize;
    g2.drawString(text, x, y);

    if (menuCommandNum == 2) {
      g2.drawString(">", x - gp.tileSize / 4 * 3, y);
    }

  }

  public void drawPauseScreen() {
    g2.setFont(g2.getFont().deriveFont(Font.BOLD, 40));
    String text = "PAUSED";
    int x = getXValueToCenter(text);
    int y = gp.screenHeight / 2 - 50;

    g2.drawString(text, x, y);
  }

  public void drawInteractionScreen() {

    // Window
    int x = gp.tileSize * 2;
    int y = gp.tileSize / 2;
    int width = gp.screenWidth - gp.tileSize * 4;
    int height = gp.tileSize * 4;

    drawInteractionWindow(x, y, width, height);
    g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 32F));
    x += gp.tileSize;
    y += gp.tileSize;
    for (String line : currentInteraction.split("\n")) {
      g2.drawString(line, x, y);
      y += 40;
    }
  }

  public void drawInteractionWindow(int x, int y, int width, int height) {
    Color c = new Color(0, 0, 0, 200);
    g2.setColor(c);
    g2.fillRoundRect(x, y, width, height, 35, 35);

    c = new Color(255, 255, 255, 200);
    g2.setColor(c);
    g2.setStroke(new BasicStroke(5));
    g2.drawRoundRect(x + 5, y + 5, width - 10, height - 10, 25, 25);
  }

  public void drawSelectionScreen() {
    // Window
    int x = gp.tileSize * 2;
    int y = gp.tileSize / 2;
    int width = gp.screenWidth - gp.tileSize * 4;
    int height = gp.tileSize * 4;

    if (currentSelection.split("\n").length > 3) {
      height += gp.tileSize * (currentSelection.split("\n").length - 4);
    }

    if (height >= gp.screenHeight) {
      height = gp.screenHeight - gp.tileSize * 2;
    }

    drawInteractionWindow(x, y, width, height);

    g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 32F));
    x += gp.tileSize;
    y += gp.tileSize;

    String[] currSel = currentSelection.split("\n");
    g2.drawString(currSel[0], x, y);

    for (int i = 1; i < currSel.length; i++) {
      y = 40 * i + overflowStartY - gp.tileSize / 3;
      if (y < gp.screenHeight - gp.tileSize * 2 && y > gp.tileSize * 2) {
        if (selectionCommandNum * 40 + overflowStartY > gp.screenHeight - gp.tileSize * 2) {
          overflowStartY -= 40;
          break;
        }
        if (selectionCommandNum * 40 + overflowStartY < gp.tileSize * 2) {
          overflowStartY += 40;
          break;
        }
        if (i == selectionCommandNum) {
          g2.setColor(Color.white);
          g2.drawString(">", x - gp.tileSize / 2, y);
          g2.drawString(currSel[i], x, y);
        } else {
          g2.setColor(Color.gray);
          g2.drawString(currSel[i], x, y);
          g2.setColor(Color.white);
        }
      }
    }

    showProductOnSelection();
  }

  public void showProductOnSelection() {
    int x = gp.screenWidth / 2 + gp.tileSize * 2;
    int y = gp.tileSize * 7 / 4;

    if (selectionContext.equals("displayProducts")) {
      for (int i = 0; i < gp.aeki.backend.db.products.size(); i++) {
        if (selectionCommandNum == i + 1) {

          String path;
          switch (gp.aeki.backend.db.products.get(i).getName()) {
            case "Sofa":
              path = "/res/objects/sofa0.png";
              break;
            case "Stuhl":
              path = "/res/objects/chair00.png";
              break;
            default:
              path = "/res/objects/heart_blank.png";
              break;
          }
          try {
            BufferedImage prod = ImageIO.read(getClass().getResourceAsStream(path));
            g2.drawImage(prod, x, y, gp.tileSize * 2, gp.tileSize * 2, null);
          } catch (IOException e) {
            e.printStackTrace();
          }
        }
      }
    }
  }

  public int getXValueToCenter(String text) {
    int length = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
    int x = gp.screenWidth / 2 - length / 2;
    return x;
  }

  /**
   * importing the font from the resources folder
   */
  private void importFont() {
    try {
      InputStream is = getClass().getResourceAsStream("/res/font/x12y16pxMaruMonica.ttf");
      maruMonica = Font.createFont(Font.TRUETYPE_FONT, is);
    } catch (FontFormatException | IOException e) {
      e.printStackTrace();
    }
  }

}
