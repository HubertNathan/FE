package GameEngine;

import Units.Unit;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Selector {
    Unit unit;
    private int x;
    private int y;
    private Board board;
    private Unit selectedUnit;
    public Selector(Unit leader, Board board){
        unit = leader;

        x = leader.getXValue();
        y = leader.getYValue();
        this.board = board;
        unit.setCursored(true);
    }
    public void moveCursor(String dir){
        if (unit != null){unit.setCursored(false);}
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
        if (unit != null){
            unit.setCursored(true);
        }
    }
    public BufferedImage resizeImage(int newH, int newW, BufferedImage texture) {
        Image tmp = texture.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
        BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = dimg.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();

        return dimg;
    }
    public void draw(Graphics2D g2, float scaleX, float scaleY, BufferedImage texture) {
        if ((int) scaleX != 0 && (int) scaleY != 0) {
            texture = resizeImage((int) (scaleY * 2), (int) (scaleX) * 2, texture);
        }
        g2.drawImage(texture, (int) scaleX * x - (int) scaleX / 2, (int) scaleY * y - (int) scaleY / 2, (int) scaleX * 2, (int) scaleY * 2, null);
    }

    public Unit getUnit() {
        return unit;
    }
    public void selectUnit(Unit unit){
        if (selectedUnit != null){selectedUnit.unselect();}
        selectedUnit = unit.select();
    }

    public void unSelectUnit() {
        if (selectedUnit != null){
            selectedUnit = null;
        }
    }

    public Unit getSelectedUnit() {
        return selectedUnit;
    }

    public int getRow() {
        return y;
    }
    public int getCol(){
        return x;
    }
}
