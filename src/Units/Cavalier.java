package Units;

import GUI.Animations.Portrait;
import GUI.ResizableImage;
import GameEngine.Inventory;
import Items.Weapons.Lance;
import Items.Weapons.Sword;
import javafx.scene.image.ImageView;
import javafx.util.Pair;

import java.io.IOException;
import java.util.HashMap;

public class Cavalier extends Unit {
    public Cavalier(String name) throws IOException {
        super(name, new HashMap<>() {{
            put("LVL", 1);
            put("HP", 20);
            put("Str", 6);
            put("Mag", 0);
            put("Skl", 6);
            put("Spd", 7);
            put("Lck", 2);
            put("Def", 5);
            put("Res", 1);
            put("Con", 9);
            put("Mov", 7);
        }}, new Inventory(new Sword(Sword.IronSword), new Lance(Lance.IronLance)));
        unitType = "KnightsA";
        load();
        skin = switch (name){
            case "Kent" -> "Kent";
            case "Sain" -> "Sain";
            default -> "Cavalier";
        };
    }
    public Cavalier(String name, int[] Stats) throws IOException {
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
        }}, new Inventory(new Sword(Sword.IronSword), new Lance(Lance.IronLance)));
        unitType = "KnightsA";
        load();
        skin = switch (name){
            case "Kent" -> "Kent";
            case "Sain" -> "Sain";
            default -> "Cavalier";
        };
    }
    public Cavalier(String name, String colour) throws IOException {
        super(name, new HashMap<>() {{
            put("LVL", 1);
            put("HP", 20);
            put("Str", 6);
            put("Mag", 0);
            put("Skl", 6);
            put("Spd", 7);
            put("Lck", 2);
            put("Def", 5);
            put("Res", 1);
            put("Con", 9);
            put("Mov", 7);
        }}, new Inventory(new Sword(Sword.IronSword), new Lance(Lance.IronLance)));
        unitType = "KnightsA";
        load();
        skin = switch (name){
            case "Kent" -> "Kent";
            case "Sain" -> "Sain";
            default -> "Cavalier";
        };
        this.color = colour;
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
    public String getResourceDirectory() {
        return "file:Resources/Sprites/Cavalier/";
    }

}
