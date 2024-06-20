package GUI.Animations;

import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class BattleEffect extends Transition {
    HashMap<String, List<Integer>> frameNumbers = new HashMap<>(){{
        put("miss", Arrays.asList(17,50));
    }};
    Image battleEffect;
    ImageView imv;
    int lastIndex = -1;
    String effect;
    public BattleEffect(String effect){
        this.effect = effect;
        battleEffect = new Image("file:Resources/BattleEffects/"+effect+".png");
        setCycleDuration(Duration.seconds((double) frameNumbers.get(effect).getLast()));
        setInterpolator(Interpolator.LINEAR);
        setCycleCount(INDEFINITE);

    }
    public void play(ImageView imv){
        this.imv = imv;
        imv.setImage(battleEffect);
        super.play();
    }
    @Override
    protected void interpolate(double v) {
        int index = (int) (35 * v);
        if (index != lastIndex) {
            lastIndex = index;
            if (index < frameNumbers.get(effect).getFirst()) {
                imv.setViewport(new Rectangle2D(1 + (index % 4) * 240, 1 + (index / 4) * 160, 240, 160));
            } else {
                imv.setViewport(new Rectangle2D(1, 1 + 4 * 160, 240, 160));
            }
        }
    }
}
