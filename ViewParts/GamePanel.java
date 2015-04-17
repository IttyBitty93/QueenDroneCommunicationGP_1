package ViewParts;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

import Primary.Model;


public class GamePanel extends JPanel {
	private Model model;
	boolean modelCheck;
	
	// Constructor to set size of component
	public GamePanel()
	{
		setPreferredSize(new Dimension(900, 500));
		modelCheck = false;
	} // GamePanel()
	
	// New paint componet override
	// lets the model paint itself
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		if(modelCheck) model.paint(g);
	} // paintComponent(Graphics g)
	
	// add the model to reference
	public void addModel(Model model){
		this.model = model;
		modelCheck = true;
	} // addModel(Model model)
} // class GamePanel extends JPanel
