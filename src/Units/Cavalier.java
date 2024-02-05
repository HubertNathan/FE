package Units;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
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
        }});
        unitType = "KnightsA";
        load();
    }
    public void load() throws IOException {
        standingSprites = ImageIO.read(new File("Sprites/Cavalier/standingSprites.png"));
        selectSprites = ImageIO.read(new File("Sprites/Cavalier/movingSprites.png"));
    }
}
