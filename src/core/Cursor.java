package core;

import core.game_engine.Battle.BattleEngine;
import core.game_engine.helper_functions.Coord;
import core.game_engine.helper_functions.Dijkstra;
import units.Unit;
import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.HashMap;

import static core.game_engine.Battle.BattleEngine.tileSize;

public class Cursor {
    Unit unit;
    private byte x,y;
    private CursorTransition transition;
    private static final Board board = BattleEngine.getBoard();
    private Unit selectedUnit;
    private ImageView IMV;
    private String color = "yellow";
    public Cursor(Unit leader) {
        unit = leader;

        x = leader.getXValue();
        y = leader.getYValue();
        unit.setMode(Unit.SELECT);
    }

    public Cursor(Pane pane){
        unit = board.getLeader();
        ImageView cursorImageView = new ImageView(new Image("file:src/gui/CursorSprites/Cursor1.png", 192, 192, false, false));
        cursorImageView.setFitWidth(tileSize*2);
        cursorImageView.setFitHeight(tileSize*2);
        transition = new CursorTransition(cursorImageView, this);
        pane.getChildren().add(cursorImageView);
        if (unit == null){
            x = 0;
            y = 0;
            cursorImageView.setTranslateX(-tileSize / 2);
            cursorImageView.setTranslateY(-tileSize / 2);
        }
        else {
            x = board.getLeader().getXValue();
            y = board.getLeader().getYValue();
            board.getLeader().setMode(Unit.SELECT);
            cursorImageView.setTranslateX(board.getLeader().getXValue() * tileSize - tileSize / 2);
            cursorImageView.setTranslateY(board.getLeader().getYValue() * tileSize - tileSize / 2);
        }
        transition.play();
    }

    public void moveUp(){
        if (unit != null && unit.getMode().equals(Unit.SELECT)){
            unit.setMode(Unit.STANDING);
            unit.getSpriteAnimation().switchMode();
        }
        if (y > 0){
            y -=1;
            unit = board.get(y,x).getUnit();
        }

    }
    public void moveDown(){
        if (unit != null && unit.getMode().equals(Unit.SELECT)){
            unit.setMode(Unit.STANDING);
            unit.getSpriteAnimation().switchMode();
        }
        if (y + 1 < board.getHeight()){
            y +=1;
            unit = board.get(y,x).getUnit();
        }

    }
    public void moveLeft(){
        if (unit != null && unit.getMode().equals(Unit.SELECT)){
            unit.setMode(Unit.STANDING);
            unit.getSpriteAnimation().switchMode();
        }
        if (x > 0){
            x -=1;
            unit = board.get(y,x).getUnit();
        }

    }
    public void moveRight(){
        if (unit != null && unit.getMode().equals(Unit.SELECT)){
            unit.setMode(Unit.STANDING);
            unit.getSpriteAnimation().switchMode();
        }
        if (x + 1 < board.getWidth()){
            x +=1;
            unit = board.get(y,x).getUnit();
        }

    }
    public void endMove(){
        if (unit != null &&  unit.getColor().equals("blue") && selectedUnit == null){
            unit.setMode(Unit.SELECT);
            unit.getSpriteAnimation().switchMode();
        }
        IMV.setTranslateX(x * tileSize - tileSize / 2);
        if (IMV.getImage().getUrl().equals("file:src/gui/CursorSprites/Cursor4.png")) IMV.setImage((transition).getImage());
        if (getUnit() != null && getSelectedUnit() == null && getUnit().getColor().equals("blue")) {
            IMV.setImage(new Image("file:src/gui/CursorSprites/Cursor4.png", tileSize*4, tileSize*4, false, false));
            IMV.setTranslateY(y * tileSize - tileSize + tileSize / 8);
        } else IMV.setTranslateY(y * tileSize - tileSize / 2 - tileSize / 16);
    }
    public ArrayList<Coord> moveUnit(){
        IMV.setTranslateX(x * tileSize - (tileSize >> 1));
        IMV.setTranslateY(y * tileSize - tileSize / 2 - tileSize / 16);
        if (selectedUnit.getAvailableMoves().contains(new Coord(x,y))) {
            return Dijkstra.findPath(selectedUnit.getPosition(),new Coord(x,y),board,selectedUnit.getUnitType());
        }
        return null;
    }

    public Unit getUnit() {
        return board.get(y,x).getUnit();
    }

    public void selectUnit(Unit unit){
        if (unit==null) return;
        if (selectedUnit != null){selectedUnit.unselect("standing");}
        selectedUnit = unit.select();
    }

    public void unSelectUnit() {
        if (selectedUnit != null){
            selectedUnit.unselect(Unit.STANDING);
            selectedUnit = null;
        }
    }

    public Unit getSelectedUnit() {
        return selectedUnit;
    }

    public Coord getSquare(){
        return new Coord(x,y);
    }

    public void setIMV(ImageView IMV) {
        this.IMV = IMV;
    }

    public byte getXValue() {return x;}

    public byte getYValue() {return y;}

    public ImageView getIMV() {return IMV;}
    public String getColor() {
        return color;
    }
    public void setColor(String colour){this.color = colour;}
    public void setTo(Unit unit){
        x = unit.getXValue();
        y = unit.getYValue();
        getIMV().setTranslateX((unit.getXValue()-1./2)*tileSize);
        getIMV().setTranslateY((unit.getYValue()-1./2)*tileSize);
    }
    private static class CursorTransition extends Transition {
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
            put(1, new Image("file:src/gui/CursorSprites/Cursor1.png", 192, 192, false, false));
            put(2, new Image("file:src/gui/CursorSprites/Cursor2.png", 192, 192, false, false));
            put(3, new Image("file:src/gui/CursorSprites/Cursor3.png", 192, 192, false, false));
            put(11, new Image("file:src/gui/CursorSprites/Cursor11.png", 192, 192, false, false));
            put(12, new Image("file:src/gui/CursorSprites/Cursor12.png", 192, 192, false, false));
            put(13, new Image("file:src/gui/CursorSprites/Cursor13.png", 192, 192, false, false));
        }};

        public Image getImage() {
            return backup;
        }


        private int updateAnimation(double animFrame) {
            return (3 + (animFrame > 1 ? -1 : 0) + (animFrame > 3 ? -1 : 0) + (animFrame > 19 ? 1 : 0) + (animFrame > 21 ? 1 : 0));
        }

        @Override
        protected void interpolate(double k) {
            int index = (updateAnimation(36 * k) + (cursor.getColor().equals("red") ? 10 : 0));
            if ((index != lastIndex) && ((cursor.getUnit() != null && !cursor.getUnit().getColor().equals("blue")) || cursor.getUnit() == null || cursor.getSelectedUnit() != null)) {
                backup = CursorMap.get(index);
                imv.setImage(CursorMap.get(index));
            }
            lastIndex = index;
        }
    }
}
