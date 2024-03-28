package Units;


import GUI.ResizableImage;
import Weapon.Lance;
import Weapon.Sword;
import javafx.scene.image.ImageView;

import java.io.IOException;

public class Lyn_Lord extends Lord{
    public Lyn_Lord() throws IOException {
        super("Lyn", new Sword(Sword.IronSword));
        load();
        skin = "/Lyn";
    }
    public void load() throws IOException {
        standingSprites = new ResizableImage("file:Resources/Sprites/Lord/Lyn/standingSprites.png",32,96);
        selectSprites = new ResizableImage ("file:Resources/Sprites/Lord/Lyn/movingSprites.png",32,480);
    }

    public ImageView getSprite(int animFrame) throws IOException {
        int i = animation(animFrame,false,false);
        int scale = (int)standingSprites.getWidth()/16;
        ImageView imv = new ImageView(standingSprites.getSubimage(0,scale*16*i,scale*16,scale*16));
        imv.setFitWidth(scale*16);
        imv.setFitHeight(scale*16);
        return imv;
    }
}

