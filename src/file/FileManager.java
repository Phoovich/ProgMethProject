package file;

import canvas.PaintCanvas;
import javafx.scene.image.WritableImage;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.awt.image.BufferedImage;

public class FileManager {
    /**
     * บันทึกภาพจาก PaintCanvas เป็นไฟล์ PNG
     *
     * @param paintCanvas Canvas ที่ต้องการบันทึก
     * @param file        ไฟล์ที่ต้องการบันทึก
     */
    public static void saveCanvas(PaintCanvas paintCanvas, File file) {
        try {
            // สร้าง WritableImage จาก Canvas
            WritableImage writableImage = new WritableImage((int) paintCanvas.getCanvas().getWidth(), (int) paintCanvas.getCanvas().getHeight());
            paintCanvas.getCanvas().snapshot(null, writableImage);

            // แปลง WritableImage เป็น BufferedImage
            BufferedImage bufferedImage = new BufferedImage(
                    (int) writableImage.getWidth(),
                    (int) writableImage.getHeight(),
                    BufferedImage.TYPE_INT_ARGB
            );

            // คัดลอกพิกเซลจาก WritableImage ไปยัง BufferedImage
            for (int y = 0; y < writableImage.getHeight(); y++) {
                for (int x = 0; x < writableImage.getWidth(); x++) {
                    bufferedImage.setRGB(x, y, writableImage.getPixelReader().getArgb(x, y));
                }
            }

            // บันทึก BufferedImage เป็นไฟล์ PNG
            ImageIO.write(bufferedImage, "png", file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}