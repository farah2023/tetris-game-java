package main;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import mino.Block;
import mino.Mino;
import mino.Mino_Bar;
import mino.Mino_L1;
import mino.Mino_L2;
import mino.Mino_Square;
import mino.Mino_T;
import mino.Mino_Z1;
import mino.Mino_Z2;

public class PlayManager implements Observable  {
	//main play area
    final int WIDTH=360;		
    final int HEIGHT=600;	
    public static  int left_x;		
	public static  int right_x;	
	public static  int top_y;	
	public static  int bottom_y;	
	
	//Mino 
	Mino currentMino;
    final int MINO_START_X;
    final int MINO_START_Y;
    Mino nextMino;
    final int NEXTMINO_X;
    final int NEXTMINO_Y;
    public static ArrayList<Block> staticBlocks =new ArrayList<>();
    
    
    //others 
    public static  int dropInterval =60;
    boolean gameOver;
	
    //effect
    boolean effectConterOn;
    int effectConter;
    ArrayList<Integer> effectY =new ArrayList<>();
    
    //score
    int level=1;
    int lines;
    int score;
    
    private List<Observer> observers = new ArrayList<>();
    private GameState gameState;  // Example of a state that might change

    public void addObserver(Observer o) {
        observers.add(o);
    }

    public void removeObserver(Observer o) {
        observers.remove(o);
    }

    public void setGameState(GameState state) {
        if (this.gameState != state) { // Check if the new state is different from the current state
            this.gameState = state;
            System.out.println("Game state updated: " + state);
            notifyObservers(); // Notify observers only if the state has changed
        }
    }

    public GameState getGameState() {
        return gameState;
    }

    public void notifyObservers() {
        for (Observer observer : observers) {
            observer.update(null);
        }
    }


    private MinoPickingStrategy minoPickingStrategy;
   
	public PlayManager(MinoPickingStrategy minoPickingStrategy) {
		//main play area frame 
		left_x=(GameFacade.WIDTH/2)-(WIDTH/2);  right_x= left_x + WIDTH;top_y=50;   bottom_y = top_y+ HEIGHT;		MINO_START_X = left_x+ (WIDTH/2)-Block.SIZE;NEXTMINO_X= right_x +175; MINO_START_Y = top_y+ Block.SIZE; NEXTMINO_Y= top_y+500;
		
		 if (minoPickingStrategy != null) {
		 this.minoPickingStrategy = minoPickingStrategy;
	        // Initialize currentMino and nextMino using the strategy
	        currentMino = minoPickingStrategy.pickMino();//effectuer la strategy
	        currentMino.setXY(MINO_START_X, MINO_START_Y);
	        nextMino = minoPickingStrategy.pickMino();
	        nextMino.setXY(NEXTMINO_X, NEXTMINO_Y);
		 } else {
	            // Handle the case when strategy is null
	            throw new IllegalArgumentException("MinoPickingStrategy cannot be null");
	        }
		
	}
	 public void setMinoPickingStrategy(MinoPickingStrategy minoPickingStrategy) {
	        this.minoPickingStrategy = minoPickingStrategy;
	    }

	    private Mino pickMino() {
	        return minoPickingStrategy.pickMino();
	    }

	    public void update() {
	        // Check if the game is over
	        if (gameOver) {
	            setGameState(GameState.GAME_OVER);
	            return; // Exit the method early if the game is over
	        }

	        // Check if the current mino is inactive
	        if (currentMino.active== false) {
	            // Add the current mino to staticBlocks if it's not active
	            staticBlocks.add(currentMino.b[0]);
	            staticBlocks.add(currentMino.b[1]);
	            staticBlocks.add(currentMino.b[2]);
	            staticBlocks.add(currentMino.b[3]);

	            // Check if the current mino's position matches the starting position
	            if (currentMino.b[0].x == MINO_START_X && currentMino.b[0].y == MINO_START_Y) {
	                // If so, set game over to true
	                gameOver = true;
	            }

	            currentMino.deactivating = false;

	            // Replace current mino with the next one
	            currentMino = nextMino;
	            currentMino.setXY(MINO_START_X, MINO_START_Y);
	            nextMino = pickMino();
	            nextMino.setXY(NEXTMINO_X, NEXTMINO_Y);

	            // Check if any lines need to be deleted
	            checkDelete();
	        } else {
	            // If the current mino is active, update its position
	            currentMino.update();
	        }
	    }

	private void checkDelete() {
		int x =left_x;
		int y =top_y;
		int blockCount = 0;
		int lineCount =0;
	
	
	while(x <right_x && y < bottom_y) {
		
		for(int i =0 ; i<staticBlocks.size() ; i++) {
			if(staticBlocks.get(i).x == x && staticBlocks.get(i).y == y ) {
				blockCount++;
			}
		}
		
		x+= Block.SIZE;
		
		if(x == right_x) {
			
			if( blockCount == 12) {
				
				effectConterOn = true;
				effectY.add(y);
				for(int i = staticBlocks.size()-1; i> -1; i--) {
					if(staticBlocks.get(i).y  == y) {
						staticBlocks.remove(i);
					}
				}
				
				lineCount++;
				lines++;
				//drop speed
				//if line score hits a certain number ,increase the drop speed
				//1 is the fastest
				if(lines % 10 == 0 && dropInterval>1) {
					level++;
					if(dropInterval >10) {
						dropInterval-=10;
						
					}
					else {
						dropInterval-=1;
					}
				}
				
	
			//slide down blocks above 	deleted line
				for(int i =0 ;i<staticBlocks.size(); i++) {
			    //if a block is above the current y ,move it down with block size
					if(staticBlocks.get(i).y < y) {
						staticBlocks.get(i).y += Block.SIZE;
					}	
					
			}
			}
			blockCount = 0;
			x = left_x;
			y += Block.SIZE;
		}
	}
	if(lineCount>0) {
		int singleLineScore = 10 * level;
		score += singleLineScore * lineCount;
	}
	
	 if (lineCount > 0) {
         setGameState(GameState.LINE_CLEARED); // Call setGameState when a line is cleared
     }
	}
	
	
	
	public void draw(Graphics2D g2)	{
		//draw play area frame
		g2.setColor(Color.black);
		g2.setStroke(new BasicStroke (4f));
		g2.drawRect(left_x-4,top_y-4,WIDTH+8, HEIGHT+8 );
		
		//draw next mino Frame 
		int x= right_x+100;
		int y =bottom_y-200;
		g2.drawRect(x,y ,200,200 );
		g2.setFont(new Font ("Arial", Font.PLAIN, 30));
		g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g2.drawString("NEXT",x+60, y+60);
		
		//draw score frame
		g2.drawRect(x,top_y ,250,300 );
		x += 40;
		y= top_y+ 90;
		g2.drawString("level : " +level,x, y);y+=70;
		g2.drawString("Lines : " +lines,x, y);y+=70;
		g2.drawString("Score : " +score,x, y);
		
		
		
		//draw the currentMino
		if(currentMino!= null) {
			currentMino.draw(g2);
		}
		//draw next mino
		nextMino.draw(g2);
		
		//draw staticBlocks
		for(int i =0 ; i< staticBlocks.size() ; i++) {
			staticBlocks.get(i).draw(g2);
		}
		
		//drow effect
		
		if(effectConterOn) {
			effectConter++;
			
			g2.setColor(Color.red);
			for(int i =0 ; i< effectY.size() ; i++) {
				g2.fillRect(left_x,effectY.get(i), WIDTH, Block.SIZE);
			}
			if(effectConter==10) {
				effectConterOn=false;
				effectConter=0;
				effectY.clear();
			}
			
		}
		//draw pause
		g2.setColor(Color.red);
		g2.setFont(g2.getFont().deriveFont(50f));
		
		if(gameOver ) {
		  	   x = left_x +25;
		  	   y= top_y+320;
		  	   g2.drawString("GAME OVER", x, y);
	  }
		else if(KeyHandler.pausePressed ) {
		  	   x = left_x +70;
		  	   y= top_y+320;
		  	   g2.drawString("PAUSED", x, y);
	  }
		
		
	//draw game name
		 x = 35;
	  	 y= top_y+320;
	  	g2.setColor(Color.BLACK);
		g2.setFont(new Font("Times New Roman",Font.ITALIC,60));
		 g2.drawString("Tetris Game", x, y);
		
		
	}








}
