package tools;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import ui.Toolbar;

public class EraserTool implements Tool {
    @Override
    public void onPress(GraphicsContext gc, double x, double y, Toolbar toolbar) {
        // ตั้งค่าสีเป็นสีขาว
        gc.setFill(Color.WHITE);
        gc.setStroke(Color.WHITE);
        gc.setLineWidth(toolbar.getSelectedSize());

        // ลบจุดแรกเมื่อคลิก
        erasePoint(gc, x, y, toolbar.getSelectedSize());
    }

    @Override
    public void onDrag(GraphicsContext gc, double x1, double y1, double x2, double y2, Toolbar toolbar) {
        // ลบเส้นทีละจุดระหว่างจุดเริ่มต้นและจุดสิ้นสุด
        eraseLine(gc, x1, y1, x2, y2, toolbar.getSelectedSize());
    }

    /**
     * ลบจุดที่ตำแหน่ง (x, y) ด้วยสีขาวและขนาดที่กำหนด
     */
    private void erasePoint(GraphicsContext gc, double x, double y, double size) {
        gc.fillOval(x - size / 2, y - size / 2, size, size);
    }

    /**
     * ลบเส้นทีละจุดระหว่างจุดเริ่มต้น (x1, y1) และจุดสิ้นสุด (x2, y2) ด้วยสีขาว
     */
    private void eraseLine(GraphicsContext gc, double x1, double y1, double x2, double y2, double size) {
        double dx = x2 - x1;
        double dy = y2 - y1;
        double distance = Math.sqrt(dx * dx + dy * dy); // ระยะห่างระหว่างจุดเริ่มต้นและจุดสิ้นสุด

        // ลบจุดทีละจุดตามระยะห่าง
        for (double i = 0; i <= distance; i += 1) {
            double ratio = i / distance;
            double x = x1 + dx * ratio;
            double y = y1 + dy * ratio;
            erasePoint(gc, x, y, size);
        }
    }
}