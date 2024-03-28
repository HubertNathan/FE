package Units;

import GUI.ResizableImage;
import Weapon.Lance;

import java.io.IOException;
import java.util.HashMap;

public class Cavalier extends Unit {
    public Cavalier(String name) throws IOException {
        super(name, new HashMap<>() {{
            put("LVL", 1);
            put("HP", 60);
            put("Str", 20);
            put("Mag", 0);
            put("Skl", 20);
            put("Spd", 20);
            put("Lck", 30);
            put("Def", 20);
            put("Res", 20);
            put("Mov", 4);
            put("Con", 20);
        }}, new Lance(Lance.IronLance));
        unitType = "KnightsA";
        load();
        skin = switch (name){
            case "Kent" -> "Kent";
            case "Sain" -> "Sain";
            default -> "Cavalier";
        };
    }
    public void load() throws IOException {
        standingSprites = new ResizableImage("file:Resources/Sprites/Cavalier/standingSprites.png",32,96);
        selectSprites = new ResizableImage("file:Resources/Sprites/Cavalier/movingSprites.png",32,480);
    }

    @Override
    public String getResourceDirectory() {
        return "file:Resources/Sprites/Cavalier/";
    }
}
