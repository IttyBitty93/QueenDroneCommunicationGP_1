

import java.awt.Graphics;
import java.awt.geom.Rectangle2D;

public abstract class GameObject {
	protected ID id;
	protected double x, y;
	protected double width, height;
	protected double velX, velY;
	protected double accX, accY;
	public GameObject(double x, double y, double width, double height, ID id){
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		//should probably change these into vectors
		this.velX = 0.0;
		this.velY = 0.0;
		this.accX = 0.0;
		this.accY = 0.0;
		this.id = id;
	}
	
	public abstract void tick(double delta, double dt);
	public abstract void render(Graphics g);
	public abstract Rectangle2D getBounds2D();
	public abstract void addDamage(double dt);

	public void setID(ID id){
		this.id = id;
	}
	public void setX(double x){
		this.x = x;
	}
	
	public void setY(double y){
		this.y = y;
	}
	
	public void setVelX(double velX){
		this.velX = velX;
	}
	
	public void setVelY(double velY){
		this.velY = velY;
	}
	
	public void setAccX(double accX){
		this.accX = accX;
	}
	
	public void setAccY(double accY){
		this.accY = accY;
	}
	
	public ID getID(){
		return id;
	}
	
	public double getX(){
		return x;
	}
	
	public double getY(){
		return y;
	}
	
	public double getVelX(){
		return velX;
	}
	
	public double getVelY(){
		return velY;
	}
	
	public double getAccX(){
		return accX;
	}
	
	public double getAccY(){
		return accY;
	}

	public double getWidth() {
		return width;
	}
	
	public double getHeight() {
		return height;
	}
}
