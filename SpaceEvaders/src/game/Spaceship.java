package game;

import java.awt.Graphics;

public class Spaceship extends Polygon {
	private static final double SIZE = 30.0;
	private static final int MAXMISSILES = 4;
	
	private double xVel;
	private double yVel;
	private double xAccel;
	private double yAccel;
	
	private Missile[] missiles;
	
	/*
	 * Creates a spaceship object with only the upper left corner of the
	 * spaceship and its current rotation.
	 */
	public Spaceship(Point position, double inRotation) {
		super(instantiateShape(position, inRotation), position, inRotation);
		xVel = 0.0; yVel = 0.0; xAccel = 0.0; yAccel = 0.0;
		missiles = new Missile[MAXMISSILES];
	}
	
	/*
	 * This method creates an array with points that correspond to a square.
	 * For now the spaceship will be a square. If that needs to change, we can
	 * just edit this method.
	 * The point position corresponds to the upper left corner of the spaceship.
	 * I might override this method later with a method that will generate a
	 * square based on the center of the spaceship, but for now, this is good
	 * enough.
	 */
	private static Point[] instantiateShape(Point position, double rotation) {
		Point[] result = new Point[4];
		result[0] = new Point(position.x, position.y);
		result[1] = new Point(
				getVectorDistance(position, rotation, SIZE).x,
				position.y);
		result[2] = new Point(
				position.x,
				getVectorDistance(position, rotation, SIZE).y);
		result[3] = getVectorDistance(position, rotation, SIZE);
		
		return result;
	}
	
	/*
	 * Helper method that takes a point, an angle, and a distance, and returns
	 * the end point.
	 */
	private static Point getVectorDistance(Point position, double rotation,
			double distance) {
		return new Point(
				position.x + (distance * Math.cos(Math.toRadians(rotation))),
				position.y + (distance * Math.sin(Math.toRadians(rotation))));
	}
	
	private class Missile extends Polygon{
		private static final double LENGTH = 40.0;
		private static final double WIDTH = 20.0;
		private static final double SPAWNBUFFERSPACE = 5;
		
		private double xVel;
		private double yVel;
		private double xAccel;
		private double yAccel;
		
		public Missile(Spaceship spaceship) {
			super();
		}
		
		/*
		 * We know the spaceship is always a square. Given that it is always a
		 * square, we want to spawn the missile in front of the spaceship so 
		 * we don't have to worry about the collisions from the missile instantly
		 * killing the spaceship as soon as it spawns. This method returns the
		 * origin point for the missile as it initially spawns in.
		 */
		private static Point getSpawnLocation(Spaceship spaceship) {
			Point spawnLocation = Spaceship.getVectorDistance(spaceship.position,
					spaceship.rotation, Spaceship.SIZE + SPAWNBUFFERSPACE);
			spawnLocation.add(Spaceship.getVectorDistance(spawnLocation,
					spaceship.rotation + 90, Spaceship.SIZE / 2));
			
			return spawnLocation;
		}
		
		private static Point[] instantiateShape() {
			
		}
	}
	
}
