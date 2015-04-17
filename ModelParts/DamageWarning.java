package ModelParts;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

public class DamageWarning {
	public int x;
	public int y;
	public int[] possibleX;
	public int[] possibleY;
	int totalPossible;
	int spot;
	public boolean damageCheck = false;
	public DamageWarning(int[] x, int[] y, int totalPossible){
		this.possibleX = x;
		this.possibleY = y;
		this.totalPossible = totalPossible;
		move();
	} // DamageWarning()
	
	public void move(){
		Random gen = new Random();
		spot = gen.nextInt(3); // 0, 1, or 2 is my goal so o is inclusive and 3 is exclusive
		x = possibleX[spot];
		y = possibleY[spot];
	}
	
	/*
	 * Create a graphics object of the DamageWarning
	 * return this object
	 * Should I include a way to show all representations of stuff?
	 */
	public void paint(Graphics g){
		//if(isActive){}
		g.setColor(Color.red);
		g.fillRect(x-50, y+10, 100, 20);
		if(damageCheck){
			drawDamage(g);
		}
	}
	
	public void drawDamage(Graphics g) {
		Color myColor = new Color(250, 0, 0, 100);
		g.setColor(myColor);
		g.fillRect(x-50, y+40, 100, 160);
	}
} // DamageWarning