package gui.PanelInterface;

import gui.Combat.CombatHandler;
import core.Board;
import Items.Weapons.Weapon;
import units.Unit;
import javafx.animation.Interpolator;
import javafx.animation.ParallelTransition;
import javafx.animation.Transition;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static core.game_engine.Battle.BattleEngine.tileSize;

public class GameInterface extends PanelInterface {
    private Image ObjectivesIcon;
    private final Board board;

    class PanelOutTransition extends PanelTransition {
        PanelOutTransition(ImageView background,ImageView text, int dir, boolean out){
            super(background,text, dir, out);
            setCycleDuration(Duration.seconds(8.0/60));
        }
        @Override
        protected void interpolate(double t) {
            switch (dir) {
                case 3:
                    background.setTranslateX(curX + t * (out ? 1 : -1) * background.getFitWidth()*1.1);
                    text.setTranslateX(curX + t * (out ? 1 : -1) * text.getFitWidth()*1.1);
                    break;
                case 2:
                    background.setTranslateY(curY + t * (out ? -1 : 1) * (background.getFitHeight())*1.1);
                    text.setTranslateY(curY + t * (out ? -1 : 1) * (text.getFitHeight())*1.1);
                    break;
            }
        }
    }

    class PanelInTransition extends PanelTransition{
        PanelInTransition(ImageView background, ImageView text, int dir, boolean out){
            super(background,text, dir, out);
            setCycleDuration(Duration.seconds(7./60));
        }
        @Override
        protected void interpolate(double t){
            switch (dir){
                case 3:
                    if (out) {
                        background.setTranslateX(curX + t * background.getFitWidth() - w*(board.getWidth()+3)*2*15/14-(double)2/14*background.getFitWidth());
                        text.setTranslateX(curX + t * text.getFitWidth() - w*(board.getWidth()+3)*2*15/14-(double)2/14*text.getFitWidth());
                    }
                    else {
                        background.setTranslateX(curX + (-t) * background.getFitWidth()*2*15/14 + w*(board.getWidth()+3)+(double)2/14*background.getFitWidth());
                        text.setTranslateX(curX + (-t) * text.getFitWidth()*2*15/14 + w*(board.getWidth()+3)+(double)2/14*text.getFitWidth());
                    }
                    break;
                case 2:
                    if (out) {
                        background.setTranslateY(curY + t * -2 * (background.getFitHeight() * 1.1) + h*(board.getHeight())+background.getFitHeight() * 1);
                        text.setTranslateY(curY + t * -2 * (text.getFitHeight() * 1.2) + h*(board.getHeight())+text.getFitHeight() * 1);
                    }
                    else {
                        background.setTranslateY(curY + t * 2 * (background.getFitHeight() * 1.1) - h*(board.getHeight())+background.getFitHeight() -2 * (background.getFitHeight()));
                        text.setTranslateY(curY + t * 2 * (text.getFitHeight() * 1.1) - h*(board.getHeight())+text.getFitHeight() -2 * (text.getFitHeight()));
                    }
                    break;
            }
        }
    }

    class PanelTransition extends Transition{
        ImageView background;
        ImageView text;
        boolean out;
        int dir;
        double curX;
        double lastTime = System.nanoTime();
        double curY;
        double w;
        double h;
        PanelTransition(ImageView background, ImageView text, int dir, boolean out){
            super();
            setCycleCount(1);
            setCycleDuration(Duration.seconds((double)15/90));
            setInterpolator(Interpolator.LINEAR);
            this.background = background;
            this.text = text;
            this.dir = dir;
            this.out = out;
            switch (dir){
                case 2:
                    h = background.getFitHeight()/2.5;
                    curY = out?(h/4):(board.getHeight()-2.5)*h-h/4;

                case 3:
                    w = background.getFitWidth()/3;
                    curX = out?(board.getWidth()-3)*w:0;
            }
        }
        @Override
        protected void interpolate(double t) {
            if (System.nanoTime() - lastTime > (double) 1000000000/1500000000) {
                switch (dir) {
                    case 3:
                        if (15*t<8) {
                            background.setTranslateX(curX + t * (out ? 1 : -1) * background.getFitWidth()*2*15/14);
                            text.setTranslateX(curX + t * (out ? 1 : -1) * text.getFitWidth()*2*15/14);

                        }
                        else if (out) {
                            background.setTranslateX(curX + t * background.getFitWidth() * 2 * 15 / 14 - w * (board.getWidth() + 3) - (double) 2 / 14 * background.getFitWidth());
                            text.setTranslateX(curX + t * text.getFitWidth() * 2 * 15 / 14 - w * (board.getWidth() + 3) - (double) 2 / 14 * text.getFitWidth());
                        }
                        else {
                            background.setTranslateX(curX + (-t) * background.getFitWidth()*2*15/14 + w*(board.getWidth()+3)+(double)2/14*background.getFitWidth());
                            text.setTranslateX(curX + (-t) * text.getFitWidth()*2*15/14 + w*(board.getWidth()+3)+(double)2/14*text.getFitWidth());
                        }
                        break;
                    case 2:
                        if (15*t<8) {
                            background.setTranslateY(curY + t * (out ? -1 : 1) * (background.getFitHeight() * 2 * 1.1));
                            text.setTranslateY(curY + t * (out ? -1 : 1) * (text.getFitHeight() * 2 * 1.1));
                        }
                        else if (out) {
                            background.setTranslateY(curY + t * -2 * (background.getFitHeight() * 1.1) + h*(board.getHeight())+background.getFitHeight() * 1-6);
                            text.setTranslateY(curY + t * -2 * (text.getFitHeight() * 1.1) + h*(board.getHeight())+text.getFitHeight() * 1-6);
                        }
                        else {
                            background.setTranslateY(curY + t * 2 * (background.getFitHeight() * 1.1) - h*(board.getHeight())+background.getFitHeight() -2 * (background.getFitHeight()));
                            text.setTranslateY(curY + t * 2 * (text.getFitHeight() * 1.1) - h*(board.getHeight())+text.getFitHeight() -2 * (text.getFitHeight()));
                        }
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
        superLoad();

    }

    public List<ImageView> drawMenu(String def, String avo, String terrainType, String objectives) throws IOException {

        // Drawing lower right corner
        ImageView TI_Background = new ImageView(new Image("file:Resources/MenuSprites/Terrain.png",48*6,54*6,false,false)){{
            setOpacity(.85);
            setTranslateX(tileSize*(board.getWidth()-tileSize/16.));
            setTranslateY(tileSize*(board.getHeight()-3)-3*tileSize/8.);
            setFitWidth(tileSize*3);
            setFitHeight(tileSize*3+3*tileSize/8.);
        }};


        ImageView TI_text = new ImageView(){{
            setTranslateX(tileSize*(board.getWidth()-tileSize/16.));
            setTranslateY(tileSize*(board.getHeight()-3)-3./8*tileSize);
            setFitWidth(tileSize*3);
            setFitHeight(tileSize*3+3./8*tileSize);
        }};
        updateTI(TI_text,def,avo,terrainType);

        //drawing upper right corner
        ImageView OI_Background = new ImageView((new Image("file:Resources/MenuSprites/Objectives.png",86*6,41*6,false,false))){{
            setOpacity(.85);
            setFitWidth((tileSize+19./24*tileSize)*3);
            setFitHeight(tileSize*3 - 7./16*tileSize);
            setTranslateX(tileSize*(board.getWidth()-5)-7./16*tileSize);
            setTranslateY(4*3);
        }};
        ImageView OI_Text = new ImageView(){{
            setFitWidth((tileSize+19./24*tileSize)*3);
            setFitHeight(tileSize*3 - 7./16*tileSize);
            setTranslateX(tileSize*(board.getWidth()-5)-7./16*tileSize);
            setTranslateY(4*3);
        }};
        Canvas OI = new Canvas(86*6,40*6);
        GraphicsContext g2 = OI.getGraphicsContext2D();
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
        OI_Text.setImage(g2.getCanvas().snapshot(new SnapshotParameters(){{setFill(Color.TRANSPARENT);}},null));

        return Arrays.asList(TI_Background,TI_text,OI_Background,OI_Text);
    }
    public ImageView drawIntermediateMenu(String[] menu) throws IOException {
        Canvas canvas = buildPanel(3, 2*menu.length-2);
        GraphicsContext g = canvas.getGraphicsContext2D();
        byte offset = 0;
        for (String action: menu) {
             g.drawImage(TxtI.convertTxt(action,"white"),4*6,7*6+offset*6*16);
             offset++;
        }
        return new ImageView(g.getCanvas().snapshot(new SnapshotParameters(){{setFill(Color.TRANSPARENT);}},null)){{
            setFitWidth(this.getImage().getWidth()/2* tileSize/48.);
            setFitHeight(this.getImage().getHeight()/2* tileSize/48.);}};
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
                    setFitWidth(getImage().getWidth()/2 * tileSize/48.);
                    setFitHeight(getImage().getHeight()/2 * tileSize/48.);
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
    private Image drawTI(String def, String avo, String terrainType, Canvas canvas) throws IOException {
        GraphicsContext g = canvas.getGraphicsContext2D();
        g.drawImage(TxtI.convertTNumToImg(def), canvas.getWidth() - 6 * 8 - TxtI.convertTNumToImg(def).getWidth(), 31 * 6);
        g.drawImage(TxtI.convertTNumToImg(avo), canvas.getWidth() - 6 * 8 - TxtI.convertTNumToImg(avo).getWidth(), 39 * 6);
        Image terrainName = TxtI.convertTxt(terrainType, "white");
        g.drawImage(terrainName, (canvas.getWidth() - terrainName.getWidth()) / 2, 17 * 6);
        return g.getCanvas().snapshot(new SnapshotParameters(){{setFill(Color.TRANSPARENT);}},null);
    }
    public void updateTI(ImageView TI_Text,String def, String avo, String terrainType) throws IOException {
        TI_Text.setImage(drawTI(def,avo,terrainType, new Canvas(48*6,54*6)));

    }
    public void animate(ImageView[] imageViews, int[] dirs, boolean[] outs){
        assert imageViews.length / 2 <= dirs.length && imageViews.length / 2 <= outs.length;
        ParallelTransition pTransition = new ParallelTransition();
        pTransition.setCycleCount(1);
        for (int i = 0; i < imageViews.length; i+=2){
            PanelTransition transition = new PanelTransition(imageViews[i],imageViews[i+1],dirs[i/2],outs[i/2]);
            pTransition.getChildren().add(transition);
        }
        pTransition.play();

    }
    public void animate(ImageView background, ImageView text, int dir, boolean out){
        PanelTransition transition = new PanelTransition(background,text,dir,out);
        transition.play();
    }
    public Transition transitionOut(ImageView[] imageViews, int[] dirs, boolean[] outs){
        assert imageViews.length / 2 <= dirs.length && imageViews.length / 2 <= outs.length;
        ParallelTransition pTransition = new ParallelTransition();
        pTransition.setCycleCount(1);
        for (int i = 0; i < imageViews.length; i+=2){
            PanelOutTransition outTransition = new PanelOutTransition(imageViews[i],imageViews[i+1],dirs[i/2],outs[i/2]);
            pTransition.getChildren().add(outTransition);
        }
        pTransition.play();
        return pTransition;
    }
    public Transition transitionIn(ImageView[] imageViews, int[] dirs, boolean[] outs){
        assert imageViews.length / 2 <= dirs.length && imageViews.length / 2 <= outs.length;
        ParallelTransition pTransition = new ParallelTransition();
        for (int i = 0; i < imageViews.length; i+=2){
            System.out.println(imageViews.length);
            System.out.println(i);
            PanelInTransition inTransition = new PanelInTransition(imageViews[i],imageViews[i+1],dirs[i/2],outs[i/2]);
            pTransition.getChildren().add(inTransition);
        }
        pTransition.play();
        return pTransition;
    }
}
