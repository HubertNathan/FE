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
    private final Image tiles = new Image("file:Resources/MenuSprites/ColorSquares.png", 8 * 16, 4 * 16, false, false);

    private int animationFrame = 0;
    private AnimationTimer timer;
    private final int[][] frameCoordinates = new int[16][2];

    public ColorTilesAnimation() {
        gc.setImageSmoothing(false);
        precomputeFrameCoordinates();
    }

    private void precomputeFrameCoordinates() {
        for (int frame = 0; frame < 16; frame++) {
            int frameX = (frame % 8)*16;
            int frameY = ((frame / 8) % 2)*16;
            frameCoordinates[frame] = new int[]{frameX, frameY};
        }
    }

    private void initAnimation(byte i) {
        for (Square square : animatedSquares) {
            gc.clearRect(square.getXValue() * tileSize, square.getYValue() * tileSize, tileSize, tileSize);
            int offset = square.inRange() && !square.isReachable() ? 2 * 16 : 0;
            gc.drawImage(tiles,
                    0, offset, i+4, i+4,
                    square.getXValue() * tileSize, square.getYValue() * tileSize,
                    (i+4)*3, (i+4)*3);
        }
    }
    private void updateAnimation() {
        for (Square square : animatedSquares) {
            gc.clearRect(square.getXValue() * tileSize, square.getYValue() * tileSize, tileSize, tileSize);
            int offset = square.inRange() && !square.isReachable() ? 2 * 16 : 0;
            int frameX = frameCoordinates[animationFrame][0];
            int frameY = frameCoordinates[animationFrame][1] + offset;

            gc.drawImage(tiles,
                    frameX, frameY, 16, 16,
                    square.getXValue() * tileSize, square.getYValue() * tileSize,
                    tileSize, tileSize);
        }

        animationFrame = (animationFrame + 1) % 16;
    }

    public void start() {
        animationCanvas.setTranslateY(1);
        gc.setGlobalAlpha(0.75);
        timer = new AnimationTimer() {
            long previousTime = 0;
            byte i =0;

            @Override
            public void handle(long now) {
                if (now - previousTime > 33_333_333 && i>=12){
                    updateAnimation();
                    previousTime = now;
                }
                else if (now - previousTime > 4_166_666 && i<12) {
                    initAnimation(i);
                    i++;
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