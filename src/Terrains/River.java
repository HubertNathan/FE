package Terrains;
import java.util.HashMap;

public class River extends Terrain {

    public River() {
        super(new HashMap<>() {{
            put("Foot", 5);
            put("Armors", -1);
            put("KnightsA", -1);
            put("KnightsB", -1);
            put("Fighters", -1);
            put("Bandits", 5);
            put("Pirates", 2);
            put("Mages", -1);
            put("Fliers", 1);
            put("Manaketes", -1);
            put("Nomads", -1);
            put("Nomad_Troopers", 5);

        }});
        id = 18;
        name = "River";
        def = 0;
        avoid = 0;
        heal = 0;
    }
}
