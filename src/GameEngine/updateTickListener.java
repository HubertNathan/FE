package GameEngine;

import GUI.BoardVisualizer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class updateTickListener implements ActionListener {
    BoardVisualizer boardVisualizer;
    updateTickListener(BoardVisualizer boardVisualizer){
        this.boardVisualizer = boardVisualizer;
    }
    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
