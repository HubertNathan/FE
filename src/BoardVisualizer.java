import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;

import PACKAGE_NAME.GameState;
public class BoardVisualizer extends JPanel {
    public static final int CELL_WIDTH = 16;
    public static final int CELL_HEIGHT = 16;

    protected final int height;
    protected final int width;
    protected JFrame mainFrame;
    protected JLabel[][] labels;
    protected BufferedImage image;
    Board board;
    public BoardVisualizer(ReadMapFile mapReader, Board board) throws FileNotFoundException {
        this.board = board;
        height = board.getHeight();
        width = board.getWidth();
        image = mapReader.getTileSetData();
        this.mainFrame = new JFrame("Intelligent tsp.pacman.Pacman");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setPreferredSize(new Dimension(16*width, 16*height));
        mainFrame.setLocationRelativeTo(null);
        mainFrame.pack();
        mainFrame.setVisible(true);
        mainFrame.add(this);
        repaint();
    }
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2D = (Graphics2D) g;
        for (int i = 0; i < height; i++){
            for (int j = 0; j < width; j++) {
                g.drawImage(board.get(i,j).image, 16*j,16*i,16,16,null );
            }
        }
    }
    public static void main(String[] args) throws FileNotFoundException {
        ReadMapFile mapReader = new ReadMapFile();
        mapReader.load();
        Board board = new Board(mapReader);
        BoardVisualizer pv = new BoardVisualizer(mapReader, board);
    }
}