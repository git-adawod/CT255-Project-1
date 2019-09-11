import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferStrategy;
import java.util.Iterator;
import java.util.ArrayList;

import javax.swing.*;

public class InvadersApplication extends JFrame implements Runnable, KeyListener {

	//WindowSize
	private static final Dimension WindowSize = new Dimension(1000, 600);
	//Number of Aliens
	private static final int NUMALIENS = 30;
	//Aliens Array
	private Alien[] AliensArray = new Alien[NUMALIENS];
	//Player Ship
	private Spaceship PlayerShip;
	//Avoid loading errors
	private boolean isInitialised = false;
	//Image location
	private static String workingDirectory = System.getProperty("user.dir");
	//Buffer strategy
	private BufferStrategy strategy;
	//Bullet List
	private ArrayList<PlayerBullet> bulletList = new ArrayList<PlayerBullet>();
	//Game States
	private enum STATE {
		Menu,
		Game,
		Dead
	};
	private STATE gameState = STATE.Menu;
	
	//Menu
	private Menu menu;
	//Moved thread to global
	private Thread t;
	//know when to startNewWave()
	private int aliensKilled = 0;
	private int score = 0;
	//Speed of aliens will be dependent on this value
	private int level = 1;
	
	private String pause = "Start";
	
	
	public InvadersApplication() {
		//Basic Setup
		this.setTitle("Assignment 3");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		Dimension screensize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
		int x = screensize.width/2 - WindowSize.width/2;
		int y = screensize.height/2 - WindowSize.height/2;
		setBounds(x, y, WindowSize.width, WindowSize.height);
		setVisible(true);
		
		//Buffer Strategy
		createBufferStrategy(2);
		strategy = getBufferStrategy();
		
		startNewWave();
		
		
		PlayerShip = new Spaceship(new ImageIcon(workingDirectory + "\\player_ship.png").getImage(), WindowSize.width);
		//Need the player ship to start at the bottom-center
		PlayerShip.setPosition(300, 550);
		
		
		
		
		isInitialised = true;
		
		addKeyListener(this);
		t = new Thread(this);
		this.repaint();
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(gameState == STATE.Game) {
			if(e.getKeyCode() == KeyEvent.VK_D) {
				PlayerShip.setXSpeed(20);
			} else if(e.getKeyCode() == KeyEvent.VK_A) {
				PlayerShip.setXSpeed(-20);
			}
			
			if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
				pause = "Resume";
				gameState = STATE.Menu;
			}
		} else if(gameState == STATE.Menu) {
			gameState = STATE.Game;
			System.out.println(gameState);
			if(!t.isAlive()) {
				t.start();
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if(gameState == STATE.Game) {
			if(e.getKeyCode() == KeyEvent.VK_D) {
				PlayerShip.setXSpeed(0);
			} else if(e.getKeyCode() == KeyEvent.VK_A) {
				PlayerShip.setXSpeed(0);
			}
		}
		
		if(e.getKeyCode() == KeyEvent.VK_SPACE) {
			shootBullet();
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		
	}

	@Override
	public void run() {
		while(true) {
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(gameState == STATE.Game) {
				if(aliensKilled >= NUMALIENS) {
					aliensKilled = 0;
					level++;
					startNewWave();
				}
				
				
				
				//Move the Enemies AND Player Death
				for(int i = 0; i < NUMALIENS; i++) {
					if(AliensArray[i].isAlive) {
						AliensArray[i].move();
						Alien alien = AliensArray[i];
						if(((alien.x < PlayerShip.x && alien.x + alien.myImage.getWidth(null) > PlayerShip.x) || 
								(PlayerShip.x < alien.x && PlayerShip.x + PlayerShip.myImage.getWidth(null) > alien.x)) &&
								((alien.y < PlayerShip.y && alien.y + alien.myImage.getHeight(null) > PlayerShip.y) ||
										(PlayerShip.y < alien.y && PlayerShip.y + PlayerShip.myImage.getHeight(null) > alien.y))) {
							pause = "Start";
							gameState = STATE.Menu;
							level = 1;
							score = 0;
							startNewWave();
						}
					} else {
						//If they die
							//Can't use null as it throws an exception
							// Using instead a false path to get a blank screen image
						AliensArray[i].myImage = new ImageIcon(workingDirectory).getImage();
						AliensArray[i].myImage2 = new ImageIcon(workingDirectory).getImage();
						//Remove them
						AliensArray[i].setPosition(-100, -100);
					}
				}
				
				//Update player location
				PlayerShip.move();
				
				//Move bullet AND Alien Death
				for(int i = 0; i < bulletList.size(); i++) {
					PlayerBullet pb = bulletList.get(i);
					pb.move();
					//Collision Detection
					for(int j = 0; j < NUMALIENS; j++) {
						Alien alien = AliensArray[j];
						if(((alien.x < pb.x && alien.x + alien.myImage.getWidth(null) > pb.x) || 
								(pb.x < alien.x && pb.x + pb.myImage.getWidth(null) > alien.x)) &&
								((alien.y < pb.y && alien.y + alien.myImage.getHeight(null) > pb.y) ||
										(pb.y < alien.y && pb.y + pb.myImage.getHeight(null) > alien.y))) {
							//Terminate Alien
							alien.isAlive = false;
							//Remove bullet From Map
							pb.y = 0;
							aliensKilled++;
							score++;
						}
					}
					
					//Remove bullet from map
					if(pb.y < 0) {
						bulletList.remove(pb);
					}
					
				}
				
				
			}
			
			this.repaint();
			}
			
		
		
	}
	
	public void paint(Graphics g) {
		if(!isInitialised) {return;}
		
		g = strategy.getDrawGraphics();
		
		if(gameState == STATE.Game) {
			
			
			g.setColor(Color.black);
			g.fillRect(0, 0, WindowSize.width, WindowSize.height);
			
			Font subFont = new Font("Monospaced", Font.BOLD, 30);
			g.setColor(Color.white);
			g.setFont(subFont);
			
			g.drawString("Score: " + score, 40, 70);
			g.drawString("Level: " + level, 40, 140);
			
			PlayerShip.paint(g);
			
			for(int i = 0; i < NUMALIENS; i++) {
				AliensArray[i].paint(g);
			}
			
			Iterator iterator = bulletList.iterator();
			while(iterator.hasNext()) {
				PlayerBullet b = (PlayerBullet) iterator.next();
				b.paint(g);
			}
		} else if(gameState == STATE.Menu) {
			g.setColor(Color.black);
			g.fillRect(0, 0, WindowSize.width, WindowSize.height);
//			menu = new Menu((WindowSize.width / 4), 100);
//			menu.display(g);
			
			Font headingFont = new Font("Monospaced", Font.BOLD, 50);
			g.setFont(headingFont);
			
			g.setColor(Color.white);
			g.drawString("Space Invaders", WindowSize.width/4, 100);
			
			Font subFont = new Font("Monospaced", Font.BOLD, 30);
			g.setFont(subFont);
			
			g.setColor(Color.white);
			g.drawString("Press any button to " + pause, WindowSize.width/4, 300);
		}
		
		
		
		
		
		strategy.show();
	}
	
	public void shootBullet() {
		PlayerBullet b = new PlayerBullet(new ImageIcon(workingDirectory + "\\bullet.png").getImage(), WindowSize.width);
		b.setPosition(PlayerShip.x+54/2, PlayerShip.y);
		bulletList.add(b);
	}
	
	public void startNewWave() {
		aliensKilled = 0;
		for(int i = 0; i < NUMALIENS; i++) {
			AliensArray[i] = new Alien(new ImageIcon(workingDirectory + "\\alien_ship_1.png").getImage(), new ImageIcon(workingDirectory + "\\alien_ship_2.png").getImage(), WindowSize.width);
			AliensArray[i].isAlive = true;
			AliensArray[i].setXSpeed(level*5);
		}
		
		int xGrid = 40, yGrid = 40;
		int alienID = 0;
		
		for(int i = 0; i < 6; i++) {
			for(int j = 0; j < 5; j++) {
				AliensArray[alienID].setPosition(xGrid, yGrid);
				yGrid += 40;
				alienID++;
			}
			xGrid += 80;
			yGrid = 40;
		}
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		InvadersApplication app = new InvadersApplication();
	}
}
