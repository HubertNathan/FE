package units;

import gui.Animations.Portrait;
import gui.ResizableImage;
import core.Inventory;
import Items.Weapons.Lance;
import Items.Weapons.Sword;
import javafx.scene.image.ImageView;
import javafx.util.Pair;

import java.io.IOException;
import java.util.HashMap;

public class Cavalier extends Unit {
    public Cavalier(String name, String color) throws IOException {
        super(
                name,
                color,
                new short[] { 1,20,6,0,6,7,2,5,1,9,25},
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
        Sprites = new HashMap<>(){{
        put("blue", new Pair<>(new ResizableImage("file:Resources/Sprites/Cavalier/standingSprites_BLUE.png", 32, 96), new ResizableImage("file:Resources/Sprites/Cavalier/movingSprites_BLUE.png", 32, 480)));
        put("red", new Pair<>(new ResizableImage("file:Resources/Sprites/Cavalier/standingSprites_RED.png", 32, 96), new ResizableImage("file:Resources/Sprites/Cavalier/movingSprites_RED.png", 32, 480)));
        put("green", new Pair<>(new ResizableImage("file:Resources/Sprites/Cavalier/standingSprites_GREEN.png", 32, 96), new ResizableImage("file:Resources/Sprites/Cavalier/movingSprites_GREEN.png", 32, 480)));
        put("gray", new Pair<>(new ResizableImage("file:Resources/Sprites/Cavalier/standingSprites_GRAY.png", 32, 96), new ResizableImage("file:Resources/Sprites/Cavalier/movingSprites_GRAY.png", 32, 480)));
        }};
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
    public String getBaseResourceDirectory() {
        return "file:Resources/Sprites/Cavalier/";
    }

}
