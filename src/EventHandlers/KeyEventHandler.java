package EventHandlers;

import GUI.Battle;
import javafx.event.EventHandler;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.io.IOException;

public class KeyEventHandler implements EventHandler<KeyEvent> {
    Battle app;
    double time;
    int menuId;
    public KeyEventHandler(Battle app){
        this.app = app;
        time = System.nanoTime();
    }
    @Override
    public void handle(KeyEvent e) {
        menuId = app.getMenuId();
        KeyCode c = e.getCode();
        if (System.nanoTime() - time<30000000){
            time = System.nanoTime();
            return;
        }
        time = System.nanoTime();
        switch (c){
            case KeyCode.UP, KeyCode.DOWN,KeyCode.LEFT,KeyCode.RIGHT:
                try {
                    ImageView menu;
                    switch (menuId) {
                        case 2:
                            menu = app.getActiveMenu(2);
                            app.moveMenuPointer(c,menu.getTranslateY(),menu.getTranslateY()+menu.getFitHeight(),(int)(menu.getFitHeight()/3-25)/16);
                            break;
                        case 10:
                            menu = app.getActiveMenu(4);
                            app.moveMenuPointer(c,menu.getTranslateY(),menu.getTranslateY()+menu.getImage().getHeight()/2,(int)(menu.getImage().getHeight()/6-25)/16);
                        default:
                            app.moveCursor(c);
                    }
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                break;
            case KeyCode.ENTER:
                try {
                    app.enter();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                break;
            case KeyCode.B:
                try {
                    app.b();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                break;
            case KeyCode.A:
                app.revertToGameScene();
                break;
        }
    }
}
