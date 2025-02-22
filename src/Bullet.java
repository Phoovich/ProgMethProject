import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Bullet {
    public static final double BULLET_SIZE = 10;
    private static final double BULLET_SPEED = 8;
    private Rectangle shape;
    private boolean movingRight;

    public Bullet(double startX, double startY, boolean movingRight) {
        this.movingRight = movingRight;
        shape = new Rectangle(BULLET_SIZE, BULLET_SIZE, Color.RED);
        shape.setUserData("bullet");  // Mark this node as a bullet
        // Position the bullet depending on the player's facing direction.
        shape.setTranslateX(startX + (movingRight ? Player.PLAYER_SIZE : -BULLET_SIZE));
        shape.setTranslateY(startY);
    }

    public Rectangle getShape() {
        return shape;
    }

    public double getX() {
        return shape.getTranslateX();
    }

    public void update() {
        if (movingRight) {
            shape.setTranslateX(shape.getTranslateX() + BULLET_SPEED);
        } else {
            shape.setTranslateX(shape.getTranslateX() - BULLET_SPEED);
        }
    }
}
