package game;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Rectangle extends Polygon implements KeyListener{
	private static final double LENGTH = 40;
	private static final double WIDTH = 20;
	
	private static final double SPEED = 5;
	
	private boolean isGoingLeft;
	private boolean isGoingRight;
	private boolean isGoingUp;
	private boolean isGoingDown;
	
	public Rectangle(Point position, double rotation) {
		super(instantiateShape(position, rotation), position, rotation);
	}
	
	private static Point[] instantiateShape(Point position, double rotation) {
		Point[] result = new Point[4];
		result[0] = new Point(position.x, position.y);
		result[1] = new Point(
				getVectorDistance(position, rotation, LENGTH).x,
				position.y);
		result[2] = new Point(
				getVectorDistance(position, rotation, LENGTH).x,
				getVectorDistance(position, rotation + 90, WIDTH).y);
		result[3] = new Point(
				position.x,
				getVectorDistance(position, rotation + 90, WIDTH).y);
		
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
	
	public void paint(Graphics brush) {
		
		handleMovement();
		
		Point[] points = getPoints();
		
		int[] x = new int[points.length];
		int[] y = new int[points.length];
		
		for (int i = 0; i < points.length; i++) {
			x[i] = (int) Math.round(points[i].x);
			y[i] = (int) Math.round(points[i].y);
		}
		
		brush.drawPolygon(x, y, points.length);
		
	}
	
	private void handleMovement() {
		if (this.isGoingLeft) {
			this.position.addPoint(-SPEED, 0.0);
		}
		if (this.isGoingRight) {
			this.position.addPoint(SPEED, 0.0);
		}
		if (this.isGoingUp) {
			this.position.addPoint(0.0, -SPEED);
		}
		if (this.isGoingDown) {
			this.position.addPoint(0.0, SPEED);
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyChar() == 'a') {
			this.isGoingLeft = true;
		}
		if (e.getKeyChar() == 'w') {
			this.isGoingUp = true;
			
		}
		if (e.getKeyChar() == 's') {
			this.isGoingDown = true;
		}
		if (e.getKeyChar() == 'd') {
			this.isGoingRight = true;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyChar() == 'a') {
			this.isGoingLeft = false;
		}
		if (e.getKeyChar() == 'w') {
			this.isGoingUp = false;
		}
		if (e.getKeyChar() == 's') {
			this.isGoingDown = false;
		}
		if (e.getKeyChar() == 'd') {
			this.isGoingRight = false;
		}
	}
	
}
