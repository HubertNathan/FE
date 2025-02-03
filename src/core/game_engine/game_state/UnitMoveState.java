package core.game_engine.game_state;

import core.game_engine.helper_functions.Coord;
import gui.Animations.ColorTilesAnimation;
import core.game_engine.Battle.BattleEngine;
import core.Cursor;
import javafx.animation.Transition;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.util.ArrayList;

import static core.game_engine.Battle.BattleEngine.*;

public class UnitMoveState implements GameState {
    private final Cursor cursor;
    private GameStateContext context;
    private ColorTilesAnimation tilesAnimation ;
    private ArrayList<Coord> path = null;
    private final String objectives;
    private boolean moving = false;

    UnitMoveState(Cursor cursor, String objectives){
        this.cursor = cursor;
        this.objectives = objectives;
        transitionIn();
    };
    @Override
    public void handleInput(KeyEvent e) throws IOException {
        if (moving) return;
        switch (e.getCode()) {
            case UP -> moveUp();
            case DOWN -> moveDown();
            case LEFT -> moveLeft();
            case RIGHT -> moveRight();
            case ESCAPE -> prev();
            case ENTER -> next();
        }
    }

    @Override
    public void setContext(GameStateContext context) {
        this.context = context;
    }

    @Override
    public void transitionIn() {
        cursor.selectUnit(cursor.getUnit());
        tilesAnimation = new ColorTilesAnimation();
        getAnimationPane().getChildren().add(tilesAnimation.getAnimationCanvas());
        tilesAnimation.start();

    }

    @Override
    public void next() {
        if (cursor.getSelectedUnit().getAvailableMoves().contains(cursor.getSquare())&&(cursor.getXValue() != cursor.getSelectedUnit().getXValue() || cursor.getYValue() != cursor.getSelectedUnit().getYValue())) {
            moving = true;
            getArrowPane().getChildren().clear();
            getAnimationPane().getChildren().clear();
            Transition move = cursor.getSelectedUnit().makeMove(path);
            tilesAnimation.stop();
            cursor.getIMV().setOpacity(1);
            BattleEngine.getBoard().resetReachableSquares();
            move.setOnFinished(e -> {
                try {
                    context.setCurrentState(new ActionSelectionState(cursor,objectives));
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            });
            move.play();

        }
    }

    @Override
    public void prev() throws IOException {
        if (tilesAnimation != null){
            tilesAnimation.stop();
            getArrowPane().getChildren().clear();
            getAnimationPane().getChildren().clear();
        }
        cursor.setTo(cursor.getSelectedUnit());
        cursor.unSelectUnit();
        context.setCurrentState(new UnitSelectionState(cursor,objectives));
        cursor.getUnit().getSpriteAnimation().switchMode();
    }
    private void moveUp() {
        cursor.moveUp();
        BattleEngine.getArrowPane().getChildren().clear();
        path = cursor.moveUnit();
        if (BattleEngine.getBoard().get(cursor.getSquare()).isReachable() && !(cursor.getSquare().getFirst() == cursor.getSelectedUnit().getPosition().getFirst() && cursor.getSquare().getLast() == cursor.getSelectedUnit().getPosition().getLast())) {
            displayPath(path);
        }
    }
    private void moveDown() {
        cursor.moveDown();
        BattleEngine.getArrowPane().getChildren().clear();
        path = cursor.moveUnit();
        if (BattleEngine.getBoard().get(cursor.getSquare()).isReachable() && !(cursor.getSquare().getFirst() == cursor.getSelectedUnit().getPosition().getFirst() && cursor.getSquare().getLast() == cursor.getSelectedUnit().getPosition().getLast())) {
            displayPath(path);
        }

    }
    private void moveLeft() {
        cursor.moveLeft();
        BattleEngine.getArrowPane().getChildren().clear();
        path = cursor.moveUnit();
        if (BattleEngine.getBoard().get(cursor.getSquare()).isReachable() && !(cursor.getSquare().getFirst() == cursor.getSelectedUnit().getPosition().getFirst() && cursor.getSquare().getLast() == cursor.getSelectedUnit().getPosition().getLast())) {
            displayPath(path);
        }
    }
    private void moveRight() {
        cursor.moveRight();
        BattleEngine.getArrowPane().getChildren().clear();
            path = cursor.moveUnit();
        if (BattleEngine.getBoard().get(cursor.getSquare()).isReachable() && !(cursor.getSquare().getFirst() == cursor.getSelectedUnit().getPosition().getFirst() && cursor.getSquare().getLast() == cursor.getSelectedUnit().getPosition().getLast())) {
            displayPath(path);
        }
    }
    private int determineIntermediatePathSegment(Coord prev, Coord cur, Coord next){
        int dx1 = cur.getFirst() - prev.getFirst();
        int dx2 = next.getFirst() - cur.getFirst();
        int dy1 = cur.getLast() - prev.getLast();
        int dy2 = next.getLast() - cur.getLast();

        if ((dx1 == 1 && dy2 == -1) || (dx2 == -1 && dy1 == 1)) return 4;
        if ((dx1 == 1 && dy2 == 1) || (dx2 == -1 && dy1 == -1)) return 5;
        if ((dx2 == 1 && dy1 == -1) || (dx1 == -1 && dy2 == 1)) return 6;
        if ((dx1 == -1 && dy2 == -1) || (dx2 == 1 && dy1 == 1)) return 7; // Left to Down

        if (dx1 == 0 && dx2 == 0) return 12; // Vertical
        return 13; // Horizontal

    }
    private int determineFirstPathSegment(Coord cur, Coord next){
        int dx = next.getFirst() - cur.getFirst();
        int dy = next.getLast() - cur.getLast();

        if (dy == -1) return 0;
        if (dx == -1) return 1;
        if (dy == 1) return 2;
        return 3;
    }
    private int determineLastPathSegment(Coord prev, Coord cur){
        int dx = cur.getFirst() - prev.getFirst();
        int dy = cur.getLast() - prev.getLast();

        if (dy == -1) return 8;
        if (dx == -1) return 9;
        if (dy == 1) return 10;
        return 11;
    }
    private void displayPath(ArrayList<Coord> path){
        Pane arrowPane = BattleEngine.getArrowPane();
        Image arrows = new Image("file:/home/nathan-hubert/Desktop/Dev/Fire Emblem/Resources/MenuSprites/Arrows.png",64*3,64*3,false,false);
        int firstDir = determineFirstPathSegment(path.getFirst(),path.get(1));
        ArrayList<Integer> dirs = new ArrayList<Integer>();
        dirs.add(firstDir);
        arrowPane.getChildren().add(new ImageView(arrows){{
            setFitWidth(tileSize);
            setFitHeight(tileSize);
            setTranslateX(path.getFirst().getFirst()*tileSize);
            setTranslateY(path.getFirst().getLast()*tileSize);
            setViewport(new Rectangle2D((firstDir%4)*48,0,48,48));
        }});
        for(int i = 1; i < path.size()-1;i++){
            int dir = determineIntermediatePathSegment(path.get(i-1),path.get(i),path.get(i+1));
            dirs.add(dir);
            final int j = i;
            arrowPane.getChildren().add(new ImageView(arrows){{
                setFitWidth(tileSize);
                setFitHeight(tileSize);
                setTranslateX(path.get(j).getFirst()*tileSize);
                setTranslateY(path.get(j).getLast()*tileSize);
                setViewport(new Rectangle2D((dir%4)*48,(dir/4)*48,48,48));
            }});
        }
        int lastDir = determineLastPathSegment(path.get(path.size()-2),path.getLast());
        dirs.add(lastDir);
        arrowPane.getChildren().add(new ImageView(arrows){{
            setFitWidth(tileSize);
            setFitHeight(tileSize);
            setTranslateX(path.getLast().getFirst()*tileSize);
            setTranslateY(path.getLast().getLast()*tileSize);
            setViewport(new Rectangle2D((lastDir%4)*48,(lastDir/4)*48,48,48));
        }});
    }
}
