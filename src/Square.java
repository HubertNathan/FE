import Units.Unit;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Square {
    BufferedImage terrainTexture;
    BufferedImage texture;
    int terrainType = 0;
    Unit unit = null;

    public Square(){

    }
    public BufferedImage resizeImage(int newH, int newW) {
        Image tmp = texture.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
        BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = dimg.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();

        return dimg;
    }
    public void draw(Graphics2D g2, int i, int j, float scaleX, float scaleY){
        texture = terrainTexture;
        if (scaleX != (float) 16 && scaleY != (float) 16 && (int) scaleX != 0 && (int) scaleY != 0){
            texture = resizeImage((int) (scaleY),(int) (scaleX));
        }
        g2.drawImage(texture, (int) scaleX * j, (int) scaleY * i,null);
    }

    public Square(BufferedImage image){
        this.terrainTexture = image;
    }
    public void setTerrain(BufferedImage terrain){
        this.terrainTexture = terrain;
    }
    public void  setTerrainType(int terrainType){
        this.terrainType = terrainType;
    }
    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    public BufferedImage getTerrain(){
        return terrainTexture;
    }
    public Unit getUnit() {
        return unit;
    }
}
