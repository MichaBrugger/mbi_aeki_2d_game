package entity;

import java.util.Random;

import main.GamePanel;

public class NPC_Customer extends Entity {

  public NPC_Customer(GamePanel gp) {
    super(gp);

    direction = "down";
    speed = 1;
    getImage();
    setInteraction();
    setSelectionOptions();
  }

  public void getImage() {

    up1 = setup("/npc/red/red_08");
    up2 = setup("/npc/red/red_09");
    down1 = setup("/npc/red/red_20");
    down2 = setup("/npc/red/red_23");
    left1 = setup("/npc/red/red_14");
    left2 = setup("/npc/red/red_16");
    right1 = setup("/npc/red/red_02");
    right2 = setup("/npc/red/red_04");
  }

  public void setInteraction() {
    interactions[0] = "Hello, friend!\nI have a few items for sale.";
    interactions[1] = "Hello, friend!1";
    interactions[2] = "Hello, friend!2";
    interactions[3] = "Hello, friend!3";
  }

  public void setAction() {

    actionLockCounter++;
    if (actionLockCounter == 60) {

      Random random = new Random();
      int i = random.nextInt(100) + 1;

      if (i <= 25) {
        direction = "up";
      }
      if (i > 25 && i <= 50) {
        direction = "down";
      }
      if (i > 50 && i <= 75) {
        direction = "left";
      }
      if (i > 75 && i <= 100) {
        direction = "right";
      }
      actionLockCounter = 0;
    }
  }

  public void speak() {
    super.speak();
  }
}
