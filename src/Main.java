import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Main extends Application {

    private static final double PLAYER_SIZE = 40;
    private static final double BULLET_SIZE = 10;
    private static final double BULLET_SPEED = 8;
    private static final double MOVE_SPEED = 5;
    private static final double JUMP_STRENGTH = -12;
    private static final double GRAVITY = 0.5;
    private static final double SCENE_WIDTH = 800;
    private static final double SCENE_HEIGHT = 400;
    private static final double WORLD_WIDTH = 1600;
    
    private Rectangle player;
    private Pane world;
    private double velocityY = 0;
    private boolean isJumping = false;
    private boolean facingRight = true;
    private final Set<KeyCode> activeKeys = new HashSet<>();
    private final List<Bullet> bullets = new ArrayList<>();

    @Override
    public void start(Stage primaryStage) {
        Pane root = new Pane();
        root.setPrefSize(SCENE_WIDTH, SCENE_HEIGHT);
        root.setStyle("-fx-background-color: skyblue;");

        world = new Pane();
        world.setPrefSize(WORLD_WIDTH, SCENE_HEIGHT);

        // Ground
        Rectangle ground = new Rectangle(WORLD_WIDTH, 50, Color.DARKGREEN);
        ground.setTranslateY(SCENE_HEIGHT - 50);
        world.getChildren().add(ground);

        // Platforms
        addPlatform(300, 280, 200);
        addPlatform(700, 200, 200);

        // Player
        player = new Rectangle(PLAYER_SIZE, PLAYER_SIZE, Color.WHITE);
        player.setTranslateY(SCENE_HEIGHT - 100);
        player.setTranslateX(100);
        world.getChildren().add(player);

        root.getChildren().add(world);
        Scene scene = new Scene(root, SCENE_WIDTH, SCENE_HEIGHT);

        // Key tracking
        scene.setOnKeyPressed(event -> {
            activeKeys.add(event.getCode());
            if (event.getCode() == KeyCode.J) {
                shootBullet(); // Press J to shoot
            }
        });

        scene.setOnKeyReleased(event -> activeKeys.remove(event.getCode()));

        // Game loop
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                Platform.runLater(() -> updateMovement());
                updateBullets();
            }
        };
        timer.start();

        primaryStage.setTitle("Contra-Style Shooting Game (Free Bullets)");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void addPlatform(double x, double y, double width) {
        Rectangle platform = new Rectangle(width, 20, Color.BROWN);
        platform.setTranslateX(x);
        platform.setTranslateY(y);
        world.getChildren().add(platform);
    }

    private void updateMovement() {
        double dx = 0;

        // **Contra-style movement logic**
        if (activeKeys.contains(KeyCode.A)) {
            dx -= MOVE_SPEED;
            facingRight = false;
        }
        if (activeKeys.contains(KeyCode.D)) {
            dx += MOVE_SPEED;
            facingRight = true;
        }

        // **Jumping Mechanic**
        if (activeKeys.contains(KeyCode.SPACE) && !isJumping) {
            velocityY = JUMP_STRENGTH;
            isJumping = true;
        }

        // **Crouch Mechanic (Down Arrow)**
        if (activeKeys.contains(KeyCode.S) || activeKeys.contains(KeyCode.DOWN)) {
            player.setHeight(PLAYER_SIZE / 2);
        } else {
            player.setHeight(PLAYER_SIZE);
        }

        // Move player within world bounds
        double newPlayerX = player.getTranslateX() + dx;
        if (newPlayerX > 0 && newPlayerX < WORLD_WIDTH - PLAYER_SIZE) {
            player.setTranslateX(newPlayerX);
        }

        // **Gravity**
        velocityY += GRAVITY;
        player.setTranslateY(player.getTranslateY() + velocityY);

        // **Collision with Ground**
        if (player.getTranslateY() >= SCENE_HEIGHT - PLAYER_SIZE - 50) {
            player.setTranslateY(SCENE_HEIGHT - PLAYER_SIZE - 50);
            velocityY = 0;
            isJumping = false;
        }

        // **Collision with Platforms**
        for (javafx.scene.Node node : world.getChildren()) {
            if (node instanceof Rectangle && node != player) {
                Rectangle platform = (Rectangle) node;
                if (player.getBoundsInParent().intersects(platform.getBoundsInParent()) &&
                        velocityY > 0) { // Falling onto platform
                    player.setTranslateY(platform.getTranslateY() - PLAYER_SIZE);
                    velocityY = 0;
                    isJumping = false;
                }
            }
        }
    }

    private void shootBullet() {
        Bullet bullet = new Bullet(player.getTranslateX(), player.getTranslateY() + PLAYER_SIZE / 2 - BULLET_SIZE / 2, facingRight);
        world.getChildren().add(bullet.shape);
        bullets.add(bullet);
    }

    private void updateBullets() {
        List<Bullet> toRemove = new ArrayList<>();

        for (Bullet bullet : bullets) {
            bullet.update();

            // Remove bullets if they go off-screen
            if (bullet.shape.getTranslateX() > WORLD_WIDTH || bullet.shape.getTranslateX() < 0) {
                toRemove.add(bullet);
            }
        }

        // Remove bullets that are out of bounds
        bullets.removeAll(toRemove);
        for (Bullet bullet : toRemove) {
            world.getChildren().remove(bullet.shape);
        }
    }

    // **Bullet Class for Independent Movement**
    class Bullet {
        Rectangle shape;
        boolean movingRight;

        public Bullet(double x, double y, boolean movingRight) {
            this.movingRight = movingRight;
            shape = new Rectangle(BULLET_SIZE, BULLET_SIZE, Color.RED);
            shape.setTranslateX(x + (movingRight ? PLAYER_SIZE : -BULLET_SIZE));
            shape.setTranslateY(y);
        }

        public void update() {
            if (movingRight) {
                shape.setTranslateX(shape.getTranslateX() + BULLET_SPEED);
            } else {
                shape.setTranslateX(shape.getTranslateX() - BULLET_SPEED);
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}