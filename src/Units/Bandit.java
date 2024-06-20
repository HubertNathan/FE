package Units;

import GUI.Animations.Portrait;
import GUI.ResizableImage;
import Items.Weapons.Axe;
import javafx.scene.image.ImageView;
import javafx.util.Pair;

import java.io.IOException;
import java.util.HashMap;

public class Bandit extends Unit{
    public Bandit(String name, int[] Stats, String colour) throws IOException {
        super(name,new HashMap<>(){{
            put("LVL", Stats[0]);
            put("HP", Stats[1]);
            put("Str", Stats[2]);
            put("Mag", Stats[3]);
            put("Skl", Stats[4]);
            put("Spd", Stats[5]);
            put("Lck", Stats[6]);
            put("Def", Stats[7]);
            put("Res", Stats[8]);
            put("Mov", Stats[9]);
            put("Con", Stats[10]);
        }},
        new Axe(Axe.IronAxe)
        );
        unitType = "Bandits";
        load();
        color = colour;
        skin = switch (name) {
            case "Zugu" -> "Zugu";
            default     -> "Brigand";
        };

    }
    public Bandit(String name) throws IOException {
        super(name, new HashMap<>() {{
            put("LVL", 1);
            put("HP", 20);
            put("Str", 5);
            put("Mag", 0);
            put("Skl", 1);
            put("Spd", 5);
            put("Lck", 0);
            put("Def", 3);
            put("Res", 0);
            put("Mov", 5);
            put("Con", 12);
        }}, new Axe(Axe.IronAxe));
        unitType = "Bandits";
        load();
        color = "blue";
        skin = switch (name) {
            case "Zugu" -> "Zugu";
            default     -> "Brigand";
        };
    }
    public Bandit(String name, String color) throws IOException {
        super(name, new HashMap<>() {{
            put("LVL", 1);
            put("HP", 20);
            put("Str", 5);
            put("Mag", 0);
            put("Skl", 1);
            put("Spd", 5);
            put("Lck", 0);
            put("Def", 3);
            put("Res", 0);
            put("Mov", 5);
            put("Con", 12);
        }}, new Axe(Axe.IronAxe));
        unitType = "Bandits";
        load();
        this.color = color;
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
    public String getResourceDirectory() {
        return "file:Resources/Sprites/Brigand/";
    }
}