package core.game_engine.game_state;

import javafx.scene.Scene;

import java.io.IOException;

public class InputManager {
    private GameStateContext gameStateContext;
    public InputManager(GameStateContext gameStateContext){
        this.gameStateContext = gameStateContext;
    }

    public void addEventHandler(Scene scene){
        scene.setOnKeyPressed(e-> {
            try {
                gameStateContext.handleInput(e);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
    }
}
