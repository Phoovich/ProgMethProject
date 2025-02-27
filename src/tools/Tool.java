package tools;

import javafx.scene.canvas.GraphicsContext;
import ui.Toolbar;

public interface Tool {
    void onPress(GraphicsContext gc, double x, double y, Toolbar toolbar);
    void onDrag(GraphicsContext gc, double x1, double y1, double x2, double y2, Toolbar toolbar);
}
