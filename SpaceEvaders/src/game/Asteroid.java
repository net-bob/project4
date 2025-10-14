package game;

public class Asteroid extends Polygon implements Projectile {

	public Asteroid(Point[] inShape, Point inPosition, double inRotation) {
		super(inShape, inPosition, inRotation);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Point getVelocity() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean checkCollision(Polygon polygon) {
		// TODO Auto-generated method stub
		return false;
	}
	
}
