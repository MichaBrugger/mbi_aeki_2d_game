package object;

import java.io.IOException;
import javax.imageio.ImageIO;

import main.GamePanel;

public class OBJ_Computer extends SuperObject {

  GamePanel gp;
  int x, y;

  public OBJ_Computer(GamePanel gp, int x, int y) {
    name = "Computer";
    this.gp = gp;
    this.worldX = x * gp.tileSize;
    this.worldY = y * gp.tileSize;

    collision = true;

    try {
      image = ImageIO.read(getClass().getResourceAsStream("/res/objects/computer00.png"));
      uTool.scaleImage(image, gp.tileSize, gp.tileSize);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}