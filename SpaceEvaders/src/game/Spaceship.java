package game;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Iterator;

public class Spaceship extends Polygon implements KeyListener, Iterable<Projectile>{
	
	private static int id = 1;
	
	private static final int SIZE = 30; // Will be deprecated once Hans is done
	private static final double ACCELRATE = 0.1;
	private static final int ROTATERATE = 3;
	private static final int MAXPROJECTILES = 400;
	
	private int player;
	private double xVel;
	private double yVel;
	private double xAccel;
	private double yAccel;
	private boolean isAccelerating;
	private boolean isTurningLeft;
	private boolean isTurningRight;
	private boolean missileFired;
	private Projectile[] firedProjectiles;
	
	public Spaceship(Point position, double rotation) {
		super(instantiateShape(), position, rotation);
		xVel = 0.0;
		yVel = 0.0;
		xAccel = 0.0;
		yAccel = 0.0;
		firedProjectiles = new Projectile[MAXPROJECTILES];
		player = id++;
		
		if (player == 2) {
			this.rotate(180);
		}
	}
	
	/*
	 * This method is passed in the origin point and constructs the rest of the
	 * shape. Hans, your custom polygon construction code should probably go
	 * here.
	 */
	private static Point[] instantiateShape() {
		Point[] shape = {new Point(0,0), new Point(2,0), new Point(1,1), new Point(3,1), new Point(4,2), new Point(3,3), new Point(1,3), new Point(2,4), new Point(0,4), new Point(0,0)};
		return shape;
	}
	
	
	/*
	 * This is the code that is called every frame.
	 * After handling movement, the polygon is drawn by extracting the x coords
	 * and the y coords from the point array.
	 */
	public void paint(Graphics brush) {
		
		this.handleMovement();
		
		this.drawMissiles(brush);
		
		Point[] points = this.getPoints();
		
		int[] x = new int[points.length];
		int[] y = new int[points.length];
		
		for (int i = 0; i < points.length; i++) {
			x[i] = (int) Math.round(points[i].x);
			y[i] = (int) Math.round(points[i].y);
		}
		
		brush.fillPolygon(x, y, points.length);
		
	}
	
	/*
	 * This method handles movement.
	 * Every frame, the velocities are added to the current position.
	 * Then, if the spaceship is accelerating, the accelerations are added
	 * to their respective velocities.
	 * 
	 * Then, if the respective keys are being held down, the spaceship turns
	 * accordingly.
	 */
	private void handleMovement() {
		
		this.position.addToPoint(xVel, yVel);
		
		if (this.isAccelerating) {
			xAccel = ACCELRATE * Math.cos(Math.toRadians(rotation));
			yAccel = ACCELRATE * Math.sin(Math.toRadians(rotation));
			
			xVel += xAccel;
			yVel += yAccel;
		}
		
		if (this.isTurningLeft) {
			this.rotate(-ROTATERATE);
		}
		if (this.isTurningRight) {
			this.rotate(ROTATERATE);
		}
	}
	
	private void fireMissile() {
		int openMissile = canFireMissile();
		if (openMissile != -1) {
			firedProjectiles[openMissile] = new Spaceship.Missile(this, this.rotation);
		}
	}
	
	private int canFireMissile() {
		for (int i = 0; i < MAXPROJECTILES; i++) {
			if (firedProjectiles[i] == null) {
				return i;
			}
		}
		return -1;
	}
	
	private void drawMissiles(Graphics brush) {
		for (int i = 0; i < MAXPROJECTILES; i++) {
			if (firedProjectiles[i] != null) {
				((Missile)firedProjectiles[i]).paint(brush);
			}
		}
	}
	
	@Override
	public void keyTyped(KeyEvent e) {}
	
	@Override
	public void keyPressed(KeyEvent e) {
		if (player == 1) {
			if (e.getKeyChar() == 'w') {
				isAccelerating = true;
			}
			if (e.getKeyChar() == 'a') {
				isTurningLeft = true;
			}
			if (e.getKeyChar() == 'd') {
				isTurningRight = true;
			}
			if (e.getKeyChar() == 's') {
				if (!this.missileFired) {
					this.fireMissile();
					this.missileFired = true;
				}
			}
		}
		else if (player == 2) {
			if (e.getKeyChar() == 'i') {
				isAccelerating = true;
			}
			if (e.getKeyChar() == 'j') {
				isTurningLeft = true;
			}
			if (e.getKeyChar() == 'l') {
				isTurningRight = true;
			}
			if (e.getKeyChar() == 'k') {
				if (!this.missileFired) {
					this.fireMissile();
					this.missileFired = true;
				}
			}
		}
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (player == 1) {
			if (e.getKeyChar() == 'w') {
				isAccelerating = false;
			}
			if (e.getKeyChar() == 'a') {
				isTurningLeft = false;
			}
			if (e.getKeyChar() == 'd') {
				isTurningRight = false;
			}
			if (e.getKeyChar() == 's') {
				this.missileFired = false;
			}
		}
		if (player == 2) {
			if (e.getKeyChar() == 'i') {
				isAccelerating = false;
			}
			if (e.getKeyChar() == 'j') {
				isTurningLeft = false;
			}
			if (e.getKeyChar() == 'l') {
				isTurningRight = false;
			}
			if (e.getKeyChar() == 'k') {
				this.missileFired = false;
			}
		}
	}
	
	/*
	 * Class that handles the Missile game object
	 */
	private class Missile extends Polygon implements Projectile {
		
		/*
		 * These two will be deprecated when Hans is done.
		 */
		private static final double LENGTH = 40;
		private static final double WIDTH = 20;
		
		private static final double MISSILEINITSPEED = 3.0;
		private static final int SPAWNIMMUNITY = 60;
		
		private double xVel;
		private double yVel;
		private int initTime;
		
		/*
		 * These acceleration variables should not be relevant until the black
		 * hole exists, since they fly straight.
		 */
		private double xAccel;
		private double yAccel;
		
		public Missile(Spaceship spaceship, double rotation) {
			super(instantiateShape(), starterLocation(spaceship), rotation);
			this.xVel = MISSILEINITSPEED * Math.cos(Math.toRadians(rotation));
			this.yVel = MISSILEINITSPEED * Math.sin(Math.toRadians(rotation));
			this.initTime = SpaceEvaders.getCounter();
		}
		
		/*
		 * This is the Missile's code to construct its own shape. Hans, your
		 * missile polygon construction code should go here.
		 */
		private static Point[] instantiateShape() {
			Point[] shape = {new Point(0,0), new Point(4,0), new Point(3,1), new Point(3,4), new Point(2,5), new Point(1,4), new Point(1,1), new Point(0,0)};	
			return shape;
		}
		
		
		/*
		 * This method determines where the missile spawns relative to the
		 * spaceship. 
		 */
		private static Point starterLocation(Spaceship spaceship) {
			Point start = spaceship.position.clone();
			
			// Centers the missile on the center of the spaceship
			start.addToPoint(
				(SIZE - LENGTH) / 2,
				(SIZE - WIDTH) / 2
			);
			
			
			return start;
		}
		
		/*
		 * This is the code that is called every frame. This should be called
		 * for every missile.
		 */
		public void paint(Graphics brush) {
			this.handleMovement();
			
			Point[] points = this.getPoints();
			
			int[] x = new int[points.length];
			int[] y = new int[points.length];
			
			for (int i = 0; i < points.length; i++) {
				x[i] = (int) Math.round(points[i].x);
				y[i] = (int) Math.round(points[i].y);
			}
			
			brush.drawPolygon(x, y, points.length);
		}
		
		/*
		 * This code is effectively the same as the outer class' code, but with
		 * a few minor changes. Firstly, the rotation of the missile is
		 * determined not by player control but by the velocity of the missile.
		 * This is to make the missile path more intuitive to understand:
		 * if it's pointing that way, it's going that way.
		 * 
		 * It's also always "accelerating" but only if the black hole is in play.
		 */
		private void handleMovement() {
			
			this.rotation = Math.toDegrees(Math.atan2(this.yVel, this.xVel));
			
			this.position.addToPoint(this.xVel, this.yVel);
			
			this.xVel += this.xAccel;
			this.yVel += this.yAccel;
		}

		@Override
		public Point getVelocity() {
			// TODO Auto-generated method stub
			return new Point(xVel, yVel);
		}

		@Override
		public boolean checkBlackHoleCollision(BlackHole blackHole) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean checkShipCollision(Spaceship spaceship) {
			// TODO Auto-generated method stub
			return false;
		}
	}
	
	
	//comments for this iterator must explicitly state that it mutates as it traverses...
	@Override
	public Iterator<Projectile> iterator() {
		Iterator<Projectile> it = new Iterator<Projectile>() { 
			int pos = 0; //so basically u call the next method manually and never use a for in enhaced for loop and it iterates to the next null after rmemoving any out of bounds missiles and then u just use the pos instance var to set that null value to a new missile; essentially the next method only serves to iterate the pos instance to the next null elemnt rather than returning the next null elemnt...
			public boolean hasNext() {
				eraseOutOfBoundsProjectiles();
				if(pos < MAXPROJECTILES && hasNullsAfter(pos)) {
					return true;
				}
				return false;
			}
			public Projectile next() {
				eraseOutOfBoundsProjectiles();
				pos++;
				while(firedProjectiles[pos] != null) {
					pos++;
				}
				return firedProjectiles[pos];
			}
			
			private boolean hasNullsAfter(int pos) {
				for(int i = pos; i < firedProjectiles.length; i++) {
					if(firedProjectiles[i] == null) {
						return true;
					}
				}
				return false;
			}
			
			public void eraseOutOfBoundsProjectiles() {
				for(int i = 0; i < firedProjectiles.length; i++) {
					if(!firedProjectiles[i].checkCollision(getBorderHitbox())) { //how can I access this method from the space evaders class?
						firedProjectiles[i] = null;
					}
				}
			}
		};
		return it;
	}
	
	
	private class Mine extends Polygon implements Projectile {
		public Mine() {
			super(instantiateShape(), Spaceship.position, Spaceship.roatation);
			this.scalePolygon(7);
		}
		
		private static Point[] instantiateShape() {
			Point[] shape = {new Point(1,0), new Point(3,0), new Point(4,1), new Point(4,3), new Point(3,4), new Point(1,4), new Point(0,3), new Point(0,1), new Point(1,0)};
			return shape;
		}
		
		
	}
	
	
}
