package Primary;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Scanner;

import javax.swing.JButton;

public class Controller {
	private boolean ModelCheck;
	private boolean ViewCheck;
	private Model model;
	private View view;
	private JButton fasterButton;
	private JButton slowerButton;
	private int timer;
	private SpeedButtonListener SBL;
	public Controller(){
		ModelCheck = false;
		ViewCheck = false;
		timer = 2500;
	}
	public void add(Model model){
		this.model = model;
		ModelCheck = true;
	}
	public void add(View view){
		this.view = view;
		ViewCheck = true;
	}
	public void run() throws InterruptedException{
		if(ModelCheck&&ViewCheck)
		{
			//So its running now...
			/*
			 * The Panel needs to change its display in steps
			 * Initial: Show the queen in the bottom left(forground)
			 * 			Show three tiles in the middle of the screen(middleground)
			 * 			Show the drone on any one of the tiles(middleground)
			 * 			Show the damage warning above any one of the tiles(middleground)
			 * 			Set the color of the panel(background)
			 * 
			 * Turns:
			 * 		1. Queen views the environment
			 * 		2. Queen gets and idea
			 * 		3. Queen sends a command to drone
			 * 		4. Drone gets an idea
			 * 		5. Drone reacts to its idea
			 * 		6. Tile under damage warning takes damage
			 * 		7. Damage warning moves to a new tile
			 */
			
			//This might look bad but I want to add two buttons to view
			fasterButton = new JButton("Faster");
			slowerButton = new JButton("Slower");
			SBL = new SpeedButtonListener();
			fasterButton.addActionListener(SBL);
			slowerButton.addActionListener(SBL);
			view.getGamePanel().add(fasterButton);
			view.getGamePanel().add(slowerButton);
			
			view.getGamePanel().addModel(model);
			
			
			// I need a pause...
			for(int i = 0; i < 10000; i++){
				//Initialize view
				updateView();
				Thread.sleep(timer);
				
				//I need a pause in between each step...
				//Draw a transparent cone emitting from the queen that encapsulates the 3 tiles
				//Queen finds all objects
				model.queenView(true);
				updateView();
				Thread.sleep(timer);
				model.queenView(false);
				
				//Draw a bubble around the queen
				//Queen determines her next command
				model.queenIdea(true);
				updateView();
				Thread.sleep(timer);
				model.queenIdea(false);
				
				//Draw a line from queen to drone
				//Draw the command above the queen
				//Send command to drone
				model.queenCommand(true);
				updateView();
				Thread.sleep(timer);
				model.queenCommand(false);
				
				//Draw a bubble around the drone
				//Drone determines reaction
				model.droneIdea(true);
				updateView();
				Thread.sleep(timer);
				model.droneIdea(false);
				
				//Show text of the drones reaction
				//Move drone and apply damage
				model.droneReaction(true);
				updateView();
				Thread.sleep(timer);
				model.droneReaction(false);
				
				//Show damage from environment
				//Apply damage
				model.damage(true);
				updateView();
				Thread.sleep(timer);
				model.damage(false);
				
				//Show that the damging warning has moved
				//move damage warning
				model.moveDamageWarning();
			}
		}
	} // void run()
	
	
	private void updateView(){
		view.getGamePanel().repaint();
		view.getGamePanel().revalidate();
	}
	
		//*****************************************************************
		// Represents the action listener for the timer.
		//*****************************************************************
		private class SpeedButtonListener implements ActionListener
		{
			//--------------------------------------------------------------
			// Updates the position of the image and possibly the direction
			// of movement whenever the timer fires an action event.
			//--------------------------------------------------------------
			public void actionPerformed(ActionEvent event) {
				if(event.getSource() == fasterButton){
					if((timer-=100)<=0){
						if((timer-=10)<=0){
							if((timer-=1)<=0) timer = 1;
						}
						else timer-=10;
					}
					else timer-=100;
				}
				else if(event.getSource() == slowerButton){
					timer+=1000;
				}
			} // actionPerformed(ActionEvent event)
		} // class SpeedButtonListener implements ActionListener

		
} // end Class Controller
