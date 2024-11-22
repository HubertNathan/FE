package GameEngine.Battle;

import GUI.Battle;
import GUI.ReadMapFile;
import GUI.SpriteSheet;
import GameEngine.Board;
import GameEngine.Cursor;
import GameEngine.Save.LoadSaveBoard;
import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.ParallelTransition;
import javafx.animation.Transition;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;

import static GUI.FireEmblemApp.FACTOR;
import static GUI.FireEmblemApp.TILE_SIZE;

public class BattleEngine {
    private final Pane background = new Pane(), spritePane = new Pane(), overlayPane = new Pane();
    Board board;
    public BattleEngine(Stage window, String Chapter) throws IOException {
        Pane root = new Pane();
        window.setScene(new Scene(root));
        root.getChildren().addAll(background, spritePane, overlayPane);
        ReadMapFile mapFile = new ReadMapFile("CH1");
        loadBoard(mapFile);
    }
    private void loadBoard(ReadMapFile mapFile) throws IOException {
        board = new Board(mapFile);
        loadBackground();
        new LoadSaveBoard("CH1").loadBoard(board);
        displayUnits();
        displayCursor();
    }
    private void loadBackground(){
        background.getChildren().add(new ImageView(board.drawFX(new Canvas(SpriteSheet.tileSize * board.getWidth(), SpriteSheet.tileSize * board.getHeight())).snapshot(null, new WritableImage(SpriteSheet.tileSize * board.getWidth(), SpriteSheet.tileSize * board.getHeight()))));
    }
    private void displayUnits(){
        final ParallelTransition spriteAnimation = new ParallelTransition();
        board.getUnits().forEach(unit -> {
            if (unit != null) {

                ImageView imv = new ImageView(unit.getSprites());
                imv.setViewport(new Rectangle2D(0, 0, 6 * 32, 6 * 32));
                unit.setImv(imv);
                imv.setFitWidth(2*SpriteSheet.tileSize);
                imv.setFitHeight(2*SpriteSheet.tileSize);
                imv.setTranslateX(unit.getXValue() * SpriteSheet.tileSize - SpriteSheet.tileSize/2.);
                imv.setTranslateY(unit.getYValue() * SpriteSheet.tileSize - SpriteSheet.tileSize);
                unit.setImv(imv);
                spritePane.getChildren().add(imv);
                Animation animation = unit.getSpriteAnimation();
                spriteAnimation.getChildren().add(animation);
            }
        });
        spriteAnimation.play();
    }
    private void displayCursor(){
        Cursor cursor = new Cursor(board.getLeader(),board);
        ImageView cursorImageView = new ImageView(new Image("file:src/GUI/CursorSprites/Cursor1.png", 192, 192, false, false));
        cursorImageView.setFitWidth(96);
        cursorImageView.setFitHeight(96);
        cursorImageView.setTranslateX(board.getLeader().getXValue() * TILE_SIZE*FACTOR - 24);
        cursorImageView.setTranslateY(board.getLeader().getYValue() * 48 - 24);
        overlayPane.getChildren().add(cursorImageView);
        new CursorTransition(cursorImageView,cursor).play();
    }
    private class CursorTransition extends Transition {
        ImageView imv;
        public Image backup;

        CursorTransition(ImageView imv, Cursor cursor) {
            this.imv = imv;
            this.cursor = cursor;
            this.cursor.setIMV(imv);
            cursorImageView = imv;
            setCycleDuration(Duration.millis(1200));
            setInterpolator(Interpolator.LINEAR);
            setCycleCount(Transition.INDEFINITE);
        }

        final Cursor cursor;
        final ImageView cursorImageView;
        int lastIndex = 0;
        final HashMap<Integer, Image> CursorMap = new HashMap<>() {{
            put(1, new Image("file:src/GUI/CursorSprites/Cursor1.png", 192, 192, false, false));
            put(2, new Image("file:src/GUI/CursorSprites/Cursor2.png", 192, 192, false, false));
            put(3, new Image("file:src/GUI/CursorSprites/Cursor3.png", 192, 192, false, false));
            put(11, new Image("file:src/GUI/CursorSprites/Cursor11.png", 192, 192, false, false));
            put(12, new Image("file:src/GUI/CursorSprites/Cursor12.png", 192, 192, false, false));
            put(13, new Image("file:src/GUI/CursorSprites/Cursor13.png", 192, 192, false, false));
        }};

        public Image getImage() {
            return backup;
        }


        private int updateAnimation(double animFrame) {
            return 3 + (animFrame > 1 ? -1 : 0) + (animFrame > 3 ? -1 : 0) + (animFrame > 19 ? 1 : 0) + (animFrame > 21 ? 1 : 0);
        }

        @Override
        protected void interpolate(double k) {
            int index = updateAnimation(36 * k) + (cursor.getColor().equals("red") ? 10 : 0);
            if ((index != lastIndex) && ((cursor.getUnit() != null && !cursor.getUnit().getColor().equals("blue")) || cursor.getUnit() == null || cursor.getSelectedUnit() != null)) {
                backup = CursorMap.get(index);
                imv.setImage(CursorMap.get(index));
            }
            lastIndex = index;
        }

    }
}
