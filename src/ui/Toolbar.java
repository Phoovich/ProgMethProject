package ui;

import canvas.PaintCanvas;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import tools.BrushTool;
import tools.EraserTool;
import tools.HighlightTool;
import tools.Tool;

public class Toolbar {
    private PaintCanvas paintCanvas;
    private HBox toolbar;
    private Slider sizeSlider;
    private ColorPicker colorPicker;
    private double selectedSize = 5;
    private Color selectedColor = Color.BLACK;

    public Toolbar(PaintCanvas paintCanvas) {
        this.paintCanvas = paintCanvas;
        toolbar = new HBox();
        toolbar.setSpacing(10);
        toolbar.setAlignment(javafx.geometry.Pos.CENTER);
        initializeTools();
        initializeSizeSlider();
        initializeColorPicker();
    }

    private void initializeTools() {
        // ไอคอน Brush
        Image brushIcon = new Image(getClass().getResourceAsStream("/res/brush.png"));
        ImageView brushView = new ImageView(brushIcon);
        brushView.setFitWidth(25);
        brushView.setFitHeight(25);
        Button brushButton = new Button("", brushView);
        brushButton.setOnAction(e -> paintCanvas.setCurrentTool(new BrushTool()));

        // ไอคอน Eraser
        Image eraserIcon = new Image(getClass().getResourceAsStream("/res/eraser.png"));
        ImageView eraserView = new ImageView(eraserIcon);
        eraserView.setFitWidth(25);
        eraserView.setFitHeight(25);
        Button eraserButton = new Button("", eraserView);
        eraserButton.setOnAction(e -> paintCanvas.setCurrentTool(new EraserTool()));

        // ไอคอน Highlight
        Image highlightIcon = new Image(getClass().getResourceAsStream("/res/highlight.png"));
        ImageView highlightView = new ImageView(highlightIcon);
        highlightView.setFitWidth(25);
        highlightView.setFitHeight(25);
        Button highlightButton = new Button("", highlightView);
        highlightButton.setOnAction(e -> paintCanvas.setCurrentTool(new HighlightTool()));

        // เพิ่มเครื่องมือลงใน Toolbar
        toolbar.getChildren().addAll(brushButton, eraserButton, highlightButton);
    }

    private void initializeSizeSlider() {
        sizeSlider = new Slider(1, 50, selectedSize);
        sizeSlider.setShowTickLabels(true);
        sizeSlider.setShowTickMarks(true);
        sizeSlider.setMajorTickUnit(10);
        sizeSlider.setMinorTickCount(5);
        sizeSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            selectedSize = newVal.doubleValue();
        });

        toolbar.getChildren().add(sizeSlider);
    }

    private void initializeColorPicker() {
        colorPicker = new ColorPicker(selectedColor);
        colorPicker.setOnAction(e -> {
            selectedColor = colorPicker.getValue();
        });

        toolbar.getChildren().add(colorPicker);
    }

    public HBox getToolbar() {
        return toolbar;
    }

    public double getSelectedSize() {
        return selectedSize;
    }

    public Color getSelectedColor() {
        return selectedColor;
    }
}