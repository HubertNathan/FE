import GUI.CombatRenderer;
import GUI.FireEmblemApp;
import GUI.ReadMapFile;
import GameEngine.Board;
import GameEngine.CombatAnimation;
import GameEngine.GameInterface;
import Units.Bandit;
import Units.Cavalier;
import Units.Lyn_Lord;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.*;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Board board = new Board(new ReadMapFile("CH1"));
        GameInterface gi = new GameInterface(board);
        Lyn_Lord lyn = new Lyn_Lord();
        Cavalier kent = new Cavalier("Kent");
        Bandit brg = new Bandit("Bandit");
        ImageView imv1 = new ImageView(), imv2 = new ImageView();
        CombatRenderer cb = new CombatRenderer(new FireEmblemApp(), stage,board,lyn,brg);


        stage.show();
    }
}