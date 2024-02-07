package Units;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;

public class Lord extends Unit {
    Lord(String name) throws IOException {
        super(name, new HashMap<String, Integer>() {{
            put("LVL", 1);
            put("HP", 60);
            put("Str", 20);
            put("Mag", 0);
            put("Skl", 20);
            put("Spd", 20);
            put("Lck", 30);
            put("Def", 20);
            put("Res", 20);
            put("Mov", 3);
            put("Con", 20);
        }});
        this.name = name;
        this.unitType = "Foot";
    }

    public void load() throws IOException {
        standingSprites = ImageIO.read(new File("/Sprites/Lord/standingSprites"));
    }
    public Unit copy() throws IOException {
        return new Lord(name);
    }
}