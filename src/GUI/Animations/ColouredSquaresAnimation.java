package GUI.Animations;
import GameEngine.Board;
import GameEngine.Square;
import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.ParallelTransition;
import javafx.animation.Transition;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import java.util.HashMap;

public class ColouredSquaresAnimation {
    Board board;
    Pane animationPane;
    ParallelTransition parallelTransition;;
    final HashMap<Integer, Image> colouredSquares = new HashMap<>(){{
        for (int i = 1; i < 33; i++) {
            put(i, new Image(getClass().getResource("ColorSquares/" + i + ".png").toExternalForm(), 96, 96, false, false));
        }

    }};
    public ColouredSquaresAnimation(Board board, Pane animationPane){
        parallelTransition = new ParallelTransition();
        parallelTransition.setCycleCount(Animation.INDEFINITE);
        this.board = board;
        this.animationPane = animationPane;
    }
    public void play(double width, double height){
        board.squareAsList().forEach(square -> {
            if (square.isReachable() || square.inRange()) {
                ImageView imv = new ImageViewer(colouredSquares.get(1),square);
                imv.setOpacity(.65);
                imv.setFitWidth(width);
                imv.setFitHeight(height);
                imv.setTranslateX(width*square.getXValue());
                imv.setTranslateY(height*square.getYValue());
                animationPane.getChildren().add(imv);

                parallelTransition.getChildren().add(new Transition() {
                    final int offset;
                    {
                     offset = square.inRange()?16:0;
                    setCycleDuration(Duration.seconds(1.066666666667));
                    setInterpolator(Interpolator.LINEAR);
                }
                    @Override
                    protected void interpolate(double k) {
                        imv.setImage(colouredSquares.get(Math.min((int)(k*16)+offset+1,16+offset)));
                    }
                });
            }
        });
        parallelTransition.play();
    }
    public void stop(){
        parallelTransition.stop();
        animationPane.getChildren().clear();
        parallelTransition.getChildren().clear();
    }
}
