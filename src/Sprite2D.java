import java.awt.*;

public class Sprite2D {

	protected double x, y;
	protected double xSpeed = 0;
	protected Image myImage, myImage2;
	protected int framesDrawn = 0;
	int winWidth;
	
	public Sprite2D(Image i, int winWidth) {
		myImage = i;
		this.winWidth = winWidth;
	}
	
	public void setPosition(double xx, double yy) {
		this.x = xx;
		this.y = yy;
	}
	
	public void setXSpeed(double dx) {
		this.xSpeed = dx;
	}
	
	public void paint(Graphics g) {
		framesDrawn++;
		if(framesDrawn % 100 < 50  && this.getClass().getName().equals("Alien")) {
			g.drawImage(myImage, (int)x, (int)y, null);
		}
		else if(framesDrawn % 100 > 50 && this.getClass().getName().equals("Alien")) {
			g.drawImage(myImage2, (int)x, (int)y, null);
		}
		else {
			g.drawImage(myImage, (int)x, (int)y, null);
		}
		
	}
}
