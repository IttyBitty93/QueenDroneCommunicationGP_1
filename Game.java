

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;

public class Game extends Canvas implements Runnable{
	
	public static final int WIDTH = 800, HEIGHT = 600;
	public double dt = .1;
	
	private Thread thread;
	private boolean running = false;
	
	private Handler handler;
	
	public Game(){
		handler = new Handler();
		this.addKeyListener(new KeyInput(handler));
		
		new View(WIDTH, HEIGHT, "The Tower GP: Queen to Drone Communication!", this);
		
		//Lets create the floor object
		handler.addObject(new Floor(20.0, 150.0, WIDTH-320, HEIGHT-200, ID.Floor, 10, handler, dt));
		
		//Lets create the wall objects
		//						   x		  y			 width		height
		handler.addObject(new Wall(0.0, 	  130.0, 	 20, 		HEIGHT-160, 1.0, ID.LeftWall));
		handler.addObject(new Wall(WIDTH-300, 130.0, 	 20, 		HEIGHT-160, 1.0, ID.RightWall));
		handler.addObject(new Wall(0.0, 	  130.0, 	 WIDTH-280, 20, 		1.0, ID.TopWall));
		handler.addObject(new Wall(0.0, 	  HEIGHT-50, WIDTH-280, 20.0, 		1.0, ID.BotWall));
		
		//Lets create the Drone object
		handler.addObject(new Drone(10.0, 200.0, 300.0, 30.0, 30.0, 1.0, ID.Drone, handler));
		
		//Lets create the Queen object
		handler.addObject(new Queen(0.0, 0.0, WIDTH-280, 130.0, 1.0, ID.Queen, handler));
		
	}

	public synchronized void start(){
		thread = new Thread(this);
		thread.start();
		running = true;
	}
	
	public synchronized void stop(){
		try{
			thread.join();
			running = false;
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void run(){
		this.requestFocus();
		long lastTime = System.nanoTime();
		//How many actions(ticks), do we want in a second of time?
		double amountOfTicks = 60.0;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		long timer = System.currentTimeMillis();
		int frames = 0;
		while(running){
			long now = System.nanoTime();
			//We add the fraction of a second, times 60, that has passed while running this loop
			delta += (now - lastTime) / ns;
			lastTime = now;
			//This loop activates every 1/60 seconds
			while(delta >= 1){
				//this sends the handler delta: (1/60 <= delta < 2/60) seconds
				//dt is a scalar used to represent simulated time at a different rate then 1/60 seconds
				tick(delta, dt);
				delta--;
			}
			if(running){
				render();
			}
			//updates to this are slowed most often by render()
			//After so many renders, tick() also slows this variable
			frames++;
			
			//System.currentTimeMillis() is slightly less accurate then System.nanoTime
			//Still more accurate then System.currentTimeSeconds()
			if(System.currentTimeMillis() - timer > 1000){
				timer += 1000;
				
				//How many times has render run within 1 second?
				//System.out.println("FPS: " + frames);
				frames = 0;
			}
		} // while(running)
		stop();
	} // run()
	
	private void tick(double delta, double dt){
		handler.tick(delta, dt);
	}
	
	private void render(){
		BufferStrategy bs = this.getBufferStrategy();
		if(bs == null){
			this.createBufferStrategy(3);
			return;
		}
		
		Graphics g = bs.getDrawGraphics();
		
		g.setColor(Color.gray);
		g.fillRect(0, 0, WIDTH, HEIGHT);
		
		handler.render(g);
		
		g.dispose();
		bs.show();
	}
	
	public static void main(String[] args)
	{
		new Game();
	}
}
