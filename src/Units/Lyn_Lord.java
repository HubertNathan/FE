package Units;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
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
    /*public void draw(Graphics2D g, int i, int j, float scaleX, float scaleY, int animFrame) {
        if (mode.equals("standing")) {
            int animStep = animation(animFrame);
            BufferedImage sprite = resizedStandingSprites.getSubimage(0, animStep * 16, 16, 16);
            g.drawImage(sprite, (int) (scaleX) * j, (int) (scaleY) * i, null);
        }
        else if (mode.equals("select")) {
            int animStep = animation((2*animFrame)%36);
            BufferedImage sprite = resizedStandingSprites.getSubimage(0, (12 + animStep) * 16, 16, 16);
            g.drawImage(sprite, (int) (scaleX) * j - (int) (scaleX)/2, (int) (scaleY) * (i - 1) +(int) (scaleY)/16,null);
        }
    }*/
    public void draw(Graphics2D g, int i, int j, int scaleX, int scaleY, int animFrame) {
        if (scaleX<=0 || scaleY<=0){return;}
        if (mode.equals("standing")) {
            int animStep = animation(animFrame, false);
            BufferedImage sprite = resizedStandingSprites.getSubimage(0, animStep * (int) scaleY, (int)scaleX, (int) scaleY);
            g.drawImage(sprite, (int) (scaleX) * j, (int) (scaleY) * (i)-(int)(scaleY)/16, null);
        }
        else  {
            super.draw(g,i,j,scaleX,scaleY,animFrame);
        }
    }
    public void resizeSprites(int newW, int  newH) {
        if(newH<=0 || newW<=0){return;}
        Image tmp1 = standingSprites.getScaledInstance(newW, 3*newH, Image.SCALE_SMOOTH);
        resizedStandingSprites= new BufferedImage(newW, 3*newH, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = resizedStandingSprites.createGraphics();
        g2d.drawImage(tmp1, 0, 0, null);
        g2d.dispose();
        Image tmp2 = selectSprites.getScaledInstance(2*newW, 15*2*newH, Image.SCALE_SMOOTH);
        resizedSelectSprites= new BufferedImage(2*newW, 15*2*newH, BufferedImage.TYPE_INT_ARGB);
        g2d = resizedSelectSprites.createGraphics();
        g2d.drawImage(tmp2, 0, 0, null);
        g2d.dispose();
    }
    public Unit copy() throws IOException {
        return new Lyn_Lord();
    }
}

