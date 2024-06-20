package GUI.Animations;

import Units.Unit;
import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.geometry.Rectangle2D;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import javafx.scene.image.ImageView;


public class SpriteAnimation extends Transition {
    Duration cycleDuration = Duration.millis(2400);
    private int offsetY;
    private final int width;
    private final int height;
    private int lastIndex;
    private final ImageView imv;
    Unit unit;

    public SpriteAnimation(Unit unit, ImageView imv){
        this.imv = imv;
        this.unit = unit;
        switch (unit.getMode()){
            case "standing":
                offsetY = 0;
                break;
            case "select":
                offsetY = 12;
                break;
            default:
                offsetY = 0;
        }
        if (unit.getMode().equals("standing")) {
            height = (int) imv.getImage().getHeight() / 3;
        } else height = (int) imv.getImage().getHeight() / 15;
        width = (int)imv.getImage().getWidth();

        setCycleDuration(cycleDuration);
        setInterpolator(Interpolator.LINEAR);
    }
    @Override
    protected void interpolate(double k) {
        final int index = unit.animation((int)(72*k));
        final int index2=(int)(144*k);
        if (lastIndex!= index2) {
            switch (unit.getMode()) {
                case Unit.SELECT:
                    imv.setImage(unit.getSprites());
                    offsetY = 12;
                    break;
                case Unit.STANDING:
                    imv.setImage(unit.getSprites());
                    offsetY = 0;
                    break;
                case Unit.RIGHT:
                    Image image = unit.getSprites();
                    Canvas canvas = new Canvas(6*32, 6*480);
                    double xoff = 15;
                    GraphicsContext gc = canvas.getGraphicsContext2D();
                    gc.translate(image.getWidth() + xoff * 2, 0);
                    gc.scale(-1, 1);
                    gc.drawImage(image, xoff, 0);
                    image = canvas.snapshot(new SnapshotParameters(){{
                        setFill(Color.TRANSPARENT);
                    }},null);
                    imv.setImage(image);
                    offsetY = 0;
                    break;
                case Unit.LEFT:
                    imv.setImage(unit.getSprites());
                    offsetY = 0;
                    break;
                case Unit.DOWN:
                    imv.setImage(unit.getSprites());
                    offsetY = 4;
                    break;
                case Unit.UP:
                    imv.setImage(unit.getSprites());
                    offsetY = 8;
                    break;
            }

            final int x = 0;
            final int y = (index) * height + offsetY * height;
            imv.setViewport(new Rectangle2D(0, y, width, height));
            lastIndex = index2;
        }
    }
}
