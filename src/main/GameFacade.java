package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

public class GameFacade  extends JPanel implements Observer ,Runnable {
	public static final int WIDTH=1200;		
	public static final int HEIGHT=700;	
	final int FPS =60;
	Thread gameThread;
	
	PlayManager pm;
	KeyHandler keyhandler =new KeyHandler();
	
	public GameFacade(MinoPickingStrategy minoPickingStrategy)
	{
		this.setPreferredSize(new Dimension(WIDTH,WIDTH));
		this.setBackground(Color.WHITE);
		this.setLayout(null);
		//implement key listener
		this.addKeyListener(keyhandler);
		this.setFocusable(true);
		
		  pm = new PlayManager(minoPickingStrategy);
		  
		  pm.addObserver(this);
	}
	  public void setMinoPickingStrategy(MinoPickingStrategy minoPickingStrategy) {
	       pm.setMinoPickingStrategy(minoPickingStrategy);
	    }
	public void launchGame() {
		gameThread = new Thread(this);
		gameThread.start();
	}

	@Override
	public void run() {
		System.out.print("run");
		// game loop
		double drawInterval=1000000000/FPS;
		double delta =0;
		long lastTime = System.nanoTime();
		long currentTime;
		
		while (gameThread != null) {
			currentTime=System.nanoTime();
			delta +=(currentTime - lastTime )/drawInterval;
			lastTime = currentTime;
			if (delta >= 1) {
				update();
				repaint();
				delta--;
			}
		}
		
	}
	
	private void update() {
		  if(KeyHandler.pausePressed == false) {
			  	   pm.update();
		  }

		}
	  public void paintComponent(Graphics g) {
		  super.paintComponent(g);
		  
		  Graphics2D g2=(Graphics2D)g;
		  pm.draw(g2);
		
		}

@Override
public void update(Observable observable) {
	  System.out.println("Game state updated");
}

}
