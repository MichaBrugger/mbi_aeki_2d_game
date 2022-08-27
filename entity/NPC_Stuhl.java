package entity;

import main.GamePanel;

public class NPC_Stuhl extends Entity {

  GamePanel gp;

  public NPC_Stuhl(GamePanel gp) {
    super(gp);

    this.gp = gp;
    direction = "down";
    getImage();
    setInteraction();
  }

  public void getImage() {

    up1 = setup("/objects/chair00");
    up2 = setup("/objects/chair00");
    down1 = setup("/objects/chair00");
    down2 = setup("/objects/chair00");
    left1 = setup("/objects/chair00");
    left2 = setup("/objects/chair00");
    right1 = setup("/objects/chair00");
    right2 = setup("/objects/chair00");
  }

  @Override
  public void update() {
    setAction();
  }

  public void setInteraction() {
    interactions[0] = "Was ein schöner Stuhl!";
  }

  public void setAction() {
    actionLockCounter++;
    if (actionLockCounter == 10) {
      if (worldY < 7 * gp.tileSize - 20) {
        worldY += 4;
        actionLockCounter = 0;
        return;
      }
      if (worldX <= gp.tileSize * 20) {
        worldX += 4;
      }
      actionLockCounter = 0;
    }
  }

  public void speak() {
    super.speak();
  }
}
