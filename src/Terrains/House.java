package Terrains;

import java.util.HashMap;

public class House extends Terrain {
    public House(){
        super(new HashMap<>(){{
            put("Foot",1);
            put("Armors",1);
            put("KnightsA",1);
            put("KnightsB",1);
            put("Fighters",1);
            put("Bandits",1);
            put("Pirates",1);
            put("Mages",1);
            put("Fliers",1);
            put("Manaketes",1);
            put("Nomads",1);
            put("Nomad_Troopers",1);

        }});
        id = 12;
        name = "House";
        def = 0;
        avoid = 10;
        heal = 0;

    }
}

