package gui.Animations;

import javafx.animation.AnimationTimer;
import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import units.Unit;

public class SpriteAnimation extends Transition {
    private final ImageView imv;
    private final Unit unit;

    private int offsetY = 0;
    private final int height;
    private final int width;
    private int lastIndex = -1;

    public SpriteAnimation(Unit unit, ImageView imv) {
        this.imv = imv;
        this.unit = unit;

        // Determine the offset and frame dimensions based on mode
        switch (unit.getMode()) {
            case "standing" -> offsetY = 0;
            case "select" -> offsetY = 12;
            default -> offsetY = 0;
        }

        height = (unit.getMode().equals("standing"))
                ? (int) imv.getImage().getHeight() / 3
                : (int) imv.getImage().getHeight() / 15;

        width = (int) imv.getImage().getWidth();

        setCycleDuration(Duration.millis(2400)); // Set animation cycle duration (adjustable)
        setInterpolator(Interpolator.LINEAR); // Smooth frame transition
    }

    @Override
    protected void interpolate(double k) {
        final int index = unit.animation((int) (72 * k));
        final int index2 = (int) (144 * k);

        if (lastIndex != index2) {
            switch (unit.getMode()) {
                case Unit.RIGHT -> {
                    imv.setImage(unit.getSprites());
                    imv.setScaleX(-1);
                    offsetY = 0;
                }
                case Unit.LEFT -> {
                    imv.setImage(unit.getSprites());
                    imv.setScaleX(1);
                    offsetY = 0;
                }
                case Unit.DOWN -> {
                    imv.setImage(unit.getSprites());
                    imv.setScaleX(1);
                    offsetY = 4;
                }
                case Unit.UP -> {
                    imv.setImage(unit.getSprites());
                    imv.setScaleX(1);
                    offsetY = 8;
                }
                case Unit.SELECT -> {
                    imv.setImage(unit.getSprites());
                    imv.setScaleX(1);
                    offsetY = 12;
                }
                case Unit.STANDING -> {
                    imv.setImage(unit.getSprites());
                    imv.setScaleX(1);
                    offsetY = 0;
                }
            }
            final int y = index * height + offsetY * height;
            imv.setViewport(new Rectangle2D(0, y, width, height));

            lastIndex = index2;
        }
    }
}