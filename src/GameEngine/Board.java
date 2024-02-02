package GameEngine;

import GUI.ReadMapFile;
import Units.Unit;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;

public class Board extends GameEngine{
    int height = 0;
    int width = 0;
    private Square[][] board;
    private KeyHandler KeyH;
    public Board(ReadMapFile mapReader) throws FileNotFoundException {
        int[] dimensions = mapReader.getDimensions();
        height = dimensions[0];
        width = dimensions[1];
        board = new Square[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                String terrain = mapReader.getMap().get(width*i+j);
                int terrainType = Integer.parseInt(mapReader.getTerrains().get(width*i+j));
                board[i][j] = new Square(mapReader.getTileSetData().getSubimage(16*(Integer.parseInt(terrain)%32), (Integer.parseInt(terrain)/32)*16,16,16));
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
        board[i][j].setUnit(unit);
        unit.setBoard(this);
        System.out.println(j);
        unit.setX(j);
        unit.setY(i);
    }
    public void setUnit(Unit unit,int i, int j, boolean isLeader){
        unit.setLeader(isLeader);
        setUnit(unit,i,j);
    }

    public static void main(String[] args) throws FileNotFoundException {
        int[][] types = {{0,1},{0,0}};
        Board b = new Board(new ReadMapFile());

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
    public void draw(Graphics2D g2, int i, int j, float scaleX, float scaleY, BufferedImage texture) {
        if ((int) scaleX != 0 && (int) scaleY != 0) {
            texture = resizeImage((int) (scaleY), (int) (scaleX), texture);
        }
        g2.drawImage(texture, (int) scaleX * j, (int) scaleY * i+(int)scaleY/16,(int) scaleX - (int) scaleX/16,(int) scaleY - (int) scaleY/16, null);
    }
    public BufferedImage resizeImage(int newH, int newW, BufferedImage texture) {
        Image tmp = texture.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
        BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = dimg.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();

        return dimg;
    }
    public void draw(Graphics2D g2, int i, int j, float scaleX, float scaleY) {
        BufferedImage texture = board[i][j].getOriginalTexture();
        if ((int) scaleX != 0 && (int) scaleY != 0) {
            texture = resizeImage((int) (scaleY), (int) (scaleX), texture);
        }
        g2.drawImage(texture, (int) scaleX * j, (int) scaleY * i, null);
    }
    public void draw(Graphics2D g2){
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                g2.drawImage(board[i][j].getOriginalTexture(),16 * j,16 * i,null);
            }

        }
    }
}