package game;

public interface Projectile {
	double getVelocity();
	boolean checkBlackHoleCollision(Polygon blackHole);
	boolean checkShipCollision(Polygon shipHitbox);
}
