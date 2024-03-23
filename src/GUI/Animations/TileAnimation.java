package GUI.Animations;

import GameEngine.Square;
import javafx.animation.Transition;
import javafx.scene.image.Image;
import java.util.HashMap;

public class TileAnimation extends Transition {
    Square sq;
    TileAnimation(Square square){
        sq = square;
    }


    @Override
    protected void interpolate(double k) {
        int index = (int)(32*k)+1;


    }
}
