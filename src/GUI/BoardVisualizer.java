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
    ArrayList<Square> TilesToDraw = new ArrayList<>();
    public BoardVisualizer(ReadMapFile mapReader, Board board, Selector selector, GameState gs) throws IOException {
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

        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setLocationRelativeTo(null);
        mainFrame.add(this);
        mainFrame.setVisible(true);
        KeyH = new KeyHandler();
        this.addKeyListener(KeyH);
        this.setFocusable(true);
        this.selector = selector;
    }
    public void paintComponent(Graphics g){
        super.paintComponents(g);
        long fps = (long) 1000000000.0/(lastTime-System.nanoTime());
        lastTime = System.nanoTime();
        System.out.println("fps:"+fps);
        Graphics2D g2 = (Graphics2D) g;
        update();
        g2.drawImage(bufferImage,0,0,null);
        g2.dispose();
    }
    public void resizeImages(int newH, int newW) {
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
                unit.resizeSprites(newW, newH);
            }
        }
        animate();
    }
    public void animate(){

        xx = this.getWidth()/this.getWidthValue();
        yy = this.getHeight()/this.getHeightValue();
        //System.out.println(xx+" "+yy);
        animFrame= (animFrame+1)%36;
        clearBoard(xx,yy);
        for (Unit unit : board.getUnits()) {
            if (unit != null){
                unit.draw(bufferImageGraphics, unit.getYValue(), unit.getXValue(), xx, yy, animFrame);
            }
        }
        selector.draw(bufferImageGraphics,xx,yy,animFrame);
        repaint();
    }
    public void clearBoard(int scaleX,int scaleY) {
        for (Unit unit : board.getUnits()) {
            if (unit != null) {
                board.draw(bufferImageGraphics, unit.getYValue(), unit.getXValue(), scaleX, scaleY);
                board.draw(bufferImageGraphics, (unit.getYValue() - 1+board.getHeight()) % board.getHeight(), unit.getXValue(), scaleX, scaleY);
            }
        }
        board.draw(bufferImageGraphics, selector.getRow(), selector.getCol(), scaleX, scaleY);
        board.draw(bufferImageGraphics, (selector.getRow() + board.getHeight() - 1) % board.getHeight(), selector.getCol(), scaleX, scaleY);
        board.draw(bufferImageGraphics, (selector.getRow() + board.getHeight() - 1) % board.getHeight(), (selector.getCol() + board.getWidth() - 1) % board.getWidth(), scaleX, scaleY);
        board.draw(bufferImageGraphics, selector.getRow(), (selector.getCol() + board.getWidth() - 1) % board.getWidth(), scaleX, scaleY);
        board.draw(bufferImageGraphics, (selector.getRow() + 1) % board.getHeight(), (selector.getCol() + board.getWidth() - 1) % board.getWidth(), scaleX, scaleY);
        board.draw(bufferImageGraphics, (selector.getRow() + 1) % board.getHeight(), selector.getCol(), scaleX, scaleY);
        board.draw(bufferImageGraphics, (selector.getRow() + 1) % board.getHeight(), (selector.getCol() + 1) % board.getWidth(), scaleX, scaleY);
        board.draw(bufferImageGraphics, selector.getRow(), (selector.getCol() + 1) % board.getWidth(), scaleX, scaleY);
        board.draw(bufferImageGraphics, (selector.getRow() + board.getHeight() - 1) % board.getHeight(), (selector.getCol() + 1) % board.getWidth(), scaleX, scaleY);
    }
    public void clearBoard(){
        xx = this.getWidth()/this.getWidthValue();
        yy = this.getHeight()/this.getHeightValue();
        clearBoard(xx,yy);

    }

    private void enter() {
        Unit selectedUnit = selector.getUnit();
        if (selectedUnit != null) {
            selector.selectUnit(selectedUnit);
        } else if (selector.getSelectedUnit() != null && board.get(selector.getRow(),selector.getCol()).isReachable()) {
            clearBoard();
            selector.getSelectedUnit().makeMove(selector.getRow(),selector.getCol());

            // Only redrawing the one unit would fairly be better
            for (Unit unit : board.getUnits()) {
                if (unit != null) unit.draw(bufferImageGraphics, unit.getYValue(), unit.getXValue(), xx, yy,animFrame);
            }
            selector.draw(bufferImageGraphics,xx,yy,animFrame);
            selector.unSelectUnit();

        }
    }
    public void update(){
        long bufferTime = (long) 1000000000/(20);
        if (KeyH.isArrowPressed() && currentTime + bufferTime < System.nanoTime()) {
            clearBoard();
            currentTime = moveSelector();
            clearBoard();
            for (Unit unit : board.getUnits()) {
                if (unit != null) {
                    unit.draw(bufferImageGraphics, unit.getYValue(), unit.getXValue(), xx, yy, animFrame);
                }
            }
            selector.draw(bufferImageGraphics,xx,yy,animFrame);
        } else if (KeyH.isEnterPressed()){
            enter();
        } else if (KeyH.isEscapePressed() && selector.getSelectedUnit() != null) {
            selector.getSelectedUnit().unselect();
        }
    }
    public int getWidthValue() {
        return width;
    }
    public int getHeightValue() {
        return height;
    }
    public long moveSelector(){
        if (KeyH.isUpPressed()){
            selector.moveCursor("up");
        } else if (KeyH.isDownPressed()) {
            selector.moveCursor("down");
        } else if (KeyH.isLeftPressed()) {
            selector.moveCursor("left");
        } else if (KeyH.isRightPressed()) {
            selector.moveCursor("right");
        }
        return System.nanoTime();
    }
}