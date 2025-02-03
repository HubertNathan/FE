package units;

import core.Inventory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.util.HashMap;

public abstract class Lord extends Unit {
    Lord(String name, Inventory inventory) throws IOException {
        super(
                name,
                "blue",
                new byte[] { 1, 16, 4, 0, 7, 9, 5, 2, 0, 5, 5},
                inventory);
        this.name = name;
        this.unitType = "Foot";
    }
    public abstract void load() throws IOException;
    @Override
    public abstract Image getSprites();
    @Override
    public abstract ImageView getPortrait();
    @Override
    public String getBaseResourceDirectory() {
        return "file:Resources/Sprites/Lord/";
    }
}