package GameEngine;

import GUI.Animations.MenuPointerAnimation;
import javafx.scene.image.ImageView;

public class MenuPointer extends ImageView {
    MenuPointerAnimation animation = new MenuPointerAnimation(this);
    public void changeTranslateX(double v){
        animation.setX(v);
    }
    public double getBaseX(){
        return animation.getX();
    }
    public void play(){
        animation.setX(getTranslateX());
        animation.play();}
    public void stop(){
        animation.stop();
        setTranslateX(animation.getX());
    }
}
