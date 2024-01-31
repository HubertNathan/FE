import java.awt.*;
import java.awt.image.BufferedImage;

public class Square {
    BufferedImage image;
    int terrainType = 0;

    public Square(){

    }
    public BufferedImage resize(int newH, int newW) {
        Image tmp = image.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
        BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = dimg.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();

        return dimg;
    }

    public Square(BufferedImage image){
        this.image = image;
    }
    public void setTerrain(BufferedImage terrain){
        this.image = terrain;
    }
    public void  setTerrainType(int terrainType){
        this.terrainType = terrainType;
    }
    public BufferedImage getTerrain(){
        return image;
    }
}
