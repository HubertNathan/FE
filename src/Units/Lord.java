package Units;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Lord extends Unit{
     Lord(String name){
         super();
         HP = 60;
         Str = 20;
         Mag = 0;
         Skl = 20;
         Spd = 20;
         Lck = 30;
         Def = 20;
         Res = 20;
         Mov = 15;
         Con = 20;
         this.name = name;
     }
     public void load() throws IOException {
         movingSprites = ImageIO.read(new File("/Sprites/Lord/movingSprites"));
     }

    @Override
    public void stand() {
        animState = (animState+1)%15;
        if (animState <12){
            animState = 12;

        }
    }

}
