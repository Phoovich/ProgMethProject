import javafx.scene.layout.Pane;
import java.util.ArrayList;
import java.util.List;

public class GameLogic {
    private static final double WORLD_WIDTH = 1600;
    private Pane world;
    private Player player;
    private List<Bullet> bullets;

    public GameLogic(Pane world, Player player, List<Bullet> bullets) {
        this.world = world;
        this.player = player;
        this.bullets = bullets;
    }

    public void shootBullet() {
        Bullet bullet = new Bullet(
            player.getX(),
            player.getY() + Player.PLAYER_SIZE / 2 - Bullet.BULLET_SIZE / 2,
            player.isFacingRight()
        );
        world.getChildren().add(bullet.getShape());
        bullets.add(bullet);
    }

    public void updateBullets() {
        List<Bullet> toRemove = new ArrayList<>();
        for (Bullet bullet : bullets) {
            bullet.update();
            // Remove bullets if they go off-screen
            if (bullet.getX() > WORLD_WIDTH || bullet.getX() < 0) {
                toRemove.add(bullet);
            }
        }
        bullets.removeAll(toRemove);
        for (Bullet bullet : toRemove) {
            world.getChildren().remove(bullet.getShape());
        }
    }
}
