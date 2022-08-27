package main;

import entity.NPC_Customer;
import entity.NPC_Sofa;
import entity.NPC_Stuhl;
import object.OBJ_Computer;
import object.OBJ_Door;
import object.OBJ_TableSmall;

public class AssetSetter {

  GamePanel gp;
  public int productCounter = 1;

  public AssetSetter(GamePanel gp) {
    this.gp = gp;
  }

  public void setObject() {
    gp.obj[0] = new OBJ_TableSmall(gp, 10, 8);
    gp.obj[1] = new OBJ_TableSmall(gp, 12, 8);
    gp.obj[2] = new OBJ_Computer(gp, 10, 8);
    gp.obj[3] = new OBJ_Computer(gp, 12, 8);
    gp.obj[4] = new OBJ_Door(gp, 15, 5);
    gp.obj[5] = new OBJ_Door(gp, 15, 15);
  }

  public void setNPC() {
    gp.npc[0] = new NPC_Customer(gp);
    gp.npc[0].setPosition(14, 14);
  }

  public void displayProductInProduction(String product) {
    if (productCounter > 4) {
      productCounter = 1;
    }
    switch (product) {
      case "Sofa":
        gp.npc[productCounter] = new NPC_Sofa(gp);
        gp.npc[productCounter].setPosition(14, 5);
        break;
      case "Stuhl":
        gp.npc[productCounter] = new NPC_Stuhl(gp);
        gp.npc[productCounter].setPosition(16, 5);
        break;
      default:
        return;
    }
    productCounter++;
  }

}
