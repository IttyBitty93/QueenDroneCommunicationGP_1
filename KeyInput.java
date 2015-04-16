

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class KeyInput extends KeyAdapter{

	private Handler handler;
	
	public KeyInput(Handler handler){
		this.handler = handler;
	}
	
	public void keyPressed(KeyEvent e){
		int key = e.getKeyCode();
		//System.out.println(key);
		
		if(key == KeyEvent.VK_ESCAPE) System.exit(1);
		if(key == KeyEvent.VK_W) {
			for(int i = 0; i < handler.object.size(); i++){
				GameObject tempObject = handler.object.get(i);
				
				if(tempObject.getID() == ID.Drone){
					//tempObject.dc.addThrustY(-50.0);
					tempObject.velY -= 5;
				}
			}
		} // up
		if(key == KeyEvent.VK_D) {
			for(int i = 0; i < handler.object.size(); i++){
				GameObject tempObject = handler.object.get(i);
				
				if(tempObject.getID() == ID.Drone){
					//tempObject.dc.addThrustX(50.0);
					tempObject.velX += 5;
				}
			}
		} // right
		if(key == KeyEvent.VK_A) {
			for(int i = 0; i < handler.object.size(); i++){
				GameObject tempObject = handler.object.get(i);
				
				if(tempObject.getID() == ID.Drone){
					//tempObject.dc.addThrustX(-50.0);
					tempObject.velX -= 5;
				}
			}
		} // left
		if(key == KeyEvent.VK_S) {
			for(int i = 0; i < handler.object.size(); i++){
				GameObject tempObject = handler.object.get(i);
				
				if(tempObject.getID() == ID.Drone){
					//tempObject.dc.addThrustY(50.0);
					tempObject.velY += 5;
				}
			}
		} // down
		if(key == KeyEvent.VK_SPACE) {
			for(int i = 0; i < handler.object.size(); i++){
				GameObject tempObject = handler.object.get(i);
				
				if(tempObject.getID() == ID.Drone){
					//if(tempObject.dc.checkThrust()){
					//	tempObject.dc.setThrust(false);
					//}
					//else tempObject.dc.setThrust(true);
				}
			}
		} // Set thrust on or off
	}
		
	
	public void keyReleased(KeyEvent e){
		
	}
	
}
