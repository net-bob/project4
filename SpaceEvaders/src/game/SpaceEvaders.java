package game;

/*
CLASS: YourGameNameoids
DESCRIPTION: Extending Game, YourGameName is all in the paint method.
NOTE: This class is the metaphorical "main method" of your program,
      it is your control center.

*/
import java.awt.*;
import java.awt.event.*;

class SpaceEvaders extends Game {
	static int counter = 0;
	public static final int WIDTH = 800;
	public static final int LENGTH = 600;

	public SpaceEvaders() {
		super("Space Evaders", WIDTH, LENGTH);
		this.setFocusable(true);
		this.requestFocus();
	}
	public void paint(Graphics brush) {
		brush.setColor(Color.black);
		brush.fillRect(0,0,width,height);
		
		// sample code for printing message for debugging
		// counter is incremented and this message printed
		// each time the canvas is repainted
		
		counter++;
		brush.setColor(Color.white);
//		brush.drawString("Counter is " + counter,10,10);
		
		
		int x = 100 + counter;
		int y = 100;
		
//		brush.fillRect(x, y, 40, 20);
		
		int[] xC = {100, 100, 140, 140};
		int[] yC = {100, 120, 120, 100};
		
		for (int i = 0; i < xC.length; i++) {
			xC[i] += counter;
			yC[i] += counter % 100;
		}
		
		brush.fillPolygon(xC, yC, 4);
		
		
		/*
		 * the comment below is just me messing with the code before we meet
		 * officially on friday
		 * feel free to delete it or something it doesn't do anything important
		 */
		brush.drawString("Time: " + Integer.toString(counter / 60) + ":" + Integer.toString(counter % 60 * 5 / 3), 10, 10);
	}
	public static void main (String[] args) {
		SpaceEvaders a = new SpaceEvaders();
		a.repaint();
	}
}