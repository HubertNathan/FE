package Terrains;

import java.util.HashMap;

public class Wall_Weak extends Terrain {
    public Wall_Weak(){
        super(new HashMap<>(){{
            put("Foot",-1);
            put("Armors",-1);
            put("KnightsA",-1);
            put("KnightsB",-1);
            put("Fighters",-1);
            put("Bandits",-1);
            put("Pirates",-1);
            put("Mages",-1);
            put("Fliers",-1);
            put("Manaketes",-1);
            put("Nomads",-1);
            put("Nomad_Troopers",-1);

        }});
        id = 30;
        name = "Wall?";
        def = 0;
        avoid = 0;
        heal = 0;
    }
}
