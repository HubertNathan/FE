package Weapon;

import GUI.ResizableImage;

import java.io.IOException;
import java.util.List;
public class Sword extends Weapon{
    Sword(String name, String rank, List<Integer> Stats) throws IOException {
        super(name,rank,"Sword",Stats);
        loadIcon();

    }
    public Sword(String name) throws IOException {
        super(name,"Sword");
        loadIcon();

    }
    @Override
    protected void loadIcon() throws IOException {
        int id = switch (name){
            case IronSword   -> 0;
            case SlimSword   -> 1;
            case SteelSword  -> 2;
            case SilverSword -> 3;
            case IronBlade   -> 4;
            case SteelBlade  -> 5;
            case SilverBlade -> 6;
            case PoisonSword -> 7;
            case Rapier      -> 8;
            case ManiKatti   -> 9;
            case BraveSword  -> 10;
            case WoDao       -> 11;
            case KillingEdge -> 12;
            case ArmorSlayer -> 13;
            case WyrmSlayer  -> 14;
            //case LightBrand  -> 15;
            case RuneSword   -> 16;
            case LanceReaver -> 17;
            case LongSword   -> 18;
            case EmblemBlade -> 125;
            case Durandal    -> 129;
            case SolKatti    -> 137;
            case RegalBlade  -> 141;
            case WindSword   -> 148;
            default -> -1;
        };
        icon = new ResizableImage("file:Resources/FE7Symbols.png",472,274).getSubimage(302*6+(id%10)*17*6,6+(id/10)*6*17,16*6,16*6);
        //302*6+id%10*16*6,(id/10)*6,16*6,16*6
    }

    public static final String IronSword = "Iron Sword";
    public static final String SlimSword = "Slim Sword";
    public static final String SteelSword = "Steel Sword";
    public static final String SilverSword = "Silver Sword";
    public static final String IronBlade = "Iron Blade";
    public static final String SteelBlade = "Steel Blade";
    public static final String SilverBlade = "Silver Blade";
    public static final String PoisonSword = "Poison Sword";
    public static final String Rapier = "Rapier";
    public static final String ManiKatti = "Mani Katti";
    public static final String BraveSword = "Brave Sword";
    public static final String WoDao = "Wo Dao";
    public static final String KillingEdge = "Killing Edge";
    public static final String ArmorSlayer = "Armor Slayer";
    public static final String WyrmSlayer = "Wyrmslayer";
    public static final String LightBrand = "Light Brand";
    public static final String RuneSword = "Rune Sword";
    public static final String LanceReaver = "Lancereaver";
    public static final String LongSword = "Longsword";
    public static final String EmblemBlade = "Emblem Blade";
    public static final String Durandal = "Durandal";
    public static final String SolKatti = "Sol Katti";
    public static final String RegalBlade = "Regal Blade";
    public static final String WindSword = "Wind Sword";





}
