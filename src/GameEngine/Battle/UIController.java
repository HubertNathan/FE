package GameEngine.Battle;

import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class UIController implements EventHandler<KeyEvent> {
    private static KeyCode Ok;
    private static KeyCode Back;
    private GameStateManager gameStateManager = new GameStateManager();
    UIController(KeyCode Ok, KeyCode Back){
        UIController.Ok = Ok;
        UIController.Back = Back;
    }
    UIController(){
        Ok = KeyCode.ENTER;
        Back = KeyCode.ESCAPE;
    }

    @Override
    public void handle(KeyEvent keyEvent) {
        KeyCode code = keyEvent.getCode();
        if (code.equals(Ok)){
            gameStateManager.next();
        }

    }
}
