import java.awt.*;
import java.awt.image.BufferedImage;

public class Square {
    BufferedImage image;
    int terrainType = 0;

    public Square(){

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
