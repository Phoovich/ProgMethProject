import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import java.util.HashSet;
import java.util.Set;

public class Main extends Application {

    private static final double BLOCK_SIZE = 40;
    private static final double MOVE_SPEED = 5;
    private static final double JUMP_STRENGTH = -12;
    private static final double GRAVITY = 0.5;
    private static final double SCENE_WIDTH = 800;
    private static final double SCENE_HEIGHT = 400;
    private static final double WORLD_WIDTH = 3000; // Large world like Contra

    private Rectangle player;
    private Pane world;
    private double velocityY = 0;
    private boolean isJumping = false;
    private final Set<KeyCode> activeKeys = new HashSet<>();

    @Override
    public void start(Stage primaryStage) {
        Pane root = new Pane();
        root.setPrefSize(SCENE_WIDTH, SCENE_HEIGHT);
        root.setStyle("-fx-background-color: skyblue;"); // Fixed background

        // World (contains platforms and player)
        world = new Pane();
        world.setPrefSize(WORLD_WIDTH, SCENE_HEIGHT);

        // Create ground
        Rectangle ground = new Rectangle(WORLD_WIDTH, 50, Color.DARKGREEN);
        ground.setTranslateY(SCENE_HEIGHT - 50);
        world.getChildren().add(ground);

        // Create platforms (like in Contra)
        Rectangle platform1 = new Rectangle(200, 20, Color.BROWN);
        platform1.setTranslateX(300);
        platform1.setTranslateY(280);
        world.getChildren().add(platform1);

        Rectangle platform2 = new Rectangle(200, 20, Color.BROWN);
        platform2.setTranslateX(700);
        platform2.setTranslateY(200);
        world.getChildren().add(platform2);

        // Player character (white block)
        player = new Rectangle(BLOCK_SIZE, BLOCK_SIZE, Color.WHITE);
        player.setTranslateY(SCENE_HEIGHT - 100);
        player.setTranslateX(100);
        world.getChildren().add(player);

        root.getChildren().add(world);
        Scene scene = new Scene(root, SCENE_WIDTH, SCENE_HEIGHT);

        // Key press/release handling
        scene.setOnKeyPressed(event -> activeKeys.add(event.getCode()));
        scene.setOnKeyReleased(event -> activeKeys.remove(event.getCode()));

        // Game loop for movement and jumping
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                Platform.runLater(() -> updateMovement());
            }
        };
        timer.start();

        primaryStage.setTitle("Contra-Like JavaFX Game");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void updateMovement() {
        double dx = 0;

        // Move left/right
        if (activeKeys.contains(KeyCode.A)) {
            dx -= MOVE_SPEED;
        }
        if (activeKeys.contains(KeyCode.D)) {
            dx += MOVE_SPEED;
        }

        // Keep player within world bounds
        double newPlayerX = player.getTranslateX() + dx;
        if (newPlayerX > 0 && newPlayerX < WORLD_WIDTH - BLOCK_SIZE) {
            player.setTranslateX(newPlayerX);
        }

        // Jumping
        if (activeKeys.contains(KeyCode.SPACE) && !isJumping) {
            velocityY = JUMP_STRENGTH;
            isJumping = true;
        }

        // Apply gravity
        velocityY += GRAVITY;
        player.setTranslateY(player.getTranslateY() + velocityY);

        // Collision with ground
        if (player.getTranslateY() >= SCENE_HEIGHT - BLOCK_SIZE - 50) {
            player.setTranslateY(SCENE_HEIGHT - BLOCK_SIZE - 50);
            velocityY = 0;
            isJumping = false;
        }

        // Collision with platforms
        for (javafx.scene.Node node : world.getChildren()) {
            if (node instanceof Rectangle && node != player) {
                Rectangle platform = (Rectangle) node;
                if (player.getBoundsInParent().intersects(platform.getBoundsInParent()) &&
                        velocityY > 0) { // Falling onto platform
                    player.setTranslateY(platform.getTranslateY() - BLOCK_SIZE);
                    velocityY = 0;
                    isJumping = false;
                }
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}