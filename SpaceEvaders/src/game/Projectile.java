package game;

public interface Projectile {
	double getVelocity();
	boolean checkCollision(Polygon other);
}
