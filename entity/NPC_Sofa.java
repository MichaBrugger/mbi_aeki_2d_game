package entity;

import main.GamePanel;

public class NPC_Sofa extends Entity {

  GamePanel gp;

  public NPC_Sofa(GamePanel gp) {
    super(gp);
    this.gp = gp;
    speed = 0;
    direction = "down";

    getImage();
    setInteraction();
  }

  public void getImage() {

    down1 = setup("/objects/sofa0");
    down2 = setup("/objects/sofa0");
    up1 = setup("/objects/sofa01");
    up2 = setup("/objects/sofa02");
    left1 = setup("/objects/sofa03");
    left2 = setup("/objects/sofa04");
    right1 = setup("/objects/empty");
    right2 = setup("/objects/empty");
  }

  public void setInteraction() {
    interactions[0] = "Ein Traum von einem Sofa!";
  }

  public void setAction() {

    actionLockCounter++;
    if (actionLockCounter == 10) {
      if (worldY < 7 * gp.tileSize - 20) {
        worldY += 4;
        actionLockCounter = 0;
        return;
      }

      if (worldX >= gp.tileSize * 9) {
        worldX -= 4;
      } else {
        switch (direction) {
          case "down":
            if (spriteNum == 1) {
              direction = "down";
            } else {
              direction = "up";
            }
            break;
          case "up":
            if (spriteNum == 1) {
              direction = "up";
            } else {
              direction = "left";
            }
            break;
          case "left":
            if (spriteNum == 1) {
              direction = "left";
            } else {
              direction = "right";
            }
            break;
          case "right":
            if (spriteNum == 1) {
              direction = "right";
            } else {
              spriteNum = 2;
            }
            break;
        }
      }

      actionLockCounter = 0;
    }
  }

  public void speak() {
    super.speak();
  }
}
