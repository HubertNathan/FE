package GameEngine;


import GUI.BoardVisualizer;

import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

public class ComponentHandler implements ComponentListener {
    public void componentResized(ComponentEvent e){
         Component c = e.getComponent();
        System.out.println("componentResized "+c.getSize().toString());
         if (c instanceof BoardVisualizer){
             int W = ((BoardVisualizer) c).getWidthValue();
             int H = ((BoardVisualizer) c).getHeightValue();
             int w = (c.getWidth()/W)*W;
             int h = (c.getHeight()/H)*H;
             ((BoardVisualizer) c).resizeImage(3*h,3*w);
             c.repaint();

         }
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
