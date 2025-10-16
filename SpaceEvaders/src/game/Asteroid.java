package game;

import java.awt.Graphics;

public class Asteroid extends Polygon implements Projectile {
	
	// FOR NOW ITS A CIRCLE
	private static final double RADIUS = 20.0;
	
	private static final int MAXASTEROIDS = 20;
	
	public static Asteroid[] asteroids = new Asteroid[MAXASTEROIDS];
	
	private double xVel;
	private double yVel;
	private double xAccel;
	private double yAccel;
	private int rotationAmount;
	private Polygon hitbox;
	

	public Asteroid() {
		super(instantiateShape(), startPosition(), (int)(Math.random() * (361)));
		rotationAmount = (int) (Math.random() * 2) + 1;
		hitbox = new Polygon(instantiateShape(), this.position, this.rotation);
	}
	
	private static Point[] instantiateShape(){
		Point[] shape = {new Point(1,0), new Point(3,0), new Point(4,1), new Point(4,3), new Point(2,7), new Point(0,3), new Point(0,1), new Point(1,0)};
		return shape;
	}
	
	private static Point startPosition() {
		Point start;
		int whichBorder = (int) (Math.random() * 4) + 1;
		int direction;
		/*
		 * 1 is up
		 * 2 is right
		 * 3 is down
		 * 4 is left
		 */
		
		if (whichBorder == 1) {
			start = new Point(
					(Math.random() * SpaceEvaders.WIDTH + 1),
					0
			);
			direction = (int) (Math.random() * 180);
			
			
		}
		else if (whichBorder == 2) {
			start = new Point(
					SpaceEvaders.WIDTH,
					(Math.random() * SpaceEvaders.LENGTH + 1)
			);
			
			
			
		}
		else if (whichBorder == 3) {
			start = new Point(
					(Math.random() * SpaceEvaders.WIDTH + 1),
					SpaceEvaders.LENGTH
			);
		}
		else if (whichBorder == 4) {
			start = new Point(
					0,
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
			asteroids[openAsteroid] = new Asteroid();
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
		
		brush.fillPolygon(x, y, points.length);
	}
	
	private void handleMovement() {
		this.position.addToPoint(xVel, yVel);
		
		xVel += xAccel;
		yVel += yAccel;
		
		this.rotation += rotationAmount;
	}

	@Override
	public Point getVelocity() {
		return new Point(xVel, yVel);
	}

	@Override
	public boolean checkCollision(Polygon polygon) {
		// TODO Auto-generated method stub
		return false;
	}
	
}
