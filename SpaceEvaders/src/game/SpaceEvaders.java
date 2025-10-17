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
	
	private static final int TIMESPAWNBLACKHOLE = 10;

	private BorderGenerator borderGen;
	private static Polygon borderHitbox;
	public static boolean gameOver = false;
	public static Spaceship thing1 = new Spaceship(new Point(300, 300), 0);
	public static Spaceship thing2 = new Spaceship(new Point(600, 300), 0);
	public static BlackHole blackhole = null;
	
	

	public SpaceEvaders() {
		super("Space Evaders", WIDTH, LENGTH);
		this.setFocusable(true);
		this.requestFocus();
		
		borderGen = (Graphics brush) -> {
			int[] XCoords = {50, 750, 750, 50};
			int[] YCoords = {50, 50, 550, 550};

			brush.setColor(Color.BLUE);
			brush.drawPolygon(XCoords, YCoords, 4);
		};
		Point[] shape = {
				new Point(50, 50), new Point(WIDTH - 50,50),
				new Point(WIDTH - 50, LENGTH -  50), new Point(50, LENGTH - 50)};
		borderHitbox = new Polygon(shape, new Point(0,0), 0);
		
		this.addKeyListener(thing1);
		this.addKeyListener(thing2);
		
		BlackHole.objects.add(thing1);
		BlackHole.objects.add(thing1);
	}
	
	public static Polygon getBorderHitbox() {
		return borderHitbox;
	}
	
	public void paint(Graphics brush) {
		if (!gameOver) {
			brush.setColor(Color.black);
			brush.fillRect(0,0,width,height);
			
			// sample code for printing message for debugging
			// counter is incremented and this message printed
			// each time the canvas is repainted
			
			borderGen.generateBorder(brush);
			
			brush.setColor(Color.white);
			thing1.paint(brush);
			thing2.paint(brush);
			
			if (counter % 100 == 0) {
				Asteroid.summonAsteroid();
			}
			
			if (counter == TIMESPAWNBLACKHOLE) {
				blackhole = new BlackHole();
			}
			if (counter >= TIMESPAWNBLACKHOLE) {
				blackhole.paint(brush);
			}
			
			for (Asteroid i : Asteroid.asteroids) {
				if (i != null) {
					i.paint(brush);
				}
			}
			
//			for (int i = 0; i < borderHitbox.getPoints().length; i++) {
//				System.out.println(borderHitbox.getPoints()[i]);
//			}
			
			/*
			 * the comment below is just me messing with the code before we meet
			 * officially on friday
			 * feel free to delete it or something it doesn't do anything important
			 */
			brush.drawString("Counter: " + counter + " Time: " + Integer.toString(counter / 60) + ":" + Integer.toString(counter % 60 * 5 / 3), 10, 10);	
			counter++;
		}
	}
	
	public static int getCounter() {
		return counter;
	}
	
	public static void main (String[] args) {
		SpaceEvaders a = new SpaceEvaders();
		a.repaint();
	}
}