package Units;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class Brigand extends Unit{
    public Brigand(String name) throws IOException {
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
            put("Mov", 5);
            put("Con", 20);
        }});
        unitType = "Bandits";
        load();
    }

    @Override
    public BufferedImage getSprite(String mode) {
        if (mode.equals("standing")) {
            return standingSprites.getSubimage(0, animState * 32, 16, 32);
        }
        else if (mode.equals("select")){
            return selectSprites.getSubimage(0, (12 + animState) * 32, 32, 32);
        }
        return standingSprites.getSubimage(0,16,16,16);
    }

    @Override
    public void draw(Graphics2D g, int i, int j, float scaleX, float scaleY, String mode) {
        if (mode.equals("standing")) {
            BufferedImage sprite = getSprite(mode);
            if (scaleX != (float) 16 && scaleY != (float) 16 && (int) scaleX != 0 && (int) scaleY != 0) {
                sprite = resizeImage((int) (2 * scaleY), (int) (scaleX), mode);
            }
            g.drawImage(sprite, (int) (scaleX) * j, (int) (scaleY) * (i - 1), null);
            animation();
        }
        else if (mode.equals("select")) {
            BufferedImage sprite = getSprite(mode);
            if (scaleX != (float) 16 && scaleY != (float) 16 && (int) scaleX != 0 && (int) scaleY != 0) {
                sprite = resizeImage((int) (scaleY) * 2, (int) (scaleX) * 2, mode);
            }
            g.drawImage(sprite, (int) (scaleX) * j - (int) (scaleX)/2, (int) (scaleY) * (i - 1) +(int) (scaleY)/16, null);
            animation();
            animation();
        }
    }
    public void load() throws IOException {
        standingSprites = ImageIO.read(new File("Sprites/Brigand/standingSprites.png"));
        selectSprites = ImageIO.read(new File("Sprites/Brigand/movingSprites.png"));
    }
}