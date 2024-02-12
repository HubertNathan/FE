package Terrains;
import java.util.HashMap;

public class Lake extends Terrain {

    public Lake() {
        super(new HashMap<>() {{
            put("Foot", -1);
            put("Armors", -1);
            put("KnightsA", -1);
            put("KnightsB", -1);
            put("Fighters", -1);
            put("Bandits", -1);
            put("Pirates", 3);
            put("Mages", -1);
            put("Fliers", 1);
            put("Manaketes", -1);
            put("Nomads", -1);
            put("Nomad_Troopers", -1);

        }});
        id = 9;
        name = "Lake";
        def = 1;
        avoid = 30;
        heal = 0;
    }
}
