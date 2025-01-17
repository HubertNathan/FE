package core.game_engine.game_state;

import javafx.scene.input.KeyEvent;

import java.io.IOException;

public class GameStateContext {

    private GameState currentState;

    public void setCurrentState(GameState state){
        this.currentState = state;
        state.setContext(this);
    }
    public void handleInput(KeyEvent e) throws IOException {
        currentState.handleInput(e);
    }
}
