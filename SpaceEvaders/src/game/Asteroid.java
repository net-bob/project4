package game;

public class Asteroid extends Polygon implements Projectile {
	private int XVelocity;
	private int YVelocity;
	Polygon hitbox;
	
	public Asteroid() {
		super(instantiateShape(), new Point(300, 0), 180);
		this.scalePolygon(37);
		XVelocity = 0;
		YVelocity = 20;
		hitbox = new Polygon(instantiateShape(), this.position, this.rotation);
	}
	private static Point[] instantiateShape(){
		Point[] shape = {new Point(1,0), new Point(3,0), new Point(4,1), new Point(4,3), new Point(2,7), new Point(0,3), new Point(0,1), new Point(1,0)};
		return shape;
	}
	@Override
	public boolean checkCollision(Polygon other) {
		// TODO Auto-generated method stub
		return false;
	}
}
