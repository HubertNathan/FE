package Terrains;

import java.util.HashMap;

public class Fort extends Terrain {
    public Fort(){
        super(new HashMap<>(){{
            put("Foot",2);
            put("Armors",2);
            put("KnightsA",2);
            put("KnightsB",2);
            put("Fighters",2);
            put("Bandits",2);
            put("Pirates",2);
            put("Mages",2);
            put("Fliers",2);
            put("Manaketes",2);
            put("Nomads",2);
            put("Nomad_Troopers",2);

        }});
        id = 10;
        name = "Fort";
        def = 2;
        avoid = 20;
        heal = 20;

    }
}

