package object;

import java.io.IOException;
import javax.imageio.ImageIO;

import main.GamePanel;

public class OBJ_Sofa extends SuperObject {
  GamePanel gp;
  int x, y;
  int actionLockCounter = 0;
  int spriteNum = 1;

  public OBJ_Sofa(GamePanel gp, int x, int y) {
    name = "Sofa";
    this.gp = gp;
    this.worldX = x * gp.tileSize;
    this.worldY = y * gp.tileSize;

    try {
      image = ImageIO.read(getClass().getResourceAsStream("/res/objects/tableSmall00.png"));
      uTool.scaleImage(image, gp.tileSize, gp.tileSize);
    } catch (IOException e) {
      e.printStackTrace();
    }

    collision = true;
  }

}
