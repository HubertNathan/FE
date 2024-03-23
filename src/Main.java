import GameEngine.CombatAnimationHandler;
import Units.Lyn_Lord;
import Weapon.Axe;
import Weapon.Lance;
import Weapon.Sword;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
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
        ImageView imv = new ImageView();
        imv.setFitWidth(3*248);
        imv.setFitHeight(3*160);
        new CombatAnimationHandler(lyn,"*Melee Critical*").play(imv);
        Pane pane = new Pane();
        pane.getChildren().add(imv);
        Scene scene = new Scene(pane);
        scene.setFill(Color.BLACK);
        stage.setScene(scene);

        stage.show();
    }
}