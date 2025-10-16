package game;

public interface Projectile {
	Point getVelocity();
	boolean checkCollision(Polygon polygon);
}
