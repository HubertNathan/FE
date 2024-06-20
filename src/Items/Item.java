package Items;

import javafx.scene.image.Image;

import java.io.IOException;

public interface Item {
    void loadIcon() throws IOException;
    void use();
    String getType();
    String getName();
    int getUses();
    Image getIcon();
    int getPrice();


}
