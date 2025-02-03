package gui.PanelInterface;

import core.TextInterpreter;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.io.IOException;

import static core.game_engine.Battle.BattleEngine.tileSize;

public abstract class PanelInterface {
    protected static double scale = tileSize / 16.;
    protected static final Image UpperLeftCorner = new Image("file:Resources/MenuSprites/ULC.png"),
    UpperBound = new Image("file:Resources/MenuSprites/UB.png"),
    UpperRightCorner = new Image("file:Resources/MenuSprites/URC.png"),
    RightBound = new Image("file:Resources/MenuSprites/RB.png"),
    LowerRightCorner = new Image("file:Resources/MenuSprites/LoRC.png"),
    LowerBound = new Image("file:Resources/MenuSprites/LoB.png"),
    LowerLeftCorner = new Image("file:Resources/MenuSprites/LoLC.png"),
    LeftBound = new Image("file:Resources/MenuSprites/LB.png"),
    Middle = new Image("file:Resources/MenuSprites/M.png");

    protected static Canvas buildPanel(int width, int height){
        double wOffset = 0,hOffset = 0;
        GraphicsContext g = new Canvas((12+8*width+13)*scale,(12+8*height+13)*scale).getGraphicsContext2D();
        g.setImageSmoothing(false);
        g.drawImage(
                UpperLeftCorner,
                0,0,
                UpperLeftCorner.getWidth()*scale,UpperLeftCorner.getHeight()*scale
        );
        wOffset += 12*scale;
        for (int i = 0; i < width; i++) {
            g.drawImage(
                    UpperBound,
                    wOffset,hOffset,
                    UpperBound.getWidth()*scale,UpperBound.getHeight()*scale
            );
            wOffset += 8*scale;
        }
        g.drawImage(
                UpperRightCorner,
                wOffset,hOffset,
                UpperRightCorner.getWidth()*scale,UpperRightCorner.getHeight()*scale
        );
        hOffset += 12*scale;
        for (int i = 0; i < height; i++) {
            wOffset = 0;
            g.drawImage(
                    LeftBound,
                    wOffset,hOffset,
                    LeftBound.getWidth()*scale,LeftBound.getHeight()*scale
            );
            wOffset += 12*scale;
            for (int j = 0; j < width; j++) {
                g.drawImage(
                        Middle,
                        wOffset,hOffset,
                        Middle.getWidth()*scale,Middle.getHeight()*scale
                );
                wOffset+=8*scale;
            }
            g.drawImage(
                    RightBound,
                    wOffset,hOffset,
                    RightBound.getWidth()*scale,RightBound.getHeight()*scale
            );
            hOffset+=8*scale;
        }
        wOffset=0;
        g.drawImage(
                LowerLeftCorner,
                wOffset,hOffset,
                LowerLeftCorner.getWidth()*scale,LowerLeftCorner.getHeight()*scale
        );
        wOffset+= 12*scale;
        for (int i = 0; i < width; i++) {
            g.drawImage(
                    LowerBound,
                    wOffset,hOffset,
                    LowerBound.getWidth()*scale,LowerBound.getHeight()*scale
            );
            wOffset += 8*scale;
        }
        g.drawImage(
                LowerRightCorner,
                wOffset,hOffset,
                LowerRightCorner.getWidth()*scale,LowerRightCorner.getHeight()*scale
                );

        return g.getCanvas();

    }
}
