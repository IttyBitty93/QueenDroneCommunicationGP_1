package ModelParts;

import java.awt.Color;
import java.awt.Graphics;

public class Tile {
	public int x;
	public int y;
	public int width;
	public Tile(int x, int y){
		this.x = x;
		this.y = y;
		this.width = 0;
		
	} // Tile()
	
	/*
	 * Create a graphics object of the Tile
	 * return this object
	 */
	public void paint(Graphics g){

		g.setColor(Color.green);
		g.fillRect(x-48, y+10, 96, 20);
	}
} // Tile