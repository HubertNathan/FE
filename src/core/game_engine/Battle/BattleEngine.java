package core.game_engine.Battle;

import gui.PanelInterface.GameInterface;
import gui.ReadMapFile;
import core.Board;
import core.Cursor;
import core.game_engine.game_state.GameStateContext;
import core.game_engine.game_state.InputManager;
import core.game_engine.game_state.UnitSelectionState;
import core.game_engine.save.LoadSaveBoard;
import javafx.animation.Animation;
import javafx.animation.ParallelTransition;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

import static gui.FireEmblemApp.TILE_SIZE;

public class BattleEngine {
    public static int tileSize = TILE_SIZE;
    private static final Pane background = new Pane(), spritePane = new Pane(), overlayPane = new Pane(), animationPane = new Pane(), arrowPane = new Pane();
    private static Board board = new Board();
    private GameInterface gi;
    public BattleEngine(Stage window, String Chapter) throws IOException {
        Pane root = new Pane();
        Scene mainScene = new Scene(root);
        window.setScene(mainScene);
        root.getChildren().addAll(background, animationPane,arrowPane, spritePane,overlayPane);
        ReadMapFile mapFile = new ReadMapFile("CH1");
        loadBoard(mapFile,mainScene);
    }
    private void loadBoard(ReadMapFile mapFile, Scene scene) throws IOException {
        board = new Board();
        board.init(mapFile);
        gi = new GameInterface(board);
        loadBackground();
        new LoadSaveBoard("CH1").loadBoard(board);
        Cursor cursor = new Cursor(overlayPane);
        String obj = mapFile.getObjectives();
        new InputManager(new GameStateContext(){{setCurrentState(new UnitSelectionState(cursor,gi,obj));}}).addEventHandler(scene);
        displayUnits();
    }
    private void loadBackground(){
        background.getChildren().add(new ImageView(board.drawFX(new Canvas(tileSize * board.getWidth(), tileSize * board.getHeight())).snapshot(null, new WritableImage(tileSize * board.getWidth(), tileSize * board.getHeight()))));
    }
    private void displayUnits(){
        final ParallelTransition spriteAnimation = new ParallelTransition();
        board.getUnits().forEach(unit -> {
            if (unit != null) {

                ImageView imv = new ImageView(unit.getSprites());
                imv.setViewport(new Rectangle2D(0, 0, 6 * 32, 6 * 32));
                unit.setImv(imv);
                imv.setFitWidth(2*tileSize);
                imv.setFitHeight(2*tileSize);
                imv.setTranslateX(unit.getXValue() * tileSize - tileSize/2.);
                imv.setTranslateY(unit.getYValue() * tileSize - tileSize);
                unit.setImv(imv);
                spritePane.getChildren().add(imv);
                Animation animation = unit.getSpriteAnimation();
                spriteAnimation.getChildren().add(animation);
            }
        });
        spriteAnimation.play();
    }
    public static Pane getBackground() {
        return background;
    }
    public static Pane getAnimationPane() {
        return animationPane;
    }
    public static Pane getArrowPane() {
        return arrowPane;
    }
    public static Pane getSpritePane() {
        return spritePane;
    }
    public static Pane getOverlayPane() {
        return overlayPane;
    }
    public static Board getBoard() {
        return board;
    }
}
