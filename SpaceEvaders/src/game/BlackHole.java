package game;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

/**
 * BlackHole represents a black hole in the game that pulls in
 * spaceships and asteroids.
 */
public class BlackHole extends Polygon {
	/**
	 * scales the black hole
	 */
	public static final int SIZE = 10;
	/**
	 * Determines the strength/pull of the black hole.
	 */
	public static final int GRAVITATION = 300;
	/**
	 * Holds all of the game elements that are affected by the black hole.
	 */
	public static ArrayList<Polygon> objects = new ArrayList<Polygon>();
	private Polygon hitbox;
	private int counter = 0;
	
	/**
     * Constructs a BlackHole positioned at the center of the game field.
     * Initializes its shape and hitbox for collision detection.
     */
	public BlackHole() {
		super(instantiateShape(), new Point(SpaceEvaders.WIDTH / 2 - 50, SpaceEvaders.LENGTH / 2 - 50), 0);
		Point[] shape = {
			new Point(SIZE, 0), new Point(3 * SIZE, 0), new Point(4 * SIZE, SIZE),
			new Point(4 * SIZE, 3 * SIZE), new Point(3 * SIZE, 4 * SIZE),
			new Point(SIZE, 4 * SIZE), new Point(0, 3 * SIZE), new Point(0, SIZE),
			new Point(SIZE, 0)};
		Point hitboxCenter = new Point(SpaceEvaders.WIDTH / 2, SpaceEvaders.LENGTH / 2);
		hitbox = new Polygon(shape, hitboxCenter, this.rotation);
		
	}

	/**
	 * Creates and returns the set of Points that define the shape of the BlackHole.
	 * Used for initializing the BlackHole graphic and the hitbox.
	 *
	 * @return An array of Points representing the BlackHole shape.
	 */
	private static Point[] instantiateShape() {
		Point[] shape = {
			new Point(2 * SIZE, 2 * SIZE), new Point(5 * SIZE, 3 * SIZE),
			new Point(6 * SIZE, 0), new Point(7 * SIZE, 3 * SIZE), 
			new Point(10 * SIZE, 2 * SIZE), new Point(9 * SIZE, 5 * SIZE),
			new Point(12 * SIZE, 6 * SIZE), new Point(9 * SIZE, 7 * SIZE),
			new Point(10 * SIZE, 10 * SIZE), new Point(7 * SIZE, 9 * SIZE),
			new Point(6 * SIZE, 12 * SIZE), new Point(5 * SIZE, 9 * SIZE),
			new Point(2 * SIZE, 10 * SIZE), new Point(3 * SIZE, 7 * SIZE), 
			new Point(0,6 * SIZE), new Point(3 * SIZE, 5 * SIZE)
		};
		return shape;
	}
	
	/**
     * Paints the black hole and its hitbox in the game.
     * Also handles the rotation animation and applies gravity to nearby objects.
     *
     * @param brush the Graphics object used for drawing the black hole
     */
	public void paint(Graphics brush) {
		
		handleGravity();
		
		if (checkCollision(SpaceEvaders.thing1)) {
			System.out.println("Player 2 wins");
			SpaceEvaders.gameOver = true;
		}
		else if (checkCollision(SpaceEvaders.thing2)) {
			System.out.println("Player 1 wins");
			SpaceEvaders.gameOver = true;
		}
		
		this.rotate(SpaceEvaders.getCounter());
		
		int[] x2 = new int[this.getPoints().length];
		int[] y2 = new int[this.getPoints().length];
		for(int i = 0; i < x2.length; i++) {
			x2[i] = (int) this.getPoints()[i].x;
			y2[i] = (int) this.getPoints()[i].y;
		}
		
		brush.setColor(Color.DARK_GRAY);
		brush.fillPolygon(x2, y2, x2.length);
		
		hitbox.rotation = this.rotation;
		hitbox.position = this.position.clone();
		hitbox.position.addToPoint(40, 40);
		int[] x3 = new int[hitbox.getPoints().length];
		int[] y3 = new int[hitbox.getPoints().length];
		for(int i = 0; i < x3.length; i++) {
			x3[i] = (int) hitbox.getPoints()[i].x;
			y3[i] = (int) hitbox.getPoints()[i].y;
		}
		
		brush.setColor(Color.BLACK);
		brush.fillPolygon(x3, y3, x3.length);
		
		counter++;
	}
	
	/**
     * Returns the hitbox of the black hole used for collision detection.
     *
     * @return the Polygon representing the hitbox of the black hole
     */
	public Polygon getHitbox() {
		return hitbox;
	}
	
	/**
     * Checks whether the black hole collides with the given polygon.
     *
     * @param polygon the polygon to check collision against
     * @return true if any point of the polygon or black hole hitbox overlaps; false otherwise
     */
	public boolean checkCollision(Polygon polygon) {
		
		Point[] polygonPoints = polygon.getPoints();
		Point[] blackHolePoints = this.getHitbox().getPoints();
		
		for (Point point : polygonPoints) {
			if (this.getHitbox().contains(point)) {
				return true;
			}
		}
		for (Point point : blackHolePoints) {
			if (polygon.contains(point)) {
				return true;
			}
		}
		
		return false;
	}
	
	/**
     * Applies gravitational acceleration to all objects in the BlackHole.objects list.
     * Spaceships and their projectiles, as well as asteroids, are affected by the gravity.
     */
	private void handleGravity() {
		for (Polygon object : objects) {
			if (object instanceof Spaceship) {
				double distance = Point.distanceBetween(
						new Point(
							object.position.x + Spaceship.SIZE / 2,
							object.position.y + Spaceship.SIZE / 2
						)
						, 
						new Point(
							this.position.x + 6 * SIZE,
							this.position.y + 6 * SIZE
						)
					);
				
				double totalInfluence = GRAVITATION / Math.pow(distance, 2);
				
				double yDistance = object.position.y - this.position.y;
				double xDistance = object.position.x - this.position.x;
				
				double theta = Math.atan2(yDistance, xDistance);
				
				Point accelerate = new Point(
					-totalInfluence * Math.cos(theta),
					-totalInfluence * Math.sin(theta)
				);
				((Spaceship)object).blackHoleAccelerate(accelerate);
				((Spaceship)object).accelerateProjectiles();
				
				
			}
			else if (object instanceof Asteroid) {
				double distance = Point.distanceBetween(
						new Point(
							object.position.x + Asteroid.SIZE / 2,
							object.position.y + Asteroid.SIZE / 2
						)
						, 
						new Point(
							this.position.x + 6 * SIZE,
							this.position.y + 6 * SIZE
						)
					);
				
				double totalInfluence = GRAVITATION / Math.pow(distance, 2);
				
				double yDistance = object.position.y - this.position.y;
				double xDistance = object.position.x - this.position.x;
				
				double theta = Math.atan2(yDistance, xDistance);
				
				Point accelerate = new Point(
					-totalInfluence * Math.cos(theta),
					-totalInfluence * Math.sin(theta)
				);
				((Asteroid)object).blackHoleAccelerate(accelerate);
			}
		}
		
	}
	
	
}
