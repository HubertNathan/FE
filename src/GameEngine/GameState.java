package GameEngine;

import GUI.BoardVisualizer;
import GUI.ReadMapFile;
import Units.Brigand;
import Units.Cavalier;
import Units.Lyn_Lord;
import Units.Unit;

import javax.swing.*;
import java.io.IOException;

public class GameState  extends JPanel{
    int FPS = 30;
    long refreshPeriod = (long) 1000000000/(FPS*1000000);
    int c = 0;
    KeyHandler KeyH;
    Thread gameThread;
    public GameState() throws IOException, InterruptedException {
        long s = (long) 1000000000.0;
        ReadMapFile mapReader = new ReadMapFile("CH2");
        Board board = new Board(mapReader);
        board.setUnit(new Lyn_Lord(),2,2,true);
        board.setUnit(new Cavalier("Sain"), 4, 4);
        board.setUnit(new Brigand("Brigand"),6,6);
        Unit leader = board.getLeader();
        Selector selector = new Selector(leader, board);
        BoardVisualizer BV = new BoardVisualizer(mapReader, board,selector);
        startGameThread();


        while (true) {
            c += 1;
            long lastTime = System.nanoTime();
            BV.repaint();
            long fps = -s/(lastTime - System.nanoTime());
            Thread.sleep(refreshPeriod);
            lastTime = System.nanoTime();
        }
    }
    public void startGameThread(){
        gameThread = new Thread(String.valueOf(this));
        gameThread.start();

    }
}
