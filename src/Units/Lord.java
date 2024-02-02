package Units;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;

public class Lord extends Unit {
    Lord(String name) {
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
            put("Mov", 10);
            put("Con", 20);
        }});
        this.name = name;
        this.unitType = "Foot";
    }

    public void load() throws IOException {
        standingSprites = ImageIO.read(new File("/Sprites/Lord/standingSprites"));
    }

    public BufferedImage getSprite(String mode) {
        if (mode.equals("standing")) {
            return standingSprites.getSubimage(0, animState * 16, 16, 16);
        }
        if (mode.equals("select")) {
            return selectSprites.getSubimage(0, (12 + animState) * 32, 32, 32);
        }
        return standingSprites.getSubimage(0, 16, 16, 16);
    }



    public void draw(Graphics2D g, int i, int j, float scaleX, float scaleY, String mode) {
        if (mode.equals("standing")) {
            BufferedImage sprite = getSprite(mode);
            if (scaleX != (float) 16 && scaleY != (float) 16 && (int) scaleX != 0 && (int) scaleY != 0) {
                sprite = resizeImage((int) (scaleY), (int) (scaleX), mode);
            }
            g.drawImage(sprite, (int) (scaleX) * j, (int) (scaleY) * i, null);
            animation();
        }
        else if (mode.equals("select")){
            BufferedImage sprite = getSprite(mode);
            if (scaleX != (float) 16 && scaleY != (float) 16 && (int) scaleX != 0 && (int) scaleY != 0) {
                sprite = resizeImage((int) (scaleY) * 2, (int) (scaleX) * 2, mode);
            }
            g.drawImage(sprite, (int) (scaleX) * j - (int) (scaleX)/2, (int) (scaleY) * i - (int) (scaleY) +(int) (scaleY)/16, null);
            animation();
        }
    }
}
