package GUI.PanelInterface;

import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

import java.io.IOException;

public class MainMenuInterface extends PanelInterface{
    public MainMenuInterface() throws IOException {
        superLoad();
    }

    @Override
    protected void load() {
        superLoad();
    }
    public ImageView drawBTPanel(){
        GraphicsContext g = buildPanel(10,0).getGraphicsContext2D();
        g.drawImage(new Image("file:Resources/MainMenu/Menu10/bloodTypes.png",96*6,16*6,false,false),10*6,4*6);
        return new ImageView(g.getCanvas().snapshot(new SnapshotParameters(){{setFill(Color.TRANSPARENT);}},null)){{
            setFitWidth((12+8*10+13)*3);
            setFitHeight(25*3);
            setTranslateX(28*3);
            setTranslateY(92*3);
        }};
    }
    public ImageView drawMonthPanel(){
        GraphicsContext g = buildPanel(22, 2).getGraphicsContext2D();
        g.drawImage(new Image("file:Resources/MainMenu/Menu10/months.png",192*6,32*6,false,false),10*6,4*6);
        return new ImageView(g.getCanvas().snapshot(new SnapshotParameters(){{setFill(Color.TRANSPARENT);}},null)){{
            setFitWidth((12+8*22+13)*3);
            setFitHeight((12+8*2+13)*3);
            setTranslateX(20*3);
            setTranslateY(92*3);
        }};
    }
    public ImageView drawGenderPanel(){
        GraphicsContext g = buildPanel(3, 0).getGraphicsContext2D();
        g.drawImage(new Image("file:Resources/MainMenu/Menu10/gender.png",32*6,16*6,false,false),10*6,4*6);
        return new ImageView(g.getCanvas().snapshot(new SnapshotParameters(){{setFill(Color.TRANSPARENT);}},null)){{
            setFitWidth((12+8*3+13)*3);
            setFitHeight((12+13)*3);
            setTranslateX(172*3);
            setTranslateY(92*3);
        }};
    }
    public ImageView drawRawPanel(int w, int h){
        return new ImageView(buildPanel(w,h).snapshot(new SnapshotParameters(){{setFill(Color.TRANSPARENT);}},null)){{
            setFitWidth(getImage().getWidth()/2);
            setFitHeight(getImage().getHeight()/2);
        }};
    }
}
