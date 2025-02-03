package units;

import gui.Animations.Portrait;
import core.Inventory;
import Items.Weapons.Lance;
import Items.Weapons.Sword;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.IOException;

public class Cavalier extends Unit {
    protected static Image standingSprites, movingSprites;
    public Cavalier(String name, String color) throws IOException {
        super(
                name,
                color,
                new byte[] { 1,20,6,0,6,7,2,5,1,9,25},
                new Inventory(new Sword(Sword.IronSword), new Lance(Lance.IronLance))
        );
        unitType = "KnightsA";
        load();
        skin = switch (name){
            case "Kent" -> "Kent";
            case "Sain" -> "Sain";
            default -> "Cavalier";
        };
    }
    public void load() {
        standingSprites = new Image("file:Resources/Sprites/Cavalier/standingSprites.png",32*4*3,96*3,false,false);
        movingSprites = new Image("file:Resources/Sprites/Cavalier/movingSprites.png",32*4*3,480*3,false,false);
    }

    @Override
    public ImageView getPortrait() {
        if (name.equals("Kent")){
            return Portrait.getPortrait(Portrait.KENT);
        } else if (name.equals("Sain")) {
            return Portrait.getPortrait(Portrait.SAIN);
        }
        return null;
    }
    @Override
    public Image getSprites() {
        if (mode.equals(STANDING)){
            return  standingSprites;
        }
        return movingSprites;
    }
    @Override
    public String getBaseResourceDirectory() {
        return "file:Resources/Sprites/Cavalier/";
    }

}
