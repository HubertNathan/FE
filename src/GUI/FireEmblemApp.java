package GUI;

import GameEngine.Board;
import Units.Brigand;
import Units.Cavalier;
import Units.Lyn_Lord;
import Units.Unit;
import javafx.animation.Animation;
import javafx.animation.ParallelTransition;
import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.*;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

public class FireEmblemApp extends Application {
    Stage window;
    Scene gameScene;
    Board board;
    Pane root = new Pane();
    ImageView background;
    @Override
    public void start(Stage stage) throws Exception {
        window = stage;
        window.setTitle("Fire Emblem");
        gameScene = new Scene(root);
        gameScene.setFill(Color.WHITE);
        loadBoard(new ReadMapFile());
        Lyn_Lord unit =new Lyn_Lord();
        displayUnit(unit);
        window.show();
        window.setScene(gameScene);
        stage.widthProperty().addListener((obs, oldVal, newVal) -> {
            background.setFitWidth((Double) newVal);
        });

        stage.heightProperty().addListener((obs, oldVal, newVal) -> {
            background.setFitHeight((Double) newVal-37);
        });


    }

    private void ResizeBackground(int scaleFactor){
        Image image = background.getImage();
        background = new ImageView(
                resample(
                        image,
                        scaleFactor
                )
        );
    }
    private Image resample(Image input, int scaleFactor) {
        final int W = (int) input.getWidth();
        final int H = (int) input.getHeight();
        final int S = scaleFactor;

        WritableImage output = new WritableImage(
                W * S,
                H * S
        );

        PixelReader reader = input.getPixelReader();
        PixelWriter writer = output.getPixelWriter();

        for (int y = 0; y < H; y++) {
            for (int x = 0; x < W; x++) {
                final int argb = reader.getArgb(x, y);
                for (int dy = 0; dy < S; dy++) {
                    for (int dx = 0; dx < S; dx++) {
                        writer.setArgb(x * S + dx, y * S + dy, argb);
                    }
                }
            }
        }

        return output;
    }
    private void loadBoard(ReadMapFile mapReader) throws IOException {
        board = new Board(mapReader);
        loadBackground();
        board.setUnit(new Lyn_Lord(),2,2,true);
        board.setUnit(new Cavalier("Sain"), 4, 4);
        board.setUnit(new Brigand("Brigand"),6,6);
        displayUnits(board);
    }
    private void loadBackground() {
        background = new ImageView(board.drawFX(new Canvas(16*board.getWidth(),16*board.getHeight())).snapshot(null,new WritableImage(16*board.getWidth(),16*board.getHeight())));
        ResizeBackground(6);
        background.setFitWidth(48*board.getWidth());
        background.setFitHeight(48*board.getHeight());
        root.getChildren().add(background);
    }
    private void displayUnit(Unit unit){
        ImageView imv = new ImageView(unit.getSprites());
        imv.setViewport(new Rectangle2D(0, 6*16, 6*16, 6*16));;
        imv.setFitWidth(48);
        imv.setFitHeight(48);
        Animation animation = new SpriteAnimation(unit,imv);
        animation.setCycleCount(Animation.INDEFINITE);
        animation.play();
        root.getChildren().add(imv);
    }
    private void displayUnits(Board board){
        ParallelTransition spriteAnimation = new ParallelTransition();
        board.getUnits().forEach(unit -> {
            if (unit != null) {

                ImageView imv = new ImageView(unit.getSprites());
                if (unit.toString().equals("Lyn") && unit.getMode().equals("standing")) {
                    imv.setViewport(new Rectangle2D(0, 6 * 16, 6 * 16, 6 * 16));
                    imv.setFitWidth(48);
                    imv.setFitHeight(48);
                    imv.setTranslateX(3* 48);

                    imv.setTranslateY(unit.getYValue() * 48);
                    root.getChildren().add(imv);
                } else {
                    imv.setViewport(new Rectangle2D(0, 6 * 32, 6 * 32, 6 * 32));
                    imv.setFitWidth(96);
                    imv.setFitHeight(96);
                    imv.setTranslateX(unit.getXValue() * 48 - 24);
                    imv.setTranslateY(unit.getYValue() * 48 - 24);
                    root.getChildren().add(imv);
                }
                Animation animation = new SpriteAnimation(unit, imv);
                animation.setCycleCount(Animation.INDEFINITE);
                spriteAnimation.getChildren().add(animation);
            }
        });
        spriteAnimation.play();

    }


    public static void main(String[] args) {
        launch();
    }
}
