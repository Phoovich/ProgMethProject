package ui;

import canvas.PaintCanvas;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class MainLayout {
    private BorderPane root;
    private PaintCanvas paintCanvas;
    private Toolbar toolbar;
    private Menubar menubar;

    public MainLayout(PaintCanvas paintCanvas, Toolbar toolbar, Menubar menubar) {
        this.paintCanvas = paintCanvas;
        this.toolbar = toolbar;
        this.menubar = menubar;
        root = new BorderPane();
        initializeLayout();
    }

    private void initializeLayout() {
        // สร้าง VBox เพื่อจัดเรียง MenuBar และ Toolbar ในแนวตั้ง
        VBox topContainer = new VBox();
        topContainer.getChildren().addAll(menubar.getMenuBar(), toolbar.getToolbar()); // เพิ่ม MenuBar และ Toolbar ลงใน VBox
        topContainer.setSpacing(10); // กำหนดระยะห่างระหว่าง MenuBar และ Toolbar
        topContainer.setPadding(new javafx.geometry.Insets(10)); // กำหนด padding 10px

        // วาง VBox ที่มี MenuBar และ Toolbar ไว้ด้านบนของ BorderPane
        root.setTop(topContainer);
        root.setCenter(paintCanvas.getCanvas()); // Canvas อยู่ตรงกลาง
    }

    public BorderPane getRoot() {
        return root;
    }
}