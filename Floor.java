

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.Random;

public class Floor extends GameObject{

	Panel[][] panels;
	int numPanels;
	double x;
	double y;
	double width;
	double height;
	Handler handler;
	double dt;
	int numFinish = 1;
	
	public Floor(double x, double y, double width, double height, ID id, int numPanels, Handler handler, double dt) {
		super(x, y, width, height, id);
		//Need to specify and create a 2D array
		//The array needs to hold objects that represent the tiles
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.handler = handler;
		this.dt = dt;
		
		this.numPanels = numPanels;
		panels = new Panel[numPanels][numPanels];
		for(int i = 0; i < numPanels; i++){
			for(int j = 0; j < numPanels; j++){
				panels[i][j] = new Panel((width/numPanels)*(double)i + x, (height/numPanels)*(double)j + y, width/numPanels, height/numPanels, 
						10, true, false, false);
			}
		}
		Random rand = new Random();
		int a;
		int b;
		for(int i = 0; i < numFinish; i++){
			a = rand.nextInt(numPanels);
			b = rand.nextInt(numPanels);
			panels[a][b].setFinish(true);
			panels[a][b].setDestructable(false);
		}
	}
	
	
	
	
	

	@Override
	public void tick(double delta, double dt) {
		for(int i = 0; i < numPanels; i++){
			for(int j = 0; j < numPanels; j++){
				if(panels[i][j].getHealthCurrent() <= 0){
					panels[i][j].setDestroyed(true);
				}
			}
		}
		collisionDrone(dt);
	}
	
	public void collisionDrone(double dt){
		double droneX = 0.0;
		double droneY = 0.0;
		double droneW = 0.0;
		double droneH = 0.0;
		boolean droneFound = false;
		
		for(int i = 0; i < handler.object.size(); i++){
			GameObject tempObject = handler.object.get(i);
			if(tempObject.getID() == ID.Drone){
				droneFound = true;
				droneX = tempObject.getX();
				droneY = tempObject.getY();
				droneW = tempObject.getWidth();
				droneH = tempObject.getHeight();
			}
		}// for(int i = 0; i < handler.object.size(); i++)
		
		if(droneFound){
			int collum = 0;
			int row = 0;
			for(int i = 0; i < numPanels; i++){
				for(int j = 0; j < numPanels; j++){
					if( Math.abs(droneX+droneW/2.0-(panels[i][j].x+panels[i][j].width/2.0)) < Math.abs(droneX+droneW/2.0-(panels[collum][row].x+panels[collum][row].width/2.0)) ){
						collum = i;
						row = j;
					}
					if( Math.abs(droneY+droneH/2.0-(panels[i][j].y+panels[i][j].height/2.0)) < Math.abs(droneY+droneH/2.0-(panels[collum][row].y+panels[collum][row].height/2.0)) ){
						collum = i;
						row = j;
					}
					
				}
			}
			
			//damage tile
			panels[collum][row].addDamage(dt);
				
		}
		
	}


	@Override
	public void render(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		
		//The floor background
		Rectangle2D rect = new Rectangle2D.Double(x-10, y-10, width+20, height+20);
		g2.setColor(Color.black);
	    g2.fill(rect);
	    //The individual panels, showing remaining health as a color
	    for(int i = 0; i < numPanels; i++){
			for(int j = 0; j < numPanels; j++){
				//Only paint if the panel has health remaining
				if(!panels[i][j].checkDestroyed()){
					//Finish panels get painted special
					if(!panels[i][j].checkFinish()){
						rect = panels[i][j].getBoarderRect2D();
						g2.setColor(Color.gray);
						g2.fill(rect);
						
						rect = panels[i][j].getCenterRect2D();
						float hp = panels[i][j].getHealthPercent();
						g2.setColor(new Color(1-hp, hp, 0));
						g2.fill(rect);
					}
					else{
						rect = panels[i][j].getBoarderRect2D();
						g2.setColor(Color.gray);
						g2.fill(rect);
						
						rect = panels[i][j].getCenterRect2D();
						g2.setColor(Color.yellow);
						g2.fill(rect);
					}
				}// if(!panels[i][j].checkDestroyed())
			}
		}
	}

	@Override
	public Rectangle2D getBounds2D() {
		// TODO Auto-generated method stub
		return null;
	}


	
	private class Panel{
		float healthMax;
		float healthCurrent;
		boolean isDestructable;
		boolean isFinish;
		boolean isDestroyed;
		double x, y;
		double width, height;

		public Panel(double x, double y, double width, double height, float healthMax,
				boolean isDestructable, boolean isFinish, boolean isDestroyed){
			this.x = x;
			this.y = y;
			this.width = width;
			this.height = height;
			this.healthMax = healthMax;
			this.healthCurrent = healthMax;
			this.isDestructable = isDestructable;
			this.isDestroyed = isDestroyed;
			this.isFinish = isFinish;
		}
		
		public boolean checkFinish() {
			boolean result = false;
			if(isFinish) result = true;
			return result;
		}

		public void addDamage(double dt) {
			if(isDestructable){
				healthCurrent -= dt;
				if(healthCurrent <= 0) isDestroyed = true;
			}
			
		}
		
		public float getHealthCurrent() {
			return healthCurrent;
		}

		public Rectangle2D getCenterRect2D(){
			return new Rectangle2D.Double(x+width/40.0, y+height/40.0, (39.0*width)/40.0, (39.0*height)/40.0);
		}
		
		public Rectangle2D getBoarderRect2D(){
			return new Rectangle2D.Double(x, y, width, height);
		}
		
		public float getHealthPercent(){
			float result = 0;
			result = healthCurrent / healthMax;
			return result;
		}
		
		public void setHealthMax(float hmax){
			healthMax = hmax;
		}
		
		public void setHealthCurrent(float hcur){
			healthCurrent = hcur;
		}
		
		public void setDestructable(boolean state){
			isDestructable = state;
		}
		
		public void setFinish(boolean state){
			isFinish = state;
		}
		
		public void setDestroyed(boolean state){
			isDestroyed = state;
		}
		
		public boolean checkDestroyed(){
			return isDestroyed;
		}
	}

	@Override
	public void addDamage(double dt) {
		// TODO Auto-generated method stub
		
	}
	

}
