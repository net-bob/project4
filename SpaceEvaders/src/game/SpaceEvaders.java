package game;

/**
 * SpaceEvaders is the main game class. 
 * It handles starting and setting up the game, constantly refreshing the frame, rendering, 
 * and making and having a lot of the game objects,
 * including spaceships, asteroids, and the black hole. 
 */
import java.awt.*;

@SuppressWarnings("serial")
class SpaceEvaders extends Game {
	/*
	 * A counter that increments every time the frame refreshes.
	 */
	public static int counter = 0;
	/*
	 * Width of game screen
	 */
	public static final int WIDTH = 1200;
	/*
	 * Length of game screen
	 */
	public static final int LENGTH = 900;
	
	private static final int TIMESPAWNASTEROID = 1500;
	private static final int TIMESPAWNBLACKHOLE = 1000;
	
	private BorderGenerator borderGen;
	private static Polygon borderHitbox;
	/*
	 * Determines weather the game is over or not.
	 */
	public static boolean gameOver = false;
	/*
	 * Represents the first of two space ships in the game.
	 */
	public static Spaceship thing1 = new Spaceship(new Point(WIDTH / 4, LENGTH / 2), 0);
	/*
	 * Represents the second of two space ships in the game.
	 */
	public static Spaceship thing2 = new Spaceship(new Point(WIDTH * 3/4, LENGTH / 2), 0);
	/*
	 * Represents the black hole in the game.
	 */
	public static BlackHole blackhole = null;
	
	
	/**
     * Constructs the SpaceEvaders game window, sets up the border, 
     * initializes spaceships, and adds key listeners for player input.
     */
	public SpaceEvaders() {
		super("Space Evaders", WIDTH, LENGTH);
		this.setFocusable(true);
		this.requestFocus();
		
		borderGen = (Graphics brush) -> {
			int[] XCoords = {50, WIDTH - 50, WIDTH - 50, 50};
			int[] YCoords = {50, 50, LENGTH -  50, LENGTH -  50};

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
		BlackHole.objects.add(thing2);
	}
	
	/**
     * Returns the polygon representing the game border hitbox.
     *
     * @return the Polygon representing the border hitbox
     */

	public static Polygon getBorderHitbox() {
		return borderHitbox;
	}
	
	/**
     * Paints the game objects to the canvas each frame. 
     * Updates and renders spaceships, asteroids, the black hole, 
     * the border, and the counter/time display.
     * A Black hole and asteroids spawn after a designated time, and asteroids spawn every 100 frames.
     *
     * @param brush the Graphics object used for rendering
     */
	public void paint(Graphics brush) {
		if (!gameOver) {
			brush.setColor(Color.black);
			brush.fillRect(0,0,width,height);
			
			borderGen.generateBorder(brush);
			
			
			thing1.paint(brush);
			thing2.paint(brush);
			
			if (counter % 100 == 0 && counter >= TIMESPAWNASTEROID) {
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
			
			brush.setColor(Color.WHITE);
			brush.drawString("Counter: " + counter + " Time: " + Integer.toString(counter / 60) + ":" + Integer.toString(counter % 60 * 5 / 3), 10, 10);	
			counter++;
		}
	}
	
	/**
     * Returns the counter for the game.
     *
     * @return the current counter value
     */
	public static int getCounter() {
		return counter;
	}
	
	/**
     * Starts the SpaceEvaders game.
     *
     * @param args an array of Strings
     */
	public static void main (String[] args) {
		SpaceEvaders a = new SpaceEvaders();
		a.repaint();
	}
}