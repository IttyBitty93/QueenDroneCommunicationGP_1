

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

public class Drone extends GameObject{
	double mass;
	double fnetX, fnetY;
	//Screen dimensions
	double scale;
	Handler handler;
	double totalTime = 0.0;
	protected DroneControls dc;

	public Drone(double mass, double x, double y, double width, double height, double scale, ID id, Handler handler)
	{
		super(x, y, width*scale, height*scale, id);
		this.scale = scale;
		this.handler = handler;
		this.mass = mass;
		this.fnetX = 0.0;
		this.fnetY = 0.0;
		dc = new DroneControls();
	}
	
	public Rectangle2D getBounds2D(){
		return new Rectangle2D.Double(x, y, width, height);
	}
	
	
	public void tick(double delta, double dt){
		//Update forceNet
		fnetX = 0.0;
		fnetY = 0.0;
		//fnetX += collisionX(dt); // horizontal collisions fnetX
		//fnetY += collisionY(dt); // horizontal collisions fnetX
		//collision(dt);
		fnetX += dc.getThrustX();
		fnetY += dc.getThrustY();
		//Update acceleration
		accX = fnetX/mass;
		accY = fnetY/mass;
		
		//Update velocity
		velX += accX*dt;
		velY += accY*dt;

		//Update position
		x += velX*dt;
		y += velY*dt;
		
		//Stay within x
		if(x<=20.0){
			x = 20.0;
			if(velX < 0.0){
				velX = -velX/10;
			}
		}
		if(x>=500.0-width){
			x = 500.0-width;
			if(velX > 0.0){
				velX = -velX/10;
			}
		}
			
		//Stay within y
		if(y<=150.0){
			y = 150.0;
			if(velY < 0.0){
				velY = -velY/10;
			}
		}
		if(y>=550.0-height){
			y = 550.0-height;
			if(velY > 0.0){
				velY = -velY/10;
			}
		}
		
		//Apply x friction
		if(velX > 0.0 || velX < 0.0) velX = (99.0*velX) / 100.0;
		
		//Apply y friction
		if(velY > 0.0 || velY < 0.0) velY = (99.0*velY) / 100.0;
		
	}
	
	public void addDamage(double dt){
		
	}
	
	public double collisionX(double dt){
		double result = 0.0;
		//Checks wall sides and tile sides
		double dx;
		for(int i = 0; i < handler.object.size(); i++){
			dx = 0.0;
			GameObject tempObject = handler.object.get(i);
			if(tempObject.getID() == ID.LeftWall){
				if(getBounds2D().intersects(tempObject.getBounds2D())){
					//collision code for LeftWall
					//find the amount of intersection
					dx = getBounds2D().createIntersection(tempObject.getBounds2D()).getWidth();
					result += 100.0*dx;
					if(velX > 0.0) result += velX*100;
					else if(velX < 0.0) result -= velX*100;
				}
			} // if(tempObject.getID() == ID.LeftWall)
			
			if(tempObject.getID() == ID.RightWall){
				if(getBounds2D().intersects(tempObject.getBounds2D())){
					//collision code for RightWall
					//find the amount of intersection
					dx = getBounds2D().createIntersection(tempObject.getBounds2D()).getWidth();
					result -= 100.0*dx;
					if(velX > 0.0) result -= velX*100;
					else if(velX < 0.0) result += velX*100;
				}
			} // if(tempObject.getID() == ID.LeftWall)
			
			if(tempObject.getID() == ID.Tile){
				if(getBounds2D().intersects(tempObject.getBounds2D())){
					//collision code for Tile
					//find the amount of intersection
					dx = getBounds2D().createIntersection(tempObject.getBounds2D()).getWidth();
					if(y + height > tempObject.getY() + height){
						if(velX > 0.0) result -= 100.0*dx;
						else if(velX < 0.0) result += 100.0*dx;
					}
				}
			} // if(tempObject.getID() == ID.Tile)
			
			
		} //loop through objects
		return result;
	}
	
	public double collisionY(double dt){
		double result = 0.0;
		//Checks wall sides and tile sides
		double dy;
		double dx;
		boolean tileCollision = false;
		int tileA = 0;
		double widthA = 0.0;
		double widthB = 0.0;
		for(int i = 0; i < handler.object.size(); i++){
			GameObject tempObject = handler.object.get(i);
			if(tempObject.getID() == ID.Tile){
				if(getBounds2D().intersects(tempObject.getBounds2D())){
					if(!tileCollision){
						tileCollision = true;
						tileA = i;
					}
					else{
						GameObject tempObjectA = handler.object.get(tileA);
						widthA = getBounds2D().createIntersection(tempObjectA.getBounds2D()).getWidth();
						widthB = getBounds2D().createIntersection(tempObject.getBounds2D()).getWidth();
						if(widthA < widthB) tileA = i;
					}
				} //Did we collide with a tile?
			} // if(tempObject.getID() == ID.Tile)
		} //loop through objects
		
		if(tileCollision){
			GameObject tempObjectB = handler.object.get(tileA);
			dy = getBounds2D().createIntersection(tempObjectB.getBounds2D()).getHeight();
			dx = getBounds2D().createIntersection(tempObjectB.getBounds2D()).getWidth();
			tempObjectB.addDamage(dt);
			//Check to see if we at on top of the tile
			if( (dx > width/10) && (y+height < tempObjectB.y + tempObjectB.height)){
				result -= 100.0*dy - 9.8*mass;
				
				if(velY > 0.0) result -= velY*100;
				else if(velY < 0.0) result += velY*100;
			}
		} // if(tileCollision)
		return result;
	}
	
	
	public void collision(double dt){
		boolean tileCollision = false;
		int tileA = 0;
		double widthA = 0.0;
		double widthB = 0.0;
		for(int i = 0; i < handler.object.size(); i++){
			GameObject tempObject = handler.object.get(i);
			
			if(tempObject.getID() == ID.Wall){
				if(getBounds2D().intersects(tempObject.getBounds2D())){
					//collision code for wall
					//find the amount of intersection
					double ds = getBounds2D().createIntersection(tempObject.getBounds2D()).getWidth();
					double el = .1; //m == 1m
					if(tempObject.x < x){
						//collision code for left wall
						if(velX<0.0) velX = -velX/10.0;
						if(ds>=el)	 x += ds;
					}
					else{
						//collision code for right wall
						if(velX>0.0) velX = -velX/10.0;
						if(ds>=el)	 x -= ds;
					}
					
					
					//sliding friction
					if(velY>0.0) fnetY -= mass*9.8*.7;
					if(velY<0.0) fnetY += mass*9.8*.7;
				}
			} // if(tempObject.getID() == ID.Wall)
			
			if(tempObject.getID() == ID.Tile){
				if(getBounds2D().intersects(tempObject.getBounds2D())){
					if(!tileCollision){
						tileCollision = true;
						tileA = i;
					}
					else{
						GameObject tempObjectA = handler.object.get(tileA);
						widthA = getBounds2D().createIntersection(tempObjectA.getBounds2D()).getWidth();
						widthB = getBounds2D().createIntersection(tempObject.getBounds2D()).getWidth();
						if(widthA < widthB) tileA = i;
					}
				} //Did we collide with a tile?
			} // if(tempObject.getID() == ID.Tile)
			
			if(tempObject.getID() == ID.Elevator){
				if(getBounds2D().intersects(tempObject.getBounds2D())){
					if(!tileCollision){
						tileCollision = true;
						tileA = i;
					}
					else{
						GameObject tempObjectA = handler.object.get(tileA);
						widthA = getBounds2D().createIntersection(tempObjectA.getBounds2D()).getWidth();
						widthB = getBounds2D().createIntersection(tempObject.getBounds2D()).getWidth();
						if(widthA < widthB) tileA = i;
					}
				} //Did we collide with a tile?
			} // if(tempObject.getID() == ID.Elevator)
			
			
		}// end first loop
		
		//Now for collision with one tile or one elevator based on who is colliding the most
		if(tileCollision){
			GameObject tempObjectB = handler.object.get(tileA);
			double ds = 0.0;
			ds = getBounds2D().createIntersection(tempObjectB.getBounds2D()).getHeight();
			tempObjectB.addDamage(dt);
			//Check to see if we at on top of the tile
			if(y + height <= tempObjectB.y+tempObjectB.height/2){
				if(tempObjectB.getID() == ID.Tile){
					//collision code for the Tile
					//el is the equilibrium length of the tile
					double el = .1; //m == 1m
					//lose 10% of velocity
					if(velY>=0. ){
						velY = -velY*(1 + 1/(mass*10)) - (100.0/(mass*10))*ds;
						if(fnetY>=0.0)
							fnetY = 0.0;
					}
					if(el<ds) y -= ds-2.0*el/3.0;
					//Sliding friction
					if(fnetY > 0.1){
						if(velX>0.1) velX = velX - fnetY*.7*dt;
						else if(velX<0.1) velX = velX + mass*9.8*.7*dt;
					}
				} // Elevator
				
				if(tempObjectB.getID() == ID.Elevator){
					//collision code for the Elevator
					//el is the equilibrium length of the tile
					double el = .1; //m == 1m
					//lose 10% of velocity
					if(velY>=0.0){
						velY = -velY/100.0;
						if(fnetY>=0. )
							fnetY = 0.0;
					}
					if(el<ds) y -= ds-2.0*el/3.0;
					//Sliding friction
					if(velX>0.0) fnetX -= mass*9.8*.7;
					if(velX<0.0) fnetX += mass*9.8*.7;
				} // Elevator
			}
			else{
				
			}
			
		} // if(tileCollision)
		
	}//collision()
	
	public void render(Graphics g){
		Graphics2D g2 = (Graphics2D) g;
		
		Rectangle2D rect = new Rectangle2D.Double(x, y, width, height);
		g2.setColor(Color.gray);
	    g2.fill(rect);
	}
	
	
	//======================================================================
	private class DroneControls{
		boolean thrustActive;
		boolean breakActive;
		double thrustX;
		double thrustY;
		public DroneControls(){
			thrustActive = false;
			breakActive = false;
			thrustX = 0.0;
			thrustY = 0.0;
		}
		//Going to set some hard limits...
		public void addThrustX(double x){
			thrustX += x;
			if(!(thrustX >= -100.0 && thrustX <= 100.0)) {
				thrustX = 0.0;
			}
		}
				
		public void addThrustY(double y){
			thrustY += y;
			if(!(thrustY >= -100.0 && thrustY <= 100.0)) {
				thrustY = 0.0;
			}
		}
				
		public void setThrust(boolean set){
			thrustActive = set;
		}
			
		public boolean checkThrust(){
			return thrustActive;
		}
				
		public void activateBreak(){
			breakActive = true;
		}
				
		public void disableBreak(){
			breakActive = false;
		}
				
		public double getThrustX(){
			if(thrustActive && (thrustX < 100.0)) return thrustX;
			else return 0.0;
		}
				
		public double getThrustY(){
			if(thrustActive && (thrustY < 100.0)) return thrustY;
			else return 0.0;
		}
	}
}