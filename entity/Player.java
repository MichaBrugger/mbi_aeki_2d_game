package entity;

import main.GamePanel;
import main.KeyHandler;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Player extends Entity {

  KeyHandler keyH;

  public final int screenX;
  public final int screenY;

  public Player(GamePanel gp, KeyHandler keyH) {

    super(gp);

    this.keyH = keyH;

    screenX = (gp.screenWidth / 2) - (gp.tileSize / 2);
    screenY = (gp.screenHeight / 2) - (gp.tileSize / 2);

    solidArea = new Rectangle();
    solidArea.x = 8;
    solidArea.y = 16;

    solidAreaDefaultX = solidArea.x;
    solidAreaDefaultY = solidArea.y;
    solidArea.width = 32;
    solidArea.height = 32;

    setDefaultValues();
    getImage();
  }

  public void setDefaultValues() {
    this.setPosition(10, 10);
    speed = 4;
    direction = "down";
  }

  public void getImage() {
    up1 = setup("/player/player07");
    up2 = setup("/player/player10");
    down1 = setup("/player/player23");
    down2 = setup("/player/player19");
    left1 = setup("/player/player14");
    left2 = setup("/player/player15");
    right1 = setup("/player/player02");
    right2 = setup("/player/player04");
  }

  public void update() {

    if (keyH.upPressed == true ||
        keyH.downPressed == true ||
        keyH.leftPressed == true ||
        keyH.rightPressed == true) {
      if (keyH.upPressed == true) {
        direction = "up";
      } else if (keyH.downPressed == true) {
        direction = "down";
      } else if (keyH.leftPressed == true) {
        direction = "left";
      } else if (keyH.rightPressed == true) {
        direction = "right";
      }

      // Check tile collision
      collisionOn = false;
      gp.cChecker.checkTile(this);

      // check object collision
      int objIndex = gp.cChecker.checkObject(this, true);
      this.pickUpObject(objIndex);

      // check NPC collision
      int npcIndex = gp.cChecker.checkEntity(this, gp.npc);
      interactNPC(npcIndex);

      // check Event
      gp.eHandler.checkEvent();

      gp.keyH.spacePressed = false;

      if (collisionOn == false) {
        switch (direction) {
          case "up":
            worldY -= speed;
            break;
          case "down":
            worldY += speed;
            break;
          case "left":
            worldX -= speed;
            break;
          case "right":
            worldX += speed;
            break;
        }
      }

      spriteCounter++;

      if (spriteCounter > 10) {
        if (spriteNum == 1) {
          spriteNum = 2;
        } else if (spriteNum == 2) {
          spriteNum = 1;
        }
        spriteCounter = 0;
      }
    }
  }

  public void pickUpObject(int i) {
    if (i != 999) {

    }
  }

  public void interactNPC(int i) {
    if (i != 999) {
      if (gp.keyH.spacePressed == true) {
        gp.gameState = gp.interactionState;
        gp.npc[i].speak();
      }
    }
  }

  public void draw(Graphics2D g2) {

    BufferedImage image = null;

    switch (direction) {
      case "up":
        if (spriteNum == 1) {
          image = up1;
        }
        if (spriteNum == 2) {
          image = up2;
        }
        break;
      case "down":
        if (spriteNum == 1) {
          image = down1;
        }
        if (spriteNum == 2) {
          image = down2;
        }
        break;
      case "left":
        if (spriteNum == 1) {
          image = left1;
        }
        if (spriteNum == 2) {
          image = left2;
        }
        break;
      case "right":
        if (spriteNum == 1) {
          image = right1;
        }
        if (spriteNum == 2) {
          image = right2;
        }
        break;
      default:
        System.out.println("Error: Player.draw()");
        break;
    }

    g2.drawImage(image, screenX, screenY, null);
    if (gp.keyH.debugPressed == true) {
      g2.setColor(Color.RED);
      g2.drawRect(screenX + solidArea.x, screenY + solidArea.y, solidArea.width, solidArea.height);
    }
  }
}
