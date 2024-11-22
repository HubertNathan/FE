package GameEngine.Battle;

import GUI.ReadMapFile;
import GUI.SpriteSheet;
import GameEngine.Board;
import GameEngine.Save.LoadSaveBoard;
import javafx.animation.Animation;
import javafx.animation.ParallelTransition;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

import static GameEngine.Battle.GameState.*;

public class GameStateManager {
    private static final ObjectProperty<GameState> stateProperty = new SimpleObjectProperty<>(GameState.IDLE);
    private static GameState previousState;
    public void nextState(GameState state){
        previousState = stateProperty.get();
        stateProperty.set(state);
    }
    public void next(){
        GameState newState;
        newState = switch (stateProperty.get()){
            case IDLE -> MOVE;
            case MOVE -> ATTACK;
            case ATTACK -> ATTACK_SELECT_ENEMY;
            case ATTACK_SELECT_ENEMY -> ATTACK_SELECT_WEAPON;
            case ATTACK_SELECT_WEAPON -> BATTLE;
            default -> IDLE;
        };
        nextState(newState);
    }
    public GameState getState(){
        return stateProperty.get();
    }
    public GameState getPreviousState(){
        return previousState;
    }
}