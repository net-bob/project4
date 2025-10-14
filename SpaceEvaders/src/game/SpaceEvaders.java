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
	public static int counter = 0;
	public static final int WIDTH = 800;
	public static final int LENGTH = 600;
	
	private static final int MAXASTEROIDS = 20;
	
	public static Spaceship thing1 = new Spaceship(new Point(300, 300), 0);
	public static Spaceship thing2 = new Spaceship(new Point(600, 300), 0);
	public static BlackHole blackhole = null;
	public static Asteroid[] asteroids = new Asteroid[MAXASTEROIDS];

	public SpaceEvaders() {
		super("Space Evaders", WIDTH, LENGTH);
		this.setFocusable(true);
		this.requestFocus();
		this.addKeyListener(thing1);
		this.addKeyListener(thing2);
	}
	public void paint(Graphics brush) {
		
		brush.setColor(Color.black);
		brush.fillRect(0,0,width,height);
		
		// sample code for printing message for debugging
		// counter is incremented and this message printed
		// each time the canvas is repainted
		
		
		brush.setColor(Color.white);
		
		thing1.paint(brush);
		thing2.paint(brush);
		
		
		/*
		 * the comment below is just me messing with the code before we meet
		 * officially on friday
		 * feel free to delete it or something it doesn't do anything important
		 */
		brush.drawString("Time: " + Integer.toString(counter / 60) + ":" + Integer.toString(counter % 60 * 5 / 3), 10, 10);	
		counter++;
	}
	
	public static int getCounter() {
		return counter;
	}
	
	public static void main (String[] args) {
		SpaceEvaders a = new SpaceEvaders();
		a.repaint();
	}
}