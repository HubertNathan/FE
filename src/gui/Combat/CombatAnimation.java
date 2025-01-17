package gui.Combat;

import units.Unit;
import javafx.animation.AnimationTimer;
import javafx.geometry.Rectangle2D;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Pair;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import static java.util.Collections.max;

public class CombatAnimation extends AnimationTimer {
    private int frameNumber = 0;
    private int hitFrame, effectDuration;
    private ArrayList<Pair<Integer, Unit>> hitFrames = new ArrayList<>();
    CombatRenderer cr;
    private int c = 0;
    private ImageView imv1, imv2, imv3;
    private ArrayList<String> effects = new ArrayList<>();
    private final BattleEffects battleEffect = new BattleEffects();
    private double lastTime = System.nanoTime();
    private final HashMap<Integer,Integer> AttackAnimations = new HashMap<>(), DefenseAnimations = new HashMap<>();
    private final HashMap<Integer, Pair<Image, Float>> EffectAnimations = new HashMap<>();
    private boolean death = false;
    private Unit killedUnit = null;
    private final String attackerDirectory, defenderDirectory;
    public CombatAnimation(CombatRenderer combatRenderer, Unit attacker, Unit defender, ArrayList<String> turn) throws FileNotFoundException {
        attackerDirectory = attacker.getResourceDirectory();
        defenderDirectory = defender.getResourceDirectory();
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
            loadAttack(defender, attacker, turn.removeFirst(),DefenseAnimations);
            loadDefense(attacker,turn.removeFirst(),AttackAnimations);
        }
        if (!turn.isEmpty()){
            loadAttack(attacker, defender, turn.removeFirst());
            loadDefense(defender,turn.removeFirst());
        }
    }
    private void loadAttack(Unit attacker, Unit defender, String attack) throws FileNotFoundException {
        loadAttack(attacker,defender,attack,AttackAnimations);
    }
    private void loadAttack(Unit attacker, Unit defender, String attack, HashMap<Integer,Integer> Animations) throws FileNotFoundException {
        long splittingTime = 0;
        long t = System.nanoTime();
        long hitFrameTime = 0;
        long loadEffectTime = 0;

        File combatFile = new File(attacker.getResourceDirectory().substring(5)+".txt");
        Scanner scanner = new Scanner(combatFile);
        long now;
        long insertTime = 0;
        boolean reached = false;
        int frame = frameNumber;
        while (scanner.hasNextLine()){
            String[] line = scanner.nextLine().split("-");
            if (reached && !(line.length==1)){
                Animations.put(frame, Integer.parseInt(line[1])+2);
                frame +=Integer.parseInt(line[0]);
            }
            else if (line[0].startsWith(attack)){
                reached = true;
            } else if (reached && line[0].startsWith("C1A") || line[0].startsWith("C1B")){
                hitFrame = frame;
                if (!attack.equals(MISS)) {
                    now = System.nanoTime();
                    hitFrames.add(new Pair<>(hitFrame,defender));
                    hitFrameTime+=System.nanoTime()-now;
                }
                now = System.nanoTime();
                battleEffect.add(effects.removeFirst(),frame);
                loadEffectTime+=System.nanoTime()-now;

            } else if (reached && line[0].startsWith("C01")) {
                frame+=battleEffect.getEffectDuration();

            } else if (line[0].equals("~~~") && reached) break;
        }
        Animations.put(frame, 2);
        System.out.println("Loading Effect : "+loadEffectTime);
        System.out.println("Total Time : "+(System.nanoTime()-t));

    }
    private void loadDefense(Unit defender, String defense) throws FileNotFoundException {
        loadDefense(defender,defense,DefenseAnimations);
    }
    private void loadDefense(Unit defender, String defense, HashMap<Integer,Integer> Animations) throws FileNotFoundException{
        Animations.put(frameNumber,2);
        int frame;
        switch (defense){
            case DAMAGE:
                Animations.put(hitFrame,1);
                Animations.put(hitFrame+8,2);
                break;
            case DEATH:
                killedUnit = defender;
                frame = hitFrame;
                Animations.put(hitFrame,1);
                frame+=8;
                Animations.put(frame,2);
                frame+=effectDuration;
                Animations.put(frame,71);
                frame+=6;
                Animations.put(frame,2);
                frame++;
                Animations.put(frame,71);
                frame+=6;
                Animations.put(frame,2);
                frame++;
                Animations.put(frame,71);
                frame+=6;
                Animations.put(frame,2);
                frame++;
                Animations.put(frame,71);
                frame+=6;
                Animations.put(frame,2);
                frame++;
                Animations.put(frame,71);
                frame+=6;
                Animations.put(frame,2);
                frameNumber = frame;
                death = true;
                break;
            case DODGE_MELEE:
                File combatFile = new File(defender.getResourceDirectory().substring(5)+".txt");
                Scanner scanner = new Scanner(combatFile);
                boolean reached = false;
                frame = hitFrame;
                while (scanner.hasNextLine()){
                    String[] line = scanner.nextLine().split("-");
                    if (reached && !(line.length==1)){
                        Animations.put(frame, Integer.parseInt(line[1])+2);
                        frame +=Integer.parseInt(line[0]);
                    }
                    else if (line[0].startsWith(defense)){
                        reached = true;
                    } else if (reached && line[0].startsWith("C01")) {
                        frame+=effectDuration;
                    } else if (line[0].equals("~~~") && reached) break;
                }
                Animations.put(frame, 2);
                break;
        }
        frameNumber = max((Animations.equals(DefenseAnimations)?AttackAnimations.keySet():DefenseAnimations.keySet()));
    }
    public void play(ImageView imv1, ImageView imv2, ImageView imv3){
        this.imv1 = imv1;
        long now = System.nanoTime();
        new Thread(()->this.imv1.setImage(new Image(attackerDirectory+".png", 240*3*8,160*3*9,false,false))){{setDaemon(true);start();}};
        System.out.println(System.nanoTime()-now);
        this.imv2 = imv2;
        this.imv2.setImage(new Image(defenderDirectory+".png", 240*3*8,160*3*9,false,false));
        this.imv3 = imv3;
        start();
    }
    private void loadEffect(String effect) throws FileNotFoundException {
        int frame = hitFrame;
        battleEffect.add(effect,hitFrame);
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
    public static final String DAMAGE = "*Take Damage*";
    public static final String DEATH = "*Die*";
    public static final String DODGE_MELEE = "*Dodge Melee*";
    public static final String MISS = "*Attack Missed*";
    public static final String MELEE_ATTACK = "*Melee Attack*";
    public static final String MELEE_CRITICAL = "*Melee Critical*";
    public static final String RANGED_ATTACK = "*Ranged Attack*";
    @Override
    public void handle(long l) {
        long now = System.nanoTime();
        if (System.nanoTime() - lastTime > (double)1000000000./60) {
            if (AttackAnimations.containsKey(c)) {
                imv1.setViewport(new Rectangle2D((AttackAnimations.get(c)%8)*240*3, (AttackAnimations.get(c)/8)*160*3,240*3,160*3));
            }
            if (DefenseAnimations.containsKey(c)) {
                imv2.setViewport(new Rectangle2D((DefenseAnimations.get(c)%8)*240*3, (DefenseAnimations.get(c)/8)*160*3,240*3,160*3));
            }
            if (battleEffect.getEffectAnimations().containsKey(c)){
                if (!battleEffect.get(battleEffect.getEffectAnimations().get(c)[0]).equals(imv3.getImage())){
                    imv3.setImage(battleEffect.get(battleEffect.getEffectAnimations().get(c)[1]));
                }
                imv3.setViewport(new Rectangle2D((battleEffect.getEffectAnimations().get(c)[1]%8)*240*3, (battleEffect.getEffectAnimations().get(c)[1]/8)*160*3,240*3,160*3));
                imv3.setOpacity(battleEffect.getEffectAnimations().get(c)[2]/100.);
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

