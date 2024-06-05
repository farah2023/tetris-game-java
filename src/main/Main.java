package main;

import javax.swing.JFrame;

public class Main {

	public static void main(String[] args) {
		
    JFrame window = new JFrame("Simple Terits");
    window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    window.setResizable(false);
    // Create an instance of the strategy
    MinoPickingStrategy strategy = new RandomMinoPickingStrategy();
    // Add gamePanel to the window
    GameFacade gf = new GameFacade(strategy);
    gf.setMinoPickingStrategy(new SequentialMinoPickingStrategy());
    window.add(gf);
    window.pack();
    
    window.setLocationRelativeTo(null);
    window.setVisible(true);
    
    gf.launchGame();

	}

}
