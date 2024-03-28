import GameEngine.CombatAnimation;
import Units.Brigand;
import Units.Lyn_Lord;
import javafx.animation.ParallelTransition;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Lyn_Lord lyn = new Lyn_Lord();
        Brigand brg = new Brigand("Brigand");
        ImageView imv = new ImageView();
        ImageView imv1 = new ImageView();
        imv.setFitWidth(3*240);
        imv.setFitHeight(3*160);
        imv1.setFitWidth(3*240);
        imv1.setFitHeight(3*160);
        CombatAnimation animation = new CombatAnimation(lyn,CombatAnimation.MISS);
        CombatAnimation animation1 = new CombatAnimation(brg,CombatAnimation.DODGE_MELEE,animation.getHitFrame(),animation.getFrameNumber());

        animation.play(imv1);
        animation1.play(imv);
        //System.out.println(animation.getCombatAnimations());
        //System.out.println(animation.getFrameNumber());
        System.out.println(animation1.getCombatAnimations());
        Pane pane = new Pane();
        pane.getChildren().addAll(imv,imv1);
        Scene scene = new Scene(pane);
        scene.setFill(Color.BLACK);
        stage.setScene(scene);

        stage.show();
    }
}