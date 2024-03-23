package EventHandlers;

import GUI.FireEmblemApp;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.io.IOException;

public class KeyEventHandler implements EventHandler<KeyEvent> {
    FireEmblemApp app;
    double time;
    public KeyEventHandler(FireEmblemApp app){
        this.app = app;
        time = System.nanoTime();
    }
    @Override
    public void handle(KeyEvent e) {
        KeyCode c = e.getCode();
        if (System.nanoTime() - time<30000000){
            time = System.nanoTime();
            return;
        }
        time = System.nanoTime();
        switch (c){
            case KeyCode.UP, KeyCode.DOWN,KeyCode.LEFT,KeyCode.RIGHT:
                try {
                    app.moveCursor(c);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                break;
            case KeyCode.ENTER:
                app.enter();
                break;
            case KeyCode.B:
                try {
                    app.b();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                break;
            case KeyCode.A:
                System.out.println("a");
                app.revertToGameScene();
                break;
        }
    }
}
