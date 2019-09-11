import java.awt.Image;

public class PlayerBullet extends Sprite2D {

	public PlayerBullet(Image i, int winWidth) {
		super(i, winWidth);
	}
	
	public boolean move() {
		this.y -= 20;
		return true;
	}

}
