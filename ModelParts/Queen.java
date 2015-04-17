package ModelParts;

import java.awt.Color;
import java.awt.Graphics;

public class Queen {
	public int x;
	public int y;
	
	int droneX;
	int droneY;
	boolean droneFound;
	int damageWarningX;
	boolean damageWarningFound;
	int tile1X;
	boolean tile1Found;
	int tile2X;
	boolean tile2Found;
	int tile3X;
	boolean tile3Found;
	
	public int command;
	
	//Status info
	public boolean viewCheck = false;
	public boolean ideaCheck = false;
	public boolean commandCheck = false;
	
	public Queen(int x, int y){
		this.x = x;
		this.y = y;
		droneFound = false;
		damageWarningFound = false;
		tile1Found = false;
		tile2Found = false;
		tile3Found = false;
		command = 0;
		
	} // Queen()
	
	public void findDrone(int x, int y){
		droneX = x;
		droneY = y;
		droneFound = true;
	} // findDrone(int x)
	
	public void findDamageWarning(int x){
		damageWarningX = x;
		damageWarningFound = true;
	} // findDamageWarning(int x)
	
	public void findTile1(int x){
		tile1X = x;
		tile1Found = true;
	} // findTile1(int x)
	
	public void findTile2(int x){
		tile2X = x;
		tile2Found = true;
	} // findTile2(int x)
	
	public void findTile3(int x){
		tile3X = x;
		tile3Found = true;
	} // findTile3(int x)
	
	//Determines the shortest distance from danger and creates a command for the drone
	//This is really simple at the moment...
	//Only 3 commands
	//	0 : stay		00
	// 	1 : move right	01
	//	2 : move left	10
	public void calculateCommand(){
		if(droneFound && damageWarningFound && tile1Found && tile2Found && tile3Found){
			if( (damageWarningX-50 > droneX) || (droneX > damageWarningX+50)) command = 0;
			else {
				int distance1 = Math.abs(droneX-tile1X);
				int distance2 = Math.abs(droneX-tile2X);
				int distance3 = Math.abs(droneX-tile3X);
				if(distance1<distance2 & distance1<distance3){
					command = 1;
				}
				else if(distance3<distance1 & distance3<distance2){
					command = 2;
				}
				else if(distance3<=distance1){
					command = 1;
				}
				else 
					command = 2;
			}
		}
		else command = 0;
	} // calculateCommand()
	
	/*
	 * Create a graphics object of the queen
	 * return this object
	 * Should I include a way to show all representations of stuff?
	 */
	public void paint(Graphics g){
		//if(isViewing){}
		//if(isThinking){}
		//if(isTalking){}
		g.setColor(Color.yellow);
		g.fillOval(x, y, 50, 100);
		if(viewCheck){
			drawQueenView(g);
		}
		if(ideaCheck){
			drawQueenIdea(g);
		}
		if(commandCheck){
			drawQueenCommand(g);
		}
	}
	
	public void drawQueenView(Graphics g) {
		int points = 3;
		int xPoints[] = {x+50, 600, 1400};
		int yPoints[] = {y+50, -600, 400};
		Color myColor = new Color(250, 250, 0, 100);
		g.setColor(myColor);
		g.fillPolygon(xPoints, yPoints, points);
		
	}
	
	public void drawQueenIdea(Graphics g) {
		Color myColor = new Color(100, 100, 0, 100);
		g.setColor(myColor);
		g.fillOval(x-25, y, 100, 100);
		
	}

	public void drawQueenCommand(Graphics g) {
		Color myColor = new Color(0, 0, 0);
		g.setColor(myColor);
		g.drawLine(x+25, y+25, droneX, droneY+50);
		String result = "blank";
		if(command==0) result = "Stay still";
		if(command==1) result = "Move right";
		if(command==2) result = "Move left";
		g.drawString(result, x, y-10);
	}
} // Queen
