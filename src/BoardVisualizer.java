import Units.Lyn_Lord;
import Units.Unit;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;

public class BoardVisualizer extends JPanel{

    protected int tileSize = 48;
    protected int height;
    protected int width;
    protected JFrame mainFrame;
    protected JLabel[][] labels;
    protected BufferedImage image;
    Board board;
    public BoardVisualizer(ReadMapFile mapReader, Board board) {
        this.board = board;
        height = mapReader.getDimensions()[0];
        width = mapReader.getDimensions()[1];
        this.mainFrame = new JFrame(mapReader.getChapter());
        mainFrame.setSize(new Dimension(tileSize* width,tileSize*height));
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setLocationRelativeTo(null);
        mainFrame.add(this);
        mainFrame.setVisible(true);
    }
    public void paintComponent(Graphics g){
        super.paintComponents(g);
        Dimension size = mainFrame.getSize();
        int w = size.width;
        int h = size.height;
        float W = (float) w/width;
        float H = (float) h/height;
        Graphics2D g2 = (Graphics2D) g;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                Unit unit = board.get(i,j).getUnit();
                board.get(i,j).draw(g2,i,j,W,H);
                if (unit != null){
                    unit.draw(g2,i,j,W,H);
                }
            }

        }
        g2.dispose();
    }
    public static void main(String[] args) throws IOException, InterruptedException {
        ReadMapFile mapReader = new ReadMapFile("CH3.map");
        mapReader.load();
        Board board = new Board(mapReader);
        board.get(0,1).setUnit(new Lyn_Lord());
        BoardVisualizer pv = new BoardVisualizer(mapReader, board);
        while (true) {
            pv.repaint();
            Thread.sleep(500);
        }


    }
}