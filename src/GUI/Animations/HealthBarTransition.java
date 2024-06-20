package GUI.Animations;

import javafx.animation.FillTransition;
import javafx.animation.Transition;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Shape;
import javafx.util.Duration;

public class HealthBarTransition extends Transition {
    Color startColor, intermediateColor, color;
    Shape shape;
    public HealthBarTransition(Duration d, Shape shape, Color startColor, Color intermediateColor){
        this.shape = shape;
        this.startColor = startColor;
        this.intermediateColor = intermediateColor;
        setCycleCount(INDEFINITE);
        setCycleDuration(d);
    }
    @Override
    protected void interpolate(double v) {
        if (2*v<1) color = startColor.interpolate(intermediateColor, 2*v);
        else  color = intermediateColor.interpolate(startColor, 2*v-1);
        shape.setFill(color);
    }

}

