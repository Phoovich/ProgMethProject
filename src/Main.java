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
    private static final double SCENE_WIDTH = 1000;
    private static final double SCENE_HEIGHT = 400;
    private static final double WORLD_WIDTH = 5000;

    private Pane world;
    private Player player;
    private final Set<KeyCode> activeKeys = new HashSet<>();
    private final List<Bullet> bullets = new ArrayList<>();
    private GameLogic gameLogic;

    @Override
    public void start(Stage primaryStage) {
        // Set up root pane and world
        Pane root = new Pane();
        root.setPrefSize(SCENE_WIDTH, SCENE_HEIGHT);
        root.setStyle("-fx-background-color: skyblue;");

        world = new Pane();
        world.setPrefSize(WORLD_WIDTH, SCENE_HEIGHT);

        // Create the map (ground and platforms)
        createMap();

        // Create Player and add to the world
        player = new Player(100, SCENE_HEIGHT - 100);
        world.getChildren().add(player.getShape());

        // Initialize GameLogic with the world, player, and bullets list.
        gameLogic = new GameLogic(world, player, bullets);

        root.getChildren().add(world);
        Scene scene = new Scene(root, SCENE_WIDTH, SCENE_HEIGHT);

        // Key tracking
        scene.setOnKeyPressed(event -> {
            activeKeys.add(event.getCode());
            if (event.getCode() == KeyCode.J) {
                gameLogic.shootBullet();
            }
        });

        scene.setOnKeyReleased(event -> activeKeys.remove(event.getCode()));

        // Game loop with camera logic
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                Platform.runLater(() -> {
                    // Update player's movement
                    player.update(activeKeys, world, SCENE_HEIGHT);
                    
                    // Camera following logic:
                    double offsetX = SCENE_WIDTH / 2 - (player.getX() + Player.PLAYER_SIZE / 2);
                    // Clamp offset so the world doesn't scroll past its boundaries
                    offsetX = Math.min(0, offsetX);
                    offsetX = Math.max(SCENE_WIDTH - WORLD_WIDTH, offsetX);
                    world.setTranslateX(offsetX);
                });
                gameLogic.updateBullets();
            }
        };
        timer.start();

        primaryStage.setTitle("Contra-Style Shooting Game (Free Bullets)");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Creates the map for the game by adding the ground and several platforms.
     * Adjust the coordinates and sizes to fit your desired level layout.
     */
    private void createMap() {
        // Ground
        Rectangle ground = new Rectangle(WORLD_WIDTH, 50, Color.DARKGREEN);
        ground.setTranslateY(SCENE_HEIGHT - 50);
        world.getChildren().add(ground);

        // Platforms for a Contra-style level:
        addPlatform(200, SCENE_HEIGHT - 150, 150);   // Platform 1
        addPlatform(400, SCENE_HEIGHT - 220, 200);   // Platform 2
        addPlatform(700, SCENE_HEIGHT - 180, 150);   // Platform 3
        addPlatform(950, SCENE_HEIGHT - 250, 200);   // Platform 4
        addPlatform(1300, SCENE_HEIGHT - 160, 250);  // Platform 5
    }

    /**
     * Helper method to add a platform to the world.
     *
     * @param x the x-coordinate of the platform
     * @param y the y-coordinate of the platform
     * @param width the width of the platform
     */
    private void addPlatform(double x, double y, double width) {
        Rectangle platform = new Rectangle(width, 20, Color.BROWN);
        platform.setTranslateX(x);
        platform.setTranslateY(y);
        world.getChildren().add(platform);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
