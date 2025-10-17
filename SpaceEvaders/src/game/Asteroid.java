package game;

import java.awt.Color;
import java.awt.Graphics;

public class Asteroid extends Polygon implements Projectile {
	
	private static final int SIZE = 24;
	
	private static final int MAXASTEROIDS = 20;
	
	private static final int INITSPEED = 3;
	
	private static final int LIFETIME = 200;
	
	public static Asteroid[] asteroids = new Asteroid[MAXASTEROIDS];
	
	private double xVel;
	private double yVel;
	private double xAccel;
	private double yAccel;
	private Polygon hitbox;
	private int initTime;
	

	public Asteroid() {
		super(instantiateShape(), startPosition(), 0);
		if (this.position.y == 50) {
			// Top wall
			this.rotation = (int)(Math.random() * 181);
		}
		else if (this.position.y == SpaceEvaders.LENGTH - 50) {
			// Bottom wall
			this.rotation = (int)(Math.random() * 181) + 180;
		}
		else if (this.position.x == 50) {
			// Left wall
			this.rotation = (int)(Math.random() * 181) - 90;
		}
		else if (this.position.x == SpaceEvaders.WIDTH - 50) {
			// Right wall
			this.rotation = ((int)Math.random() * 181) + 90;
		}
		
		xVel = INITSPEED * Math.cos(Math.toRadians(rotation));
		yVel = INITSPEED * Math.sin(Math.toRadians(rotation));
		
		initTime = SpaceEvaders.getCounter();

		hitbox = this;
		//hitbox = new Polygon(instantiateShape(), this.position, this.rotation);
	}
	
	private static Point[] instantiateShape(){
		Point[] shape = {
			new Point(SIZE / 7, 0), new Point(SIZE * 3/7, 0),
			new Point(SIZE * 4/7, SIZE / 7), new Point(SIZE * 4/7, SIZE* 3/7),
			new Point(SIZE * 2/7, SIZE), new Point(0, SIZE * 3/7),
			new Point(0,SIZE / 7), new Point(SIZE / 7,0)
		};
		return shape;
	}
	
	private static Point startPosition() {
		Point start;
		int whichBorder = (int) (Math.random() * 4) + 1;
		
		/*
		 * 1 is up
		 * 2 is right
		 * 3 is down
		 * 4 is left
		 */
		
		if (whichBorder == 1) {
			start = new Point(
					(Math.random() * SpaceEvaders.WIDTH + 1),
					50
			);
		}
		else if (whichBorder == 2) {
			start = new Point(
					SpaceEvaders.WIDTH - 50,
					(Math.random() * SpaceEvaders.LENGTH + 1)
			);
		}
		else if (whichBorder == 3) {
			start = new Point(
					(Math.random() * SpaceEvaders.WIDTH + 1),
					SpaceEvaders.LENGTH - 50
			);
		}
		else if (whichBorder == 4) {
			start = new Point(
					50,
					(Math.random() * SpaceEvaders.LENGTH + 1)
			);
		}
		else {
			start = new Point(
				SpaceEvaders.WIDTH / 2,
				SpaceEvaders.LENGTH / 2
			);
		}
		
		return start;
	}
	
	public static void summonAsteroid() {
		int openAsteroid = findOpenAsteroid();
		if (openAsteroid != -1) {
			Asteroid asteroid = new Asteroid();
			asteroids[openAsteroid] = asteroid;
			BlackHole.objects.add(asteroid);
		}
	}
	
	private static int findOpenAsteroid() {
		for (int i = 0; i < MAXASTEROIDS; i++) {
			if (asteroids[i] == null) {
				return i;
			}
		}
		return -1;
	}
	
	public void paint(Graphics brush) {
		
		this.handleMovement();
		
		
		Point[] points = this.getPoints();
		
		int[] x = new int[points.length];
		int[] y = new int[points.length];
		
		for (int i = 0; i < points.length; i++) {
			x[i] = (int) Math.round(points[i].x);
			y[i] = (int) Math.round(points[i].y);
		}
		
		brush.setColor(Color.ORANGE);
		brush.fillPolygon(x, y, points.length);
	}
	
	private static void killAsteroid(Asteroid asteroid) {
		for (int i = 0; i < MAXASTEROIDS; i++) {
			if (asteroids[i] == asteroid) {
				asteroids[i] = null;
			}
		}
		BlackHole.objects.remove(asteroid);
	}
	
	private void handleMovement() {
		
		if (SpaceEvaders.getCounter() - initTime >= LIFETIME) {
			killAsteroid(this);
		}
		if (checkCollision(SpaceEvaders.thing1)) {
			killAsteroid(this);
			System.out.println("Player 2 wins");
			SpaceEvaders.gameOver = true;
		}
		else if (checkCollision(SpaceEvaders.thing2)) {
			killAsteroid(this);
			System.out.println("Player 1 wins");
			SpaceEvaders.gameOver = true;
		}
		else if (SpaceEvaders.blackhole != null && 
				checkCollision(SpaceEvaders.blackhole.getHitbox())) {
			killAsteroid(this);
		}
		
		this.position.addToPoint(xVel, yVel);
		
		xVel += xAccel;
		yVel += yAccel;
	}

	@Override
	public Point getVelocity() {
		return new Point(xVel, yVel);
	}
	
	public Polygon getHitbox() {
		return hitbox;
	}

	@Override
	public boolean checkCollision(Polygon polygon) {
		Point[] polygonPoints = polygon.getPoints();
		Point[] asteroidPoints = hitbox.getPoints();
		
		for (Point point : polygonPoints) {
			if (hitbox.contains(point)) {
				return true;
			}
		}
		for (Point point : asteroidPoints) {
			if (polygon.contains(point)) {
				return true;
			}
		}
		
		return false;
	}
	
}
