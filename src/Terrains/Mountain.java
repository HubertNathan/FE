package Terrains;

import java.util.HashMap;

public class Mountain extends Terrain {

    public Mountain() {
        super(new HashMap<>() {{
            put("Foot", 4);
            put("Armors", -1);
            put("KnightsA", -1);
            put("KnightsB", 6);
            put("Fighters", 3);
            put("Bandits", 3);
            put("Pirates", 3);
            put("Mages", 4);
            put("Fliers", 1);
            put("Manaketes", 2);
            put("Nomads", -1);
            put("Nomad_Troopers", 5);

        }});
        id = 14;
        name = "Mntn";
        def = 1;
        avoid = 30;
        heal = 0;
    }
}
