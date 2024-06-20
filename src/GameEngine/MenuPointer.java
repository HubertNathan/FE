package GameEngine;

import GUI.Animations.MenuPointerAnimation;
import javafx.scene.image.ImageView;

public class MenuPointer extends ImageView {
    MenuPointerAnimation animation = new MenuPointerAnimation(this);
    public void play(){
        animation.setX(getTranslateX());
        animation.play();}
    public void stop(){animation.stop();}
}
