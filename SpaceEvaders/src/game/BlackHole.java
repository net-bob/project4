package game;

import java.awt.Color;
import java.awt.Graphics;

public class BlackHole extends Polygon {
	private Polygon hitbox;
	int counter = 0;
	
	public BlackHole() {
		super(instantiateShape(), new Point(400,300), 0);
		this.scalePolygon(10);
		Point[] shape = {new Point(1,0), new Point(3,0), new Point(4,1), new Point(4,3), new Point(3,4), new Point(1,4), new Point(0,3), new Point(0,1), new Point(1,0)};
		hitbox = new Polygon(shape, this.findCenter(), this.rotation);
		hitbox.scalePolygon(10);
		
	}

	private static Point[] instantiateShape() {
		Point[] shape = {new Point(2,2), new Point(5,3), new Point(6,0), new Point(7,3), new Point(10,2), new Point(9,5), new Point(12,6), new Point(9,7), new Point(10,10), new Point(7,9), new Point(6,12), new Point(5,9), new Point(2,10), new Point(3,7), new Point(0,6), new Point(3,5)};
		return shape;
	}
	
	public void paint(Graphics brush) {
		counter++; //erase later
		this.rotate(counter);//use get counter from space evaders class
		int[] x2 = new int[this.getPoints().length];
		int[] y2 = new int[this.getPoints().length];
		for(int i = 0; i < x2.length; i++) {
			x2[i] = (int) this.getPoints()[i].x;
			y2[i] = (int) this.getPoints()[i].y;
		}
		
		brush.setColor(Color.DARK_GRAY);
		brush.fillPolygon(x2, y2, x2.length);
		
		hitbox.rotation = this.rotation;
		hitbox.position = this.position;
		int[] x3 = new int[hitbox.getPoints().length];
		int[] y3 = new int[hitbox.getPoints().length];
		for(int i = 0; i < x3.length; i++) {
			x3[i] = (int) hitbox.getPoints()[i].x;
			y3[i] = (int) hitbox.getPoints()[i].y;
		}
		
		brush.setColor(Color.BLACK);
		brush.drawPolygon(x3, y3, x3.length);
		
		counter++;
	}
	
	public getHitbox() {
		//implement...
	}
}
