package GameEngine;

import Units.Unit;
import javafx.scene.image.ImageView;

import java.util.ArrayList;

public class Cursor {
    Unit unit;
    private int x;
    private int y;
    private final Board board;
    private Unit selectedUnit;
    private ImageView IMV;
    private String color = "yellow";
    public Cursor(Unit leader, Board board) {
        unit = leader;

        x = leader.getXValue();
        y = leader.getYValue();
        this.board = board;
        unit.setMode(Unit.SELECT);
    }
    public ArrayList<Integer> moveCursor(String dir){
        if (unit != null && unit.getMode().equals(Unit.SELECT)){unit.setMode(Unit.STANDING);}
        switch (dir) {
            case "up":
                if (y > 0) {
                    y = y - 1;
                    unit = board.get(y, x).getUnit();
                }
                break;
            case "down":
                if (y + 1 < board.height) {
                    y = y + 1;
                    unit = board.get(y, x).getUnit();
                }
                break;
            case "left":
                if (x > 0) {
                    x = x - 1;
                    unit = board.get(y, x).getUnit();
                }
                break;
            case "right":
                if (x +1 < board.width) {
                    x = x + 1;
                    unit = board.get(y, x).getUnit();
                }
                break;
        }
        if (unit != null &&  unit.getColor().equals("blue") && selectedUnit == null){
            unit.setMode(Unit.SELECT);
        }
        if (selectedUnit != null && unit != selectedUnit && board.get(y,x).isReachable()) return selectedUnit.BFS_Algorithm(x+board.getWidth()*y);
        else return null;
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

    public Square getSquare(){
        return board.get(y,x);
    }

    public void setIMV(ImageView IMV) {
        this.IMV = IMV;
    }

    public int getXValue() {return x;}

    public int getYValue() {return y;}

    public ImageView getIMV() {return IMV;}
    public String getColor() {
        return color;
    }
    public void setColor(String colour){this.color = colour;}
    public void setTo(Unit unit, double tileWidth, double tileHeight){
        x = unit.getXValue();
        y = unit.getYValue();
        getIMV().setTranslateX((unit.getXValue()-1./2)*tileWidth);
        getIMV().setTranslateY((unit.getYValue()-1./2)*tileHeight);
    }
}
