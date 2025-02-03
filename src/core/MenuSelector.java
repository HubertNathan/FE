package core;

import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import static core.game_engine.Battle.BattleEngine.tileSize;

public class MenuSelector extends ImageView {
    MenuSelectorAnimation animation = new MenuSelectorAnimation(this);

    public MenuSelector() {
        setFitHeight(3. / 4 * tileSize);
        setFitWidth(5. / 4 * tileSize);
    }

    public void changeTranslateX(double v) {
        animation.setX(v);
    }

    public double getBaseX() {
        return animation.getX();
    }

    public void play() {
        animation.setX(getTranslateX());
        animation.play();
    }

    public void stop() {
        animation.stop();
        setTranslateX(animation.getX());
    }

    private static class MenuSelectorAnimation extends Transition {
        ImageView imv;
        double x;
        double lastTime = System.nanoTime();

        public MenuSelectorAnimation(ImageView imv) {
            this.imv = imv;
            imv.setImage(new Image("file:Resources/MenuSprites/Cursor.png", 20 * 3, 12 * 3, false, false));
            setCycleCount(-1);
            setCycleDuration(Duration.seconds(32. / 60));
            setInterpolator(Interpolator.LINEAR);
        }

        public void setX(double x) {
            this.x = x;
        }

        public double getX() {
            return x;
        }

        @Override
        protected void interpolate(double t) {
            int frame = (int) (32 * t);
            if (System.nanoTime() - lastTime > .5 / 60) {
                imv.setTranslateX(x + (frame >= 7 ? 3 : 0) + (frame >= 9 ? 3 : 0) + (frame >= 12 ? 3 : 0) + (frame >= 16 ? 3 : 0) - (frame >= 23 ? 3 : 0) - (frame >= 25 ? 3 : 0) - (frame >= 28 ? 3 : 0));
            }

        }
    }
}