package Primary;
/*
 * I'll need to create a viewing area which is the frame view
 */
public class Main {
	public static void main(String[] args)
	{
	// Create the frame for the GUI				
    View frame = new View();
    
	// show the frame
	frame.pack();
	frame.setVisible(true);
	frame.setLocationRelativeTo(null);
	
	// Create the Model
	Model model = new Model();
	
	// Create the Controller
	Controller control = new Controller();
	
	//Add the View and the Model to the Controller
	control.add(frame);
	control.add(model);
	
	//Run the application
	try {
		control.run();
	} catch (InterruptedException e) {
		e.printStackTrace();
	}
	
	} // main(String[] args)
} // class Main