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
  
  
  // yo who wrote that comment bruh - jack
  //added sip
  public double getX(){ return x;}
  public double getY(){ return y;}
  public void setX(double x){ this.x = x;}
  public void setY(double y){ this.y = y;}
  
  public void addToPoint(Point point) {
	  this.x += point.x;
	  this.y += point.y;
  }
  
  public void addToPoint(double x, double y) {
	  this.x += x;
	  this.y += y;
  }
  
  
  public String toString() {
	  return "X: " + Double.toString(x) + ", Y:" + Double.toString(y);
  }
  
  
  public Point clone() {
	  return new Point(x, y);
  }
}