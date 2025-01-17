import core.Board;
import gui.PanelInterface.GameInterface;
import core.game_engine.save.LoadSaveBoard;
import units.Cavalier;
import units.Lyn_Lord;
import javafx.application.Application;
import javafx.scene.image.*;
import javafx.stage.Stage;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        //new LoadSaveBoard("CH1").loadBoard(null);
        //Board board = new Board();
        //GameInterface gi = new GameInterface(board);
        //Lyn_Lord lyn = new Lyn_Lord();
        //Cavalier kent = new Cavalier("Kent");
        //ImageView imv1 = new ImageView(), imv2 = new ImageView();
        //CombatRenderer cb = new CombatRenderer(new FireEmblemApp(), stage,board,lyn,brg);


        stage.show();
    }
}