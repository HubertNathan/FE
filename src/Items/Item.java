package Items;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.io.IOException;

public interface Item {
    Image icons = new Image("file:Resources/FE7Symbols.png", 472, 274, false, false);
    void loadIcon() throws IOException;
    void use();
    String getType();
    String getName();
    int getUses();
    void drawIcon(GraphicsContext g,double x, double y);
    int getPrice();


}
