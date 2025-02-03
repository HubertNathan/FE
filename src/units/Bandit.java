package units;

import gui.Animations.Portrait;
import Items.Weapons.Axe;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.IOException;

public class Bandit extends Unit{
    protected static Image standingSprites, movingSprites;
    public Bandit(String name, String colour) throws IOException {
        super(
                (name.isBlank())?"Bandit":name,
                colour,
                new byte[] { 1,20,5,0,1,5,0,3,0,5,12},
                new Axe(Axe.IronAxe)
        );
        unitType = "Bandits";
        load();
        skin = switch (name) {
            case "Zugu" -> "Zugu";
            default     -> "Brigand";
        };
    }
    @Override
    public void load(){
        standingSprites = new Image("file:Resources/Sprites/Brigand/standingSprites.png",32*4*3,96*3,false,false);
        movingSprites = new Image("file:Resources/Sprites/Brigand/movingSprites.png",32*4*3,480*3,false,false);
    }

    @Override
    public ImageView getPortrait() {
        if (skin.equals("Zugu") )return Portrait.getPortrait(Portrait.ZUGU);
        return null;
    }
    @Override
    public Image getSprites() {
        if (mode.equals(STANDING)){
            return  standingSprites;
        }
        return movingSprites;
    }
    @Override
    public String getBaseResourceDirectory() {
        return "file:Resources/Sprites/Brigand/";
    }
}