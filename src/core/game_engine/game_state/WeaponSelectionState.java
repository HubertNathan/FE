package core.game_engine.game_state;

import core.Cursor;
import gui.PanelInterface.GameInterface;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;

import java.io.IOException;
import java.util.List;

import static core.game_engine.Battle.BattleEngine.*;

public class WeaponSelectionState implements GameState{

    private final Cursor cursor;
    private final String objectives;
    private GameStateContext context;

    WeaponSelectionState(Cursor cursor, String objectives) throws IOException {
        this.cursor = cursor;
        this.objectives = objectives;
        transitionIn();
    }
    @Override
    public void handleInput(KeyEvent e) throws IOException {
        switch (e.getCode()) {
            case ESCAPE -> prev();
            case ENTER -> next();
        }
    }

    @Override
    public void setContext(GameStateContext context)  {
        this.context = context;
    }

    @Override
    public void transitionIn() throws IOException {
        List<ImageView> weaponMenu = GameInterface.drawWeaponMenu(cursor.getSelectedUnit());
        weaponMenu.getFirst().setTranslateX(tileSize*3./4);
        weaponMenu.getFirst().setTranslateY(tileSize*3./4);
        weaponMenu.get(1).setTranslateX(getBoard().getWidth()*tileSize - weaponMenu.get(1).getImage().getWidth() - tileSize * 11./16);
        weaponMenu.get(1).setTranslateY(getBoard().getHeight()*tileSize - weaponMenu.get(1).getImage().getHeight() - tileSize * 11./16);
        weaponMenu.getLast().setTranslateX(getBoard().getWidth()*tileSize - weaponMenu.get(1).getImage().getWidth() - tileSize*7/16.);
        weaponMenu.getLast().setTranslateY(getBoard().getHeight()*tileSize - weaponMenu.getLast().getFitHeight() - weaponMenu.get(1).getImage().getHeight() -tileSize*11./16);
        getOverlayPane().getChildren().addAll(weaponMenu);

    }

    @Override
    public void next() throws IOException {

    }

    @Override
    public void prev() throws IOException {
        getOverlayPane().getChildren().removeLast();
        getOverlayPane().getChildren().removeLast();
        getOverlayPane().getChildren().removeLast();
        context.setCurrentState(new ActionSelectionState(cursor,objectives));
    }
}
