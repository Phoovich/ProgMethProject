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
    private static final double SCENE_WIDTH = 800;
    private static final double SCENE_HEIGHT = 400;
    private static final double WORLD_WIDTH = 1600;

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

        // Ground
        Rectangle ground = new Rectangle(WORLD_WIDTH, 50, Color.DARKGREEN);
        ground.setTranslateY(SCENE_HEIGHT - 50);
        world.getChildren().add(ground);

        // Platforms
        addPlatform(300, 280, 200);
        addPlatform(700, 200, 200);
        addPlatform(900, 200, 200);

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

        // Game loop
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                Platform.runLater(() -> {
                    player.update(activeKeys, world, SCENE_HEIGHT);
                });
                gameLogic.updateBullets();
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

    public static void main(String[] args) {
        launch(args);
    }
}
