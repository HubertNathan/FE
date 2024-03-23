package GUI.Animations;

import GameEngine.Square;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ImageViewer extends ImageView {
    final int x;
    final int y;
    ImageViewer(Image image, Square square){
        super(image);
        x = square.getXValue();
        y = square.getYValue();
    }

    public int getXValue() {
        return x;
    }
    public int getYValue() {
        return y;
    }
    
}
