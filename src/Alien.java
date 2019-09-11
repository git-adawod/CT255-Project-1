import java.awt.Image;

public class Alien extends Sprite2D {
	
	public boolean isAlive = true;
	
	public Alien(Image i, Image i2, int winWidth) {
		super(i, winWidth);
		this.myImage2 = i2;
	}
	
	public boolean move() {
		if(x >= 940) {
			reverseDirection();
		}
		
		if(x < 40) {
			xSpeed = Math.abs(xSpeed);
			this.y += 30;
		}
		
		this.x += xSpeed;
		return true;
	}
	
	public void reverseDirection() {
		xSpeed = -xSpeed;
		this.y += 30;
	}
}
