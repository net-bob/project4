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

	public SpaceEvaders() {
		super("Space Evaders",800,600);
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
		
		Point a = new Point(0, 0);
		Point b = new Point(0, 20);
		Point c = new Point(40, 0);
		Point d = new Point(40, 20);
		Point[] points = {a, b, c, d};
		
		Polygon test = new Polygon(points, new Point(100, 100), 0);
		for (int i = 0; i < test.getPoints().length; i++) {
			System.out.println(test.getPoints()[i]);
		}
		
		
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