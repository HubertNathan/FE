package Weapon;

import javafx.scene.image.Image;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public abstract class Weapon {
    protected String name;
    protected String rank;
    protected int range;
    protected int uses;
    protected int weight;
    protected int might;
    protected int hit;
    protected int crit;
    protected int wex;
    protected int cost;
    protected String type;
    protected Image icon;

    String description = "";

    private final HashMap<String,ArrayList<String>> SwordsTable = new HashMap<>(){{
        put(Sword.IronSword,    new ArrayList<>(Arrays.asList("E","1","46","5","5","90","0","1","460", "-")));
        put(Sword.SlimSword,    new ArrayList<>(Arrays.asList("E","1","30","2","3","100","5","1","480","-")));
        put(Sword.SteelSword,   new ArrayList<>(Arrays.asList("D","1","30","10","8","75","0","1","600","-")));
        put(Sword.SilverSword,  new ArrayList<>(Arrays.asList("A","1","20","8","13","80","0","1","1500","-")));
        put(Sword.IronBlade,    new ArrayList<>(Arrays.asList("D","1","35","12","9","70","0","2","980","-")));
        put(Sword.SteelBlade,   new ArrayList<>(Arrays.asList("C","1","25","14","11","65","0","2","1250","-")));
        put(Sword.SilverBlade,  new ArrayList<>(Arrays.asList("A","1","15","13","14","60","0","2","1800","-")));
        put(Sword.PoisonSword,  new ArrayList<>(Arrays.asList("D","1","40","6","3","70","0","1","-1","Coated in lethal Poison")));
        put(Sword.Rapier,       new ArrayList<>(Arrays.asList("*","1","40","5","7","95","10","2","6000","Effective against infantry")));
        put(Sword.ManiKatti,    new ArrayList<>(Arrays.asList("*","1","45","3","8","80","20","2","-1","Effective against infantry")));
        put(Sword.BraveSword,   new ArrayList<>(Arrays.asList("B","1","30","12","9","75","0","1","3000","Can strike consecutively")));
        put(Sword.WoDao,        new ArrayList<>(Arrays.asList("D","1","20","5","8","75","35","1","1200","Improves critical hit")));
        put(Sword.KillingEdge,  new ArrayList<>(Arrays.asList("C","1","20","7","9","75","30","1","1300","Improves critical hit")));
        put(Sword.ArmorSlayer,  new ArrayList<>(Arrays.asList("D","1","18","11","8","80","0","1","1260","Effective against infantry")));
        put(Sword.WyrmSlayer,   new ArrayList<>(Arrays.asList("C","1","20","5","7","75","0","1","3000","Effective against wyverns")));
        put(Sword.RuneSword,    new ArrayList<>(Arrays.asList("A","1,2","15","11","12","65","0","1","3300","Imbued with dark magic")));
        put(Sword.LanceReaver,  new ArrayList<>(Arrays.asList("C","1","15","9","9","75","5","2","1800","Strong against Spears")));
        put(Sword.LongSword,    new ArrayList<>(Arrays.asList("D","1","18","11","6","85","0","1","1260","Effective against cavalry")));
        put(Sword.EmblemBlade,  new ArrayList<>(Arrays.asList("E","1","60","5","5","90","0","1","-1","A tactician's sword")));
        put(Sword.Durandal,     new ArrayList<>(Arrays.asList("*","1","20","16","17","90","0","1","-1","Increases strength by 5 pts")));
        put(Sword.SolKatti,     new ArrayList<>(Arrays.asList("*","1","30","14","12","95","25","2","-1","Forged to battle wyvers")));
        put(Sword.RegalBlade,   new ArrayList<>(Arrays.asList("S","1","25","9","20","85","0","1","15000","For experienced units only")));
        put(Sword.WindSword,    new ArrayList<>(Arrays.asList("B","1,2","40","9","9","70","0","1","8000","A magical sword capable of")));
    }};
    private final HashMap<String,ArrayList<String>> LancesTable = new HashMap<>(){{
        put(Lance.IronLance,    new ArrayList<>(Arrays.asList("E","1","45","8","7","80","0","1","360", "-")));
        put(Lance.SlimLance,    new ArrayList<>(Arrays.asList("E","1","30","4","4","85","5","1","450","-")));
        put(Lance.SteelLance,   new ArrayList<>(Arrays.asList("D","1","30","13","10","70","0","2","480","-")));
        put(Lance.SilverLance,  new ArrayList<>(Arrays.asList("A","1","20","10","14","75","0","1","1200","-")));
        put(Lance.PoisonLance,  new ArrayList<>(Arrays.asList("E","1","40","8","4","60","0","1","-1","Coated in lethal Poison")));
        put(Lance.BraveLance,   new ArrayList<>(Arrays.asList("B","1","30","14","10","70","0","1","7500","Can strike consecutively")));
        put(Lance.KillerLance,  new ArrayList<>(Arrays.asList("C","1","20","9","10","70","30","1","1300","Improves critical hit")));
        put(Lance.Horseslayer,  new ArrayList<>(Arrays.asList("D","1","16","13","7","80","0","1","1260","Effective against infantry")));
        put(Lance.Javelin,      new ArrayList<>(Arrays.asList("E","1,2","20","11","6","65","0","1","400","Doubles as ranged attack")));
        put(Lance.Spear,        new ArrayList<>(Arrays.asList("B","1,2","15","10","12","70","5","1","9000","-")));
        put(Lance.VaidaSpear,   new ArrayList<>(Arrays.asList("B","1,2","15","10","12","70","0","1","1800","-")));
        put(Lance.AxeReaver,    new ArrayList<>(Arrays.asList("C","1","15","11","10","70","5","1","1260","Strong against axes")));
        put(Lance.EmblemLance,  new ArrayList<>(Arrays.asList("E","1","60","8","7","80","0","1","-1","A tactician's lance")));
        put(Lance.RexHasta,     new ArrayList<>(Arrays.asList("S","1","25","11","21","80","0","1","15000","For experienced units only")));
        put(Lance.HeavySpear,   new ArrayList<>(Arrays.asList("D","1","16","14","9","70","0","1","1200","Effective against knights")));
        put(Lance.ShortSpear,   new ArrayList<>(Arrays.asList("C","1,2","18","12","9","60","0","1","900","Doubles as ranged attack")));
    }};
    private final HashMap<String,ArrayList<String>> AxesTable = new HashMap<>(){{
        put(Axe.IronAxe,        new ArrayList<>(Arrays.asList("E","1","45","10","8","75","0","1","270","-")));
        put(Axe.SteelAxe,       new ArrayList<>(Arrays.asList("E","1","30","15","11","65","0","2","360","-")));
        put(Axe.SilverAxe,      new ArrayList<>(Arrays.asList("A","1","20","12","15","70","0","1","1000","-")));
        put(Axe.PoisonAxe,      new ArrayList<>(Arrays.asList("D","1","40","10","4","60","0","1","-1","Coated in lethal Poison")));
        put(Axe.BraveAxe,       new ArrayList<>(Arrays.asList("B","1","30","16","10","65","0","1","2250","Allows user to strike twice in one attack")));
        put(Axe.KillerAxe,      new ArrayList<>(Arrays.asList("C","1","20","11","11","65","30","1","1000","Improves critical hit")));
        put(Axe.Halberd,        new ArrayList<>(Arrays.asList("D","1","18","15","10","60","0","1","810","Effective against cavalry")));
        put(Axe.Hammer,         new ArrayList<>(Arrays.asList("D","1","20","15","10","55","0","2","800","Effective against knights")));
        put(Axe.DevilAxe,       new ArrayList<>(Arrays.asList("E","1","20","18","18","55","0","1","900","Might injure its wielder")));
        put(Axe.HandAxe,        new ArrayList<>(Arrays.asList("E","1,2","20","12","7","60","0","1","300","Doubles as ranged attacks")));
        put(Axe.Tomahawk,       new ArrayList<>(Arrays.asList("A","1,2","15","14","13","65","0","1","3000","Effective against cavalry")));
        put(Axe.SwordReaver,    new ArrayList<>(Arrays.asList("C","1","15","13","11","65","5","2","2100","Strong against swords")));
        put(Axe.SwordSlayer,    new ArrayList<>(Arrays.asList("C","1","20","13","11","80","5","1","2000","Good against myrmidons")));
        put(Axe.DragonAxe,      new ArrayList<>(Arrays.asList("C","1","40","11","10","60","0","1","6000","An axe designed to strike")));
        put(Axe.Armads,         new ArrayList<>(Arrays.asList("*","1","25","18","18","85","0","1","-1","Effective against infantry")));
        put(Axe.WolfBeil,       new ArrayList<>(Arrays.asList("*","1","30","10","10","75","5","1","6000","Lightning-charged axe")));
        put(Axe.Basilikos,      new ArrayList<>(Arrays.asList("S","1","25","13","22","75","0","1","15000","For experienced units only")));
    }};
    Weapon(String name, String rank, String type, List<Integer> Stats){
        this.name = name;
        this.rank = rank;
        this.type = type;
        range = Stats.removeFirst();
        uses = Stats.removeFirst();
        weight = Stats.removeFirst();
        might = Stats.removeFirst();
        hit = Stats.removeFirst();
        crit = Stats.removeFirst();
        wex = Stats.removeFirst();
        cost = Stats.removeFirst();
    }
    Weapon(String name, String rank, String type, String description, List<Integer> Stats){
        this.name = name;
        this.rank = rank;
        this.type = type;
        this.description = description;
        range = Stats.removeFirst();
        uses = Stats.removeFirst();
        weight = Stats.removeFirst();
        might = Stats.removeFirst();
        hit = Stats.removeFirst();
        crit = Stats.removeFirst();
        wex = Stats.removeFirst();
        cost = Stats.removeFirst();
    }
    Weapon(String name, String type) {
        this.name = name;
        this.type = type;
        List<String> Stats = switch (type) {
            case "Sword" -> SwordsTable.get(name);
            case "Lance" -> LancesTable.get(name);
            case "Axe" -> AxesTable.get(name);
            default -> throw new IllegalStateException("Unexpected value: " + type);
        };
        description = Stats.getLast();
        rank = Stats.removeFirst();
        range = Integer.parseInt(Stats.removeFirst());
        uses = Integer.parseInt(Stats.removeFirst());
        weight = Integer.parseInt(Stats.removeFirst());
        might = Integer.parseInt(Stats.removeFirst());
        hit = Integer.parseInt(Stats.removeFirst());
        crit = Integer.parseInt(Stats.removeFirst());
        wex = Integer.parseInt(Stats.removeFirst());
        cost = Integer.parseInt(Stats.removeFirst());
    }
    protected abstract void loadIcon() throws IOException;
    public void use(){
        uses--;
    }

    public String getType() {
        return type;
    }
    public String getName() {
        return name;
    }
    public int getUses() {
        return uses;
    }
    public int getMight() {
        return might;
    }
    public int getWeight() {
        return weight;
    }
    public int getHit() {
        return hit;
    }

    public int getCrit() {
        return crit;
    }

    public Image getIcon() {
        System.out.println(icon==null);
        return icon;
    }
}
