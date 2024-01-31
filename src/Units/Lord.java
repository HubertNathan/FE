package Units;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class Lord extends Unit{
     Lord(String name){
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
}
