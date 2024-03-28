package Units;

import Weapon.Weapon;

import java.io.IOException;
import java.util.HashMap;

public abstract class Lord extends Unit {
    Lord(String name, Weapon weapon) throws IOException {
        super(name, new HashMap<String, Integer>() {{
            put("LVL", 1);
            put("HP", 16);
            put("Str", 4);
            put("Mag", 0);
            put("Skl", 7);
            put("Spd", 9);
            put("Lck", 5);
            put("Def", 2);
            put("Res", 0);
            put("Mov", 5);
            put("Con", 5);
        }}, weapon);
        this.name = name;
        this.unitType = "Foot";
    }

    public void load() throws IOException {
        //standingSprites = ImageIO.read(new File("/Sprites/Lord/standingSprites"));
    }

    @Override
    public String getResourceDirectory() {
        return "file:Resources/Sprites/Lord/";
    }
}