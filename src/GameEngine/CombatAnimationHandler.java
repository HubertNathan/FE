package GameEngine;

import Units.Lyn_Lord;
import Units.Unit;
import javafx.animation.Transition;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import javafx.util.Pair;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class CombatAnimationHandler extends Transition {
    ImageView imv;
    int frame;
    int lastIndex = -1;
    HashMap<Integer, Image> CombatAnimations = new HashMap<>();
    ArrayList<Pair<Integer,String>> CombatAnimations_ = new ArrayList<>();

    public CombatAnimationHandler(Unit unit, String mode) throws FileNotFoundException {
        String resourceDirectory = (unit.getResourceDirectory()+"Battle Animations/"+unit.getWieldedWeapon().getType()+"/").substring(5);
        File combatFile = new File(resourceDirectory+unit.getWieldedWeapon().getType()+".txt");
        Scanner scanner = new Scanner(combatFile);
        boolean modeReached  = false;
        frame = 0;
        while (scanner.hasNextLine()){
            String[] line = scanner.nextLine().split("-");
            if (modeReached && !(line.length==1)){
                frame+=Integer.parseInt(line[0]);
                CombatAnimations.put(frame,new Image("file:"+resourceDirectory+line[1],6*248,6*160,false,false));
                //CombatAnimations_.add(new Pair<>(frame,resourceDirectory+line[1]));
            }
            else if (line[0].equals(mode)){
                modeReached = true;
            }
            else if (line[0].contains("*")&& modeReached) break;
        }
        setCycleDuration(Duration.millis((double)1000/60*frame));
        setCycleCount(INDEFINITE);
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
        int index = (int)(k*frame);
        if (CombatAnimations.containsKey(index) && index != lastIndex){
            imv.setImage(CombatAnimations.get(index));
            lastIndex = index;
        }

    }
}
