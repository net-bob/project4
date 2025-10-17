package game;
import java.awt.Graphics;

/**
 * BorderGenerator represents any object capable of generating a visual border
 * in the game.
 */
public interface BorderGenerator {
	
	/**
     * Generates and draws the border with the provided Graphics object.
     *
     * @param brush the Graphics object used for drawing the border
     */
	void generateBorder(Graphics brush);
}