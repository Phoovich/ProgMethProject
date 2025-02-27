package ui;

import canvas.PaintCanvas;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import file.FileManager;

import java.io.File;
import java.util.Optional;

public class Menubar {
    private MenuBar menuBar;
    private PaintCanvas paintCanvas;

    public Menubar(PaintCanvas paintCanvas) {
        this.paintCanvas = paintCanvas;
        menuBar = new MenuBar();
        initializeMenu();
    }

    private void initializeMenu() {
        Menu fileMenu = new Menu("File");

        // เมนู "New"
        MenuItem newItem = new MenuItem("New Canvas");
        newItem.setOnAction(e -> createNewCanvas()); // เรียกเมธอดเมื่อคลิกที่ "New"

        // เมนู "Export as PNG"
        MenuItem exportItem = new MenuItem("Export as PNG");
        exportItem.setOnAction(e -> exportAsPNG());

        fileMenu.getItems().addAll(newItem, exportItem); // เพิ่มเมนู "New" และ "Export as PNG"
        menuBar.getMenus().add(fileMenu);
    }

    /**
     * สร้างภาพใหม่ (ล้าง Canvas) พร้อมแสดง Alert ยืนยัน
     */
    private void createNewCanvas() {
        // สร้าง Alert เพื่อยืนยันการล้าง Canvas
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("New Canvas");
        alert.setHeaderText("Are you sure you want to create a new canvas?");
        alert.setContentText("All unsaved changes will be lost.");

        // รอผลลัพธ์จากผู้ใช้
        Optional<ButtonType> result = alert.showAndWait();

        // ถ้าผู้ใช้กด OK ให้ล้าง Canvas
        if (result.isPresent() && result.get() == ButtonType.OK) {
            paintCanvas.clearCanvas(); // เรียกเมธอด clearCanvas() ใน PaintCanvas
        }
    }

    /**
     * Export ภาพเป็น PNG
     */
    private void exportAsPNG() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Image");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PNG files (*.png)", "*.png"));
        File file = fileChooser.showSaveDialog(new Stage());

        if (file != null) {
            try {
                FileManager.saveCanvas(paintCanvas, file);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public MenuBar getMenuBar() {
        return menuBar;
    }
}