package game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Iterator;

/**
 * Represents a controllable spaceship in the game world. 
 * Handles player movement, input, projectile management, and rendering. 
 * Each Spaceship instance corresponds to one player.
 */
public class Spaceship extends Polygon implements KeyListener, Iterable<Projectile>{
	
	private static int id = 1;
	
	/**
	 * scales the spaceship
	 */
	public static final int SIZE = 40;
	private static final double ACCELRATE = 0.05;
	private static final int ROTATERATE = 5;
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
	
	
	/**
	 * Constructs a new Spaceship at the specified position and rotation.
	 * Initializes movement parameters, assigns player ID, and sets up projectile array.
	 *
	 * @param position The initial position of the spaceship.
	 * @param rotation The initial rotation angle in degrees.
	 */
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
	
	/**
	 * Returns the hitbox polygon of the spaceship.
	 *
	 * @return The Polygon representing this spaceships hitbox.
	 */
	public Polygon getHitbox() {
		return hitbox;
	}
	
	/**
	 * Creates and returns the set of Points that define the shape of a spaceship.
	 * Used for initializing the spaceship graphic and the hitbox.
	 *
	 * @return An array of Points representing the spaceship shape.
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
	
	
	/**
	 * Updates the spaceship’s position, handles movement, and draws
	 * the ship and its projectiles onto the screen each frame.
	 *
	 * @param brush The Graphics object used for drawing.
	 */
	public void paint(Graphics brush) {
		
		this.handleMovement();
		
		this.drawProjectiles(brush);
		
		Point[] points = this.getPoints();
		
		int[] x = new int[points.length];
		int[] y = new int[points.length];
		
		for (int i = 0; i < points.length; i++) {
			x[i] = (int) Math.round(points[i].x);
			y[i] = (int) Math.round(points[i].y);
		}
		
		if (player == 1) {
			brush.setColor(Color.BLUE);
		}
		else if (player == 2) {
			brush.setColor(Color.RED);
		}
		brush.fillPolygon(x, y, points.length);
		
	}
	
	/**
	 * Handles movement updates for the spaceship.
	 * Applies acceleration, velocity, and rotation based on current key states.
	 * Called automatically once per frame in the paint loop.
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
	
	/**
	 * Attempts to fire a Mine projectile if a projectile slot is available.
	 * Instantiates a new mine object and stores it in the projectile array.
	 */
	private void fireMine() {
		int openMine = canFireProjectile();
		if (openMine != -1) {
			Mine newMine = new Spaceship.Mine(this);
			projectiles[openMine] = newMine;
		}
	}
	
	/**
	 * Attempts to fire a Missile projectile if a projectile slot is available.
	 * Instantiates a new missile object and stores it in the projectile array.
	 */
	private void fireMissile() {
		int openMissile = canFireProjectile();
		if (openMissile != -1) {
			Missile newMissile = new Spaceship.Missile(this);
			projectiles[openMissile] = newMissile;
		}
	}
	
	/**
	 * Determines the index of the next available projectile slot.
	 *
	 * @return The first available array index, or -1 if all slots are filled.
	 */
	private int canFireProjectile() {
		for (int i = 0; i < MAXPROJECTILES; i++) {
			if (projectiles[i] == null) {
				return i;
			}
		}
		return -1;
	}
	
	/**
	 * Removes the specified projectile from the projectile array.
	 *
	 * @param projectile The projectile to remove.
	 */
	private void killProjectile(Projectile projectile) {
		for (int i = 0; i < MAXPROJECTILES; i++) {
			if (projectiles[i] == projectile) {
				projectiles[i] = null;
			}
		}
	}
	
	/**
	 * Iterates through projectiles and renders each one.
	 * Also uses the custom iterator to remove projectiles that move out of bounds.
	 *
	 * @param brush The Graphics object used for drawing.
	 */

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
	
	/**
	 * Returns an iterator over this spaceship's projectile array.
	 * The iterator iterates to projectiles that leave/dont collide with the border.
	 * Contains an anonymous class that defines the iterator
	 *
	 * @return A Projectiel iterator to travese over the projectile array.
	 */	
	@Override
	public Iterator<Projectile> iterator() {

		/**
		 * Represents an iterator that traverses to out of bounds projectiles in the projectiles array
		 */
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
	
	/**
	 * Adds a gravitational acceleration influence due to the blach hole to the spaceship’s current acceleration.
	 *
	 * @param a The acceleration vector to add.
	 */
	public void blackHoleAccelerate(Point a) {
		this.xAccel += a.x;
		this.yAccel += a.y;
	}
	
	/**
	 * Applies black hole gravitational acceleration to all active projectiles.
	 */
	public void accelerateProjectiles() {
		for (int i = 0; i < MAXPROJECTILES; i++) {
			if (projectiles[i] != null && projectiles[i] instanceof Missile) {
				double distance = Point.distanceBetween(
						new Point(
							((Missile)projectiles[i]).position.x + 
							Spaceship.Missile.LENGTH / 2,
							((Missile)projectiles[i]).position.y + 
							Spaceship.Missile.WIDTH / 2
						)
						, 
						new Point(
							SpaceEvaders.blackhole.position.x + 
							6 * BlackHole.SIZE,
							SpaceEvaders.blackhole.position.y + 
							6 * BlackHole.SIZE
						)
					);
				
				double totalInfluence = BlackHole.GRAVITATION / Math.pow(distance, 2);
				
				double yDistance = ((Missile)projectiles[i]).position.y - 
						SpaceEvaders.blackhole.position.y;
				double xDistance = ((Missile)projectiles[i]).position.x - 
						SpaceEvaders.blackhole.position.x;
				
				double theta = Math.atan2(yDistance, xDistance);
				
				Point accelerate = new Point(
					-totalInfluence * Math.cos(theta),
					-totalInfluence * Math.sin(theta)
				);
				
				((Missile)projectiles[i]).blackHoleAccelerate(accelerate);
			}
			else if (projectiles[i] != null && projectiles[i] instanceof Mine) {
				double distance = Point.distanceBetween(
						new Point(
							((Mine)projectiles[i]).position.x + 
							Mine.SIZE / 2,
							((Mine)projectiles[i]).position.y + 
							Mine.SIZE / 2
						)
						, 
						new Point(
							SpaceEvaders.blackhole.position.x + 
							6 * BlackHole.SIZE,
							SpaceEvaders.blackhole.position.y + 
							6 * BlackHole.SIZE
						)
					);
				
				double totalInfluence = BlackHole.GRAVITATION / Math.pow(distance, 2);
				
				double yDistance = ((Mine)projectiles[i]).position.y - 
						SpaceEvaders.blackhole.position.y;
				double xDistance = ((Mine)projectiles[i]).position.x - 
						SpaceEvaders.blackhole.position.x;
				
				double theta = Math.atan2(yDistance, xDistance);
				
				Point accelerate = new Point(
					-totalInfluence * Math.cos(theta),
					-totalInfluence * Math.sin(theta)
				);
				
				((Mine)projectiles[i]).blackHoleAccelerate(accelerate);
			}
		}
	}
	
	/**
	 * Called when a key is typed (unused).
	 *
	 * @param e The KeyEvent associated with the typed key.
	 */
	@Override
	public void keyTyped(KeyEvent e) {}
	
	/**
	 * Handles player input when a key is pressed.
	 * Maps keypresses to acceleration, turning, and projectile firing depending on the player ID.
	 *
	 * @param e The KeyEvent triggered by key press.
	 */
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

	/**
	 * Handles player input when a key is released.
	 * Resets acceleration, turning, and projectile-firing booleans.
	 *
	 * @param e The KeyEvent caused by key release.
	 */
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
	
	/**
	 * Represents a missile projectile fired by a spaceship.
	 */
	private class Missile extends Polygon implements Projectile {
		
		private static final double LENGTH = 25;
		private static final double WIDTH = 20;
		
		private static final double MISSILEINITSPEED = 3.0;
		private static final int SPAWNIMMUNITY = 25;
		
		private double xVel;
		private double yVel;
		private int initTime;
		
		private double xAccel;
		private double yAccel;
		
		/**
		 * Constructs a missile launched from a given spaceship.
		 * Initializes velocity based on the ship’s current speed and rotation.
		 *
		 * @param spaceship The spaceship firing this missile.
		 */
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
			
			xVel += totalShipInfluence * Math.cos(Math.toRadians(spaceship.rotation));
			yVel += totalShipInfluence * Math.sin(Math.toRadians(spaceship.rotation));
			
			this.initTime = SpaceEvaders.getCounter();
		}
		
		/**
		 * Constructs the polygon shape representing the missile.
		 * 
		 * @return An array of Point objects that define the missile’s outline.
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
		
		
		/**
		 * Determines the missile's initial position based on the spaceship’s current
		 * position and rotation.
		 * Centers the missile on the spaceship before launch.
		 *
		 * @param spaceship The spaceship firing this missile.
		 * @return A Point representing the missile’s starting location.
		 */
		private static Point starterLocation(Spaceship spaceship) {
			Point start = spaceship.position.clone();
			
			start.addToPoint(
				(SIZE - LENGTH) / 2,
				(SIZE - WIDTH) / 2
			);
			
			return start;
		}
		
		/**
		 * Updates missile movement and renders it each frame.
		 * Handles collision detection and deactivation if needed.
		 *
		 * @param brush The Graphics object used for rendering.
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
		
		/**
		 * Applies gravitational acceleration from the black hole to the missile.
		 *
		 * @param a The acceleration vector to add to the missile’s velocity.
		 */

		public void blackHoleAccelerate(Point a) {
			this.xAccel += a.x;
			this.yAccel += a.y;
		}
		
		/**
		 * Handles missile movement and collision logic each frame.
		 * Updates rotation based on direction of travel.
		 * Checks collisions with spaceships and the black hole, deactivating upon impact.
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

		/**
		 * Returns the missile’s current velocity vector.
		 *
		 * @return A Point representing velocity in x and y directions.
		 */
		@Override
		public Point getVelocity() {
			return new Point(xVel, yVel);
		}

		/**
		 * Determines whether the missile collides with a given polygon.
		 * Checks for overlap between missile points and the polygon’s points.
		 *
		 * @param polygon The polygon to test for intersection.
		 * @return True if there is a collision;false otherwise.
		 */
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
	
	/**
	 * Represents a mine projectile that is fired by a spaceship.
	 * A mine's existence has a limited lifetime, and
	 * detonates (or despawns) after its windup period or upon collision.
	 */
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
		
		
		/**
		 * Constructs a new mine launched from a spaceship.
		 * Initializes velocity based on the ship’s movement and rotation.
		 *
		 * @param spaceship The spaceship deploying the mine.
		 */
		public Mine(Spaceship spaceship) {
			super(instantiateShape(), starterLocation(spaceship), spaceship.rotation);
			this.xVel = MINEINITSPEED * Math.cos(Math.toRadians(spaceship.rotation))
			+ spaceship.xVel;
			this.yVel = MINEINITSPEED * Math.sin(Math.toRadians(spaceship.rotation))
			+ spaceship.yVel;
			
			this.initTime = SpaceEvaders.getCounter();
		}
		
		/**
		 * Updates mine movement and renders it each frame.
		 * Handles collision checks and despawns after its active duration ends.
		 *
		 * @param brush The Graphics object used for rendering.
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
			
			brush.setColor(Color.ORANGE);
			brush.fillPolygon(x, y, points.length);
		}
		
		/**
		 * Applies gravitational acceleration from the black hole to the mine.
		 *
		 * @param a The acceleration vector to add to the mine’s velocity.
		 */
		public void blackHoleAccelerate(Point a) {
			this.xAccel += a.x;
			this.yAccel += a.y;
		}
		
		/**
		 * Handles movement, timing, and collision logic for the mine.
		 * The mine despawns after a set windup time or upon collision.
		 */
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

		/**
		 * Constructs the polygon shape representing the mine.
		 *
		 * @return An array of Point objects defining the mine’s outline.
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
		
		
		/**
		 * Determines the mine’s initial spawn position relative to the spaceship.
		 * Centers the mine on the spaceship when deployed.
		 *
		 * @param spaceship The spaceship deploying this mine.
		 * @return A Point representing the mine’s starting location.
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

		/**
		 * Returns the mines velocity vector.
		 *
		 * @return A Point representing velocity in x and y directions.
		 */
		@Override
		public Point getVelocity() {
			return new Point(xVel, yVel);
		}

		/**
		 * Checks if the mine collides with another polygon.
		 * Used for detecting impacts with ships or the black hole.
		 *
		 * @param polygon The polygon to test for collision.
		 * @return True if there is a collision; false otherwise.
		 */

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
