package core.game_engine.game_state;

import core.Cursor;
import core.MenuSelector;
import core.game_engine.Battle.BattleEngine;
import gui.Animations.ColorTilesAnimation;
import gui.PanelInterface.GameInterface;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import units.Unit;
import java.io.IOException;
import java.util.ArrayList;

import static core.game_engine.Battle.BattleEngine.*;

public class ActionSelectionState implements GameState{
    private final Cursor cursor;
    private MenuSelector selector;
    private final GameInterface gi;
    private final String objectives;
    private ColorTilesAnimation colorTilesAnimation;
    private GameStateContext context;
    String[] menu;

    ActionSelectionState(Cursor cursor, GameInterface gi, String objectives) throws IOException {
        this.cursor = cursor;
        this.gi = gi;
        this.objectives = objectives;
        transitionIn();
    }
    @Override
    public void handleInput(KeyEvent e) throws IOException {
        switch (e.getCode()){
            case ESCAPE -> prev();
            case UP -> moveUp();
            case DOWN -> moveDown();
            case ENTER -> next();
        }
    }

    @Override
    public void setContext(GameStateContext context)  {
        this.context = context;
    }

    @Override
    public void transitionIn() throws IOException {

        boolean isNextToEnemies = false, isNextToAllies = false;

        ArrayList<Unit> enemiesInRange = cursor.getSelectedUnit().findEnemiesInReach();

        for (Unit enemy : enemiesInRange){
            cursor.setTo(enemy);
            BattleEngine.getBoard().get(enemy.getYValue(),enemy.getXValue()).range(true);
        }

        if (!enemiesInRange.isEmpty()) {
            isNextToEnemies = true;
            colorTilesAnimation = new ColorTilesAnimation();
            colorTilesAnimation.start();
            getAnimationPane().getChildren().add(colorTilesAnimation.getAnimationCanvas());
        }
        if (!cursor.getSelectedUnit().findAlliesInReach().isEmpty()) isNextToAllies = true;

        menu = buildMenu(isNextToEnemies,isNextToAllies);
        ImageView intermediateMenu = gi.drawIntermediateMenu(menu);

        if (cursor.getXValue() > getBoard().getWidth()/2) intermediateMenu.setTranslateX(3*tileSize / 4);
        else intermediateMenu.setTranslateX(getBoard().getWidth()*tileSize - 4*tileSize + tileSize/4);
        intermediateMenu.setTranslateY(tileSize + 3 * tileSize / 4);
        getOverlayPane().getChildren().add(intermediateMenu);

        selector = new MenuSelector(){{
            setTranslateX(intermediateMenu.getTranslateX() - tileSize + tileSize/16);
            setTranslateY(intermediateMenu.getTranslateY() + (7*tileSize)/16);
            play();
        }};
        getOverlayPane().getChildren().add(selector);
    }

    @Override
    public void next() throws IOException {
        getAnimationPane().getChildren().clear();
        //getOverlayPane().getChildren().removeLast();
        //getOverlayPane().getChildren().removeLast();
        switch (menu[(int)(selector.getTranslateY()-(getOverlayPane().getChildren().get(1)).getTranslateY())/tileSize]){
            case "Attack":
                System.out.println("Attack");
                break;
            case "Support":
                System.out.println("Support");
                break;
            case "Item":
                System.out.println("Item");
                break;
            case "Trade":
                System.out.println("Trade");
                break;
            case "Wait":
                System.out.println("Wait");
                break;
        }
    }

    @Override
    public void prev() throws IOException {
        if (colorTilesAnimation!=null) colorTilesAnimation.stop();
        cursor.getSelectedUnit().revertMove();
        cursor.setTo(cursor.getSelectedUnit());
        getAnimationPane().getChildren().clear();
        cursor.getIMV().setOpacity(1);
        getOverlayPane().getChildren().removeLast();
        getOverlayPane().getChildren().removeLast();
        context.setCurrentState(new UnitMoveState(cursor,gi,objectives));
    }
    private String[] buildMenu(boolean isNextToEnemy, boolean isNextToAlly){
        StringBuilder menu = new StringBuilder();
        if (isNextToEnemy){
            menu.append("Attack ");
        }
        if (isNextToAlly){
            menu.append("Support ");
            menu.append("Item ");
            menu.append("Trade ");
        }
        else menu.append("Item ");
        menu.append("Wait");
        return menu.toString().split(" ");
    }
    private void moveUp() {
        short n = (short)(((int) ((ImageView) getOverlayPane().getChildren().get(1)).getFitHeight()) / tileSize);
        if ((int) (selector.getTranslateY()-(getOverlayPane().getChildren().get(1)).getTranslateY())/tileSize == 1 && colorTilesAnimation!= null) colorTilesAnimation.start();
        else if (colorTilesAnimation!= null) colorTilesAnimation.stop();
        if (selector.getTranslateY() - tileSize > getOverlayPane().getChildren().get(1).getTranslateY()) selector.setTranslateY(selector.getTranslateY() - tileSize);
        else selector.setTranslateY(selector.getTranslateY() + (n-1) * tileSize);
    }
    private void moveDown(){
        short n = (short)(((int) ((ImageView) getOverlayPane().getChildren().get(1)).getFitHeight()) / tileSize);
        if (selector.getTranslateY() + tileSize < getOverlayPane().getChildren().get(1).getTranslateY()+tileSize*n) {
            selector.setTranslateY(selector.getTranslateY() + tileSize);
            if (colorTilesAnimation != null) colorTilesAnimation.stop();
        }
        else {
            selector.setTranslateY(selector.getTranslateY() - (n-1) * tileSize);
            if (colorTilesAnimation != null) colorTilesAnimation.start();
        }
    }
}
