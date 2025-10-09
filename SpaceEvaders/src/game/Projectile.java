package game;

public interface Projectile {
	Point getVelocity();
	boolean checkBlackHoleCollision(BlackHole blackHole);
	boolean checkShipCollision(Spaceship shipHitbox);
}
