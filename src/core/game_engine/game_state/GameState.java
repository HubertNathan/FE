package core.game_engine.game_state;

import javafx.scene.input.KeyEvent;

import java.io.IOException;

public interface GameState {
    void handleInput(KeyEvent e) throws IOException;
    void setContext(GameStateContext context);
    void transitionIn() throws IOException;
    void next() throws IOException;
    void prev() throws IOException;
}
