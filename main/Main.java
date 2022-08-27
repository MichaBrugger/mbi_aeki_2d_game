package main;

import javax.swing.JFrame;
import aeki.Software;

public class Main {
  public static void main(String[] args) {
 Software aeki = new Software();

    JFrame window = new JFrame();
    window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    window.setResizable(false);
    window.setTitle("AEKI 2D");

    GamePanel gamePanel = new GamePanel(aeki);
    window.add(gamePanel);
    window.pack();

    // displays the window at the center of the screen
    window.setLocationRelativeTo(null);
    window.setVisible(true);

    gamePanel.setupGame();
    gamePanel.startGameThread();
    
  }
  
}