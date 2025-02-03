package units;


import gui.Animations.Portrait;
import core.Inventory;
import Items.Weapons.Sword;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.IOException;

public class Lyn_Lord extends Lord{
    protected static Image standingSprites, movingSprites;
    public Lyn_Lord() throws IOException {
        super("Lyn", new Inventory(new Sword(Sword.IronSword){{uses = 39;}}));
        load();
        skin = "/Lyn";
    }
    public Lyn_Lord(String name, String colour) throws IOException {
        super(name, new Inventory(new Sword(Sword.IronSword){{uses = 39;}}));
        load();
        this.color = colour;
        skin = "/Lyn";
    }
    public void load(){
        standingSprites = new Image("file:Resources/Sprites/Lord/Lyn/standingSprites.png",32*4*3,96*3,false,false);
        movingSprites = new Image("file:Resources/Sprites/Lord/Lyn/movingSprites.png",32*4*3,480*3,false,false);
    }
    @Override
    public Image getSprites() {
        if (mode.equals(STANDING)){
            return  standingSprites;
        }
        return movingSprites;
    }
    @Override
    public ImageView getPortrait() {
        return Portrait.getPortrait(Portrait.LYN_NORMAL);
    }
}

