package GUI;

import GameEngine.Board;
import GameEngine.CombatAnimation;
import GameEngine.CombatHandler;
import GameEngine.TextInterpreter;
import Units.Unit;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public class CombatRenderer {
    final Pane root = new Pane();
    FireEmblemApp app;
    ImageView background;
    final Pane UpperPane = new Pane();
    final Pane LowerPane = new Pane(){{setTranslateY(109);}};
    final Unit attacker;
    final Unit defender;
    final CombatHandler combatHandler;
    final TextInterpreter TI = new TextInterpreter();
    public CombatRenderer(FireEmblemApp app,Stage window, Board board, Scene scene, Unit attacker, Unit defender) throws IOException {
        this.app = app;
        this.attacker = attacker;
        this.defender = defender;
        combatHandler = new CombatHandler(attacker,defender, board);
        load(window, scene, board);
        playCombatAnimation();
        //addWeaponIcons();
        //playCombatAnimation();
    }
    private void addWeaponIcons(Unit attacker, Unit defender) throws IOException {
        Image aw = attacker.getWieldedWeapon().getIcon();
        ImageView a_imv = new ImageView(aw);
        a_imv.setFitWidth(a_imv.getImage().getWidth()/2);
        a_imv.setFitHeight(a_imv.getImage().getHeight()/2);
        Image dw = defender.getWieldedWeapon().getIcon();
        ImageView d_imv = new ImageView(dw);
        d_imv.setFitWidth(d_imv.getImage().getWidth()/2);
        d_imv.setFitHeight(d_imv.getImage().getHeight()/2);
        ImageView aw_name_imv = new ImageView();
        aw_name_imv.setFitWidth(aw_name_imv.getImage().getWidth()/2);
        aw_name_imv.setFitHeight(aw_name_imv.getImage().getHeight()/2);
        ImageView dw_name_imv = new ImageView(TI.convertTxt("white",defender.getWieldedWeapon().getName()));
        dw_name_imv.setFitWidth(dw_name_imv.getImage().getWidth()/2);
        dw_name_imv.setFitHeight(dw_name_imv.getImage().getHeight()/2);

        if (attacker.getColor().equals("blue")) {
            a_imv.setTranslateX(123*3);
            a_imv.setTranslateY(122*3);
            d_imv.setTranslateX(44*3);
            d_imv.setTranslateY(122*3);
            aw_name_imv.setTranslateX(139*3+57 - aw_name_imv.getFitWidth()/3);
            aw_name_imv.setTranslateY(124*3);
        }
        else {
            a_imv.setTranslateX(44*3);
            a_imv.setTranslateY(122*3);
            d_imv.setTranslateX(123*3);
            d_imv.setTranslateY(122*3);
        }
        //textPane.getChildren().addAll(a_imv,d_imv,aw_name_imv,dw_name_imv);


    }

    private void load(Stage window, Scene scene, Board board) throws IOException {
        loadBackground(board);
        root.getChildren().add(new Pane(){{getChildren().add(new Rectangle(window.getWidth(),window.getHeight()-37){{setFill(Color.color(0,0,0,.2));}});}});
        Scene battleScene = new Scene(root);
        battleScene.setOnKeyPressed(new EventHandler<>() {
            @Override
            public void handle(KeyEvent e) {

                KeyCode c = e.getCode();
                System.out.println(c);
                if (c.equals(KeyCode.ESCAPE)){
                    app.revertToGameScene();
                }
            }
        });
        window.setScene(battleScene);
        addUpperUI(window);
        addLowerUI(window);
    }

    private void addLowerUI(Stage window) throws IOException {
        Canvas lowerCanvas = new Canvas(240*6,51*6);
        lowerCanvas.getGraphicsContext2D().drawImage(new Image("file:Resources/MenuSprites/Lower_Scene.png",240*6,51*6,false,false),0,0);
        List<Integer> Numbers = combatHandler.getCalculations();
        for (int i = 0; i < 6; i++) {
            TextInterpreter TI = new TextInterpreter();
            Image img = TI.convertBattleNumbers(Numbers.get(i));
            lowerCanvas.getGraphicsContext2D().drawImage(img,39*6+(i/3)*200*6-img.getWidth(),3*6+(i%3)*8*6);
        }
        lowerCanvas.getGraphicsContext2D().drawImage(attacker.getWieldedWeapon().getIcon(),123*6,12*6);
        lowerCanvas.getGraphicsContext2D().drawImage(defender.getWieldedWeapon().getIcon(),44*6,12*6);

        Image text = TI.convertTxt("white",attacker.getWieldedWeapon().getName());
        lowerCanvas.getGraphicsContext2D().drawImage(text,139*6+19*6 - text.getWidth()/3,14*6);

        text = TI.convertTxt("white",defender.getWieldedWeapon().getName());
        lowerCanvas.getGraphicsContext2D().drawImage(text,60*6+19*6 - text.getWidth()/3,14*6);

        ImageView lowerIMV = new ImageView(lowerCanvas.snapshot(new SnapshotParameters(){{setFill(Color.TRANSPARENT);}},null)){{
            setFitWidth(getImage().getWidth()/2);
            setFitHeight(getImage().getHeight()/2);
        }};
        LowerPane.getChildren().add(lowerIMV);
        LowerPane.setTranslateY(109*3);
        root.getChildren().add(LowerPane);
    }

    private void loadBackground(Board board) {
        background = new ImageView(board.drawFX(new Canvas(16 * board.getWidth(), 16 * board.getHeight())).snapshot(null, new WritableImage(16 * board.getWidth(), 16 * board.getHeight())));
        ResizeBackground(6);
        background.setFitWidth(48 * board.getWidth());
        background.setFitHeight(48 * board.getHeight());
        root.getChildren().add(background);
    }
    private void addUpperUI(Stage window) throws IOException {
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
        Image text = TI.convertTxt("white",attacker.toString());
        upperCanvas.getGraphicsContext2D().drawImage(text,dx1+(48 * 6 - text.getWidth())/2,6*6+(21*6-text.getHeight())/2);
        text = TI.convertTxt("white",defender.toString());
        upperCanvas.getGraphicsContext2D().drawImage(text,dx2+(48 * 6 - text.getWidth())/2,6*6+(21*6-text.getHeight())/2);
        ImageView upperIMV = new ImageView(upperCanvas.snapshot(new SnapshotParameters(){{setFill(Color.TRANSPARENT);}},null)){{
            setFitWidth(getImage().getWidth()/2);
            setFitHeight(getImage().getHeight()/2);
        }};
        UpperPane.getChildren().add(upperIMV);
        root.getChildren().add(UpperPane);

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
        List<Boolean> attack_turn = combatHandler.getTurn();
        System.out.println(attack_turn);
        CombatAnimation attack =new CombatAnimation(attacker,  attack_turn.getFirst()?(attack_turn.get(1)?CombatAnimation.MELEE_CRITICAL:CombatAnimation.MELEE_ATTACK):CombatAnimation.MISS);
        CombatAnimation defense = new CombatAnimation(defender,attack_turn.getFirst()?CombatAnimation.DAMAGE:CombatAnimation.DODGE_MELEE, attack.getHitFrame(),attack.getFrameNumber());
        System.out.println(defense.getCombatAnimations());
        attack.setOnFinished(e->{
            try {
                CombatAnimation attack2 =new CombatAnimation(defender,  attack_turn.get(2)?(attack_turn.getLast()?CombatAnimation.MELEE_CRITICAL:CombatAnimation.MELEE_ATTACK):CombatAnimation.MISS);
                CombatAnimation defense2 = new CombatAnimation(attacker,attack_turn.get(2)?CombatAnimation.DAMAGE:CombatAnimation.DODGE_MELEE, attack.getHitFrame(),attack.getFrameNumber());
                attack2.play(A_IMV);
                defense2.play(D_IMV);
                //attack2.setOnFinished(e2->app.revertToGameScene());
            } catch (FileNotFoundException ex) {
                throw new RuntimeException(ex);
            }
        });
        attack.play(A_IMV);
        defense.play(D_IMV);
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

        WritableImage output = new WritableImage(
                W * scaleFactor,
                H * scaleFactor
        );

        PixelReader reader = input.getPixelReader();
        PixelWriter writer = output.getPixelWriter();

        for (int y = 0; y < H; y++) {
            for (int x = 0; x < W; x++) {
                final int argb = reader.getArgb(x, y);
                for (int dy = 0; dy < scaleFactor; dy++) {
                    for (int dx = 0; dx < scaleFactor; dx++) {
                        writer.setArgb(x * scaleFactor + dx, y * scaleFactor + dy, argb);
                    }
                }
            }
        }
        return output;
    }
}
