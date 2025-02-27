package tools;

import javafx.scene.canvas.GraphicsContext;
import ui.Toolbar;

public class BrushTool implements Tool {
    @Override
    public void onPress(GraphicsContext gc, double x, double y, Toolbar toolbar) {
        // ตั้งค่าสีและขนาดของแปรง
        gc.setFill(toolbar.getSelectedColor());
        gc.setStroke(toolbar.getSelectedColor());
        gc.setLineWidth(toolbar.getSelectedSize());

        // วาดจุดแรกเมื่อคลิก
        drawPoint(gc, x, y, toolbar.getSelectedSize());
    }

    @Override
    public void onDrag(GraphicsContext gc, double x1, double y1, double x2, double y2, Toolbar toolbar) {
        // วาดเส้นทีละจุดระหว่างจุดเริ่มต้นและจุดสิ้นสุด
        drawLine(gc, x1, y1, x2, y2, toolbar.getSelectedSize());
    }

    /**
     * วาดจุดที่ตำแหน่ง (x, y) ด้วยขนาดที่กำหนด
     */
    private void drawPoint(GraphicsContext gc, double x, double y, double size) {
        gc.fillOval(x - size / 2, y - size / 2, size, size);
    }

    /**
     * วาดเส้นทีละจุดระหว่างจุดเริ่มต้น (x1, y1) และจุดสิ้นสุด (x2, y2)
     */
    private void drawLine(GraphicsContext gc, double x1, double y1, double x2, double y2, double size) {
        double dx = x2 - x1;
        double dy = y2 - y1;
        double distance = Math.sqrt(dx * dx + dy * dy); // ระยะห่างระหว่างจุดเริ่มต้นและจุดสิ้นสุด

        // วาดจุดทีละจุดตามระยะห่าง
        for (double i = 0; i <= distance; i += 1) {
            double ratio = i / distance;
            double x = x1 + dx * ratio;
            double y = y1 + dy * ratio;
            drawPoint(gc, x, y, size);
        }
    }
}