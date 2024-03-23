package GameEngine;

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

public class GameInterface {
    Image ObjectivesIcon;
    Image TerrainIconBackground;
    Board board;
    TextInterpreter TxtI = new TextInterpreter();
    TextInterpreter terrainTI;

    class panelTransition extends Transition{
        ImageView imv;
        boolean out;
        int dir;
        double curX;
        double lastTime = System.nanoTime();
        double curY;
        double w;
        double h;
        panelTransition(ImageView imv, int dir, boolean out){
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
        this.board = board;
        terrainTI = new TextInterpreter("terrainNumbers");
        load();
    }

    private void load() {
        ObjectivesIcon = new Image("file:Resources/MenuSprites/Objectives.png",86*6,40*6,false,false);
        TerrainIconBackground = new Image("file:Resources/MenuSprites/Terrain.png",48*6,54*6,false,false);
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
            Image Objectives = TxtI.convertTxt("white", objectivesList[0]);
            g2.drawImage(Objectives, (ObjectivesIcon.getWidth() - (Objectives.getWidth())) / 2, 6 * 6);
        }
        else {
            for (int i = 0; i < 2; i++) {
                Image Objectives = TxtI.convertTxt("white", objectivesList[i]);
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
    private Image drawTI(String def, String avo, String terrainType) throws IOException {
        Canvas TI = new Canvas(48 * 6, 54 * 6);
        GraphicsContext g = TI.getGraphicsContext2D();
        g.drawImage(TerrainIconBackground, 0, 0);
        g.drawImage(TxtI.convertTNumToImg(def), TerrainIconBackground.getWidth() - 6 * 8 - TxtI.convertTNumToImg(def).getWidth(), 31 * 6);
        g.drawImage(TxtI.convertTNumToImg(avo), TerrainIconBackground.getWidth() - 6 * 8 - TxtI.convertTNumToImg(avo).getWidth(), 39 * 6);
        Image terrainName = TxtI.convertTxt("white", terrainType);
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
            panelTransition transition = new panelTransition(key, animations.get(key).getKey(), animations.get(key).getValue());
            pTransition.getChildren().add(transition);
        });
        pTransition.play();

    }
    public void animate(ImageView imv, int dir, boolean out){
        panelTransition transition = new panelTransition(imv,dir,out);
        transition.play();
    }
}
