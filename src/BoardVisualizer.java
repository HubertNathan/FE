import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;

public class BoardVisualizer {
    public static final int CELL_WIDTH = 16;
    public static final int CELL_HEIGHT = 16;

    protected int height;
    protected int width;
    protected JFrame mainFrame;
    protected JLabel[][] labels;
    protected BufferedImage image;
    Board board;
    public BoardVisualizer(ReadMapFile mapReader, Board board) {
        height = mapReader.getDimensions()[0];
        width = mapReader.getDimensions()[1];
        this.mainFrame = new JFrame("Intelligent tsp.pacman.Pacman");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setLayout(new GridLayout(height, width));
        mainFrame.setLocationRelativeTo(null);
        labels = new JLabel[height][width];

        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                JLabel label = getLabel(board.get(row,col).getTerrain());
                labels[row][col] = label;
                mainFrame.add(label);
            }
        }
        mainFrame.pack();
        mainFrame.setVisible(true);
    }

    private JLabel getLabel(BufferedImage image) {
        JLabel label = new JLabel();
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setPreferredSize(new Dimension(CELL_WIDTH, CELL_HEIGHT));
        drawTile(image, label);
        label.setOpaque(true);
        return label;
    }

    protected static void drawTile(BufferedImage image, JLabel label) {
        label.setIcon(new ImageIcon(image));
        label.repaint();
    }
    public static void update(ReadMapFile mapReader, Board board,JFrame frame, int height, int width, JLabel[][] labels){
        Dimension size = frame.getBounds().getSize();
        int h = size.height;
        int w = size.width;
        float H = (float) ((float) h /(16*(float)height));
        float W = (float) w /(16*width);
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                drawTile(board.get(i,j).resize((int)(H*CELL_HEIGHT), (int)(W*CELL_HEIGHT)),labels[i][j]);
            }
        }
    }
    public static void main(String[] args) throws FileNotFoundException {
        ReadMapFile mapReader = new ReadMapFile("CH3.map");
        mapReader.load();
        Board board = new Board(mapReader);
        BoardVisualizer pv = new BoardVisualizer(mapReader, board);
        update(mapReader, board,pv.mainFrame,pv.height,pv.width,pv.labels);
        while (true) {
            update(mapReader, board, pv.mainFrame, pv.height, pv.width, pv.labels);
        }

    }
}
