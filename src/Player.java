import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import java.util.Set;

public class Player {
    public static final double PLAYER_SIZE = 40;
    private static final double MOVE_SPEED = 4;
    private static final double JUMP_STRENGTH = -12;
    private static final double GRAVITY = 0.5;

    private Rectangle shape;
    private double velocityY = 0;
    private boolean isJumping = false;
    private boolean facingRight = true;

    public Player(double startX, double startY) {
        shape = new Rectangle(PLAYER_SIZE, PLAYER_SIZE, Color.WHITE);
        shape.setTranslateX(startX);
        shape.setTranslateY(startY);
    }

    public Rectangle getShape() {
        return shape;
    }
    
    public double getX() {
        return shape.getTranslateX();
    }
    
    public double getY() {
        return shape.getTranslateY();
    }

    public boolean isFacingRight() {
        return facingRight;
    }

    /**
     * Updates the player movement and handles jumping, gravity, and collisions.
     *
     * @param activeKeys the set of currently pressed keys
     * @param world the Pane containing game objects (used for collision detection)
     * @param sceneHeight the height of the visible scene
     */
    public void update(Set<KeyCode> activeKeys, Pane world, double sceneHeight) {
        double dx = 0;

        // Contra-style left/right movement
        if (activeKeys.contains(KeyCode.A)) {
            dx -= MOVE_SPEED;
            facingRight = false;
        }
        if (activeKeys.contains(KeyCode.D)) {
            dx += MOVE_SPEED;
            facingRight = true;
        }

        // Jumping mechanic
        if (activeKeys.contains(KeyCode.SPACE) && !isJumping) {
            velocityY = JUMP_STRENGTH;
            isJumping = true;
        }

        // Crouch mechanic
        if (activeKeys.contains(KeyCode.S) || activeKeys.contains(KeyCode.DOWN)) {
            shape.setHeight(PLAYER_SIZE / 2);
        } else {
            shape.setHeight(PLAYER_SIZE);
        }

        // Update horizontal movement (ensuring the player stays within the world bounds)
        double newX = shape.getTranslateX() + dx;
        if (newX > 0 && newX < world.getPrefWidth() - PLAYER_SIZE) {
            shape.setTranslateX(newX);
        }

        // Apply gravity
        velocityY += GRAVITY;
        shape.setTranslateY(shape.getTranslateY() + velocityY);

        // Collision with ground
        if (shape.getTranslateY() >= sceneHeight - PLAYER_SIZE - 50) {
            shape.setTranslateY(sceneHeight - PLAYER_SIZE - 50);
            velocityY = 0;
            isJumping = false;
        }

        // Only check platform collisions if the player is NOT pressing "S" (or Down)
        if (!activeKeys.contains(KeyCode.S) && !activeKeys.contains(KeyCode.DOWN)) {
            // Collision with platforms
            for (Node node : world.getChildren()) {
                if (node instanceof Rectangle && node != shape) {
                    // (Optional) Skip nodes marked as bullets if you're using that logic:
                    if ("bullet".equals(node.getUserData())) {
                        continue;
                    }
                    Rectangle platform = (Rectangle) node;
                    if (shape.getBoundsInParent().intersects(platform.getBoundsInParent()) && velocityY > 0) {
                        shape.setTranslateY(platform.getTranslateY() - PLAYER_SIZE);
                        velocityY = 0;
                        isJumping = false;
                    }
                }
            }
        }

    }
}
