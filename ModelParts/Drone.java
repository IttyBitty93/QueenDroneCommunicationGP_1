package ModelParts;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;
import java.util.Vector;

public class Drone {
	public int x;
	public int y;
	public int[] possibleX;
	public int[] possibleY;
	int totalPossible;
	int spot;
	public boolean damage;
	public int reaction;
	public String reactionDescription;
	public int command;
	
	//Status info
	public boolean ideaCheck = false;
	public boolean reactionCheck = false;
	
	private SolutionBatch solutionBatch;
	
	public Drone(int[] x, int[] y, int totalPossible){
		this.possibleX = x;
		this.possibleY = y;
		this.totalPossible = totalPossible;
		Random gen = new Random();
		spot = gen.nextInt(3); // 0, 1, or 2 is my goal so o is inclusive and 3 is exclusive
		this.x = possibleX[spot];
		this.y = possibleY[spot];
		
		//useful for testing fitness
		damage = false;
		reaction = 0;
		reactionDescription = "Blank";
		
		solutionBatch = new SolutionBatch();
	} // Drone()
	
	public void recieveCommand(int command) {
		this.command = command;
	} // recieveCommand(int command)
	
	public void determineReaction() {
		System.out.println("Drone.determineReaction");
		// Check to see if the command matches any previously heard tags
		if(solutionBatch.hasCommand(command)){
			System.out.println("Has command: " + command);
			solutionBatch.setLocations(command);
			// If so, check to see what generation it is on
			// If generation ten, then choose solution 0 of generation 9
			if(solutionBatch.getCurrentGeneration()==100){
				System.out.println("Current Generation is 100");
				solutionBatch.setCurrentSolutionLocation(0);
				solutionBatch.setCurrentFitnessLocation(0);
			}
			else{
				System.out.println("Current Generation is: " + solutionBatch.getCurrentGeneration());
				//Check the to see if the currentSolutionLocation is 9
				if(solutionBatch.getCurrentSolutionLocation()==9){
					System.out.println("Current Solution location is 9");
					solutionBatch.setCurrentSolutionLocation(0);
					solutionBatch.setCurrentFitnessLocation(0);
					solutionBatch.setCurrentGeneration(solutionBatch.getCurrentGeneration()+1);
					//Create a new set of solutions keeping the locations the same
					solutionBatch.geneticAlgorithm();
				}
				else{
					System.out.println("Current Solution location is: " + solutionBatch.getCurrentSolutionLocation());
					//increment solution and fitness locations then proceed to reaction
					solutionBatch.setCurrentSolutionLocation(solutionBatch.getCurrentSolutionLocation()+1);
					solutionBatch.setCurrentFitnessLocation(solutionBatch.getCurrentFitnessLocation()+1);
				}
			}
		}
		// If not, create a new solution batch
		else {
			solutionBatch.addCommand(command);
			//Set the locations for everything...
			solutionBatch.setLocations(command);
		}
		
		//set the correct reaction...
		reaction = solutionBatch.getCurrentSolution();
		reactionDescription = nameReaction(solutionBatch.getCurrentSolution());
	} // determineReaction()
	
	private String nameReaction(int currentSolution) {
		String reactionDescription = "Blank";
		switch (currentSolution){
		case 0:
			reactionDescription = "Stay still?";
			break;
		case 1:
			reactionDescription = "Move left?";
			break;
		case 2:
			reactionDescription = "Move right?";
			break;
		case 3:
			reactionDescription = "Move all the way to the left?";
			break;
		case 4:
			reactionDescription = "Move all the way to the right?";
			break;
		case 5:
			reactionDescription = "Hurt myself while moving left?";
			break;
		case 6:
			reactionDescription = "Hurt myself while moving right?";
			break;
		case 7:
			reactionDescription = "Hit myself in confusion?";
			break;
		}
		return reactionDescription;
	} // nameReaction(int currentSolution)
	
	public void move(int distance){ // negative is left, positive is right
		spot = spot + distance;
		if(spot<0) spot = 0;
		if(spot>2) spot = 2;
		x = possibleX[spot];
		
	} // move(int distance)
	
	public void reaction() {
		//Try to carry out the order
		//Check possible movement
		//move
		//apply damage if needed...
		switch (reaction){
		case 0:	// "Stay still?"
			// Nothing to do here
			break;
		case 1: // "Move left?"
			//try to move left 1 space
			move(-1);
			break;
		case 2: // "Move right?"
			//try to move right one space
			move(1);
			break;
		case 3: // "Move all the way to the left?"
			//try to move left two spaces
			move(-2);
			break;
		case 4: // "Move all the way to the right?"
			//try to move right two spaces
			move(2);
			break;
		case 5: // "Hurt myself while moving left?"
			//take 2 damage
			solutionBatch.takeDamage(2, 0);
			//try to move left 1 space
			move(-1);
			break;
		case 6: // "Hurt myself while moving right?"
			//take 2 damage
			solutionBatch.takeDamage(2, 0);
			// try to move right 1 space
			move(1);
			break;
		case 7: // "Hit myself in confusion?"
			//simply take 2 damage
			solutionBatch.takeDamage(2, 0);
			break;
		}
	} // reaction()

	public void takeDamage(int i, int j) {
		solutionBatch.takeDamage(i, j);
	}
	
	/*
	 * Create a graphics object of the Drone
	 * return this object
	 * Should I include a way to show all representations of stuff?
	 */
	public void paint(Graphics g){
		g.setColor(Color.blue);
		g.fillOval(x-25, y+25, 50, 50);
		if(ideaCheck){
			drawDroneIdea(g);
		}
		if(reactionCheck){
			drawDroneReaction(g);
		}
	}
	
	public void drawDroneIdea(Graphics g) {
		Color myColor = new Color(0, 0, 100, 100);
		g.setColor(myColor);
		g.fillOval(x-50, y, 100, 100);
	}
	
	public void drawDroneReaction(Graphics g) {
		Color myColor = new Color(0, 0, 0, 100);
		g.setColor(myColor);
		g.drawString(reactionDescription, x-25, y+10);
	}
	
	//==============================================================================================================
	//Just making the private class stand out!
	//==============================================================================================================
	
	private class SolutionBatch{
		Vector<Integer> commands; 			//int value from queen : 0, 1, and 2
		Vector<Vector<Integer>> solutions;	//int[10] values ranging from 0 to 7
		Vector<Integer> generations;		//int value ranging from 0 to 10
		Vector<Integer> numbers;			//int value ranging from 0 to 10
		Vector<Vector<Integer>> fitnesses;	//int[10] values ranging from 0 to 1
		int currentCommandLocation;
		int currentSolutionLocation;
		int currentGenerationLocation;
		int currentNumberLocation;
		int currentFitnessLocation;
		
		public SolutionBatch(){
			commands = new Vector<Integer>();
			solutions = new Vector<Vector<Integer>>();
			generations = new Vector<Integer>();
			numbers = new Vector<Integer>();
			fitnesses = new Vector<Vector<Integer>>();
			currentCommandLocation = 0;
			currentSolutionLocation = 0;
			currentGenerationLocation = 0;
			currentNumberLocation = 0;
			currentFitnessLocation = 0;
		}
		
		public void geneticAlgorithm() {
			Vector<Integer>newSolutions = new Vector<Integer>(10);
			int parentA = 0;
			int parentB = 0;
			//Take the old solutions
			//Determine there fitness
			Vector<Integer>oldSolutions = solutions.get(currentCommandLocation);
			Vector<Integer>oldFitnesses = fitnesses.get(currentCommandLocation);
			//Rank in order of there fitness
			int temp = 0;
			for(int i = 0; i<10; i++){
				for(int j = 9; j>i; j--){
					//check j to j-1
					//if j>(j-1) swap
					if(oldFitnesses.get(j)>oldFitnesses.get(j-1)){
						//swap fitnesses
						temp = oldFitnesses.get(j-1);
						oldFitnesses.set((j-1), oldFitnesses.get(j));
						oldFitnesses.set(j, temp);
						
						//swap solutions
						temp = oldSolutions.get(j-1);
						oldSolutions.set((j-1), oldSolutions.get(j));
						oldSolutions.set(j, temp);
					}	
				}
			}// end swaps
			//determine total fintness N
			int totalFitness = 0;
			for(int i = 0; i<10; i++){
				totalFitness += oldFitnesses.get(i);
			}
			int tempFitness;
			int choosenLocation;
			Random rand = new Random();
			//Produce 10 new solution, two at a time
			for(int i = 0; i<5; i++){
				tempFitness = 0;
				choosenLocation = rand.nextInt(totalFitness+1);
				//Select two parents at a time
				//find parentA
				for(int A = 0; A<10; A++){
					tempFitness += oldFitnesses.get(A);
					//found parentA
					if(tempFitness>=choosenLocation){
						parentA = oldSolutions.get(A);
						tempFitness = 0;
						choosenLocation = rand.nextInt(totalFitness+1);
						//find parentB
						for(int B = 0; B<10; B++){
							tempFitness += oldFitnesses.get(B);
							//found parentB
							if(tempFitness>=choosenLocation){
								parentB = oldSolutions.get(B);
								//Perform crossover
								//A:XX0
								//B:00X
								//Select the x's
								int childA = parentA&4 | parentB&1;
								int childB = parentA&1 | parentB&4;
								//mutate each child at a rate set by fitness
									//m = (1 - (P1f +p2f)/(maxf*2))/nbits
								int combinedFitness = oldFitnesses.get(A) + oldFitnesses.get(B);
								childA = mutateChild(childA, oldFitnesses.get(A), oldFitnesses.get(B), 5, 3);
								childB = mutateChild(childB, oldFitnesses.get(A), oldFitnesses.get(B), 5, 3);
								newSolutions.add(childA);
								newSolutions.add(childB);
								A=10;
								B=10;
							}// Finished!
						}// finding parentB
					}// found parentA
				}// finding parentA
			}// trying to produce 10 new solutions
			//Set the new solutions to the correct location in solutions
			solutions.set(currentCommandLocation, newSolutions);
			
			//Refresh the fitness values
			Vector<Integer> newFitnesses = new Vector<Integer>(10);
			for(int i = 0; i<10; i++)	newFitnesses.add(5);
			fitnesses.set(currentCommandLocation, newFitnesses);
			//Change the numbers value to 0
			numbers.set(currentCommandLocation, 0);
			
		}// geneticAlgorithm()

		public int mutateChild(int child, double p1F, double p2F, double maxF, double nBits){
			double chance = 0.0;
			Random rand = new Random();
			double mutationRate = ( 1-((p1F+p2F)/(maxF*2)) ) / nBits;
			if(nBits == 3){
				chance = rand.nextDouble();
				if(chance<=mutationRate) child = child^4;
				chance = rand.nextDouble();
				if(chance<=mutationRate) child = child^2;
				chance = rand.nextDouble();
				if(chance<=mutationRate) child = child^1;
			}
			return child;
		}
		public void setCurrentGeneration(int newGenerationNumber) {
			generations.set(currentGenerationLocation, newGenerationNumber);
		}

		public int getCurrentFitnessLocation() {
			return currentFitnessLocation;
		}

		public int getCurrentSolutionLocation() {
			return currentSolutionLocation;
		}

		public void setCurrentFitnessLocation(int i) {
			currentFitnessLocation = i;
		}

		public int getCurrentGeneration() {
			int generation = 0;
			generation = generations.get(currentGenerationLocation);
			return generation;
		}

		public void setCurrentSolutionLocation(int i) {
			currentSolutionLocation = i;
			
		}

		public int getCurrentSolution() {
			int solution = 0;
			Vector<Integer> tempSolutions;
			tempSolutions = solutions.get(currentCommandLocation);
			solution = tempSolutions.get(currentSolutionLocation);
			return solution;
		}

		public void setLocations(int command) {
			if(hasCommand(command)){
				//set currentCommandLocation
				currentCommandLocation = commands.indexOf(command);
				System.out.println("Found the command location: " + currentCommandLocation);
				//set currentGenerationLocation
				currentGenerationLocation = currentCommandLocation;
				//set currentNumberLocation
				currentNumberLocation = currentCommandLocation;
				//Modify the value in numbers
				numbers.set(currentNumberLocation, numbers.get(currentNumberLocation)+1);
				//set currentFitnessLocation
				currentFitnessLocation = numbers.get(currentNumberLocation);
				//set currentSolutionLocation
				currentSolutionLocation = currentFitnessLocation;
			}
		}

		public void addCommand(int command) {
			commands.add(command);
			//Create ten new solutions...
			Vector<Integer> tempSolutions = new Vector<Integer>(10);
			Random rand = new Random();
			for(int i = 0; i<10; i++){
				tempSolutions.add(rand.nextInt(8)); // 0 to 7
			}
			solutions.add(tempSolutions);
			//Set the generation for these new solutions to 0
			generations.add(0);
			//Set the currently selected solution to 0
			numbers.add(0);
			//Set the fitness for each new solution to zero then add to fitnesses
			Vector<Integer> tempFitnesses = new Vector<Integer>(10);
			for(int i = 0; i<10; i++){
				tempFitnesses.add(5); // each fitness starts at 5
			}
			fitnesses.add(tempFitnesses);
			
		}

		public boolean hasCommand(int command){
			boolean result = false;
			if(commands.contains(command)) result = true;
			return result;
		}

		public void takeDamage(int ammount, int offsetLocation) { // effects the fitness of the current solution
			if(currentFitnessLocation<=0) offsetLocation=0;
			Vector<Integer> temp = fitnesses.get(currentCommandLocation);
			temp.set(currentFitnessLocation+offsetLocation, temp.get(currentFitnessLocation+offsetLocation)-ammount);
			//fix negative fitness values..
			if(temp.get(currentFitnessLocation)<0){
				temp.set(currentFitnessLocation, 0);
			}
			//finish
			fitnesses.set(currentCommandLocation, temp);
		}
		
	} // Class Solution Batch
	
} // Drone

