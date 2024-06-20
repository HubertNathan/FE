package GameEngine;

import Items.Weapons.Axe;
import Items.Weapons.Lance;
import Items.Weapons.Sword;
import Items.Weapons.Weapon;
import Units.Unit;

import java.util.Arrays;
import java.util.List;

public class CombatHandler {
    private final Board board;
    private final int atkHit;
    private final int atkDmg;
    private final int atkCrit;
    private final int defHit;
    private final int defDmg;
    private final int defCrit;
    private final Unit attacker;
    Unit defender;

    public CombatHandler(Unit attacker, Unit defender, Board board){
        this.board = board;
        this.attacker = attacker;
        this.defender = defender;

        atkDmg = Math.max(calculateAtk(attacker,defender) - calculateDef(defender, board),0) ;
        atkHit = Math.min(Math.max(calculateHit(attacker,defender) - calculateAvoid(defender, board),0),100);
        atkCrit = Math.max(calculateCrit(attacker) - calculateCritAvoid(defender),0);

        defDmg = Math.max(calculateAtk(defender,attacker) - calculateDef(attacker, board),0);
        defHit = Math.min(Math.max(calculateHit(defender,attacker) - calculateAvoid(attacker, board),0),100);
        defCrit = Math.max(calculateCrit(defender) - calculateCritAvoid(attacker),0);

    }

    public static List<Integer> getUnitCalculations(Unit attacker, Board board) {
        return Arrays.asList(
                attacker.getStr()+attacker.getWieldedWeapon().getMight(),
                attacker.getWieldedWeapon().getCrit() + attacker.getSkl()/2,
                attacker.getWieldedWeapon().getHit() + 2 * attacker.getSkl() + attacker.getLck()/2,
                2 * attacker.getSpd() + attacker.getLck()+board.get(attacker.getYValue(),attacker.getXValue()).getTerrain().getAvoid()
        );
    }
    public List<Integer> getCombatStats(){
        return getCombatStats(false);
    }
    public List<Integer> getCombatStats(boolean b){
        if (b) return Arrays.asList(defHit, defDmg, defCrit, atkHit, atkDmg, atkCrit);
        return Arrays.asList(atkHit, atkDmg, atkCrit, defHit, defDmg, defCrit);
    }
    public static List<Integer> getCombatStats(Unit attacker, Unit defender, Board board){
        return Arrays.asList(
                Math.max(calculateAtk(attacker,defender) - calculateDef(defender, board),0),
                Math.min(Math.max(calculateHit(attacker,defender) - calculateAvoid(defender, board),0),100),
                Math.max(calculateCrit(attacker) - calculateCritAvoid(defender),0),

                Math.max(calculateAtk(defender,attacker) - calculateDef(attacker, board),0),
                Math.min(Math.max(calculateHit(defender,attacker) - calculateAvoid(attacker, board),0),100),
                Math.max(calculateCrit(defender) - calculateCritAvoid(attacker),0)
        );
    }

    public List<Boolean> getTurn(){
        boolean Hit1 = false, Crit1 = false, Hit2 = false, Crit2 = false, Hit3 = false, Crit3 = false;
        int diceRoll1 = (int)(Math.random()*100);
        int diceRoll2 = (int)(Math.random()*100);
        int critRoll = (int)(Math.random()*100);
        if ((diceRoll1+diceRoll2)/2 < atkHit) {
            Hit1 = true;
            if (critRoll < atkCrit) Crit1 = true;
        }
        diceRoll1 = (int)(Math.random()*100);
        diceRoll2 = (int)(Math.random()*100);
        critRoll = (int)(Math.random()*100);
        if ((diceRoll1+diceRoll2)/2 < defHit) {
            Hit2 = true;
            if (critRoll < defCrit) Crit2 = true;
        }
        diceRoll1 = (int)(Math.random()*100);
        diceRoll2 = (int)(Math.random()*100);
        critRoll = (int)(Math.random()*100);
        if ((diceRoll1+diceRoll2)/2 < atkHit) {
            Hit3 = true;
            if (critRoll < atkCrit) Crit3 = true;
        }
        return Arrays.asList(Hit1,Crit1,Hit2,Crit2,Hit3,Crit3);
    }

    public int calculateAS(Unit attacker){
        if (attacker.getCon() >= attacker.getWieldedWeapon().getWeight()){
            return attacker.getSpd();
        }
        else return attacker.getSpd() + attacker.getCon() - attacker.getWieldedWeapon().getWeight();
    }
    private static int calculateAtk(Unit attacker, Unit defender){
        //Atk = (Str || Mag) + Effectiveness * ( Weapon Might + Weapon Triangle ) + Support
        return attacker.getStr() + (attacker.getWieldedWeapon().getMight() + weaponTriangle(attacker.getWieldedWeapon(), defender.getWieldedWeapon()));
    }
    private static int calculateDef(Unit defender, Board board){
        //Df = (Def || Res) + Terrain Defense + Support
        return defender.getDef() +board.get(defender.getYValue(),defender.getXValue()).getTerrain().getDef();
    }
    private static int calculateHit(Unit attacker, Unit defender){
        //Hit = Weapon Hit + 2 * Skl + Lck/2 + Weapon Triangle + Support Hit
        return attacker.getWieldedWeapon().getHit() + 2 * attacker.getSkl() + attacker.getLck() / 2+15*weaponTriangle(attacker.getWieldedWeapon(), defender.getWieldedWeapon());
    }
    private static int calculateAvoid(Unit defender, Board board){
        //Avo = 2 * Attack Speed + Luck + Terrain Avo + Support
        return 2 * defender.getSpd() + defender.getLck()+ board.get(defender.getYValue(),defender.getXValue()).getTerrain().getAvoid();
    }
    private static int calculateCrit(Unit attacker){
        // Crit = Weapon Crit + Skl/2 + Support
        return attacker.getWieldedWeapon().getCrit() + attacker.getSkl()/2;
    }
    private static int calculateCritAvoid(Unit defender){
        // CritAvo = Luck + support
        return defender.getLck();
    }
    private static int weaponTriangle(Weapon atkWeapon, Weapon defWeapon){
        int WT = switch (atkWeapon.getType()) {
            case "Sword" -> switch (defWeapon.getType()) {
                case "Axe" -> 1;
                case "Lance" -> -1;
                default -> 0;
            };
            case "Axe" -> switch (defWeapon.getType()) {
                case "Lance" -> 1;
                case "Sword" -> -1;
                default -> 0;
            };
            case "Lance" -> switch (defWeapon.getType()) {
                case "Sword" -> 1;
                case "Axe" -> -1;
                default -> 0;
            };
            default -> 0;
        };
        if (atkWeapon.getName().equals(Sword.LanceReaver)||atkWeapon.getName().equals(Axe.SwordReaver)||atkWeapon.getName().equals(Lance.AxeReaver)){
            if (defWeapon.getName().equals(Sword.LanceReaver)||defWeapon.getName().equals(Axe.SwordReaver)||defWeapon.getName().equals(Lance.AxeReaver)) WT *= -1;
            else WT *= -2;
        } else if (atkWeapon.getName().equals(Axe.SwordSlayer)) WT *=-1;
        return WT;
    }
}
