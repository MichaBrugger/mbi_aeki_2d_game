package object;

import java.io.IOException;

import javax.imageio.ImageIO;

import main.GamePanel;

public class OBJ_Door extends SuperObject {
  GamePanel gp;
  int x, y;

  public OBJ_Door(GamePanel gp, int x, int y) {
    name = "Door";
    this.gp = gp;
    this.worldX = x * gp.tileSize;
    this.worldY = y * gp.tileSize;
    try {
      image = ImageIO.read(getClass().getResourceAsStream("/res/objects/door.png"));
      uTool.scaleImage(image, gp.tileSize, gp.tileSize);
    } catch (IOException e) {
      e.printStackTrace();
    }

    collision = true;
  }
}