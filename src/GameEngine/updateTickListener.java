package GameEngine;

import GUI.BoardVisualizer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class updateTickListener implements ActionListener {
    BoardVisualizer boardVisualizer;
    updateTickListener(BoardVisualizer boardVisualizer){
        this.boardVisualizer = boardVisualizer;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            boardVisualizer.animate();
        } catch (InterruptedException ex) {
            throw new RuntimeException(ex);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

    }
}
