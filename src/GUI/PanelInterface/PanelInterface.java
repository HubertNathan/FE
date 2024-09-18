package GUI.PanelInterface;

import GameEngine.TextInterpreter;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.io.IOException;

public abstract class PanelInterface {
    protected TextInterpreter TxtI;
    protected Image UpperLeftCorner,UpperBound,UpperRightCorner, LeftBound,Middle,RightBound,LowerLeftCorner,LowerBound,LowerRightCorner;

    PanelInterface() throws IOException {
        TxtI = new TextInterpreter();
    }
    protected void superLoad(){
        UpperLeftCorner = new Image("file:Resources/MenuSprites/ULC.png",12*6,12*6,false,false);
        UpperBound = new Image("file:Resources/MenuSprites/UB.png",8*6,12*6,false,false);
        UpperRightCorner = new Image("file:Resources/MenuSprites/URC.png",13*6,12*6,false,false);
        RightBound = new Image("file:Resources/MenuSprites/RB.png",13*6,8*6,false,false);
        LowerRightCorner = new Image("file:Resources/MenuSprites/LoRC.png",13*6,13*6,false,false);
        LowerBound = new Image("file:Resources/MenuSprites/LoB.png",8*6,13*6,false,false);
        LowerLeftCorner = new Image("file:Resources/MenuSprites/LoLC.png",12*6,13*6,false,false);
        LeftBound = new Image("file:Resources/MenuSprites/LB.png",12*6,8*6,false,false);
        Middle = new Image("file:Resources/MenuSprites/M.png",8*6,8*6,false,false);
    }
    protected abstract void load();

    protected Canvas buildPanel(int width, int height){
        int wOffset = 0,hOffset = 0;
        Canvas panel = new Canvas((12+8*width+13)*6,(12+8*height+13)*6);
        GraphicsContext g = panel.getGraphicsContext2D();
        g.drawImage(UpperLeftCorner,0,0);
        wOffset+= 12*6;
        for (int i = 0; i < width; i++) {
            g.drawImage(UpperBound,wOffset,hOffset);
            wOffset += 8*6;
        }
        hOffset+=12*6;
        g.drawImage(UpperRightCorner,wOffset,0);
        for (int i = 0; i < height; i++) {
            wOffset = 0;
            g.drawImage(LeftBound,wOffset,hOffset);
            wOffset+=12*6;
            for (int j = 0; j < width; j++) {
                g.drawImage(Middle,wOffset,hOffset);
                wOffset+=8*6;
            }
            g.drawImage(RightBound,wOffset,hOffset);
            hOffset+=6*8;
        }
        wOffset=0;
        g.drawImage(LowerLeftCorner,wOffset,hOffset);
        wOffset+= 12*6;
        for (int i = 0; i < width; i++) {
            g.drawImage(LowerBound,wOffset,hOffset);
            wOffset += 8*6;
        }
        g.drawImage(LowerRightCorner,wOffset,hOffset);

        return panel;

    }
}
