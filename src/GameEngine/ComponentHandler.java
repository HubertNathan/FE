package GameEngine;


import GUI.BoardVisualizer;

import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.io.IOException;

public class ComponentHandler implements ComponentListener{
    GameState gs;
    boolean init;
    public ComponentHandler(GameState gs){
        super();
        this.gs = gs;
        init= true;
    }
    public void componentResized(ComponentEvent e){
        if (init) {
            init = false;
            return;
        }
         Component c = e.getComponent();
         gs.pauseTimer();
         if (c instanceof BoardVisualizer){
             int scaleX = ((BoardVisualizer) c).getWidth()/ ((BoardVisualizer) c).getWidthValue();
             int scaleY = ((BoardVisualizer) c).getHeight()/ ((BoardVisualizer) c).getHeightValue();
             System.out.println(scaleX+" "+scaleY);
             try {
                 ((BoardVisualizer) c).resizeImages(scaleY,scaleX);
             } catch (InterruptedException ex) {
                 throw new RuntimeException(ex);
             } catch (IOException ex) {
                 throw new RuntimeException(ex);
             }
             c.repaint();
         }
         gs.resumeTimer();
    }

    @Override
    public void componentMoved(ComponentEvent e) {

    }

    @Override
    public void componentShown(ComponentEvent e) {

    }

    @Override
    public void componentHidden(ComponentEvent e) {

    }

}
