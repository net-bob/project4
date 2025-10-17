package game;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

public class BlackHole extends Polygon {
	private static final int SIZE = 10;
	
	private static final int GRAVITATION = 1;
	public static ArrayList<Polygon> objects = new ArrayList<Polygon>();
	private Polygon hitbox;
	int counter = 0;
	
	public BlackHole() {
		super(instantiateShape(), new Point(SpaceEvaders.WIDTH / 2 - 50, SpaceEvaders.LENGTH / 2 - 50), 0);
		Point[] shape = {
			new Point(SIZE, 0), new Point(3 * SIZE, 0), new Point(4 * SIZE, SIZE),
			new Point(4 * SIZE, 3 * SIZE), new Point(3 * SIZE, 4 * SIZE),
			new Point(SIZE, 4 * SIZE), new Point(0, 3 * SIZE), new Point(0, SIZE),
			new Point(SIZE, 0)};
		Point hitboxCenter = new Point(400, 300);
		hitbox = new Polygon(shape, hitboxCenter, this.rotation);
		
	}

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
	
	public void paint(Graphics brush) {
		
		handleGravity();
		
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
	
	public Polygon getHitbox() {
		return hitbox;
	}
	
	
	private void handleGravity() {
		for (Polygon object : objects) {
			double distance = Point.distanceBetween(
				object.findCenter(), this.findCenter()
			);
			
			if (object instanceof Spaceship) {
				
				Point accelerate = new Point(
					GRAVITATION / (Math.pow(distance, 2)), 
					GRAVITATION / (Math.pow(distance, 2))
				);
				
				
				
				((Spaceship) object).accelerate(accelerate);
			}
			
		}
		
	}
	
	
}
