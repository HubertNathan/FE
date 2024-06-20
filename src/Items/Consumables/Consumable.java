package Items.Consumables;

import Items.Item;
import javafx.scene.image.Image;

import java.io.IOException;

public abstract class Consumable implements Item {
    protected int uses = 1;
    protected String name;
    protected int price;
    @Override
    public abstract void loadIcon() throws IOException;

    @Override
    public void use() {
        uses--;
    }

    @Override
    public String getType() {
        return "Consumable";
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getUses() {
        return uses;
    }

    @Override
    public abstract Image getIcon();

    @Override
    public int getPrice() {
        return price;
    }
    public abstract void effect();
}
