import java.awt.Image;

public class Spaceship extends Sprite2D {

	public Spaceship(Image i, int winWidth) {
		super(i, winWidth);
	}
	
	public void move() {
		this.x += xSpeed;
	}

}
