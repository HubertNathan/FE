package GUI.Animations.ColorSquares;

import javafx.animation.Transition;
import javafx.scene.image.Image;
import javafx.util.Duration;

public class HitAnimation extends Transition {
    Image hit = new Image("file:Resources/BattleEffects/hit.png");
    HitAnimation(int hitFrame, int animFrames){
        setCycleDuration(Duration.seconds((double) (animFrames-hitFrame)/60));
        setCycleCount(1);
    }
    @Override
    protected void interpolate(double v) {

    }
}
