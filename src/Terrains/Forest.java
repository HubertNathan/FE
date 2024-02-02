package Terrains;

import java.util.HashMap;

public class Forest extends Terrain {
    public Forest(){
        super(new HashMap<>(){{
            put("Foot",2);
            put("Armors",2);
            put("KnightsA",3);
            put("KnightsB",3);
            put("Fighters",2);
            put("Bandits",2);
            put("Pirates",2);
            put("Mages",2);
            put("Fliers",2);
            put("Manaketes",2);
            put("Nomads",2);
            put("Nomad_Troopers",2);

        }});
        id = 9;
        name = "Forest";
        def = 1;
        avoid = 20;
        heal = 0;

    }
}