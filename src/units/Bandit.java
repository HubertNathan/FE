package units;

import gui.Animations.Portrait;
import gui.ResizableImage;
import Items.Weapons.Axe;
import javafx.scene.image.ImageView;
import javafx.util.Pair;

import java.io.IOException;
import java.util.HashMap;

public class Bandit extends Unit{
    public Bandit(String name, String colour) throws IOException {
        super(
                (name.isBlank())?"Bandit":name,
                colour, new short[] { 1,20,5,0,1,5,0,3,0,5,12},
                new Axe(Axe.IronAxe)
        );
        unitType = "Bandits";
        load();
        skin = switch (name) {
            case "Zugu" -> "Zugu";
            default     -> "Brigand";
        };
    }
    @Override
    public void load(){
        Sprites = new HashMap<>(){{
            put("blue", new Pair<>(new ResizableImage("file:Resources/Sprites/Brigand/standingSprites_BLUE.png", 32, 96), new ResizableImage("file:Resources/Sprites/Brigand/movingSprites_BLUE.png", 32, 480)));
            put("red", new Pair<>(new ResizableImage("file:Resources/Sprites/Brigand/standingSprites_RED.png", 32, 96), new ResizableImage("file:Resources/Sprites/Brigand/movingSprites_RED.png", 32, 480)));
            put("green", new Pair<>(new ResizableImage("file:Resources/Sprites/Brigand/standingSprites_GREEN.png", 32, 96), new ResizableImage("file:Resources/Sprites/Brigand/movingSprites_GREEN.png", 32, 480)));
            put("gray", new Pair<>(new ResizableImage("file:Resources/Sprites/Brigand/standingSprites_GRAY.png", 32, 96), new ResizableImage("file:Resources/Sprites/Brigand/movingSprites_GRAY.png", 32, 480)));
        }};
    }

    @Override
    public ImageView getPortrait() {
        if (skin.equals("Zugu") )return Portrait.getPortrait(Portrait.ZUGU);
        return null;
    }

    @Override
    public String getBaseResourceDirectory() {
        return "file:Resources/Sprites/Brigand/";
    }
}