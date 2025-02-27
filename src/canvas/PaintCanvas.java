package canvas;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import tools.Tool;
import ui.Toolbar;

public class PaintCanvas {
    private Canvas canvas;
    private GraphicsContext gc;
    private double lastX, lastY;
    private Toolbar toolbar;
    private Tool currentTool;

    public PaintCanvas() {
        canvas = new Canvas(800, 600);
        gc = canvas.getGraphicsContext2D();
        clearCanvas(); // ล้าง Canvas เมื่อเริ่มต้น

        canvas.setOnMousePressed(e -> {
            lastX = e.getX();
            lastY = e.getY();
            if (currentTool != null) {
                currentTool.onPress(gc, lastX, lastY, toolbar);
            }
        });

        canvas.setOnMouseDragged(e -> {
            double currentX = e.getX();
            double currentY = e.getY();
            if (currentTool != null) {
                currentTool.onDrag(gc, lastX, lastY, currentX, currentY, toolbar);
            }
            lastX = currentX;
            lastY = currentY;
        });
    }

    public void setToolbar(Toolbar toolbar) {
        this.toolbar = toolbar;
    }

    public void setCurrentTool(Tool tool) {
        this.currentTool = tool;
    }

    public Canvas getCanvas() {
        return canvas;
    }

    /**
     * ล้าง Canvas และตั้งค่าพื้นหลังเป็นสีขาว
     */
    public void clearCanvas() {
        gc.setFill(Color.WHITE); // ตั้งค่าสีพื้นหลังเป็นสีขาว
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight()); // ล้าง Canvas
    }
}