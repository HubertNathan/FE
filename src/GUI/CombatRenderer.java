package GUI;

import GameEngine.Board;
import GameEngine.CombatHandler;
import GameEngine.TextInterpreter;
import Units.Unit;
import javafx.animation.Transition;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.*;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.List;

public class CombatRenderer {
    final Pane root = new Pane();
    Scene combatScene;
    ImageView background;
    final Pane textPane = new Pane();
    final CombatHandler combatHandler;
    final TextInterpreter TI = new TextInterpreter();
    public CombatRenderer(FireEmblemApp app,Stage window, Board board, Scene scene, Unit attacker, Unit defender) throws IOException {
        combatHandler = new CombatHandler(attacker,defender, board);
        load(window, scene, board);
        addUnitNames(attacker,defender);
        addNumbers();
        addWeaponIcons(attacker,defender);
        Transition t = new Transition() {
            {
                setCycleDuration(Duration.seconds(10));
                setOnFinished(e -> app.revertToGameScene());
            }
            @Override
            protected void interpolate(double v) {

            }
        };
        t.play();
    }

    @SuppressWarnings("DuplicatedCode")
    private void addUnitNames(Unit attacker, Unit defender) throws IOException {
        root.getChildren().add(textPane);
        int dx1, dx2;
        if (attacker.getColor().equals("blue")){
            dx1 = 193*3;
            dx2 = 0;
        }
        else {
            dx1 = 0;
            dx2 = 193*3;
        }
        textPane.getChildren().add(new ImageView(TI.convertTxt("white",attacker.toString())){
            {
                setFitWidth(getImage().getWidth()/2);
                setFitHeight(getImage().getHeight()/2);
                setTranslateX((dx1 + (48 * 3 - getFitWidth())/2));
                setTranslateY(6*3+(21*3-getFitHeight())/2);
            }
        });
        textPane.getChildren().add(new ImageView(TI.convertTxt("white",defender.toString())){
            {
                setFitWidth(getImage().getWidth()/2);
                setFitHeight(getImage().getHeight()/2);
                setTranslateX((dx2+(48 * 3 - getFitWidth())/2));
                setTranslateY(6*3+(21*3-getFitHeight())/2);
            }
        });
    }
    @SuppressWarnings("IntegerDivisionInFloatingPointContext")
    private void addNumbers() throws IOException {
        List<Integer> Numbers = combatHandler.getCalculations();
        for (int i = 0; i < 6; i++) {
            TextInterpreter TI = new TextInterpreter();
            Image img = TI.convertBattleNumbers(Numbers.get(i)  );
            ImageView imv = new ImageView(img);
            imv.setFitHeight(imv.getImage().getHeight()/2);
            imv.setFitWidth(imv.getImage().getWidth()/2);
            imv.setTranslateX(39*3+(i/3)*200*3-imv.getFitWidth());
            imv.setTranslateY(112*3+(i%3)*8*3);
            textPane.getChildren().add(imv);
        }
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
        ImageView aw_name_imv = new ImageView(TI.convertTxt("white",attacker.getWieldedWeapon().getName()));
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
            dw_name_imv.setTranslateX(60*3+57 - dw_name_imv.getFitWidth()/3);
            dw_name_imv.setTranslateY(124*3);
        }
        else {
            a_imv.setTranslateX(44*3);
            a_imv.setTranslateY(122*3);
            d_imv.setTranslateX(123*3);
            d_imv.setTranslateY(122*3);
        }
        textPane.getChildren().addAll(a_imv,d_imv,aw_name_imv,dw_name_imv);


    }

    private void load(Stage window, Scene scene, Board board){
        loadBackground(board);
        root.getChildren().add(new Pane(){{getChildren().add(new Rectangle(window.getWidth(),window.getHeight()-37){{setFill(Color.color(0,0,0,.2));}});}});
        window.setScene(new Scene(root));
        Image battleScene = new Image("file:Resources/MenuSprites/Battle_Scene.png",window.getWidth(),(window.getHeight()-37),false,false);
        root.getChildren().add(new ImageView(battleScene));
    }
    private void loadBackground(Board board) {
        background = new ImageView(board.drawFX(new Canvas(16 * board.getWidth(), 16 * board.getHeight())).snapshot(null, new WritableImage(16 * board.getWidth(), 16 * board.getHeight())));
        ResizeBackground(6);
        background.setFitWidth(48 * board.getWidth());
        background.setFitHeight(48 * board.getHeight());
        root.getChildren().add(background);
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
