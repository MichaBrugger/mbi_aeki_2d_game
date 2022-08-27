package main;

import java.awt.*;
import javax.swing.JPanel;

import entity.Entity;
import entity.Player;
import object.SuperObject;
import tile.TileManager;
import aeki.Machine;
import aeki.Software;

public class GamePanel extends JPanel implements Runnable {

  // Aeki Software
  Software aeki;

  // Screen Settings
  final int originalTileSize = 16; // 16x16 tile
  final int scale = 3;

  public final int tileSize = originalTileSize * scale; // 48x48 tile
  public final int maxScreenCol = 16; // 16 tiles wide
  public final int maxScreenRow = 12; // 12 tiles high

  public final int screenWidth = maxScreenCol * tileSize; // 768 pixels wide
  public final int screenHeight = maxScreenRow * tileSize; // 576 pixels high

  // World Settings
  public final int maxWorldCol = 30;
  public final int maxWorldRow = 20;
  public final int worldWidth = maxWorldCol * tileSize;
  public final int worldHeight = maxWorldRow * tileSize;

  // FPS
  int FPS = 60;

  public TileManager tileM = new TileManager(this);
  public KeyHandler keyH = new KeyHandler(this);
  public CollisionChecker cChecker = new CollisionChecker(this);
  public AssetSetter aSetter = new AssetSetter(this);
  public UI ui = new UI(this);
  public EventHandler eHandler = new EventHandler(this);
  public Thread gameThread;

  // Entity and Object
  public Player player = new Player(this, keyH);
  public SuperObject obj[] = new SuperObject[10];
  public Entity npc[] = new Entity[10];

  // Game State
  public int gameState;
  public final int menuState = 0;
  public final int playState = 1;
  public final int pauseState = 2;
  public final int interactionState = 3;
  public final int selectionState = 4;

  public GamePanel(Software aeki) {
    this.aeki = aeki;

    this.setPreferredSize(new Dimension(screenWidth, screenHeight));
    this.setBackground(Color.black);
    // Double buffering can improve a game's rendering performance
    this.setDoubleBuffered(true);
    this.addKeyListener(keyH);
    this.setFocusable(true);

    aeki.backend.pipeline.gp = this;
    for (Machine m : aeki.backend.db.machines) {
      m.gp = this;
    }
  }

  public void setupGame() {
    aSetter.setObject();
    aSetter.setNPC();
    gameState = menuState;
  }

  public void startGameThread() {
    gameThread = new Thread(this);
    gameThread.start();
  }

  public void run() {

    double drawInterval = 1000000000 / FPS; // 0.0166 seconds
    double delta = 0;
    long lastTime = System.nanoTime();
    long currentTime;
    long timer = 0;
    int drawCount = 0;

    while (gameThread != null) {

      currentTime = System.nanoTime();
      delta += (currentTime - lastTime) / drawInterval;
      timer += (currentTime - lastTime);
      lastTime = currentTime;

      if (delta >= 1) {
        update();
        repaint();
        delta--;
        drawCount++;
      }

      if (timer >= 1000000000) {
        drawCount = 0;
        timer = 0;
      }

    }
  }

  public void update() {

    if (gameState == playState) {
      // Player
      player.update();
      // NPC
      for (int i = 0; i < npc.length; i++) {
        if (npc[i] != null) {
          npc[i].update();
        }
      }
    }
    if (gameState == pauseState) {
    }
  }

  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2 = (Graphics2D) g;

    // Debug
    long drawStart = 0;
    if (keyH.debugPressed == true) {
      drawStart = System.nanoTime();
    }
    // Menu Screen
    if (gameState == menuState) {
      ui.draw(g2);
    } else {
      // Tile
      tileM.draw(g2);

      // Object
      for (int i = 0; i < obj.length; i++) {
        if (obj[i] != null) {
          obj[i].draw(g2, this);
        }
      }

      // NPC
      for (int i = 0; i < npc.length; i++) {
        if (npc[i] != null) {
          npc[i].draw(g2);
        }
      }

      // Player
      player.draw(g2);

      // UI
      ui.draw(g2);
    }

    // Debug
    if (keyH.debugPressed == true) {
      long drawEnd = System.nanoTime();
      long passed = (drawEnd - drawStart);
      g2.setColor(Color.white);
      g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 14F));
      g2.drawString("FPS: " + FPS, 10, 520);
      g2.drawString("Drawing time: " + passed + " ns", 10, 560);
      g2.drawString("Player Position: " + (player.worldX / tileSize) + ", " + (player.worldY / tileSize), 10, 540);
    }

    g2.dispose();
  }
}
