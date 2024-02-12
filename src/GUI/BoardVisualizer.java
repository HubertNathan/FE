package GUI;
import GameEngine.*;
import Units.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BoardVisualizer extends JPanel{
    BufferedImage originalMap;
    BufferedImage map;
    Map<Integer,BufferedImage> ColorSquaresMap;
    protected int animFrame = 0;
    protected int tileSize = 48;
    protected int height;
    protected int width;
    protected JFrame mainFrame;
    protected Board board;
    protected KeyHandler KeyH;
    protected Selector selector;
    protected BufferedImage bufferImage;
    protected Graphics2D bufferImageGraphics;
    int xx;
    int yy;
    long lastTime;
    long currentTime;
    ComponentListener ComponentH;
    ArrayList<Integer> currentPath;
    Unit movingUnit;
    Unit tempUnit = null;
    OverWorldMenu overWorldMenu;
    public BoardVisualizer(ReadMapFile mapReader, Board board, Selector selector, GameState gs) throws IOException {
        currentPath = new ArrayList<>();
        currentTime = System.nanoTime();
        ComponentH = new ComponentHandler(gs);
        this.addComponentListener(ComponentH);
        lastTime = System.nanoTime();
        this.board = board;
        height = mapReader.getDimensions()[0];
        width = mapReader.getDimensions()[1];
        this.mainFrame = new JFrame(mapReader.getChapter());
        mainFrame.setSize(new Dimension(tileSize* width,tileSize*height+37));
        originalMap = new BufferedImage(tileSize* width,tileSize*height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D mapGraphics = originalMap.createGraphics();
        board.draw(mapGraphics);
        bufferImage = new BufferedImage(tileSize* width,tileSize*height, BufferedImage.TYPE_INT_ARGB);
        bufferImageGraphics = bufferImage.createGraphics();
        overWorldMenu = new OverWorldMenu(board);
        overWorldMenu.updateObjectivesIcon(mapReader.getObjectives());


        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setLocationRelativeTo(null);
        mainFrame.add(this);
        mainFrame.setVisible(true);
        KeyH = new KeyHandler();
        this.addKeyListener(KeyH);
        this.setFocusable(true);
        this.selector = selector;
        overWorldMenu.updateTerrainIcon(selector.getSquare().getTerrain().defToString(),selector.getSquare().getTerrain().avoToString(),selector.getSquare().getTerrain().toString());
    }
    public void paintComponent(Graphics g){
        super.paintComponents(g);
        long fps = (long) 1000000000.0/(lastTime-System.nanoTime());
        lastTime = System.nanoTime();
        Graphics2D g2 = (Graphics2D) g;
        try {
            update();
        } catch (InterruptedException | IOException e) {
            throw new RuntimeException(e);
        }
        g2.drawImage(bufferImage,0,0,null);
        g2.dispose();
        bufferImageGraphics.dispose();
    }
    public void resizeImages(int newH, int newW) throws InterruptedException, IOException {
        if (newH == 0 || newW == 0){return;}
        Image tmp = originalMap.getScaledInstance(3*newW*width, 3*newH*height, Image.SCALE_SMOOTH);
        map = new BufferedImage(3*newH*height,3*newW*width,BufferedImage.TYPE_INT_ARGB);
        Graphics g = map.getGraphics();
        g.drawImage(tmp,0,0,null);
        bufferImage = new BufferedImage(3*newH*height,3*newW*width,BufferedImage.TYPE_INT_ARGB);
        bufferImageGraphics = (Graphics2D) bufferImage.getGraphics();
        bufferImageGraphics.drawImage(map,0,0,null);
        for (Unit unit : board.getUnits()) {
            if (unit != null) {
                //unit.resizeSprites(newW, newH);
            }
        }
        overWorldMenu.resizeImage(newW,newH);
        animate();
    }
    public void animate() throws InterruptedException, IOException {
        bufferImageGraphics = bufferImage.createGraphics();
        xx = this.getWidth()/this.getWidthValue();
        yy = this.getHeight()/this.getHeightValue();

        animFrame= (animFrame+1)%576;
        clearBoard(xx,yy);
        board.drawColouredSquares(bufferImageGraphics,xx,yy,(animFrame/2)%32,true);
        if (!currentPath.isEmpty() && movingUnit != null){
            int move = currentPath.removeLast();
            if (board.get(move/board.getWidth(),move%board.getWidth()).getUnit() == null) {
                updateSquaresAround(movingUnit);
                movingUnit.makeMove(move/board.getWidth(),move%board.getWidth());

            }
        }
        else if (currentPath.isEmpty()) movingUnit = null;
        for (Unit unit : board.getUnits()) {
            if (unit != null){
                //unit.draw(bufferImageGraphics, unit.getYValue(), unit.getXValue(), xx, yy, animFrame%36);
            }
        }
        if (tempUnit != null){
            board.setUnit(tempUnit.copy(),tempUnit.getYValue(),tempUnit.getXValue());
            tempUnit = null;
        }
        selector.draw(bufferImageGraphics,xx,yy,animFrame%36);
        overWorldMenu.draw(bufferImageGraphics,board.getWidth(),board.getHeight(),xx,yy);
        repaint();
    }
    public void clearBoard(int scaleX,int scaleY) {
        for (Unit unit : board.getUnits()) {
            if (unit != null) {
                board.draw(bufferImageGraphics, unit.getYValue(), unit.getXValue(), scaleX, scaleY);
                board.draw(bufferImageGraphics, (unit.getYValue() - 1+board.getHeight()) % board.getHeight(), unit.getXValue(), scaleX, scaleY);
            }
        }
        updateSquaresAround(selector);
        overWorldMenu.draw(bufferImageGraphics, board.getWidth(), board.getHeight(), xx,yy);
    }
    public void clearBoard(){
        xx = this.getWidth()/this.getWidthValue();
        yy = this.getHeight()/this.getHeightValue();
        clearBoard(xx,yy);

    }
    public void updateSquaresAround(Selector selector){
        int i = selector.getRow();
        int j = selector.getCol();
        xx = this.getWidth()/this.getWidthValue();
        yy = this.getHeight()/this.getHeightValue();
        updateSquaresAround(i,j,xx,yy);
    }
    public void updateSquaresAround(Unit unit){
        int i  = unit.getYValue();
        int j = unit.getXValue();
        xx = this.getWidth()/this.getWidthValue();
        yy = this.getHeight()/this.getHeightValue();
        updateSquaresAround(i,j,xx,yy);
    }
    public void updateSquaresAround(int i, int j, int scaleX, int scaleY){
        board.draw(bufferImageGraphics, i, j, scaleX, scaleY);
        board.draw(bufferImageGraphics, (i + board.getHeight() - 1) % board.getHeight(), j, scaleX, scaleY);
        board.draw(bufferImageGraphics, (i + board.getHeight() - 1) % board.getHeight(), (j + board.getWidth() - 1) % board.getWidth(), scaleX, scaleY);
        board.draw(bufferImageGraphics, i, (j + board.getWidth() - 1) % board.getWidth(), scaleX, scaleY);
        board.draw(bufferImageGraphics, (i + 1) % board.getHeight(), (j + board.getWidth() - 1) % board.getWidth(), scaleX, scaleY);
        board.draw(bufferImageGraphics, (i + 1) % board.getHeight(), j, scaleX, scaleY);
        board.draw(bufferImageGraphics, (i + 1) % board.getHeight(), (j + 1) % board.getWidth(), scaleX, scaleY);
        board.draw(bufferImageGraphics, i, (j + 1) % board.getWidth(), scaleX, scaleY);
        board.draw(bufferImageGraphics, (i + board.getHeight() - 1) % board.getHeight(), (j + 1) % board.getWidth(), scaleX, scaleY);
    }

    public void update() throws InterruptedException, IOException {
        long bufferTime = (long) 1000000000/(20);
        if (KeyH.isArrowPressed() && currentTime + bufferTime < System.nanoTime()) {
            clearBoard();
            currentTime = moveSelector();
            clearBoard();
            for (Unit unit : board.getUnits()) {
                if (unit != null) {
                    //unit.draw(bufferImageGraphics, unit.getYValue(), unit.getXValue(), xx, yy, animFrame%36);
                }
            }
            if (selector.getSelectedUnit() != null && selector.getSelectedUnit().getAvailableMoves() != null) {
                if (selector.getSelectedUnit().getAvailableMoves().contains(selector.getSquare())) currentPath = selector.getSelectedUnit().BFS_Algorithm(selector.getRow()*board.getWidth()+selector.getCol());

            }
            selector.draw(bufferImageGraphics,xx,yy,animFrame%36);
        } else if (KeyH.isEnterPressed()){
            enter();
        } else if (KeyH.isEscapePressed() && selector.getSelectedUnit() != null) {
            esc();
        }
    }
    public long moveSelector() throws IOException {
        if (KeyH.isUpPressed()){
            selector.moveCursor("up");
        } else if (KeyH.isDownPressed()) {
            selector.moveCursor("down");
        } else if (KeyH.isLeftPressed()) {
            selector.moveCursor("left");
        } else if (KeyH.isRightPressed()) {
            selector.moveCursor("right");
        }
        overWorldMenu.updateTerrainIcon(selector.getSquare().getTerrain().defToString(),selector.getSquare().getTerrain().avoToString(),selector.getSquare().getTerrain().toString());
        return System.nanoTime();
    }
    private void enter() throws InterruptedException {
        Unit selectedUnit = selector.getUnit();
        if (selectedUnit != null) {
            selector.selectUnit(selectedUnit);
        } else if (selector.getSelectedUnit() != null && board.get(selector.getRow(),selector.getCol()).isReachable()) {
            resetBoard();

            // Only redrawing the one unit would fairly be better
            for (Unit unit : board.getUnits()) {
                //if (unit != null) unit.draw(bufferImageGraphics, unit.getYValue(), unit.getXValue(), xx, yy,animFrame%36);
            }
            selector.draw(bufferImageGraphics,xx,yy,animFrame%36);
            movingUnit = selector.getSelectedUnit();
            selector.getSelectedUnit().setMode("standing");
            selector.unSelectUnit();

        }
    }

    private void resetBoard() {
        for (int i = 0; i < board.getHeight(); i++) {
            for (int j = 0; j < board.getWidth(); j++) {
                if (board.get(i, j).isReachable()) {
                    board.get(i, j).reach(false);
                    board.draw(bufferImageGraphics, i, j, xx, yy);
                }
            }
        }
    }

    public void esc(){
        resetBoard();
        if (selector.getSelectedUnit().equals(selector.getUnit())){selector.getSelectedUnit().unselect("select");}
        else selector.getSelectedUnit().unselect("standing");
        for (Unit unit : board.getUnits()) {
            //if (unit != null) unit.draw(bufferImageGraphics, unit.getYValue(), unit.getXValue(), xx, yy,animFrame%36);
        }
        selector.draw(bufferImageGraphics,xx,yy,animFrame%36);
    }
    public int getWidthValue() {
        return width;
    }
    public int getHeightValue() {
        return height;
    }

}