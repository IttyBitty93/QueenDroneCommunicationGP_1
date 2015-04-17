package Primary;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JButton;
import javax.swing.JFrame;

import ViewParts.GamePanel;

public class View extends JFrame {
	private GamePanel gamePanel;
	private JButton fasterButton;
	private JButton slowerButton;
	Graphics g; // used to help gamePanel I think
	
	/*
	 * Simply constructor
	 * Sets stuff for frame
	 * adds gamePanel and two buttons
	 */
	public View() {
		
		// exit on close
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// set the title default for this frame
		this.setTitle("EnvironmentQueenDroneCommunication_1");
		
		// set size defaults for this frame
		setPreferredSize(new Dimension(1000, 600));
		setMinimumSize(new Dimension(900, 500));
		setMaximumSize(new Dimension(1000, 600));
		
		// set color defaults for this frame
		setBackground(Color.GRAY);
		
		// Create the Primary Panel gamePanel
		// add the model to the gamePanel
		// add the gamePanel to the view
		gamePanel = new GamePanel();
		add(gamePanel);
		
		// Create two buttons that will be used to adjust the run speed of the application
		// add buttons to the view
		fasterButton = new JButton("Faster");
		slowerButton = new JButton("Slower");
		//add(fasterButton);
		//add(slowerButton);
		
	} // View()
	
	// Simple getter method
	public GamePanel getGamePanel(){
		return gamePanel;
	} // getGamePanel()
	
} // class View extends JFrame