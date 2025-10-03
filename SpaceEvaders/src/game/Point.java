package game;

/*
CLASS: Point
DESCRIPTION: Ah, if only real-life classes were this straight-forward. We'll
             use 'Point' throughout the program to store and access 
             coordinates.
*/

public class Point implements Cloneable {
	public double x,y;
	public Point(double inX, double inY) { x = inX; y = inY; }
	
	
	// what's sip - jack
	//added sip
	public double getX(){ return x;}
	public double getY(){ return y;}
	public void setX(double x){ this.x = x;}
	public void setY(double y){ this.y = y;}
	
	/*
	* This method is one I added so I don't have to rip my hair out every time I 
	* want to do some point math.
	* Makes it so you can add and subtract from the point with another point.
	*/
	public void add(Point point) {
		this.x += point.x;
		this.y += point.y;
	}
	
	public Point clone() {
		return new Point(x, y);
	}
}