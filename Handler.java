

import java.awt.Graphics;
import java.util.LinkedList;

public class Handler {
	
	LinkedList<GameObject> object = new LinkedList<GameObject>();
	double dt = .1;
	public void tick(double delta, double dt){
		//Perform an action for each object through that objects perspective
		//Priority based on objects order in the list?
		for(int i = 0; i < object.size(); i++){
			GameObject tempObject = object.get(i);
			
			
			tempObject.tick(delta, dt);
		}
	}
	
	public void render(Graphics g){
		for(int i = 0; i < object.size(); i++){
			GameObject tempObject = object.get(i);
			
			
			tempObject.render(g);
		}
	}
	
	public void addObject(GameObject object){
		this.object.add(object);
	}
	
	public void removeObject(GameObject object){
		this.object.remove(object);
	}
}
