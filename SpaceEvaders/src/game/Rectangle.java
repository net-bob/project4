package game;

import java.awt.Graphics;

public class Rectangle extends Polygon {
	private static final double LENGTH = 40;
	private static final double WIDTH = 20;
	
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
				position.x,
				getVectorDistance(position, rotation, WIDTH).y);
		result[3] = new Point(
				getVectorDistance(position, rotation, LENGTH).x,
				getVectorDistance(position, rotation, WIDTH).y);
		
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
		Point[] points = getPoints();
		
		for (int i = 0; i < points.length; i++) {
			System.out.println(points[i].x);
			System.out.println(points[i].y);
		}
		
		int[] x = new int[points.length];
		int[] y = new int[points.length];
		
		for (int i = 0; i < points.length; i++) {
			x[i] = (int) Math.round(points[i].x);
			y[i] = (int) Math.round(points[i].y);
		}
		
		brush.drawPolygon(x, y, points.length);
		
	}
	
}
