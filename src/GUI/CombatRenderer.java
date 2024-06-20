package GUI;

import GUI.Animations.HealthBarTransition;
import GameEngine.Board;
import GameEngine.CombatAnimation;
import GameEngine.CombatHandler;
import GameEngine.TextInterpreter;
import Units.Unit;
import javafx.animation.AnimationTimer;
import javafx.animation.ParallelTransition;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import static GUI.FireEmblemApp.getResampledImage;

public class CombatRenderer {
    private final Pane root = new Pane();
    private ImageView background;
    private final Pane UpperPane = new Pane();
    final Pane LowerPane = new Pane(){{setTranslateY(109);}};
    private final Unit attacker, defender;
    private Unit killedUnit;
    private final CombatHandler combatHandler;
    private final FireEmblemApp FEApp;
    private final TextInterpreter TI = new TextInterpreter();
    List<Boolean> turn;
    public CombatRenderer(FireEmblemApp FEApp,Stage window, Board board, Unit attacker, Unit defender) throws IOException {
        this.FEApp = FEApp;
        this.attacker = attacker;
        this.defender = defender;
        combatHandler = new CombatHandler(attacker,defender, board);
        load(window, board);
        playCombatAnimation();
    }

    private void load(Stage window, Board board) throws IOException {
        loadBackground(board);
        root.getChildren().add(new Pane(){{getChildren().add(new Rectangle(window.getWidth(),window.getHeight()-37){{setFill(Color.color(0,0,0,.2));}});}});
        Scene battleScene = new Scene(root);
        window.setScene(battleScene);
        battleScene.setOnKeyPressed(e -> {

            KeyCode c = e.getCode();
            if (c.equals(KeyCode.ESCAPE)){
                FEApp.revertToGameScene();
            }
        });
        new Thread(()-> {
            try {
                addUpperUI();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }){{
            setDaemon(true);
            start();
        }};
        new Thread(()-> {
            try {
                addLowerUI();
                Platform.runLater(() -> {
                    try {
                        addHealthBar();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }){{
            setDaemon(true);
            start();
        }};
    }

    private void loadBackground(Board board) {
        background = new ImageView(board.drawFX(new Canvas(16 * board.getWidth(), 16 * board.getHeight())).snapshot(null, new WritableImage(16 * board.getWidth(), 16 * board.getHeight())));
        ResizeBackground(6);
        background.setFitWidth(48 * board.getWidth());
        background.setFitHeight(48 * board.getHeight());
        root.getChildren().add(background);
    }

    private void addUpperUI() throws IOException {
        Canvas upperCanvas = new Canvas(240*6,27*6);
        upperCanvas.getGraphicsContext2D().drawImage(new Image("file:Resources/MenuSprites/Upper_Scene.png",240*6,27*6,false,false),0,0);
        int dx1, dx2;
        if (attacker.getColor().equals("blue")){
            dx1 = 193*6;
            dx2 = 0;
        }
        else {
            dx1 = 0;
            dx2 = 193*6;
        }
        final Image[] text = new Image[1];
        Platform.runLater(()->
        {
            try {
                text[0] = TI.convertTxt(attacker.toString(), "white");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            upperCanvas.getGraphicsContext2D().drawImage(text[0], dx1 + (48 * 6 - text[0].getWidth()) / 2, 6 * 6 + (21 * 6 - text[0].getHeight()) / 2);
        });
        Platform.runLater(()->
        {
            try {
                text[0] = TI.convertTxt(defender.toString(), "white");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            upperCanvas.getGraphicsContext2D().drawImage(text[0], dx2 + (48 * 6 - text[0].getWidth()) / 2, 6 * 6 + (21 * 6 - text[0].getHeight()) / 2);
                });

        Platform.runLater(()-> {
            UpperPane.getChildren().add(new ImageView(upperCanvas.snapshot(new SnapshotParameters() {{
                setFill(Color.TRANSPARENT);
            }}, null)) {{
                setFitWidth(getImage().getWidth() / 2);
                setFitHeight(getImage().getHeight() / 2);
            }});
            root.getChildren().add(UpperPane);
        });

    }
    @SuppressWarnings("IntegerDivisionInFloatingPointContext")
    private void addLowerUI() throws IOException {
        Canvas lowerCanvas = new Canvas(240*6,51*6);
        lowerCanvas.getGraphicsContext2D().drawImage(new Image("file:Resources/MenuSprites/Lower_Scene.png",240*6,51*6,false,false),0,0);
        List<Integer> Numbers = combatHandler.getCombatStats(!attacker.getColor().equals("red"));
        TextInterpreter TI = new TextInterpreter();
        final Image[] img = new Image[1];
        for (int i = 0; i < 6; i++) {
            int finalI = i;
            Platform.runLater(()-> {
                try {
                    img[0] = TI.convertBattleNumbers(Numbers.get(finalI));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                lowerCanvas.getGraphicsContext2D().drawImage(img[0], 39 * 6 + (finalI / 3) * 200 * 6 - img[0].getWidth(), 3 * 6 + (finalI % 3) * 8 * 6);
            });
        }

        lowerCanvas.getGraphicsContext2D().drawImage(attacker.getWieldedWeapon().getIcon(),123*6,12*6);
        lowerCanvas.getGraphicsContext2D().drawImage(defender.getWieldedWeapon().getIcon(),44*6,12*6);

        Platform.runLater(()-> {
            try {

                img[0] = TI.convertTxt(attacker.getWieldedWeapon().getName(), "white");
                lowerCanvas.getGraphicsContext2D().drawImage(img[0], 139 * 6 + 19 * 6 - img[0].getWidth() / 3, 14 * 6);

                img[0] = TI.convertTxt(defender.getWieldedWeapon().getName(), "white");
                lowerCanvas.getGraphicsContext2D().drawImage(img[0], 60 * 6 + 19 * 6 - img[0].getWidth() / 3, 14 * 6);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
                });
        Platform.runLater(()->{
        LowerPane.getChildren().add(new ImageView(lowerCanvas.snapshot(new SnapshotParameters(){{setFill(Color.TRANSPARENT);}},null)){{
            setFitWidth(getImage().getWidth()/2);
            setFitHeight(getImage().getHeight()/2);
        }});
        LowerPane.setTranslateY(109*3);
        root.getChildren().add(LowerPane);
        });
    }
    private void addHealthBar() throws IOException {

        ImageView atkHealth = new ImageView(TI.convertBattleNumbers(attacker.getHealth()));
        ImageView defHealth = new ImageView(TI.convertBattleNumbers(defender.getHealth()));
        atkHealth.setFitHeight(atkHealth.getImage().getHeight()/2);
        atkHealth.setFitWidth(atkHealth.getImage().getWidth()/2);
        defHealth.setFitHeight(defHealth.getImage().getHeight()/2);
        defHealth.setFitWidth(defHealth.getImage().getWidth()/2);
        if (attacker.getColor().equals("red")){
            atkHealth.setTranslateX(9*3);
            atkHealth.setTranslateY(36*3);
            defHealth.setTranslateX(129*3);
            defHealth.setTranslateY(36*3);
        } else {
            atkHealth.setTranslateX(129*3);
            atkHealth.setTranslateY(36*3);
            defHealth.setTranslateX(9*3);
            defHealth.setTranslateY(36*3);
        }
        Rectangle atk_health_bar = new Rectangle(2*attacker.getHP()*3,5*3,Color.web("#29a500"));
        Rectangle def_health_bar = new Rectangle(2*defender.getHP()*3,5*3,Color.web("#29a500"));
        Rectangle atk_upper_HP_bar = new Rectangle(2*attacker.getHealth()*3,3,Color.web("#efffde"));
        Rectangle def_upper_HP_bar = new Rectangle(2*defender.getHealth()*3,3,Color.web("#efffde"));
        Rectangle atk_middle_HP_bar = new Rectangle(2*attacker.getHealth()*3,3,Color.web("#deffbd"));
        Rectangle def_middle_HP_bar = new Rectangle(2*defender.getHealth()*3,3,Color.web("#deffbd"));
        Rectangle atk_lower_HP_bar = new Rectangle(2*attacker.getHealth()*3,3*2,Color.web("#bdffb5"));
        Rectangle def_lower_HP_bar = new Rectangle(2*defender.getHealth()*3,3*2,Color.web("#bdffb5"));

        if (attacker.getColor().equals("red")){
            atk_health_bar.setTranslateX(30*3);
            atk_health_bar.setTranslateY(37*3);
            def_health_bar.setTranslateX(150*3);
            def_health_bar.setTranslateY(37*3);

            atk_upper_HP_bar.setTranslateX(30 * 3);
            atk_upper_HP_bar.setTranslateY(38 * 3);
            def_upper_HP_bar.setTranslateX(150 * 3);
            def_upper_HP_bar.setTranslateY(38 * 3);

            atk_middle_HP_bar.setTranslateX(30 * 3);
            atk_middle_HP_bar.setTranslateY(39 * 3);
            def_middle_HP_bar.setTranslateX(150 * 3);
            def_middle_HP_bar.setTranslateY(39 * 3);

            atk_lower_HP_bar.setTranslateX(30 * 3);
            atk_lower_HP_bar.setTranslateY(40 * 3);
            def_lower_HP_bar.setTranslateX(150 * 3);
            def_lower_HP_bar.setTranslateY(40 * 3);
        }
        else {
            atk_health_bar.setTranslateX(150*3);
            atk_health_bar.setTranslateY(37*3);
            def_health_bar.setTranslateX(30*3);
            def_health_bar.setTranslateY(37*3);

            atk_upper_HP_bar.setTranslateX(150 * 3);
            atk_upper_HP_bar.setTranslateY(38 * 3);
            def_upper_HP_bar.setTranslateX(30 * 3);
            def_upper_HP_bar.setTranslateY(38 * 3);

            atk_middle_HP_bar.setTranslateX(150 * 3);
            atk_middle_HP_bar.setTranslateY(39 * 3);
            def_middle_HP_bar.setTranslateX(30 * 3);
            def_middle_HP_bar.setTranslateY(39 * 3);

            atk_lower_HP_bar.setTranslateX(150 * 3);
            atk_lower_HP_bar.setTranslateY(40 * 3);
            def_lower_HP_bar.setTranslateX(30 * 3);
            def_lower_HP_bar.setTranslateY(40 * 3);
        }

        ImageView atkBar = new ImageView(new Image("file:Resources/HealthBar/"+attacker.getHP()+".png",(attacker.getHP()*2+1)*6,6*6,false,false));
        ImageView defBar = new ImageView(new Image("file:Resources/HealthBar/"+defender.getHP()+".png",(defender.getHP()*2+1)*6,6*6,false,false));

        atkBar.setFitHeight(6*3);
        atkBar.setFitWidth((double) (attacker.getHP() * 2 + 1) * 3);
        defBar.setFitHeight(6*3);
        defBar.setFitWidth((double) (defender.getHP() * 2 + 1) * 3);

        if (attacker.getColor().equals("red")){
            atkBar.setTranslateX(29 * 3);
            atkBar.setTranslateY(37 * 3);

            defBar.setTranslateX(149 * 3);
            defBar.setTranslateY(37 * 3);
        }

        else {

            atkBar.setTranslateX(149 * 3);
            atkBar.setTranslateY(37 * 3);

            defBar.setTranslateX(29 * 3);
            defBar.setTranslateY(37 * 3);
        }

        new ParallelTransition(new HealthBarTransition(Duration.seconds(1), atk_upper_HP_bar,Color.web("#efffde"),Color.web("#deff4a")),
                new HealthBarTransition(Duration.seconds(1),def_upper_HP_bar, Color.web("#efffde"),Color.web("#deff4a")),
                new HealthBarTransition(Duration.seconds(1),atk_middle_HP_bar,Color.web("#deffbd"),Color.web("#9cff00")),
                new HealthBarTransition(Duration.seconds(1),def_middle_HP_bar, Color.web("#deffbd"),Color.web("#9cff00")),
                new HealthBarTransition(Duration.seconds(1),atk_lower_HP_bar,Color.web("#bdffb5"),Color.web("#00ff00")),
                new HealthBarTransition(Duration.seconds(1),def_lower_HP_bar, Color.web("#bdffb5"),Color.web("#00ff00"))).play();
        LowerPane.getChildren().addAll(atkHealth,defHealth,atk_health_bar,def_health_bar);
        LowerPane.getChildren().addAll(atk_upper_HP_bar,atk_middle_HP_bar,atk_lower_HP_bar,def_upper_HP_bar,def_middle_HP_bar,def_lower_HP_bar);
        LowerPane.getChildren().addAll(atkBar,defBar);
    }
    private void playCombatAnimation() throws FileNotFoundException {
        ImageView D_IMV = new ImageView(){{

            setFitWidth(3 * 240);
            setFitHeight(3 * 160);
            root.getChildren().add(this);
        }};
        ImageView A_IMV = new ImageView(){{
                setFitWidth(3 * 240);
                setFitHeight(3 * 160);
                root.getChildren().add(this);
            }};
        ImageView E_IMV = new ImageView(){{
            setFitWidth(3 * 240);
            setFitHeight(3 * 160);
            root.getChildren().add(this);
        }};
        turn = combatHandler.getTurn();
        CombatAnimation combat = getCombatAnimation(attacker,defender);
        killedUnit = combat.getKilledUnit();
        combat.play(A_IMV,D_IMV,E_IMV);
    }

    private CombatAnimation getCombatAnimation(Unit attacker, Unit defender) throws FileNotFoundException {
        ArrayList<String> battle = new ArrayList<>();
        String atk = turn.get(1)?CombatAnimation.MELEE_CRITICAL:(turn.getFirst()?CombatAnimation.MELEE_ATTACK:CombatAnimation.MISS),
               def1 = turn.getFirst()?(defender.getHealth() > combatHandler.getCombatStats().get(1)?CombatAnimation.DAMAGE:CombatAnimation.DEATH):CombatAnimation.DODGE_MELEE,
               ct_atk =  turn.get(3)?CombatAnimation.MELEE_CRITICAL:(turn.get(2)?CombatAnimation.MELEE_ATTACK:CombatAnimation.MISS),
               def2 = turn.get(2)?(attacker.getHealth() > combatHandler.getCombatStats().get(4)?CombatAnimation.DAMAGE:CombatAnimation.DEATH):CombatAnimation.DODGE_MELEE,
               atk2 = turn.get(5)?CombatAnimation.MELEE_CRITICAL:(turn.get(4)?CombatAnimation.MELEE_ATTACK:CombatAnimation.MISS),
               def3 = turn.get(4)?CombatAnimation.DAMAGE:CombatAnimation.DODGE_MELEE;
        battle.add(atk);
        battle.add(def1);
        boolean a = defender.getHealth() > combatHandler.getCombatStats().get(1) || !turn.getFirst();
        if (a) {
            battle.add(ct_atk);
            battle.add(def2);
        }
        boolean b = a && (attacker.getHealth()> combatHandler.getCombatStats().get(4) || !turn.get(2)) && combatHandler.calculateAS(attacker) > 2 * combatHandler.calculateAS(defender);
        if (b){
            battle.add(atk2);
            battle.add(def3);
        }
        return new CombatAnimation(this, attacker,defender,battle);
    }

    public void takeDmg(Unit unit){
        List<Integer> stats = combatHandler.getCombatStats(!attacker.getColor().equals("red"));
        int dmg = (unit.equals(attacker)?stats.get(1):stats.get(4));
        if (dmg > unit.getHealth()) dmg = unit.getHealth();
        HashMap<Integer, Integer> dmgFrames =  interpolateHealthValues(dmg);
        new ShakingAnimation(dmgFrames, unit).start();
    }
    private void reduceHealth(Unit unit, int hit) throws IOException {
        if (unit.equals(attacker)) {
            ((Rectangle) LowerPane.getChildren().get(5)).setWidth(6*(unit.getHealth()-hit));
            ((Rectangle) LowerPane.getChildren().get(6)).setWidth(6*(unit.getHealth()-hit));
            ((Rectangle) LowerPane.getChildren().get(7)).setWidth(6*(unit.getHealth()-hit));
            ((ImageView) LowerPane.getChildren().get(1)).setImage(TI.convertBattleNumbers(unit.getHealth() - hit));
            if (unit.getHealth() >= 10 && unit.getHealth() - hit < 10) {
                ((ImageView) LowerPane.getChildren().get(1)).setFitWidth(8*3);
                LowerPane.getChildren().get(1).setTranslateX(LowerPane.getChildren().get(1).getTranslateX() + 8*3);

            }
        }
        else if (unit.equals(defender)){
            ((Rectangle) LowerPane.getChildren().get(8)).setWidth(6*(unit.getHealth()-hit));
            ((Rectangle) LowerPane.getChildren().get(9)).setWidth(6*(unit.getHealth()-hit));
            ((Rectangle) LowerPane.getChildren().get(10)).setWidth(6*(unit.getHealth()-hit));
            ((ImageView) LowerPane.getChildren().get(2)).setImage(TI.convertBattleNumbers(unit.getHealth() - hit));
            if (unit.getHealth() >= 10 && unit.getHealth() - hit < 10) {
                ((ImageView) LowerPane.getChildren().get(2)).setFitWidth(8*3);
                LowerPane.getChildren().get(2).setTranslateX(LowerPane.getChildren().get(2).getTranslateX() + 8*3);

            }
        }
        unit.setHealth(unit.getHealth()-hit);
    }

    private HashMap<Integer, Integer> interpolateHealthValues(int dmg) {
        HashMap<Integer, Integer> dmgFrames = new HashMap<>();
        int curHP = dmg;
        if (dmg < 20) {
            for (int i = 0; i < 19; i++) {
                if (dmg+(int) (-0.5 - (double) dmg * i / 19) != curHP) {
                    dmgFrames.put(i, 1);
                    curHP = dmg+(int)(-0.5 - (double) dmg * i / 19);
                }
            }
        }
        return dmgFrames;
    }
    public void  endCombat(){
        Unit ally = (defender.getColor().equals("red")?attacker:defender);
        Unit enemy = (defender.getColor().equals("red")?defender:attacker);
        boolean LVL_UP = ally.addXP(enemy, ally.equals(attacker)?turn.getFirst():turn.get(2), enemy.getHealth()<=0);
        FEApp.revertToGameScene();
        if (killedUnit != null) killedUnit.die();
    }

    @SuppressWarnings("SameParameterValue")
    private void ResizeBackground(int scaleFactor) {
        Image image = background.getImage();
        background = new ImageView(
                resample(
                        image,
                        scaleFactor
                )
        );
    }
    @SuppressWarnings("DuplicatedCode")
    private Image resample(Image input, int scaleFactor) {
        final int W = (int) input.getWidth();
        final int H = (int) input.getHeight();

        return getResampledImage(input, W, H, scaleFactor);
    }

    public CombatHandler getCombatHandler() {
        return combatHandler;
    }

    private class ShakingAnimation extends AnimationTimer {
        int frame = 19;
        Unit unit;
        HashMap<Integer,Integer> dmgFrames;
        double lastTime = System.nanoTime();
        int c = 0;
        HashMap<Integer, Integer> animations = new HashMap<>() {{
            put(0, 1);
            put(6, -1);
            put(8, -1);
            put(9, 1);
            put(11, 1);
            put(12, -1);
            put(13, -1);
            put(14, 1);
            put(15, 1);
            put(16, -1);
            put(17, -1);
            put(18, 2);
            put(19, -1);
        }};
        ShakingAnimation(HashMap<Integer, Integer> dmgFrames, Unit unit) {
            this.dmgFrames = dmgFrames;
            this.unit = unit;
        }

        @Override
        public void handle(long l) {
            if (System.nanoTime() - lastTime > (double) 1000000000/60) {
                if (animations.containsKey(c)) {
                    UpperPane.setTranslateX(UpperPane.getTranslateX() + (double) 9 * animations.get(c));
                    UpperPane.setTranslateY(UpperPane.getTranslateY() + (double) 9 * animations.get(c));
                    LowerPane.setTranslateX(LowerPane.getTranslateX() + (double) 9 * animations.get(c));
                    LowerPane.setTranslateY(LowerPane.getTranslateY() + (double) 9 * animations.get(c));
                }
                if (dmgFrames.containsKey(c)) {
                    try {
                        reduceHealth(unit,dmgFrames.get(c));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                lastTime = System.nanoTime();
                c++;
                if (c > frame) stop();
            }
        }
    }
}
