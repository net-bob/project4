package game;

/**
 * Projectile represents any object that can move independently in the game,
 * such as a Missile, Mine, or asteroid. 
 */

public interface Projectile {
	
	/**
    * Returns the current velocity of the projectile as a Point, which represents a vector.
    *
    * @return a Point representing the x and y components of velocity
    */
	Point getVelocity();
	
	/**
     * Checks whether this projectile collides with the given polygon.
     *
     * @param polygon the polygon to check collision against
     * @return true if there is a collision; false otherwise
     */
	boolean checkCollision(Polygon polygon);
}
