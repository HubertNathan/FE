package Units;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class Unit extends JPanel {
    int HP;
    int Str;
    int Mag;
    int Skl;
    int Spd;
    int Lck;
    int Def;
    int Res;
    int Mov;
    int Con;
    int LVL;
    int animState;
    String name;
    BufferedImage movingSprites;

    Unit() {
        animState = 0;
        LVL = 0;
    }

    abstract public void stand();
    public BufferedImage resizeImage(int newH, int newW) {
        Image tmp = getSprite().getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
        BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = dimg.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();

        return dimg;
    }
    public BufferedImage getSprite(){
        return movingSprites.getSubimage(0,animState *32,32,32);
    }
    public void draw(Graphics2D g,int i, int j,float scaleX, float scaleY) {
        BufferedImage sprite = getSprite();
        if (scaleX != (float) 16 && scaleY != (float) 16 && (int) scaleX != 0 && (int) scaleY != 0){
            sprite = resizeImage((int) (2 * scaleY), (int) (2 * scaleX));
        }
        g.drawImage(sprite, (int) (scaleX * j - scaleX / 2), (int) (scaleY * i - scaleY), null);
        stand();
    }

}
