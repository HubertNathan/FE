package gui.Animations;

import core.Square;
import javafx.animation.Transition;

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
