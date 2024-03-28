package Units;

import GUI.ResizableImage;
import Weapon.Axe;

import java.io.IOException;
import java.util.HashMap;

public class Brigand extends Unit{
    public Brigand(String name) throws IOException {
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
        color = "red";
        skin = switch (name) {
            default     -> "Brigand";
        };
    }
    @Override
    public void load() throws IOException {
        standingSprites = new ResizableImage("file:Resources/Sprites/Brigand/standingSprites.png",32,96);
        selectSprites = new ResizableImage("file:Resources/Sprites/Brigand/movingSprites.png",32,480);
    }

    @Override
    public String getResourceDirectory() {
        return "file:Resources/Sprites/Brigand/";
    }
}