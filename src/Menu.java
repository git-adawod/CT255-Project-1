import java.awt.Graphics;

import java.awt.Color;
import java.awt.Font;

public class Menu {
	private int width, height;
	
	
	public Menu(int width, int height) {
		this.width = width;
		this.height = height;	
	}
	
	public void display(Graphics g) {
		
		
		Font headingFont = new Font("Monospaced", Font.BOLD, 50);
		g.setFont(headingFont);
		
		g.setColor(Color.white);
		g.drawString("Space Invaders", width, height);
		
		Font subFont = new Font("Monospaced", Font.BOLD, 30);
		g.setFont(subFont);
		
		g.setColor(Color.white);
		g.drawString("Press any button to start", width, height+200);
		
		
	}
}
