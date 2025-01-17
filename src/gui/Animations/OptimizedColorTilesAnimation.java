package gui.Animations;

import core.Square;
import core.game_engine.Battle.BattleEngine;
import javafx.animation.AnimationTimer;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

import java.util.List;

import static core.game_engine.Battle.BattleEngine.tileSize;

public class OptimizedColorTilesAnimation {
    public final Canvas animationCanvas = new Canvas(BattleEngine.getBoard().getWidth()*tileSize,BattleEngine.getBoard().getHeight()*tileSize);
    private final GraphicsContext gc = animationCanvas.getGraphicsContext2D();
    private int animationFrame = 0;
    private final List<Square> animatedSquares = BattleEngine.getBoard().squareAsList().stream()
            .filter(square -> square.isReachable() || square.inRange())
            .toList();
    private final Image tiles = new Image("file:Resources/MenuSprites/ColorSquares.png", 8 * 16 * 3, 4 * 16 * 3, false, false);
    private AnimationTimer timer;
    private void updateAnimation() {
        for (Square square : animatedSquares) {
            gc.clearRect(square.getXValue() * tileSize, square.getYValue() * tileSize, tileSize, tileSize);

            int offset = square.inRange() && !square.isReachable() ? 2 * 48 : 0;
            int frameX = (animationFrame % 8) * 48;
            int frameY = offset + ((animationFrame / 8) % 2) * 48;

            gc.drawImage(tiles,
                    frameX, frameY, tileSize, tileSize, // Source region
                    square.getXValue() * tileSize, square.getYValue() * tileSize, // Destination position
                    tileSize, tileSize); // Size
        }
        animationFrame = (animationFrame + 1) % 16;
    }
    public void start() {
        gc.setGlobalAlpha(.75);
        timer = new AnimationTimer() {
            int i = 0;
            long time = System.nanoTime();

            @Override
            public void handle(long now) {
                if (now - time > 1_000_000_000./60*4) {
                    System.out.println((now - time) + "ms ~ "+1000000000./(now - time) + "fps");
                    updateAnimation();
                    i = (i + 1) % (16);
                    time = now;
                }
            }
        };
        timer.start();
    }
    public void stop() {
        timer.stop();
        gc.clearRect(0, 0, animationCanvas.getWidth(), animationCanvas.getHeight());
    }

    public Canvas getAnimationCanvas() {
        return animationCanvas;
    }
}