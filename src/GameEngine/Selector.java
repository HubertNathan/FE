package GameEngine;

import Units.Unit;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Selector {
    Unit unit;
    private int x;
    private int y;
    private Board board;
    private Unit selectedUnit;
    HashMap<Integer, BufferedImage> OriginalCursorMap;
    HashMap<Integer,BufferedImage> CursorMap;
    public Selector(Unit leader, Board board) throws IOException {
        unit = leader;

        x = leader.getXValue();
        y = leader.getYValue();
        this.board = board;
        unit.setMode("select");
        OriginalCursorMap = new HashMap<>();
        CursorMap = new HashMap<>();
        for (int i = 1; i < 5; i++) {
            OriginalCursorMap.put(i, ImageIO.read(new File("src/GUI/CursorSprites/Cursor" + i + ".png")));
            CursorMap.put(i, ImageIO.read(new File("src/GUI/CursorSprites/Cursor" + i + ".png")));
        }
        resizeImages(48,48);
    }
    public void moveCursor(String dir){
        if (unit != null){unit.setMode("standing");}
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
            unit.setMode("select");
        }
    }
    private int updateAnimation(int animFrame) {
        return 3 + (animFrame > 1 ? -1 : 0) + (animFrame > 3 ? -1 : 0) + (animFrame > 19 ? 1 : 0) + (animFrame > 21 ? 1 : 0);
    }
    public void resizeImages(int newH, int newW) {
        Image tmp;
        for (int i = 1; i < OriginalCursorMap.size()+1; i++) {
            tmp = OriginalCursorMap.get(i).getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
            BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = dimg.createGraphics();
            g2d.drawImage(tmp, 0, 0, null);
            g2d.dispose();
            CursorMap.replace(i,dimg);

        }

    }
    public void draw(Graphics2D g2, int scaleX, int scaleY,int animFrame) {
        BufferedImage texture;
        if ((int) scaleX <= 0 || (int) scaleY <= 0) {return;}
        if (unit != null){texture = CursorMap.get(4);}
        else {
            texture = CursorMap.get(updateAnimation(animFrame));
        }
        g2.drawImage(texture, scaleX * x - scaleX / 2, scaleY * y - scaleY / 2, scaleX * 2, scaleY * 2, null);
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

    public Square getSquare(){
        return board.get(y,x);
    }
}
