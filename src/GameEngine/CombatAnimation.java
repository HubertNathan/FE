package GameEngine;

import Units.Unit;
import javafx.animation.Transition;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import javafx.util.Pair;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

public class CombatAnimation extends Transition {
    ImageView imv;
    int frameNumber;
    private int hitFrame;
    int lastIndex = -1;
    HashMap<Integer, Image> CombatAnimations = new HashMap<>();
    ArrayList<Pair<Integer,String>> CombatAnimations_ = new ArrayList<>();

    public CombatAnimation(Unit unit, String mode) throws FileNotFoundException {
        String resourceDirectory = (unit.getResourceDirectory()+"Battle Animations/"+unit.getSkin()+"/"+unit.getWieldedWeapon().getType()+"/").substring(5);
        File combatFile = new File(resourceDirectory+unit.getWieldedWeapon().getType()+".txt");
        Scanner scanner = new Scanner(combatFile);
        boolean modeReached  = false;
        frameNumber = 0;
        while (scanner.hasNextLine()){
            String[] line = scanner.nextLine().split("-");
            if (modeReached && !(line.length==1)){
                CombatAnimations.put(frameNumber,new Image("file:"+resourceDirectory+line[1],6*248,6*160,false,false));
                frameNumber +=Integer.parseInt(line[0]);
                //CombatAnimations_.add(new Pair<>(frame,resourceDirectory+line[1]));
            }
            else if (line[0].equals(mode)){
                modeReached = true;
            } else if (line[0].startsWith("C1A", 2) || line[0].startsWith("C1B", 2)) {
                hitFrame = frameNumber;
            } else if (line[0].equals("~~~") && modeReached) break;
        }
        setCycleDuration(Duration.millis((double)1000/60* frameNumber));
        setCycleCount(1);
    }
    public CombatAnimation(Unit unit, String mode, int hitFrame, int frameNumber) throws FileNotFoundException {
        this.frameNumber = frameNumber;
        String resourceDirectory = (unit.getResourceDirectory() + "Battle Animations/"+unit.getSkin()+"/" + unit.getWieldedWeapon().getType() + "/").substring(5);
        switch (mode) {
            case DAMAGE:
                CombatAnimations.put(0, new Image("file:" + resourceDirectory + unit.getWieldedWeapon().getType() + "_000.png", 6 * 248, 6 * 160, false, false));
                CombatAnimations.put(hitFrame, new Image("file:" + resourceDirectory + "hit.png", 6 * 248, 6 * 160, false, false));
                CombatAnimations.put(hitFrame + 8, new Image("file:" + resourceDirectory + unit.getWieldedWeapon().getType() + "_000.png", 6 * 248, 6 * 160, false, false));
                break;
            case DODGE_MELEE:
                CombatAnimations.put(0, new Image("file:" + resourceDirectory + unit.getWieldedWeapon().getType() + "_000.png", 6 * 248, 6 * 160, false, false));
                int frame = hitFrame;
                File combatFile = new File(resourceDirectory + unit.getWieldedWeapon().getType() + ".txt");
                Scanner scanner = new Scanner(combatFile);
                boolean modeReached = false;
                while (scanner.hasNextLine()) {
                    String[] line = scanner.nextLine().split("-");
                    if (modeReached && !(line.length == 1)) {
                        CombatAnimations.put(frame, new Image("file:" + resourceDirectory + line[1], 6 * 248, 6 * 160, false, false));
                        frame += Integer.parseInt(line[0]);
                        //CombatAnimations_.add(new Pair<>(frame,resourceDirectory+line[1]));
                    } else if (line[0].equals(mode)) {
                        modeReached = true;
                    } else if (line[0].equals("~~~") && modeReached) break;
                }
                CombatAnimations.put(frame, new Image("file:" + resourceDirectory + unit.getWieldedWeapon().getType() + "_000.png", 6 * 248, 6 * 160, false, false));
        }
        setCycleDuration(Duration.millis((double)1000/60* frameNumber));
        setCycleCount(1);
    }
    public void play(ImageView imv) {
        this.imv = imv;
        super.play();
    }

    public HashMap<Integer, Image> getCombatAnimations() {
        return CombatAnimations;
    }

    @Override
    protected void interpolate(double k) {
        int index = (int)(k* frameNumber);
            imv.setImage(CombatAnimations.get(getAnimFrame(index)));

            lastIndex = getAnimFrame(index);


    }
    private int getAnimFrame(int index){
        int animFrame = 0;
        for (int key : CombatAnimations.keySet()){
            if (index>=key && animFrame<= key){
                animFrame=key;
            }
            ;
        }
        return animFrame;
    }
    public int getHitFrame(){
        return hitFrame;
    }
    public int getFrameNumber(){
        return frameNumber;
    }
    public static final String DAMAGE = "*Take Damage*";
    public static final String DODGE_MELEE = "*Dodge Melee*";
    public static final String MISS = "*Attack Missed*";
    public static final String MELEE_ATTACK = "*Melee Attack*";
    public static final String MELEE_CRITICAL = "*Melee Critical*";
    public static final String RANGED_ATTACK = "*Ranged Attack*";
}
