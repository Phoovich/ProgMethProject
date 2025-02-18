import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.util.HashSet;
import java.util.Set;

public class Main extends Application {

    private static final double BLOCK_SIZE = 50;
    private static final double MOVE_SPEED = 4;
    private static final double JUMP_STRENGTH = -10;
    private static final double GRAVITY = 0.4;
    private static final double GROUND_Y = 150;  // Simulated ground level

    private Rectangle block;
    private double velocityY = 0;
    private boolean isJumping = false;
    private final Set<KeyCode> activeKeys = new HashSet<>();

    @Override
    public void start(Stage primaryStage) {
        StackPane root = new StackPane();
        root.setStyle("-fx-background-color: blue;");

        // Create a white block
        block = new Rectangle(BLOCK_SIZE, BLOCK_SIZE, Color.WHITE);
        block.setTranslateY(GROUND_Y);
        root.getChildren().add(block);

        Scene scene = new Scene(root, 600, 400);

        // Key press and release tracking
        scene.setOnKeyPressed(event -> activeKeys.add(event.getCode()));
        scene.setOnKeyReleased(event -> activeKeys.remove(event.getCode()));

        // Animation loop for smooth movement
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                updateMovement();
            }
        };
        timer.start();

        primaryStage.setTitle("Smooth Motion JavaFX");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void updateMovement() {
        double dx = 0;

        // Check movement keys
        if (activeKeys.contains(KeyCode.A)) {
            dx -= MOVE_SPEED;
        }
        if (activeKeys.contains(KeyCode.D)) {
            dx += MOVE_SPEED;
        }

        // Apply movement
        block.setTranslateX(block.getTranslateX() + dx);

        // Handle jumping
        if (activeKeys.contains(KeyCode.SPACE) && !isJumping) {
            velocityY = JUMP_STRENGTH;
            isJumping = true;
        }

        // Apply gravity
        velocityY += GRAVITY;
        block.setTranslateY(block.getTranslateY() + velocityY);

        // Stop falling at ground level
        if (block.getTranslateY() >= GROUND_Y) {
            block.setTranslateY(GROUND_Y);
            velocityY = 0;
            isJumping = false;
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}