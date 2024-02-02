package Terrains;

import java.util.Map;

public abstract class Terrain {
    int id = 17;
    String name = "Plains";
    int def;
    int avoid;
    int heal;
    Map<String,Integer> mov;
    Terrain(Map<String,Integer> mov){
        this.mov = mov;
    }
    public int getMovPenalty(String unitType) {
        return mov.get(unitType);
    }
    public int getDef() {
        return def;
    }
    public int getAvoid() {
        return avoid;
    }
    public int getHeal(){
        return heal;
    }
    @Override
    public String toString() {
        return name;
    }

    public static void main(String[] args) {
    }
}
