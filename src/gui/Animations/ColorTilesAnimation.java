package gui.Animations;

import core.Square;
import core.game_engine.Battle.BattleEngine;
import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.util.List;

import static core.game_engine.Battle.BattleEngine.tileSize;

public class ColorTilesAnimation {
    private final Canvas animationCanvas = new Canvas(BattleEngine.getBoard().getWidth() * tileSize, BattleEngine.getBoard().getHeight() * tileSize);
    private final GraphicsContext gc = animationCanvas.getGraphicsContext2D();
    private final List<Square> animatedSquares = BattleEngine.getBoard().getSquaresInReach();
    private final Image tiles = new Image("file:Resources/MenuSprites/ColorSquares.png", 8 * tileSize, 4 * tileSize, false, false);

    private int animationFrame = 0;
    private AnimationTimer timer;
    private final int[][] frameCoordinates = new int[16][2];

    public ColorTilesAnimation() {
        precomputeFrameCoordinates();
    }

    private void precomputeFrameCoordinates() {
        for (int frame = 0; frame < 16; frame++) {
            int frameX = (frame % 8) * tileSize;
            int frameY = ((frame / 8) % 2) * tileSize;
            frameCoordinates[frame] = new int[]{frameX, frameY};
        }
    }

    private void updateAnimation() {
        for (Square square : animatedSquares) {
            gc.clearRect(square.getXValue() * tileSize, square.getYValue() * tileSize, tileSize, tileSize);
            int offset = square.inRange() && !square.isReachable() ? 2 * tileSize : 0;
            int[] frameCoord = frameCoordinates[animationFrame];
            int frameX = frameCoord[0];
            int frameY = frameCoord[1] + offset;

            gc.drawImage(tiles,
                    frameX, frameY, tileSize, tileSize,
                    square.getXValue() * tileSize, square.getYValue() * tileSize -1,
                    tileSize, tileSize);
        }

        animationFrame = (animationFrame + 1) % 16;
    }

    public void start() {
        animationCanvas.setTranslateY(1);
        gc.setGlobalAlpha(0.75);
        timer = new AnimationTimer() {
            long previousTime = 0;

            @Override
            public void handle(long now) {
                if (now - previousTime > 33_333_333) {
                    updateAnimation();
                    previousTime = now;
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