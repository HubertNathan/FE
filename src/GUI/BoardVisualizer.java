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
import java.util.HashMap;
import java.util.Map;

public class BoardVisualizer extends JPanel{
    BufferedImage originalMap;
    BufferedImage map;
    Map<Integer,BufferedImage> ColorSquaresMap;
    Map<Integer,BufferedImage> CursorMap;
    protected int tileAnimStep = 0;
    protected  int cursorAnimStep = 0;
    protected int cursorAnimState = 2;
    protected int tileSize = 48;
    protected int height;
    protected int width;
    protected JFrame mainFrame;
    protected Board board;
    protected KeyHandler KeyH;
    protected Selector selector;
    protected BufferedImage bufferImage;
    long lastTime;
    public BoardVisualizer(ReadMapFile mapReader, Board board, Selector selector) throws IOException {
        ComponentListener ComponentH = new ComponentHandler();
        this.addComponentListener(ComponentH);
        lastTime = System.nanoTime();
        load();
        this.board = board;
        height = mapReader.getDimensions()[0];
        width = mapReader.getDimensions()[1];
        this.mainFrame = new JFrame(mapReader.getChapter());
        mainFrame.setSize(new Dimension(tileSize* width,tileSize*height+37));
        originalMap = new BufferedImage(tileSize* width,tileSize*height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D mapGraphics = originalMap.createGraphics();
        board.draw(mapGraphics);

        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setLocationRelativeTo(null);
        mainFrame.add(this);
        mainFrame.setVisible(true);
        KeyH = new KeyHandler();
        this.addKeyListener(KeyH);
        this.setFocusable(true);
        this.selector = selector;
    }
    public void load() throws IOException {
        ColorSquaresMap = new HashMap<>();
        for (int i = 1; i < 17; i++) {
            ColorSquaresMap.put(i,ImageIO.read(new File("src/GUI/ColorSquares/"+i+".png")));
        }
        CursorMap = new HashMap<>();
        for (int i = 1; i < 5; i++) {
            CursorMap.put(i, ImageIO.read(new File("src/GUI/CursorSprites/Cursor"+i+".png")));
        }
    }
    public void paintComponent(Graphics g){
        Dimension size = mainFrame.getSize();
        int w = size.width;
        int h = size.height;
        super.paintComponents(g);
        float W = (float) w/width;
        float H = (float) (h-37)/(height);
        long fps = (long) 1000000000.0/(lastTime-System.nanoTime());
        bufferImage = new BufferedImage((int)W* width,(int)H*height, BufferedImage.TYPE_INT_ARGB);
        lastTime = System.nanoTime();
        System.out.println("fps:"+fps);
        tileAnimStep=(tileAnimStep+1)%32;
        Graphics2D bufferImageGraphics = bufferImage.createGraphics();
        bufferImageGraphics.drawImage(map,0,0,null);
        updateCursorAnimStep();
        Graphics2D g2 = (Graphics2D) g;
        update();
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                Unit unit = board.get(i,j).getUnit();
                //board.draw(bufferImageGraphics,i,j,W,H);
                if (board.get(i,j).isReachable()){board.draw(bufferImageGraphics,i,j,W,H, ColorSquaresMap.get(((tileAnimStep+2)/2)));}
                if (unit != null){
                    if (unit.isOnCursor()) {
                        unit.draw(bufferImageGraphics, i, j, W, H, "select");
                    } else {
                        unit.draw(bufferImageGraphics, i, j, W, H, "standing");

                    }
                }
                if (selector.getUnit() != null){
                    selector.draw(bufferImageGraphics,W,H,CursorMap.get(4));
                } else selector.draw(bufferImageGraphics,W,H,CursorMap.get(cursorAnimState+1));


            }
        }
        g2.drawImage(bufferImage,0,0,null);
        g2.dispose();
    }
    public void resizeImage(int newH, int newW) {
        if (newH == 0 || newW == 0){return;}
        Image tmp = originalMap.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
        map = new BufferedImage(newH,newW,BufferedImage.TYPE_INT_ARGB);
        Graphics g = map.getGraphics();
        g.drawImage(tmp,0,0,null);
        g.dispose();
    }

    private void updateCursorAnimStep() {
        cursorAnimStep = (cursorAnimStep+1)%36;
        if (cursorAnimStep == 1|| cursorAnimStep ==3) {
            cursorAnimState = (cursorAnimState - 1);
        } else if (cursorAnimStep == 19 || cursorAnimStep == 21 ) {
            cursorAnimState = (cursorAnimState + 1);
        }
    }

    public void update(){
        if (KeyH.isUpPressed()){
            selector.moveCursor("up");
        } else if (KeyH.isDownPressed()) {
            selector.moveCursor("down");
        } else if (KeyH.isLeftPressed()) {
            selector.moveCursor("left");
        } else if (KeyH.isRightPressed()) {
            selector.moveCursor("right");
        } else if (KeyH.isEnterPressed() && selector.getUnit() != null){
            selector.getUnit().select();
        } else if (KeyH.isEscapePressed() && selector.getUnit().isSelected()) {
            System.out.println("unselect");
            selector.getUnit().unselect();
        }
    }
    public static void main(String[] args) throws IOException, InterruptedException {
        ReadMapFile mapReader = new ReadMapFile("CH3.map");
        Board board = new Board(mapReader);
        Unit Lyn = new Lyn_Lord();
        board.setUnit(Lyn,0,1,true);
        Selector selector = new Selector(Lyn, board);
        BoardVisualizer pv = new BoardVisualizer(mapReader, board,selector);
        while (true) {
            pv.repaint();
            Thread.sleep(500);
        }
    }
    public int getWidthValue() {
        return width;
    }
    public int getHeightValue() {
        return height;
    }
}