package main;

import canvas.PaintCanvas;
import tools.BrushTool;
import ui.MainLayout;
import ui.Menubar;
import ui.Toolbar;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class PaintApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Paint App");

        PaintCanvas paintCanvas = new PaintCanvas();
        Toolbar toolbar = new Toolbar(paintCanvas);
        paintCanvas.setToolbar(toolbar);

        // ตั้งค่า BrushTool เป็นเครื่องมือเริ่มต้น
        paintCanvas.setCurrentTool(new BrushTool());

        // สร้าง Menubar และ MainLayout
        Menubar menubar = new Menubar(paintCanvas);
        MainLayout mainLayout = new MainLayout(paintCanvas, toolbar, menubar);

        Scene scene = new Scene(mainLayout.getRoot(), 900, 700);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}