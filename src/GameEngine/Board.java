package GameEngine;

import GUI.ReadMapFile;
import Units.Unit;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class Board{
    int height;
    int width;
    private final Square[][] board;
    private final ArrayList<Unit> units;
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
                board[i][j] = new Square(mapReader.getTileSetData().getSubimage(16*(Integer.parseInt(terrain)%32), (Integer.parseInt(terrain)/32)*16,16,16),i,j);
                board[i][j].setTerrainType(terrainType);
            }
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
        final Unit[] leader = {null};
        units.forEach(unit->{
            if (unit != null) {
                if (unit.isLeader()) {
                    leader[0] = unit;
                }
            }
        });
        return leader[0];
    }
    public ArrayList<Unit> getUnits(){
        return units;
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

    public ArrayList<Square> squareAsList(){
        return new ArrayList<>(){{
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    add(board[i][j]);
                }
            }
        }};
    }
}