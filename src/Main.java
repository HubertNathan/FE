import Units.Lyn_Lord;

import java.io.FileNotFoundException;
import java.io.IOException;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        ReadMapFile mapReader = new ReadMapFile("CH1.map");
        mapReader.load();
        Board board = new Board(mapReader);
        board.get(0,4).setUnit(new Lyn_Lord());
        BoardVisualizer pv = new BoardVisualizer(mapReader, board);
        while (true) {
            pv.repaint();
            Thread.sleep(500);
        }
    }
}