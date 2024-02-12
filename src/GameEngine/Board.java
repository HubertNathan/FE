package GameEngine;

import GUI.ReadMapFile;
import GUI.ResizableImage;
import Units.Unit;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

import javax.imageio.ImageIO;

import java.awt.*;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class Board{
    int height = 0;
    int width = 0;
    private Square[][] board;
    private ArrayList<Unit> units;
    private HashMap<Integer, BufferedImage> ColorSquareMap;
    private HashMap<Integer, BufferedImage> OriginalSquareMap;
    public Board(ReadMapFile mapReader) throws IOException {
        int[] dimensions = mapReader.getDimensions();
        height = dimensions[0];
        width = dimensions[1];
        board = new Square[height][width];
        units = new ArrayList<>(Collections.nCopies(board.length * board[0].length, null));
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                String terrain = mapReader.getMap().get(width*i+j);
                int terrainType = Integer.parseInt(mapReader.getTerrains().get(width*i+j));
                board[i][j] = new Square(mapReader.getTileSetData().getSubimage(16*(Integer.parseInt(terrain)%32), (Integer.parseInt(terrain)/32)*16,16,16));
                board[i][j].setTerrainType(terrainType);
            }
        }
        OriginalSquareMap = new HashMap<>();
        ColorSquareMap = new HashMap<>();
        for (int i = 1; i < 17; i++) {
            OriginalSquareMap.put(i, ImageIO.read(new File("src/GUI/ColorSquares/"+i+".png")));
            ColorSquareMap.put(i, ImageIO.read(new File("src/GUI/ColorSquares/"+i+".png")));
        }
    }
    public Square get(int i, int j){
        return board[i][j];
    }

    public int getHeight() {
        return height;
    }
    public int getWidth() {
        return width;
    }
    public void setUnit(Unit unit,int i, int j){
        units.add(j+board.length * i,unit);
        board[i][j].setUnit(unit);
        unit.setBoard(this);
        unit.setX(j);
        unit.setY(i);
    }
    public void setUnit(Unit unit,int i, int j, boolean isLeader){
        unit.setLeader(isLeader);
        setUnit(unit,i,j);
    }
    public void removeUnit(Unit unit, int i, int j){
        units.remove(unit);
        board[i][j].setUnit(null);
    }
    public Unit getLeader(){
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                Unit unit = board[i][j].getUnit();
                if (unit != null){
                    if (unit.isLeader()) {
                        return unit;
                    }
                }
            }
        }
        return null;
    }
    public ArrayList<Unit> getUnits(){
        return units;
    }
    public void draw(Graphics2D g2, int i, int j, int scaleX, int scaleY, BufferedImage texture) {
        g2.drawImage(texture, (int) scaleX * j, (int) scaleY * i,(int) scaleX,(int) scaleY, null);
    }
    public void draw(Graphics2D g2, int i, int j,int scaleX,int scaleY) {
        draw(g2,i,j,scaleX,scaleY,!board[i][j].isReachable());
    }
    public void draw(Graphics2D g2){
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                //g2.drawImage(board[i][j].getOriginalTexture(),16 * j,16 * i,null);
            }
        }
    }
    public Canvas drawFX(Canvas canvas) {
        GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                graphicsContext.drawImage((board[i][j].getOriginalTexture()),j*16,i*16);
            }
        }
    return canvas;
    }
    public void draw(Graphics2D g2, int i, int j,int scaleX,int scaleY,boolean forceRedraw) {
        if (forceRedraw) {
            //BufferedImage texture = board[i][j].getOriginalTexture();
            //draw(g2, i, j, scaleX, scaleY, texture);
        }
    }
    public void drawColouredSquares(Graphics2D g2, int scaleX, int scaleY, int animFrame,boolean drawBlueSquares){
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                if (board[i][j].isReachable()) {
                    draw(g2, i, j, scaleX, scaleY,true);
                    g2.drawImage(ColorSquareMap.get(1+animFrame%16), scaleX*j,scaleY*i,null);
                }
            }
        }
    }
    public void resizeColouredSquares(int newW, int newH){
        for (int i:OriginalSquareMap.keySet()){
            if(newW<=0 || newH<=0){return;}
            Image tmp = OriginalSquareMap.get(i).getScaledInstance(newW-newW/16, newH-newH/16, Image.SCALE_SMOOTH);
            BufferedImage resizedSprite= new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = resizedSprite.createGraphics();
            g2d.drawImage(tmp, newW/16, newH/16, null);
            g2d.dispose();
            ColorSquareMap.replace(i,resizedSprite);
        }
    }
}