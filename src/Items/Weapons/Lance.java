package Items.Weapons;

import GUI.ResizableImage;

import java.io.IOException;
import java.util.List;

public class Lance extends Weapon{
    Lance(String name, String rank, List<Integer> Stats) throws IOException {
        super(name,rank,"sword",Stats);
        loadIcon();

    }
    public Lance(String name) throws IOException {
        super(name,"Lance");
        loadIcon();

    }
    @Override
    public void loadIcon() throws IOException {
        int id = switch (name){
            case IronLance   -> 19;
            case SlimLance   -> 20;
            case SteelLance  -> 21;
            case SilverLance -> 22;
            case PoisonLance -> 23;
            case BraveLance  -> 24;
            case KillerLance -> 25;
            case Horseslayer -> 26;
            case Javelin     -> 27;
            case Spear , VaidaSpear -> 28;
            case AxeReaver   -> 29;
            case EmblemLance -> 126;
            case RexHasta    -> 142;
            case HeavySpear  -> 145;
            case ShortSpear  -> 146;
            default -> -1;
        };
        ResizableImage symbols = new ResizableImage("file:Resources/FE7Symbols.png",472,274);
        icon = symbols.getSubimage(302*6+(id%10)*17*6,6+(id/10)*6*17,16*6,16*6);
        weaponType = symbols.getSubimage(233*6,6,15*6,15*6);
        //302*6+id%10*16*6,(id/10)*6,16*6,16*6
    }

    public static final String IronLance = "Iron Lance";
    public static final String SlimLance = "Slim Lance";
    public static final String SteelLance = "Steel Lance";
    public static final String SilverLance = "Silver Lance";
    public static final String PoisonLance = "Poison Lance";
    public static final String BraveLance = "Brave Lance";
    public  static  final String KillerLance = "Killer Lance";
    public static final String Horseslayer = "Horseslayer";
    public static final String Javelin = "Javelin";
    public static final String Spear = "Spear";
    public static final String VaidaSpear = "Spear (Vaida's)";
    public static final String AxeReaver = "Axereaver";
    public static final String EmblemLance = "Emblem Lance";
    public static final String RexHasta = "Rex Hasta";
    public static final String HeavySpear = "Heavy Spear";
    public static final String ShortSpear = "Short Spear";

}
