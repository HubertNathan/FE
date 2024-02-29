package GUI;

import Units.Unit;
import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.geometry.Rectangle2D;
import javafx.util.Duration;

import javafx.scene.image.ImageView;


public class SpriteAnimation extends Transition {
    Duration cycleDuration = Duration.millis(1200);
    private final int count;
    private final int offsetY;
    private final int width;
    private final int height;
    private int lastIndex;
    private final ImageView imv;
    Unit unit;


            SpriteAnimation(Unit unit, ImageView imv){
                this.imv = imv;
                this.unit = unit;
                switch (unit.getMode()){
                    case "standing":
                        count = 3;
                        offsetY = 0;
                        break;
                    case "select":
                        count = 3;
                        offsetY = 12;
                        break;
                    default:
                        count = 4;
                        offsetY = 0;
                }
                if (unit.getMode().equals("standing")) {
                    height = (int) imv.getImage().getHeight() / 3;
                } else height = (int) imv.getImage().getHeight() / 7;
                width = (int)imv.getImage().getWidth();

                setCycleDuration(cycleDuration);
                setInterpolator(Interpolator.LINEAR);
    }
    @Override
    protected void interpolate(double k) {
        final int index = unit.animation((int)(36*k),false);
        if (index != lastIndex) {
            final int x = 0;
            final int y = (index) * height + offsetY;
            imv.setViewport(new Rectangle2D(0, y, width, height));
            lastIndex = index;
        }
    }
}
