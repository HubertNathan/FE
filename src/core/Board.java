package core;

import gui.ReadMapFile;
import gui.SpriteSheet;
import units.Unit;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;
import java.util.Collections;

import static core.game_engine.Battle.BattleEngine.tileSize;

public class Board{
    private int height;
    private int width;
    private Square[][] board;
    private ArrayList<Unit> units;

    public Board() {}

    public void init(ReadMapFile mapReader){
        int[] dimensions = mapReader.getDimensions();
        height = dimensions[0];
        width = dimensions[1];
        board = new Square[height][width];
        units = new ArrayList<>(Collections.nCopies(board.length * board[0].length, null));
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                String terrain = mapReader.getMap().get(width*i+j);
                int terrainType = Integer.parseInt(mapReader.getTerrains().get(width*i+j));
                board[i][j] = new Square(SpriteSheet.getTile((Integer.parseInt(terrain)%32), (Integer.parseInt(terrain)/32)),i,j);
                board[i][j].setTerrainType(terrainType);
            }
        }
    }
    public Square get(int i, int j){
        return board[i][j];
    }
    public Square get(Coord pos) {
        return this.get(pos.getLast(),pos.getFirst());
    }

    public int getHeight() {
        return height;
    }
    public int getWidth() {
        return width;
    }
    public void setUnit(Unit unit,short i, short j){
        units.add(j+board.length * i,unit);
        board[i][j].setUnit(unit);
        unit.setX(j);
        unit.setY(i);
    }
    public void setUnit(Unit unit,short i, short j, boolean isLeader){
        unit.setLeader(isLeader);
        setUnit(unit,i,j);
    }
    public void removeUnit(Unit unit, short i, short j){
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
        for (short i = 0; i < height; i++) {
            for (short j = 0; j < width; j++) {
                graphicsContext.drawImage((board[i][j].getOriginaltexture()),j*tileSize,i*tileSize);
            }
        }
    return canvas;
    }

    public ArrayList<Square> squareAsList(){
        return new ArrayList<>(){{
            for (short i = 0; i < height; i++) {
                for (short j = 0; j < width; j++) {
                    add(board[i][j]);
                }
            }
        }};
    }
    public ArrayList<Square> getSquaresInReach(){
        return new ArrayList<>(){{
            for (short i = 0; i < height; i++) {
                for (short j = 0; j < width; j++) {
                    if (board[i][j].inRange() || board[i][j].isReachable()) add(board[i][j]);
                }
            }
        }};
    }
        public void resetReachableSquares() {
        for (short i = 0; i < height; i++) {
            for (short j = 0; j < width; j++) {
                board[i][j].range(false);
                board[i][j].reach(false);
            }
        }
    }
}