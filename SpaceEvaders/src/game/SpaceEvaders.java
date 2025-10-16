package game;

/*
CLASS: YourGameNameoids
DESCRIPTION: Extending Game, YourGameName is all in the paint method.
NOTE: This class is the metaphorical "main method" of your program,
      it is your control center.

*/
import java.awt.*;
import java.awt.event.*;
import java.util.Iterator;

class SpaceEvaders extends Game {
	static int counter = 0;
	public static final int WIDTH = 800;
	public static final int LENGTH = 600;
	private BorderGenerator borderGen;
	private Polygon borderHitbox;
	static Spaceship thing1 = new Spaceship(new Point(300, 300), 0);
	static Spaceship thing2 = new Spaceship(new Point(600, 300), 0);

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
		Point[] shape = {new Point(50,50), new Point(750,50), new Point(750, 550), new Point(50,550)};
		borderHitbox = new Polygon(shape, new Point(400,300), 0);
		this.addKeyListener(thing1);
		this.addKeyListener(thing2);
	}
	
	public Polygon getBorderHitbox() {
		return borderHitbox;
	}
	public void paint(Graphics brush) {
		
		brush.setColor(Color.black);
		brush.fillRect(0,0,width,height);
		
		// sample code for printing message for debugging
		// counter is incremented and this message printed
		// each time the canvas is repainted
		borderGen.generateBorder(brush);
		
		brush.setColor(Color.white);
		thing1.paint(brush);
		thing2.paint(brush);
		
		Asteroid ast = new Asteroid();//please turn this process into a method in polygon!!!!!!
		int[] x = new int[ast.getPoints().length];
		int[] y = new int[ast.getPoints().length];
		for(int i = 0; i < x.length; i++) {
			x[i] = (int) ast.getPoints()[i].x;
			y[i] = (int) ast.getPoints()[i].y + counter;
		}
		
		brush.drawPolygon(x, y, x.length);
		
		BlackHole bh = new BlackHole();//please turn this process into a method in polygon!!!!!!
		bh.paint(brush);
		
		
		//for(int i = 0; ) {}
		//int[] yMissileCords = 
		//int[] xMissileCords = 
		//brush.setColor(Color.ORANGE);
		//brush.fillPolygon(xMissileCords, yMissileCords, 8);
		

		
		/*
		 * the comment below is just me messing with the code before we meet
		 * officially on friday
		 * feel free to delete it or something it doesn't do anything important
		 */
		//brush.drawString("Time: " + Integer.toString(counter / 60) + ":" + Integer.toString(counter % 60 * 5 / 3), 10, 10);	
		
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