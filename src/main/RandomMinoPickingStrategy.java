package main;

import java.util.Random;

import mino.Mino;
import mino.Mino_Bar;
import mino.Mino_L1;
import mino.Mino_L2;
import mino.Mino_Square;
import mino.Mino_T;
import mino.Mino_Z1;
import mino.Mino_Z2;

public class RandomMinoPickingStrategy implements MinoPickingStrategy {

	@Override
	public Mino pickMino() {
		 Mino mino = null;
	        int i = new Random().nextInt(7);
	        switch (i) {
	            case 0:
	                mino = new Mino_L1();
	                break;
	            case 1:
	                mino = new Mino_L2();
	                break;
	            case 2:
	                mino = new Mino_Square();
	                break;
	            case 3:
	                mino = new Mino_Bar();
	                break;
	            case 4:
	                mino = new Mino_T();
	                break;
	            case 5:
	                mino = new Mino_Z1();
	                break;
	            case 6:
	                mino = new Mino_Z2();
	                break;
	        }
	        return mino;
	}

}
