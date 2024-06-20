package GameEngine;

import GUI.CombatRenderer;
import Units.Unit;
import javafx.animation.AnimationTimer;
import javafx.collections.ObservableList;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Pair;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

import static java.util.Collections.max;
import static java.util.Collections.swap;

public class CombatAnimation extends AnimationTimer {
    private int frameNumber = 0;
    private int hitFrame, effectDuration;
    private ArrayList<Pair<Integer, Unit>> hitFrames = new ArrayList<>();
    CombatRenderer cr;
    private int c = 0;
    private ImageView imv1, imv2, imv3;
    private ArrayList<String> effects = new ArrayList<>();
    private double lastTime = System.nanoTime();
    private final HashMap<Integer,Image> AttackAnimations = new HashMap<>(), DefenseAnimations = new HashMap<>();
    private final HashMap<Integer, Pair<Image, Float>> EffectAnimations = new HashMap<>();
    private boolean death = false;
    private Unit killedUnit = null;
    public CombatAnimation(CombatRenderer combatRenderer, Unit attacker, Unit defender, ArrayList<String> turn) throws FileNotFoundException {
        effects.add(switch (turn.getFirst()) {
            case MELEE_ATTACK -> "Hit";
            case MELEE_CRITICAL -> "Crit";
            default -> "Miss";
        });
        if (turn.size()>2) {
            effects.add(switch (turn.get(2)) {
                case MELEE_ATTACK -> "Hit";
                case MELEE_CRITICAL -> "Critical";
                default -> "Miss";
            });
        }
        if (turn.size()>4){
            effects.add(switch (turn.get(4)) {
                case MELEE_ATTACK -> "Hit";
                case MELEE_CRITICAL -> "Critical";
                default -> "Miss";
            });
        }
        cr = combatRenderer;
        loadAttack(attacker, defender, turn.removeFirst());
        loadDefense(defender,turn.removeFirst());
        if (!turn.isEmpty()){
            loadAttack(defender, attacker, turn.removeFirst());
            loadDefense(attacker,turn.removeFirst());
        }
        if (!turn.isEmpty()){
            loadAttack(attacker, defender, turn.removeFirst());
            loadDefense(defender,turn.removeFirst());
        }
    }
    private void loadAttack(Unit attacker, Unit defender, String attack) throws FileNotFoundException {
        String resourceDirectory = (attacker.getResourceDirectory()+"Battle Animations/"+attacker.getSkin()+"/"+attacker.getWieldedWeapon().getType()+"/").substring(5);
        File combatFile = new File(resourceDirectory+attacker.getWieldedWeapon().getType()+".txt");
        Scanner scanner = new Scanner(combatFile);
        boolean modeReached  = false;
        int frame = frameNumber;
        while (scanner.hasNextLine()){
            String[] line = scanner.nextLine().split("-");
            if (modeReached && !(line.length==1)){
                AttackAnimations.put(frame,new Image("file:"+resourceDirectory+line[1],6*248,6*160,false,false));
                frame +=Integer.parseInt(line[0]);
            }
            else if (line[0].equals(attack)){
                modeReached = true;
            } else if (modeReached && line[0].startsWith("C1A") || line[0].startsWith("C1B")) {
                hitFrame = frame;
                if (!attack.equals(MISS)) {
                    hitFrames.add(new Pair<>(hitFrame,defender));
                }
                loadEffect(effects.removeFirst());

            } else if (modeReached && line[0].startsWith("C01")) {
                frame+=effectDuration;

            } else if (line[0].equals("~~~") && modeReached) break;
        }
        AttackAnimations.put(frame,new Image("file:"+resourceDirectory+attacker.getWieldedWeapon().getType()+"_000.png",6*248,6*160,false,false));
    }
    private void loadDefense(Unit defender, String defense) throws FileNotFoundException {
        String resourceDirectory = (defender.getResourceDirectory()+"Battle Animations/"+defender.getSkin()+"/"+defender.getWieldedWeapon().getType()+"/").substring(5);
        DefenseAnimations.put(frameNumber, new Image("file:"+resourceDirectory+defender.getWieldedWeapon().getType() + "_000.png",6*248,6*160,false,false));
        int frame;
        switch (defense) {
            case DAMAGE:
                DefenseAnimations.put(hitFrame, new Image("file:" + resourceDirectory + "hit.png", 6 * 248, 6 * 160, false, false));
                DefenseAnimations.put(hitFrame+8,new Image("file:" + resourceDirectory+defender.getWieldedWeapon().getType() +"_000.png",6 * 248, 6 * 160, false, false));
                break;
            case DEATH:
                killedUnit = defender;
                frame = hitFrame;
                DefenseAnimations.put(hitFrame, new Image("file:" + resourceDirectory + "hit.png", 6 * 248, 6 * 160, false, false));
                frame+=8;
                DefenseAnimations.put(frame,new Image("file:" + resourceDirectory+defender.getWieldedWeapon().getType() +"_000.png",6 * 248, 6 * 160, false, false));
                frame+=effectDuration;
                DefenseAnimations.put(frame,new Image("file:Resources/Sprites/void.png",6 * 248, 6 * 160, false, false));
                frame+=6;
                DefenseAnimations.put(frame,new Image("file:" + resourceDirectory+defender.getWieldedWeapon().getType() +"_000.png",6 * 248, 6 * 160, false, false));
                frame++;
                DefenseAnimations.put(frame,new Image("file:Resources/Sprites/void.png",6 * 248, 6 * 160, false, false));
                frame+=6;
                DefenseAnimations.put(frame,new Image("file:" + resourceDirectory+defender.getWieldedWeapon().getType() +"_000.png",6 * 248, 6 * 160, false, false));
                frame++;
                DefenseAnimations.put(frame,new Image("file:Resources/Sprites/void.png",6 * 248, 6 * 160, false, false));
                frame+=6;
                DefenseAnimations.put(frame,new Image("file:" + resourceDirectory+defender.getWieldedWeapon().getType() +"_000.png",6 * 248, 6 * 160, false, false));
                frame++;
                DefenseAnimations.put(frame,new Image("file:Resources/Sprites/void.png",6 * 248, 6 * 160, false, false));
                frame+=6;
                DefenseAnimations.put(frame,new Image("file:" + resourceDirectory+defender.getWieldedWeapon().getType() +"_000.png",6 * 248, 6 * 160, false, false));
                frame++;
                DefenseAnimations.put(frame,new Image("file:Resources/Sprites/void.png",6 * 248, 6 * 160, false, false));
                frame+=6;
                DefenseAnimations.put(frame,new Image("file:" + resourceDirectory+defender.getWieldedWeapon().getType() +"_000.png",6 * 248, 6 * 160, false, false));
                frameNumber = frame;
                death = true;
                break;
            case DODGE_MELEE:
                frame = hitFrame;
                File combatFile = new File(resourceDirectory+ defender.getWieldedWeapon().getType()+".txt");
                Scanner scanner = new Scanner(combatFile);
                boolean modeReached = false;
                while (scanner.hasNextLine()) {
                    String[] line = scanner.nextLine().split("-");
                    if (modeReached && !(line.length == 1)) {
                        DefenseAnimations.put(frame, new Image("file:" + resourceDirectory + line[1], 6 * 248, 6 * 160, false, false));
                        frame += Integer.parseInt(line[0]);
                        //CombatAnimations_.add(new Pair<>(frame,resourceDirectory+line[1]));
                    } else if (line[0].equals(defense)) {
                        modeReached = true;
                    } else if (modeReached && line[0].startsWith("C01")) {
                        frame+=effectDuration;

                    } else if (line[0].equals("~~~") && modeReached) break;
                }
                DefenseAnimations.put(frame, new Image("file:" + resourceDirectory + defender.getWieldedWeapon().getType() + "_000.png", 6 * 248, 6 * 160, false, false));
        }
        frameNumber = max(AttackAnimations.keySet());
    }

    public void play(ImageView imv1, ImageView imv2, ImageView imv3){
        this.imv1 = imv1;
        this.imv2 = imv2;
        this.imv3 = imv3;
        start();
    }
    private void loadEffect(String effect) throws FileNotFoundException {
        int frame = hitFrame;
        String resourceDirectory = "Resources/BattleEffects/" + effect + "/";
        File effectFile = new File(resourceDirectory + effect + ".txt");
        Scanner scanner = new Scanner(effectFile);
        while (scanner.hasNextLine()){
            String[] line = scanner.nextLine().split("-");
            EffectAnimations.put(frame, new Pair<>(new Image("file:" + resourceDirectory + line[1], 6 * 248, 6 * 160, false, false),(line.length>2)?Float.parseFloat(line[2]):1));
            frame+=Integer.parseInt(line[0]);
        }
        EffectAnimations.put(frame, new Pair<>(new Image("file:Resources/BattleEffects/void.png"),(float) 1));
        effectDuration = frame - hitFrame;


    }
    public Unit getKilledUnit(){
        return killedUnit;

    }
    private double opacity = .75;
    public int getHitFrame(){
        return hitFrame;
    }
    public int getFrameNumber(){
        return frameNumber;
    }
    public static final String DAMAGE = "*Take Damage*";
    public static final String DEATH = "*Die*";
    public static final String DODGE_MELEE = "*Dodge Melee*";
    public static final String MISS = "*Attack Missed*";
    public static final String MELEE_ATTACK = "*Melee Attack*";
    public static final String MELEE_CRITICAL = "*Melee Critical*";
    public static final String RANGED_ATTACK = "*Ranged Attack*";
    @Override
    public void handle(long l) {
        if (System.nanoTime() - lastTime > (double)1000000000./60) {
            if (AttackAnimations.containsKey(c)) {

                imv1.setImage(AttackAnimations.get(c));
            }
            if (DefenseAnimations.containsKey(c)) {
                imv2.setImage(DefenseAnimations.get(c));
            }
            if (EffectAnimations.containsKey(c)){
                imv3.setImage(EffectAnimations.get(c).getKey());
                imv3.setOpacity(EffectAnimations.get(c).getValue());
            }
            if (!hitFrames.isEmpty() && hitFrames.getFirst().getKey() == c){
                cr.takeDmg(hitFrames.removeFirst().getValue());
            }
            if (death && c == max(DefenseAnimations.keySet())-7) {
                imv2.setOpacity(opacity);
                imv2.setEffect(new ColorAdjust(){{
                    setSaturation(1);
                    setBrightness(.7);
                    setContrast(.4);
                    setHue(.05);
                }});
            }
            if (c>frameNumber && (c-frameNumber)%4==0 && opacity>0){
                opacity-=.1;
                imv2.setOpacity(opacity);
            }
            if (opacity<=0.0001) {
                stop();
                cr.endCombat();
            }
            lastTime = System.nanoTime();
            c++;
        }
        if (c>frameNumber && !death) {
            stop();
            cr.endCombat();
        }

    }
}

