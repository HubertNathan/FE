package core.game_engine.game_state;

import gui.PanelInterface.GameInterface;
import core.game_engine.Battle.BattleEngine;
import core.Board;
import core.Cursor;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;

import java.io.IOException;

import static core.game_engine.Battle.BattleEngine.tileSize;
import static gui.FireEmblemApp.TILE_SIZE;

public class UnitSelectionState implements GameState{
    private final Cursor cursor;
    private static final Board board = BattleEngine.getBoard();
    private static final Pane pane = BattleEngine.getOverlayPane();
    private final GameInterface gi;
    private GameStateContext context;
    private final String objectives;
    public UnitSelectionState(Cursor cursor, GameInterface gi, String objectives) throws IOException {
        this.cursor = cursor;
        this.gi = gi;
        this.objectives = objectives;
        pane.getChildren().addAll(gi.drawMenu(Integer.toString(board.get(cursor.getSquare()).getTerrain().getDef()), Integer.toString(board.get(cursor.getSquare()).getTerrain().getAvoid()), board.get(cursor.getSquare()).getTerrain().toString(), objectives));
        transitionIn();
    }
    @Override
    public void handleInput(KeyEvent e) throws IOException {
        switch (e.getCode()) {
            case UP -> moveUp();
            case DOWN -> moveDown();
            case LEFT -> moveLeft();
            case RIGHT -> moveRight();
            case ENTER -> next();
            case E -> System.out.println("byebye");
        }

    }
    @Override
    public void setContext(GameStateContext context) {
        this.context = context;
    }

    private void moveUp() throws IOException {
        cursor.moveUp();
        cursor.endMove();
        if (cursor.getXValue() > board.getWidth() / 2 && cursor.getYValue() == board.getHeight() / 2 - 1) {
            gi.animate((ImageView) pane.getChildren().get(3),(ImageView) pane.getChildren().get(4), 2, true);
        }
        gi.updateTI((ImageView) pane.getChildren().get(2),board.get(cursor.getSquare()).getTerrain().defToString(),board.get(cursor.getSquare()).getTerrain().avoToString(),board.get(cursor.getSquare()).getTerrain().toString());
    }
    private void moveDown() throws IOException {
        cursor.moveDown();
        cursor.endMove();
        if (cursor.getXValue() > board.getWidth() / 2 && cursor.getYValue() == board.getHeight() / 2 ) {
            gi.animate((ImageView) pane.getChildren().get(3),(ImageView) pane.getChildren().get(4), 2, false);
        }
        gi.updateTI((ImageView) pane.getChildren().get(2),board.get(cursor.getSquare()).getTerrain().defToString(),board.get(cursor.getSquare()).getTerrain().avoToString(),board.get(cursor.getSquare()).getTerrain().toString());
    }
    private void moveLeft() throws IOException {
        cursor.moveLeft();
        cursor.endMove();
        if (cursor.getXValue() == board.getWidth() / 2 && cursor.getYValue() < board.getHeight() / 2) {
            gi.animate(
                    new ImageView[]{
                            (ImageView) pane.getChildren().get(1),
                            (ImageView) pane.getChildren().get(2),
                            (ImageView) pane.getChildren().get(3),
                            (ImageView) pane.getChildren().get(4)},
                    new int[]{3, 2},
                    new boolean[]{false, false});
        }
        else if (cursor.getXValue() == board.getWidth() / 2){
            gi.animate((ImageView) pane.getChildren().get(1),(ImageView) pane.getChildren().get(2),3,false);
        }
        gi.updateTI((ImageView) pane.getChildren().get(2),board.get(cursor.getSquare()).getTerrain().defToString(),board.get(cursor.getSquare()).getTerrain().avoToString(),board.get(cursor.getSquare()).getTerrain().toString());
    }
    private void moveRight() throws IOException {
        cursor.moveRight();
        cursor.endMove();
        if (cursor.getXValue() == board.getWidth() / 2 + 1 && cursor.getYValue() < board.getHeight() / 2) {

            gi.animate(
                    new ImageView[]{
                            (ImageView) pane.getChildren().get(1),
                            (ImageView) pane.getChildren().get(2),
                            (ImageView) pane.getChildren().get(3),
                            (ImageView) pane.getChildren().get(4)},
                    new int[]{3, 2},
                    new boolean[]{true, true});
        }
        else if (cursor.getXValue() == board.getWidth() / 2 + 1)
            gi.animate((ImageView) pane.getChildren().get(1),(ImageView) pane.getChildren().get(2),3,true);
        gi.updateTI((ImageView) pane.getChildren().get(2),board.get(cursor.getSquare()).getTerrain().defToString(),board.get(cursor.getSquare()).getTerrain().avoToString(),board.get(cursor.getSquare()).getTerrain().toString());
    }
    public void transitionIn(){
        gi.transitionIn(
                new ImageView[]{
                        (ImageView) pane.getChildren().get(1),
                        (ImageView) pane.getChildren().get(2),
                        (ImageView) pane.getChildren().get(3),
                        (ImageView) pane.getChildren().get(4)},
                new int[]{3, 2},
                new boolean[]{false, false});
    }
    public void next(){
        if (cursor.getUnit() == null || !cursor.getUnit().getColor().equals("blue")) return;
        gi.transitionOut(
                new ImageView[]{
                        (ImageView) pane.getChildren().get(1),
                        (ImageView) pane.getChildren().get(2),
                        (ImageView) pane.getChildren().get(3),
                        (ImageView) pane.getChildren().get(4)},
                new int[]{3, 2},
                new boolean[]{true, true})
                .setOnFinished(e -> {
                    for (int i = 0; i<4; i++) {
                        pane.getChildren().removeLast();
                    }

                });
        cursor.getIMV().setTranslateY(cursor.getYValue() * tileSize - tileSize / 2 - tileSize / 16);
        context.setCurrentState(new UnitMoveState(cursor,gi,objectives));

    }
    public void prev(){}
}