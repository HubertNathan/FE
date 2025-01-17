package gui;


import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;

import static core.game_engine.Battle.BattleEngine.tileSize;

public class SpriteSheet {
    private static Image spriteSheet;
    private static final int originalSize = 16;

    public static void loadSheet(String filePath){
        SpriteSheet.spriteSheet = new Image(filePath);
    }
    public static Image getTile(int x, int y){
        //return new WritableImage(spriteSheet.getPixelReader(), x*originalSize,y*originalSize, originalSize,originalSize);
        return scaling(new WritableImage(spriteSheet.getPixelReader(), x*originalSize,y*originalSize, originalSize,originalSize));
    }

    private static WritableImage scaling(WritableImage originalTile){
        WritableImage resizedImage = new WritableImage(tileSize, tileSize);
        PixelReader reader = originalTile.getPixelReader();
        PixelWriter writer = resizedImage.getPixelWriter();
        for (int y = 0; y < tileSize; y++) {
            for (int x = 0; x < tileSize; x++) {
                // Map the coordinates to the original image's coordinates
                int origX = x * originalSize / tileSize;
                int origY = y * originalSize / tileSize;
                writer.setArgb(x, y, reader.getArgb(origX, origY));
            }
        }
        return resizedImage;
    }
}
