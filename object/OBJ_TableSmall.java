package object;

import java.io.IOException;
import javax.imageio.ImageIO;

import main.GamePanel;

public class OBJ_TableSmall extends SuperObject {

  GamePanel gp;
  int x, y;

  public OBJ_TableSmall(GamePanel gp, int x, int y) {
    name = "Computer";
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