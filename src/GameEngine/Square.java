package GameEngine;

import Terrains.*;
import Units.Unit;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public class Square {
    private static final Map<Integer, String > terrainNames = new HashMap<>(){
        {
            put(0, "Void");
            put(1, "Arena");
            put(2, "Bridge");
            put(3, "Cliff");
            put(4, "Deck");
            put(5, "Desert");
            put(6, "Door");
            put(7, "Fence");
            put(8, "Floor");
            put(9, "Forest");
            put(10, "Fort");
            put(11, "Gate");
            put(12, "House");
            put(13, "Lake");
            put(14, "Mountain");
            put(15, "Peak");
            put(16, "Pillar");
            put(17, "Plains");
            put(18, "River");
            put(19, "Road");
            put(20, "Ruins");
            put(21, "Ruins_Village");
            put(22, "Sand");
            put(23, "Sea");
            put(24, "Shop");
            put(25, "Snag");
            put(26, "Stairs");
            put(27, "Throne");
            put(28, "Village");
            put(29, "Wall");
            put(30, "Wall_Weak");
        }};
    BufferedImage originalTexture;
    BufferedImage texture;
    Terrain terrain = new VoidTile();
    Unit unit = null;
    boolean isReachable = false;
    public Square(){

    }
    public void drawReachableTile(Graphics2D g2, int i, int j, float scaleX, float scaleY){

    }

    public Square(BufferedImage image){
        this.originalTexture = image;
    }
    public void  setTerrainType(int terrainType){
        String terrainName = terrainNames.get(terrainType);
        switch (terrainName){
            case "Bridge" :
                this.terrain = new Bridge();
                break;
            case "Cliff":
                this.terrain = new Cliff();
                break;
            case "Forest":
                this.terrain = new Forest();
                break;
            case "Lake":
                this.terrain = new Lake();
                break;
            case "Mountain":
                this.terrain = new Mountain();
                break;
            case "Peak":
                this.terrain = new Peak();
                break;
            case "Plains":
                this.terrain = new Plains();
                break;
            case  "River":
                this.terrain = new River();
                break;
            case  "Wall":
                this.terrain = new Wall();
                break;
            default:
                this.terrain = new VoidTile();
        }
    }
    public Terrain getTerrainType(){
        return terrain;
    }
    public Unit getUnit() {
        return unit;
    }
    public void setUnit(Unit unit) {
        this.unit = unit;
    }
    public void setOriginalTexture(BufferedImage texture){
        this.originalTexture = texture;
    }
    public BufferedImage getOriginalTexture(){
        return originalTexture;
    }
    public Terrain getTerrain(){
        return terrain;
    }

    public void reach(boolean b) {
        isReachable = b;
    }
    public boolean isReachable(){
        return isReachable;
    }
}
