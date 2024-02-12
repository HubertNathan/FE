package Units;


import GUI.ResizableImage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.util.Objects;

public class Lyn_Lord extends Lord{
    public Lyn_Lord() throws IOException {
        super("Lyn");
        load();
    }
    public void load() throws IOException {
        standingSprites = new ResizableImage("file:Sprites/Lord/Lyn/standingSprites.png",16,48);
        standingSprites = new ResizableImage("file:Sprites/Lord/Lyn/standingSprites.png",16,48);
        selectSprites = new ResizableImage ("file:Sprites/Lord/Lyn/movingSprites.png",32,480);
    }
    public ImageView getSprite(int animFrame) throws IOException {
        int i = animation(animFrame,false);
        int scale = (int)standingSprites.getWidth()/16;
        ImageView imv = new ImageView(standingSprites.getSubimage(0,scale*16*i,scale*16,scale*16));
        imv.setFitWidth(scale*16);
        imv.setFitHeight(scale*16);
        return imv;
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
    public Unit copy() throws IOException {
        return new Lyn_Lord();
    }
}

