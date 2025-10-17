package game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Iterator;

public class Spaceship extends Polygon implements KeyListener, Iterable<Projectile>{
	
	private static int id = 1;
	
	private static final int SIZE = 40;
	private static final double ACCELRATE = 0.1;
	private static final int ROTATERATE = 3;
	private static final int MAXPROJECTILES = 10;
	
	private int player;
	private double xVel;
	private double yVel;
	private double xAccel;
	private double yAccel;
	private boolean isAccelerating;
	private boolean isTurningLeft;
	private boolean isTurningRight;
	private boolean missileFired;
	private boolean mineFired;
	private Projectile[] projectiles;
	private Polygon hitbox;
	
	public Spaceship(Point position, double rotation) {
		super(instantiateShape(), position, rotation);
		hitbox = this;
		xVel = 0.0;
		yVel = 0.0;
		xAccel = 0.0;
		yAccel = 0.0;
		projectiles = new Projectile[MAXPROJECTILES];
		player = id++;
		
		if (player == 2) {
			this.rotate(180);
		}
		
	}
	
	public Polygon getHitbox() {
		return hitbox;
	}
	
	/*
	 * This method is passed in the origin point and constructs the rest of the
	 * shape. Hans, your custom polygon construction code should probably go
	 * here.
	 */
	private static Point[] instantiateShape() {
		Point[] shape = {
			new Point(0,0), new Point(SIZE / 2,0), new Point(SIZE / 4, SIZE / 4),
			new Point(SIZE * 3/4, SIZE / 4), new Point(SIZE, SIZE / 2),
			new Point(SIZE * 3/4, SIZE * 3/4), new Point(SIZE / 4, SIZE * 3/4), 
			new Point(SIZE / 2, SIZE), new Point(0, SIZE), new Point(0,0)
		};
		return shape;
	}
	
	
	/*
	 * This is the code that is called every frame.
	 * After handling movement, the polygon is drawn by extracting the x coords
	 * and the y coords from the point array.
	 */
	public void paint(Graphics brush) {
		
		this.handleMovement();
		
		this.drawProjectiles(brush);
		
		for (int i = 0; i < projectiles.length; i++) {
			if (projectiles[i] != null) {
				System.out.println(projectiles[i]);
			}
		}
		
		
		Point[] points = this.getPoints();
		
		int[] x = new int[points.length];
		int[] y = new int[points.length];
		
		for (int i = 0; i < points.length; i++) {
			x[i] = (int) Math.round(points[i].x);
			y[i] = (int) Math.round(points[i].y);
		}
		
		brush.setColor(Color.WHITE);
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
		

		xVel += xAccel;
		yVel += yAccel;
		
		if (this.isAccelerating) {
			xAccel = ACCELRATE * Math.cos(Math.toRadians(rotation));
			yAccel = ACCELRATE * Math.sin(Math.toRadians(rotation));
		}
		else {
			// Check if blackhole exists, if it does, then run different code
			xAccel = 0;
			yAccel = 0;
		}
		
		if (this.isTurningLeft) {
			this.rotate(-ROTATERATE);
		}
		if (this.isTurningRight) {
			this.rotate(ROTATERATE);
		}
	}
	
	private void fireMine() {
		int openMine = canFireProjectile();
		if (openMine != -1) {
			Mine newMine = new Spaceship.Mine(this);
			projectiles[openMine] = newMine;
			BlackHole.objects.add(newMine);
		}
	}
	
	private void fireMissile() {
		int openMissile = canFireProjectile();
		if (openMissile != -1) {
			Missile newMissile = new Spaceship.Missile(this);
			projectiles[openMissile] = newMissile;
			BlackHole.objects.add(newMissile);
		}
	}
	
	private int canFireProjectile() {
		for (int i = 0; i < MAXPROJECTILES; i++) {
			if (projectiles[i] == null) {
				return i;
			}
		}
		return -1;
	}
	
	private void killProjectile(Projectile projectile) {
		for (int i = 0; i < MAXPROJECTILES; i++) {
			if (projectiles[i] == projectile) {
				projectiles[i] = null;
			}
		}
		if (projectile instanceof Polygon) {
			BlackHole.objects.remove((Polygon)projectile);
		}
	}
	
	private void drawProjectiles(Graphics brush) {
		
		Iterator<Projectile> it = iterator();
		while(it.hasNext()){
		    it.next();
		    it.remove();
		}
		
		for (int i = 0; i < MAXPROJECTILES; i++) {
			if (projectiles[i] != null) {
				if (projectiles[i] instanceof Spaceship.Missile) {
					((Spaceship.Missile)projectiles[i]).paint(brush);
				}
				else if (projectiles[i] instanceof Spaceship.Mine) {
					((Spaceship.Mine)projectiles[i]).paint(brush);
				}
			}
		}
	}
	
	//comments for this iterator must explicitly state that it mutates as it traverses...
	@Override

	public Iterator<Projectile> iterator() {

		return new Iterator<Projectile>() {
			private int pos = 0;
			private int lastReturned = -1;
		
			public boolean hasNext() {
				while (pos < projectiles.length) {
	                Projectile p = projectiles[pos];
	                if (p != null && isOutOfBounds(p)) {
	                    return true;
	                }
	                pos++;
	            }
	            return false;
			}
		
			public Projectile next() {
				lastReturned = pos;
				return projectiles[pos++];
			}
		
			public void remove() {
				projectiles[lastReturned] = null;
				lastReturned = -1;
			}
		
			private boolean isOutOfBounds(Projectile p) {
				Polygon borderHitbox = SpaceEvaders.getBorderHitbox();
				Point[] projectilePoints = ((Polygon)p).getPoints();
				
				
				for (int i = 0; i < projectilePoints.length; i++) {
					if (borderHitbox.contains(projectilePoints[i])) {
						return false;
					}
				}
				return true;
			}
		};

	}
	
	public void accelerate(double x, double y) {
		this.xAccel += x;
		this.yAccel += y;
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
			if (e.getKeyChar() == 'x') {
				if (!this.mineFired) {
					this.fireMine();
					this.mineFired = true;
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
			if (e.getKeyChar() == 'm') {
				if (!this.mineFired) {
					this.fireMine();
					this.mineFired = true;
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
			if (e.getKeyChar() == 'x') {
				this.mineFired = false;
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
			if (e.getKeyChar() == 'm') {
				this.mineFired = false;
			}
		}
	}
	
	/*
	 * Class that handles the Missile game object
	 */
	private class Missile extends Polygon implements Projectile {
		
		
		private static final double LENGTH = 25;
		private static final double WIDTH = 20;
		
		private static final double MISSILEINITSPEED = 3.0;
		private static final int SPAWNIMMUNITY = 25;
		
		private double xVel;
		private double yVel;
		private int initTime;
		
		/*
		 * These acceleration variables should not be relevant until the black
		 * hole exists, since they fly straight.
		 */
		private double xAccel;
		private double yAccel;
		
		public Missile(Spaceship spaceship) {
			super(instantiateShape(), starterLocation(spaceship), spaceship.rotation);
			
			this.xVel = (MISSILEINITSPEED) * 
			Math.cos(Math.toRadians(spaceship.rotation));
			
			this.yVel = (MISSILEINITSPEED) * 		
			Math.sin(Math.toRadians(spaceship.rotation));
			
			double totalShipInfluence = 
				spaceship.xVel * Math.cos(Math.toRadians(spaceship.rotation)) -
				spaceship.yVel * Math.sin(Math.toRadians(spaceship.rotation))
			;
			
			/*
			 * Listen, I know the math for this is awful.
			 * Do I care?
			 * Maybe.
			 * I think this is "good enough".
			 */
			xVel += totalShipInfluence * Math.cos(Math.toRadians(spaceship.rotation));
			yVel += totalShipInfluence * Math.sin(Math.toRadians(spaceship.rotation));
			
			this.initTime = SpaceEvaders.getCounter();
		}
		
		/*
		 * This is the Missile's code to construct its own shape. Hans, your
		 * missile polygon construction code should go here.
		 */
		private static Point[] instantiateShape() {
			Point[] shape = {
				new Point(0, 0), new Point(LENGTH / 5, WIDTH / 4), 
				new Point(LENGTH * 4/5, WIDTH / 4), new Point(LENGTH, WIDTH / 2),
				new Point(LENGTH * 4/5, WIDTH * 3/4), 
				new Point(LENGTH / 5, WIDTH * 3/4), 
				new Point(0, WIDTH)
			};
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
			brush.setColor(Color.GRAY);
			brush.fillPolygon(x, y, points.length);
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
			
			if (checkCollision(SpaceEvaders.thing1)
				&& SpaceEvaders.getCounter() - initTime >= SPAWNIMMUNITY) {
				killProjectile(this);
				System.out.println("Player 2 wins");
				SpaceEvaders.gameOver = true;
			}
			else if (checkCollision(SpaceEvaders.thing2)
				&& SpaceEvaders.getCounter() - initTime >= SPAWNIMMUNITY) {
				killProjectile(this);
				System.out.println("Player 1 wins");
				SpaceEvaders.gameOver = true;
			}
			else if (SpaceEvaders.blackhole != null && 
					checkCollision(SpaceEvaders.blackhole.getHitbox())) {
				killProjectile(this);
			}
			
			this.rotation = Math.toDegrees(Math.atan2(this.yVel, this.xVel));
			
			this.position.addToPoint(this.xVel, this.yVel);
			
			this.xVel += this.xAccel;
			this.yVel += this.yAccel;	
		}
		
		public void accelerate(double x, double y) {
			this.xAccel += x;
			this.yAccel += y;
		}

		@Override
		public Point getVelocity() {
			return new Point(xVel, yVel);
		}

		@Override
		public boolean checkCollision(Polygon polygon) {
			
			Point[] polygonPoints = polygon.getPoints();
			Point[] missilePoints = this.getPoints();
			
			for (Point point : polygonPoints) {
				if (this.contains(point)) {
					return true;
				}
			}
			for (Point point : missilePoints) {
				if (polygon.contains(point)) {
					return true;
				}
			}
			
			return false;
		}
	}
	
	
	private class Mine extends Polygon implements Projectile {
		private static final int SIZE = 20;
		private static final int WINDUPTIME = 180;
		private static final double MINEINITSPEED = 2.0;
		private static final int SPAWNIMMUNITY = 30;
		
		private double xVel;
		private double yVel;
		private double xAccel;
		private double yAccel;
		private int initTime;
		
		
		public Mine(Spaceship spaceship) {
			super(instantiateShape(), starterLocation(spaceship), spaceship.rotation);
			this.xVel = MINEINITSPEED * Math.cos(Math.toRadians(spaceship.rotation))
			+ spaceship.xVel;
			this.yVel = MINEINITSPEED * Math.sin(Math.toRadians(spaceship.rotation))
			+ spaceship.yVel;
			
			this.initTime = SpaceEvaders.getCounter();
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
			
			brush.setColor(Color.RED);
			brush.fillPolygon(x, y, points.length);
		}
		
		public void accelerate(double x, double y) {
			this.xAccel += x;
			this.yAccel += y;
		}
		
		private void handleMovement() {
			
			if (SpaceEvaders.getCounter() - initTime >= WINDUPTIME) {
				killProjectile(this);
			}
			
			if (checkCollision(SpaceEvaders.thing1)
				&& SpaceEvaders.getCounter() - initTime >= SPAWNIMMUNITY) {
				killProjectile(this);
				System.out.println("Player 2 wins");
				SpaceEvaders.gameOver = true;
			}
			else if (checkCollision(SpaceEvaders.thing2)
				&& SpaceEvaders.getCounter() - initTime >= SPAWNIMMUNITY) {
				killProjectile(this);
				System.out.println("Player 1 wins");
				SpaceEvaders.gameOver = true;
			}
			else if (SpaceEvaders.blackhole != null && 
					checkCollision(SpaceEvaders.blackhole.getHitbox())) {
				killProjectile(this);
			}
			
			this.position.addToPoint(this.xVel, this.yVel);
			
			this.xVel += this.xAccel;
			this.yVel += this.yAccel;
		}

		/*
		 * This is the Mine's code to construct its own shape.
		 */
		private static Point[] instantiateShape() {
			Point[] shape = {
				new Point(SIZE / 4, 0), new Point(SIZE * 3/4, 0),
				new Point(SIZE, SIZE / 4), new Point(SIZE, SIZE * 3/4), 
				new Point(SIZE * 3/4, SIZE), new Point(SIZE / 4, SIZE),
				new Point(0, SIZE * 3/4), new Point(0, SIZE / 4), 
				new Point(SIZE / 4,0)};
			return shape;
		}
		
		
		/*
		 * This method determines where the mine spawns relative to the
		 * spaceship. 
		 */
		private static Point starterLocation(Spaceship spaceship) {
			Point start = spaceship.position.clone();
			
			// Centers the missile on the center of the spaceship
			start.addToPoint(
				(Spaceship.SIZE - Mine.SIZE) / 2,
				(Spaceship.SIZE - Mine.SIZE) / 2
			);
			
			return start;
		}

		@Override
		public Point getVelocity() {
			return new Point(xVel, yVel);
		}

		@Override
		public boolean checkCollision(Polygon polygon) {
			Point[] polygonPoints = polygon.getPoints();
			Point[] minePoints = this.getPoints();
			
			for (Point point : polygonPoints) {
				if (this.contains(point)) {
					return true;
				}
			}
			for (Point point : minePoints) {
				if (polygon.contains(point)) {
					return true;
				}
			}
			
			return false;
		}
	}
}
