package Units;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class Lyn_Lord extends Lord{
    public Lyn_Lord() throws IOException {
        super("Lyn");
        load();
    }
    public void load() throws IOException {
        standingSprites = ImageIO.read(new File("Sprites/Lord/Lyn/standingSprites.png"));
        selectSprites = ImageIO.read(new File("Sprites/Lord/Lyn/movingSprites.png"));
    }
}

