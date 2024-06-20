package Items.Weapons;

import GUI.ResizableImage;

import java.io.IOException;
import java.util.List;

public class Axe extends Weapon {
    Axe(String name, String rank, List<Integer> Stats) throws IOException {
        super(name, rank, "Axe", Stats);
        loadIcon();

    }

    public Axe(String name) throws IOException {
        super(name, "Axe");
        loadIcon();

    }

    @Override
    public void loadIcon() throws IOException {
        int id = switch (name) {
            case IronAxe -> 30;
            case SteelAxe -> 31;
            case SilverAxe -> 32;
            case PoisonAxe -> 33;
            case BraveAxe -> 34;
            case KillerAxe -> 35;
            case Halberd -> 36;
            case Hammer -> 37;
            case DevilAxe -> 38;
            case HandAxe -> 39;
            case Tomahawk -> 40;
            case SwordReaver -> 41;
            case SwordSlayer -> 42;
            case DragonAxe -> 88;
            case EmblemAxe -> 127;
            case Armads -> 130;
            case WolfBeil -> 138;
            case Basilikos -> 143;
            default -> -1;
        };
        ResizableImage symbols = new ResizableImage("file:Resources/FE7Symbols.png", 472, 274);
        icon = symbols.getSubimage(302 * 6 + (id % 10) * 17 * 6, 6 + (id / 10) * 6 * 17, 16 * 6, 16 * 6);
        weaponType = symbols.getSubimage(249*6,6,15*6,15*6);
        //302*6+id%10*16*6,(id/10)*6,16*6,16*6
    }

    public static final String IronAxe = "Iron Axe";
    public static final String SteelAxe = "Steel Axe";
    public static final String SilverAxe = "Silver Axe";
    public static final String PoisonAxe = "Poison Axe";
    public static final String BraveAxe = "Brave Axe";
    public static final String KillerAxe = "Killer Axe";
    public static final String Halberd = "Halberd";
    public static final String Hammer = "Hammer";
    public static final String DevilAxe = "Devil Axe";
    public static final String HandAxe = "Hand Axe";
    public static final String Tomahawk = "Tomahawk";
    public static final String SwordReaver = "Swordreaver";
    public static final String SwordSlayer = "Sword Slayer";
    public static final String EmblemAxe = "Emblem Axe";
    public static final String DragonAxe = "Dragon Axe";
    public static final String Armads = "Armads";
    public static final String WolfBeil = "Wolf Beil";
    public static final String Basilikos = "Basilikos";
}
