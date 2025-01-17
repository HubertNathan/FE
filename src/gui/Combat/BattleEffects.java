package gui.Combat;

import javafx.scene.image.Image;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

public class BattleEffects extends ArrayList<Image> {
    private int effectDuration = 0;
    private final HashMap<Integer, int[]> EffectAnimations = new HashMap<>();
    private final String[] effects = new String[3]; 
    private int n = 0;
    public void add(String effect, int frame) throws FileNotFoundException {
        System.out.println(effect);
        int hitFrame = frame;
        long loopTime = 0, loadImageTime = 0, scanningTime = 0, pushImageTime = 0, now;
        boolean duplicate = false;
        String directory = "file:Resources/BattleEffects/";
        now = System.nanoTime();
        for (int i = 0; i<3; i++) {
            if (effect.equals(effects[i])) {
                add(get(i));
                duplicate = true;
            }
        }
        loopTime = now - System.nanoTime();
        effects[n] = effect;
        if (!duplicate) {
            add(new Image(directory + effect + ".png",240*3*8,160*3*9,false,false));
        }
        File effectFile = new File((directory+effect+".txt").substring(5));
        Scanner scanner = new Scanner(effectFile);
        now = System.nanoTime();
        while (scanner.hasNextLine()){

            String[] line = scanner.nextLine().split("-");
            System.out.println(Arrays.toString(line));
            EffectAnimations.put(frame, new int[]{size()-1, Integer.parseInt(line[1]),Integer.parseInt(line[2])});
            frame+= Integer.parseInt(line[0]);
        }
        scanningTime = System.nanoTime() -now;
        EffectAnimations.put(frame,new int[]{size()-1,0,0});
        System.out.println("Loop Time : "+loopTime);
        System.out.println("Load Image : " + loadImageTime);
        System.out.println("Push Time : "+pushImageTime);
        System.out.println("Scanning Time : "+scanningTime);
        effectDuration = frame - hitFrame;
    }

    public HashMap<Integer, int[]> getEffectAnimations() {
        return EffectAnimations;
    }
    public int getEffectDuration() {
        return effectDuration;
    }
}
