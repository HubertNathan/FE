package GUI.PanelInterface;

import GUI.Combat.CombatHandler;
import GameEngine.Board;
import Items.Weapons.Weapon;
import Units.Unit;
import javafx.animation.Interpolator;
import javafx.animation.ParallelTransition;
import javafx.animation.Transition;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import javafx.util.Pair;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class GameInterface extends PanelInterface {
    private Image ObjectivesIcon, TerrainIconBackground;
    private final Board board;

    class PanelOutTransition extends PanelTransition {
        PanelOutTransition(ImageView imv, int dir, boolean out){
            super(imv, dir, out);
            setCycleDuration(Duration.seconds(8.0/60));
        }
        @Override
        protected void interpolate(double t) {
            switch (dir) {
                case 3:
                    imv.setTranslateX(curX + t * (out ? 1 : -1) * imv.getFitWidth()*2*15/14);
                    break;
                case 2:
                    imv.setTranslateY(curY + t * (out ? -1 : 1) * (imv.getFitHeight() * 2 * 1.1));
                    break;
            }
        }
    }
    class PanelInTransition extends PanelTransition{
        PanelInTransition(ImageView imv, int dir, boolean out){
            super(imv, dir, out);
            setCycleDuration(Duration.seconds(7./60));
        }
        @Override
        protected void interpolate(double t){
            switch (dir){
                case 3:
                    if (out) imv.setTranslateX(curX + t * imv.getFitWidth()*2*15/14 - w*(board.getWidth()+3)-(double)2/14*imv.getFitWidth());
                    else imv.setTranslateX(curX + (-t) * imv.getFitWidth()*2*15/14 + w*(board.getWidth()+3)+(double)2/14*imv.getFitWidth());
                    break;
                case 2:
                    if (out) {imv.setTranslateY(curY + t * -2 * (imv.getFitHeight() * 1.1) + h*(board.getHeight())+imv.getFitHeight() * 1);}
                    else imv.setTranslateY(curY + t * 2 * (imv.getFitHeight() * 1.1) - h*(board.getHeight())+imv.getFitHeight() -2 * (imv.getFitHeight()));
                    break;

            }
        }
    }

    class PanelTransition extends Transition{
        ImageView imv;
        boolean out;
        int dir;
        double curX;
        double lastTime = System.nanoTime();
        double curY;
        double w;
        double h;
        PanelTransition(ImageView imv, int dir, boolean out){
            super();
            setCycleCount(1);
            setCycleDuration(Duration.seconds((double)15/60));
            setInterpolator(Interpolator.LINEAR);
            this.imv = imv;
            this.dir = dir;
            this.out = out;
            switch (dir){
                case 2:
                    h = imv.getFitHeight()/2.5;
                    curY = out?(h/4):(board.getHeight()-2.5)*h-h/4;

                case 3:
                    w = imv.getFitWidth()/3;
                    curX = out?(board.getWidth()-3)*w:0;
            }
        }
        @Override
        protected void interpolate(double t) {
            if (System.nanoTime() - lastTime > (double) 1000000000/1500000000) {
                switch (dir) {
                    case 3:
                        if (15*t<8) imv.setTranslateX(curX + t * (out ? 1 : -1) * imv.getFitWidth()*2*15/14);
                        else if (out) imv.setTranslateX(curX + t * imv.getFitWidth()*2*15/14 - w*(board.getWidth()+3)-(double)2/14*imv.getFitWidth());
                        else imv.setTranslateX(curX + (-t) * imv.getFitWidth()*2*15/14 + w*(board.getWidth()+3)+(double)2/14*imv.getFitWidth());
                        break;
                    case 2:
                        if (15*t<8) imv.setTranslateY(curY + t * (out ? -1 : 1) * (imv.getFitHeight() * 2 * 1.1));
                        else if (out) {imv.setTranslateY(curY + t * -2 * (imv.getFitHeight() * 1.1) + h*(board.getHeight())+imv.getFitHeight() * 1);}
                        else imv.setTranslateY(curY + t * 2 * (imv.getFitHeight() * 1.1) - h*(board.getHeight())+imv.getFitHeight() -2 * (imv.getFitHeight()));
                        break;
                }
                lastTime = System.nanoTime();
            }

        }
    }
    public GameInterface(Board board) throws IOException {
        super();
        this.board = board;
        load();
    }
    protected void load() {
        ObjectivesIcon = new Image("file:Resources/MenuSprites/Objectives.png",86*6,40*6,false,false);
        TerrainIconBackground = new Image("file:Resources/MenuSprites/Terrain.png",48*6,54*6,false,false);
        superLoad();

    }

    public List<ImageView> drawMenu(String def, String avo, String terrainType, String objectives) throws IOException {
        // Drawing lower right corner
        ImageView TI_IMV = new ImageView(drawTI(def,avo,terrainType));
        TI_IMV.setFitWidth(48*3);
        TI_IMV.setFitHeight(54*3);
        TI_IMV.setTranslateX(48*(board.getWidth()-3));
        TI_IMV.setTranslateY(48*(board.getHeight()-3.25));

        //drawing upper right corner
        Canvas OI = new Canvas(86*6,40*6);
        GraphicsContext g2 = OI.getGraphicsContext2D();
        g2.drawImage(ObjectivesIcon,0,0);
        String[] objectivesList = objectives.split(";");
        if (objectivesList.length == 1){
            Image Objectives = TxtI.convertTxt(objectivesList[0],"white");
            g2.drawImage(Objectives, (ObjectivesIcon.getWidth() - (Objectives.getWidth())) / 2, 6 * 6);
        }
        else {
            for (int i = 0; i < 2; i++) {
                Image Objectives = TxtI.convertTxt(objectivesList[i],"white");
                g2.drawImage(Objectives, (ObjectivesIcon.getWidth() - (Objectives.getWidth())) / 2, 6 * 6+i*6*16);

            }
        }
        ImageView OI_IMV = new ImageView(g2.getCanvas().snapshot(new SnapshotParameters(){{setFill(Color.TRANSPARENT);}},null));
        OI_IMV.setFitWidth(86*3);
        OI_IMV.setFitHeight(40*3);
        OI_IMV.setTranslateX(48*(board.getWidth()-5.375));
        OI_IMV.setTranslateY(4*3);

        return Arrays.asList(TI_IMV,OI_IMV);
    }
    public ImageView drawIntermediateMenu(boolean isInRange) throws IOException {
        Canvas canvas = buildPanel(3,isInRange?4:2);
        GraphicsContext g = canvas.getGraphicsContext2D();
        if (isInRange) {
            g.drawImage(TxtI.convertTxt("Attack","white"), 4*6, 7*6);
        }
        g.drawImage(TxtI.convertTxt("Item","white"),4*6,7*6+(isInRange?16*6:0));
        g.drawImage(TxtI.convertTxt("Wait","white"),4*6,23*6+(isInRange?16*6:0));
        return new ImageView(g.getCanvas().snapshot(new SnapshotParameters(){{setFill(Color.WHITE);}},null)){{
            setFitWidth(this.getImage().getWidth()/2);
            setFitHeight(this.getImage().getHeight()/2);}};
    }
    public List<ImageView> drawWeaponMenu(Unit attacker) throws IOException {
        GraphicsContext g1 = buildPanel(10,2*attacker.getInventory().getWeapons().size()-2).getGraphicsContext2D();
        for (Weapon weapon : attacker.getInventory().getWeapons()) {
            g1.drawImage(weapon.getIcon(),4*6,4*6+16*attacker.getInventory().getWeapons().indexOf(weapon)*6);
            g1.drawImage(TxtI.convertTxt(weapon.getName(),"white"),20*6,7*6+16*attacker.getInventory().getWeapons().indexOf(weapon)*6);
            Image uses = TxtI.convertNum(weapon.getUses());
            g1.drawImage(uses,100*6-uses.getWidth(),8*6+16*attacker.getInventory().getWeapons().indexOf(weapon)*6);
        }
        return Arrays.asList(
                new ImageView(g1.getCanvas().snapshot(new SnapshotParameters(){{setFill(Color.TRANSPARENT);}},null)){{
                    setFitWidth(getImage().getWidth()/2);
                    setFitHeight(getImage().getHeight()/2);
                }},
                buildWeaponMenu(new ImageView(),attacker,attacker.getInventory().getWeapons().getFirst()),
                attacker.getPortrait()
        );

    }
    public ImageView buildWeaponMenu(ImageView imv, Unit unit, Weapon weapon) throws IOException {
        unit.setWieldedWeapon(weapon);
        List<Integer> Stats = CombatHandler.getUnitCalculations(unit,board);
        GraphicsContext g2 = buildPanel(10,4).getGraphicsContext2D();
        g2.drawImage(unit.getWieldedWeapon().getWeaponType(),53*6,5*6);
        g2.drawImage(TxtI.convertTxt("Affi","white"),36*6,7*6);
        g2.drawImage(TxtI.convertTxt("Atk","white"),6*6,23*6);
        Image atk =TxtI.convertNum(Stats.getFirst());
        g2.drawImage(atk,42*6-atk.getWidth(),24*6);
        g2.drawImage(TxtI.convertTxt("Crit","white"),48*6,23*6);
        Image crit = TxtI.convertNum(Stats.get(1));
        g2.drawImage(crit,96*6-crit.getWidth(),24*6);
        g2.drawImage(TxtI.convertTxt("Hit","white"),6*6,39*6);
        Image hit = TxtI.convertNum(Stats.get(2));
        g2.drawImage(hit,42*6-hit.getWidth(),40*6);
        g2.drawImage(TxtI.convertTxt("Avoid","white"),48*6,39*6);
        Image avo = TxtI.convertNum(Stats.getLast());
        g2.drawImage(avo,96*6-avo.getWidth(),40*6);

        imv.setImage(g2.getCanvas().snapshot(new SnapshotParameters(){{setFill(Color.TRANSPARENT);}}, null));
        imv.setFitWidth(imv.getImage().getWidth()/2);
        imv.setFitHeight(imv.getImage().getHeight()/2);

        return imv;
    }
    public ImageView drawForecast(Unit attacker, Unit defender) throws IOException {

        List<Integer> Calculations = CombatHandler.getCombatStats(attacker,defender,board);

        GraphicsContext g = new Canvas(73*6,121*6).getGraphicsContext2D();
        g.drawImage(new Image("file:Resources/MenuSprites/Forecast.png",73*6,121*6,false,false),0,0);
        g.drawImage(attacker.getWieldedWeapon().getIcon(),4*6,3*6);
        Image curImg = TxtI.convertTxt(attacker.toString(),"white");
        g.drawImage(curImg,(22+20)*6- curImg.getWidth()/2+12,6*6);
        curImg = TxtI.convertNum(defender.getHealth());
        g.drawImage(curImg,20*6-curImg.getWidth(),23*6);
        curImg = TxtI.convertNum(Calculations.get(3));
        g.drawImage(curImg,20*6 - curImg.getWidth(),39*6);
        curImg = TxtI.convertNum(Calculations.get(4));
        g.drawImage(curImg,20*6 - curImg.getWidth(),55*6);
        curImg = TxtI.convertNum(Calculations.get(5));
        g.drawImage(curImg,20*6 - curImg.getWidth(),71*6);
        curImg = TxtI.convertNum(attacker.getHealth());
        g.drawImage(curImg, 68*6-curImg.getWidth(),23*6);
        curImg = TxtI.convertNum(Calculations.getFirst());
        g.drawImage(curImg,68*6 - curImg.getWidth(),39*6);
        curImg = TxtI.convertNum(Calculations.get(1),true);
        g.drawImage(curImg,68*6 - curImg.getWidth(),55*6);
        curImg = TxtI.convertNum(Calculations.get(2));
        g.drawImage(curImg,68*6 - curImg.getWidth(),71*6);
        curImg = TxtI.convertTxt(defender.toString(), "white");
        g.drawImage(curImg,14*6,86*6);
        curImg = TxtI.convertTxt(defender.getWieldedWeapon().getName(),"white");
        g.drawImage(curImg,7*6+25*6-curImg.getWidth()/2,102*6);
        g.drawImage(defender.getWieldedWeapon().getIcon(),52*6,83*6);

        return new ImageView(g.getCanvas().snapshot(new SnapshotParameters(){{setFill(Color.TRANSPARENT);}},null)){{
            setFitWidth(getImage().getWidth()/2);
            setFitHeight(getImage().getHeight()/2);
        }};
    }
    private Image drawTI(String def, String avo, String terrainType) throws IOException {
        Canvas TI = new Canvas(48 * 6, 54 * 6);
        GraphicsContext g = TI.getGraphicsContext2D();
        g.drawImage(TerrainIconBackground, 0, 0);
        g.drawImage(TxtI.convertTNumToImg(def), TerrainIconBackground.getWidth() - 6 * 8 - TxtI.convertTNumToImg(def).getWidth(), 31 * 6);
        g.drawImage(TxtI.convertTNumToImg(avo), TerrainIconBackground.getWidth() - 6 * 8 - TxtI.convertTNumToImg(avo).getWidth(), 39 * 6);
        Image terrainName = TxtI.convertTxt(terrainType, "white");
        g.drawImage(terrainName, (TerrainIconBackground.getWidth() - terrainName.getWidth()) / 2, 17 * 6);
        return g.getCanvas().snapshot(new SnapshotParameters(){{setFill(Color.TRANSPARENT);}},null);
    }
    public void updateTI(ImageView TI_IMV,String def, String avo, String terrainType) throws IOException {
        TI_IMV.setImage(drawTI(def,avo,terrainType));

    }
    public void animate(HashMap<ImageView, Pair<Integer,Boolean>> animations){
        ParallelTransition pTransition = new ParallelTransition();
        pTransition.setCycleCount(1);
        animations.keySet().forEach(key ->{
            PanelTransition transition = new PanelTransition(key, animations.get(key).getKey(), animations.get(key).getValue());
            pTransition.getChildren().add(transition);
        });
        pTransition.play();

    }
    public void animate(ImageView imv, int dir, boolean out){
        PanelTransition transition = new PanelTransition(imv,dir,out);
        transition.play();
    }
    public void transitionOut(HashMap<ImageView, Pair<Integer,Boolean>> animations){
        ParallelTransition pTransition = new ParallelTransition();
        pTransition.setCycleCount(1);
        animations.keySet().forEach(key ->{
            PanelOutTransition outTransition = new PanelOutTransition(key,animations.get(key).getKey(),animations.get(key).getValue());
            pTransition.getChildren().add(outTransition);
        });
        pTransition.play();
    }
    public void transitionIn(HashMap<ImageView, Pair<Integer,Boolean>> animations){
        ParallelTransition pTransition = new ParallelTransition();
        animations.keySet().forEach(key ->{
            PanelInTransition inTransition = new PanelInTransition(key, animations.get(key).getKey(), animations.get(key).getValue());
            pTransition.getChildren().add(inTransition);
        });
        pTransition.play();
    }
}
