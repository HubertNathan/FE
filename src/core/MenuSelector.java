package core;

import gui.Animations.MenuPointerAnimation;
import javafx.scene.image.ImageView;

import static core.game_engine.Battle.BattleEngine.tileSize;

public class MenuSelector extends ImageView {
    MenuPointerAnimation animation = new MenuPointerAnimation(this);

    public MenuSelector(){
        setFitHeight(3./4*tileSize);
        setFitWidth(5./4*tileSize);
    }
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
