package Primary;
import java.awt.Graphics;
import java.util.Vector;

import ModelParts.Queen;
import ModelParts.Drone;
import ModelParts.DamageWarning;
import ModelParts.Tile;

public class Model {
	public Queen queen;
	public Drone drone;
	public DamageWarning damageWarning;
	public Tile tile1, tile2, tile3;
	
	//public Vector<Queen> modelParts;
	
	public Model(){
		/*
		 * I need the following objects: 
		 * Queen, Drone, DamageWarning, Tile1, Tile2, Tile3
		 * This mainly just holds all of the elements together
		 * Should also manage object interactions between each other within the model
		 * 
		 */
		//Possible x and y values go into constructors...
		int n = 3;
		int[] x = {350,450,550};
		int[] y = {250,250,250};
		int[] y2 = {150, 150, 150};
		
		queen = new Queen(100, 400);
		drone = new Drone(x, y, n);
		damageWarning = new DamageWarning(x, y2, n);
		tile1 = new Tile(350, 350);
		tile2 = new Tile(450, 350);
		tile3 = new Tile(550, 350);
		
		//Add the all of the model parts to the vector modelParts
		//In order of background - forground
		//modelParts.add(tile1);
		//modelParts.add(tile2);
		//modelParts.add(tile3);
		//modelParts.add(damageWarning);
		//modelParts.add(drone);
		//modelParts.add(queen);

	} // Model()
	
	// Combine all graphics objects from each model part
	// Return this graphics object
	public void paint(Graphics g){
		tile1.paint(g);
		tile2.paint(g);
		tile3.paint(g);
		damageWarning.paint(g);
		drone.paint(g);
		queen.paint(g);
	}
	//The queen gathers all needed information regarding the environment and its objects
	public void queenView(boolean viewCheck){
		if(viewCheck){
			queen.findDrone(drone.x, drone.y);
			queen.findDamageWarning(damageWarning.x);
			queen.findTile1(tile1.x);
			queen.findTile2(tile2.x);
			queen.findTile3(tile3.x);
			queen.viewCheck = viewCheck;
		}
		else queen.viewCheck = viewCheck;
	}
	
	//The queen anylizes the information it has gathered
	//The queens goal is to instruct the drone to avoid damage
	//The queen determines the drones best move
	public void queenIdea(boolean ideaCheck){
		if(ideaCheck){
			queen.calculateCommand();
			queen.ideaCheck = ideaCheck;
		}
		else queen.ideaCheck = ideaCheck;
	}
	
	//The queen issues her command to the drone
	//The drone receives this command
	//Later I plan on adding timers to this such that
	//	the queen could issue orders at different times
	//	based on the drones trust level and current success rate of change
	public void queenCommand(boolean commandCheck){
		if(commandCheck){
			drone.recieveCommand(queen.command);
			queen.commandCheck = commandCheck;
		}
		else queen.commandCheck = commandCheck;
	}
	
	//The drone analyzes the command and determines how to react
	public void droneIdea(boolean ideaCheck){
		if(ideaCheck){
			drone.determineReaction();
			drone.ideaCheck = ideaCheck;
		}
		else drone.ideaCheck = ideaCheck;
	}
	
	//The drone reacts to the queens order
	//I might add different sizes for the tiles later
	//	such that the drone may use varied movement
	public void droneReaction(boolean reactionCheck){
		if(reactionCheck){
			//Moves position
			//Calculates initial fitness results
			drone.reaction();
			drone.reactionCheck = reactionCheck;
		}
		else drone.reactionCheck = reactionCheck;
	}
	
	//Damage is given to the drone if it is in the range of the damageWarning by 50 plus or minus
	public void damage(boolean damageCheck){
		if(damageCheck){
			if( (damageWarning.x-50>drone.x) && (drone.x<damageWarning.x+50) ) drone.damage = true;
			drone.takeDamage(3, -1);
			damageWarning.damageCheck = damageCheck;
		}
		else damageWarning.damageCheck = damageCheck;
	}
	
	//The damageWarning is moved to a possible random location
	public void moveDamageWarning(){
		damageWarning.move();
	}
} // Model
