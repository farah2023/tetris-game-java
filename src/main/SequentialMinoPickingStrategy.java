package main;

import mino.Mino;
import mino.Mino_Bar;
import mino.Mino_L1;
import mino.Mino_L2;
import mino.Mino_Square;
import mino.Mino_T;
import mino.Mino_Z1;
import mino.Mino_Z2;

public class SequentialMinoPickingStrategy implements MinoPickingStrategy{
	
	private int currentIndex = 0;

	@Override
	public Mino pickMino() {
		Class<? extends Mino>[] minoTypes = new Class[]{
                Mino_L1.class,
                Mino_L2.class,
                Mino_Square.class,
                Mino_Bar.class,
                Mino_T.class,
                Mino_Z1.class,
                Mino_Z2.class
        };

        // Create an instance of the next Tetris piece in the sequence
        try {
            Mino mino = minoTypes[currentIndex].newInstance();
            currentIndex = (currentIndex + 1) % minoTypes.length; // Move to the next piece for the next call
            return mino;
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace(); 
            return null;
        }
    }
	
	}


