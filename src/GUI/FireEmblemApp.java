package GUI;

import GUI.PanelInterface.MainMenuInterface;
import GameEngine.MenuPointer;
import GameEngine.TextInterpreter;
import GameEngine.Save.Save;
import javafx.animation.AnimationTimer;
import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Point3D;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.ArrayList;

public class FireEmblemApp extends Application {
    private final Pane menuPane = new Pane();
    private final Scene menuScene = new Scene(menuPane);
    private Pane glowingPane = new Pane();
    private static final int WIDTH = 15*3*16;
    private static final int HEIGHT = 10*3*16;
    private int menuId, buttonID = 0;
    private final TextInterpreter TI = new TextInterpreter();
    private static ImageView cursor = new ImageView();
    DragonAnimation dragonAnimation;
    private Stage window;
    MenuPointer menuPointer, menuPointer2;
    int month = 1;
    String bloodType = "A";
    String gender = "Male";

    public FireEmblemApp() throws IOException {
    }
    
    @Override
    public void start(Stage stage) throws Exception {
        window = stage;
        window.setHeight(HEIGHT+37);
        window.setWidth(WIDTH);
        window.setScene(menuScene);
        menuScene.setOnKeyPressed(new MenuEventListener());
        //loadMenu(0);
        window.show();
        new Battle(window);
    }
    private void loadMenu(int ID) throws IOException {
        menuId = ID;
        String directory = "Resources/MainMenu/Menu"+ menuId+"/";
        ImageView background;
        switch (menuId){
            case 0:
                menuPane.getChildren().add(new ImageView(new Image("file:"+directory+"background.png",240*3,160*3,false,false)));
                menuPane.getChildren().add(new ImageView(new Image("file:"+directory+"axe.png",240*3,160*3,false,false)));
                menuPane.getChildren().add(new ImageView(new Image("file:"+directory+"sword.png",192*3,112*3,false,false)){{
                    setTranslateX(32*3);
                    setTranslateY(16*3);
                }});
                menuPane.getChildren().add(new ImageView(new Image("file:"+directory+"FAIAEMUBUREMU.png",240*3,48*3,false,false)){{
                    setTranslateY(48*3);
                }});
                menuPane.getChildren().add(new ImageView(new Image("file:"+directory+"FireEmblem.png",128*3,16*3,false,false)){{
                    setTranslateX((112*3)/2.);
                    setTranslateY(40*3);
                }});
                menuPane.getChildren().add(new ImageView(new Image("file:"+directory+"TheBlazingBlade3.png",112*3,24*3,false,false)){{
                    setTranslateX((128*3)/2.);
                    setTranslateY(96*3);
                }});
                menuPane.getChildren().add(new ImageView(new Image("file:"+directory+"Nintendo.png",208*3,8*3,false,false)){{
                    setTranslateX((16*3));
                    setTranslateY(144*3);
                }});
                menuPane.getChildren().add(new ImageView(new Image("file:"+directory+"PressStart.png",80*3,16*3,false,false)){{
                    setTranslateX((80*3));
                    setTranslateY(120*3);
                }});
                break;
            case 1:
                menuPane.getChildren().add(new ImageView(new Image("file:"+directory+"background.png",240*3,160*3,false,false)));
                ImageView circle = new ImageView(new Image("file:"+directory+"circle.png",240*3,240*3,false,false)){{
                setTranslateY(40*3);
                setOpacity(.75);
                setEffect(new ColorAdjust(){{
                    setContrast(.1);
                    }});
                }};
                Rotate rotation = new Rotate() {{
                    setAxis(new Point3D(1, 0, 0));
                    setPivotX(120*3);
                    setPivotY(120*3);
                    setAngle(60);
                }};
                circle.getTransforms().add(rotation);
                new AnimationTimer() {
                    int c = 0;
                    double lastTime = System.nanoTime();

                    @Override
                    public void handle(long l) {
                        if (System.nanoTime() - lastTime > 1000000000 / 36.) {
                            lastTime = System.nanoTime();
                            c++;
                            c = c%360;
                            if (circle.getTransforms().size() > 1) circle.getTransforms().remove(1);

                            Rotate rotation2 = new Rotate() {{
                                setAxis(new Point3D(0, 0 * Math.cos(Math.toRadians(60)), Math.sin(Math.toRadians(60))));
                                setPivotX(120 * 3);
                                setPivotY(120 * 3);
                                setAngle(c);
                            }};

                            circle.getTransforms().add(rotation2);
                        }
                    }
                }.start();

                menuPane.getChildren().add(circle);
                Pane buttonPane = new Pane();
                ImageView glowingButton = new ImageView(){{
                    setFitHeight(3*512./16);
                    setFitWidth(3*512./4);
                    setTranslateX(120*3-512.*3/8);
                    setTranslateY(120*3-512.*3/8);
                }};
                buttonPane.getChildren().add(glowingButton);
                new GlowingMenuPane().start(glowingButton);
                ImageView leftDragon = new ImageView(){{
                    setFitHeight(32*3);
                    setFitWidth(32*3);
                    setTranslateX(36*3);
                    setTranslateY(56*3);
                }};
                buttonPane.getChildren().add(leftDragon);
                new DragonAnimation(true).start(leftDragon);
                ImageView rightDragon = new ImageView(){{
                    setFitHeight(32*3);
                    setFitWidth(32*3);
                    setTranslateX(172*3);
                    setTranslateY(56*3);
                }};
                buttonPane.getChildren().add(rightDragon);
                new DragonAnimation(false).start(rightDragon);
                ImageView newGame = new ImageView(new Image("file:"+directory+"New Game.png",80*3,16*3,false,false)){{
                    setFitWidth(80*3);
                    setFitHeight(16*3);
                    setTranslateX(80*3);
                    setTranslateY(65*3);
                }};
                buttonPane.getChildren().add(newGame);
                menuPane.getChildren().add(buttonPane);
                break;
            case 2:
                Pane pane = new Pane();
                pane.getChildren().add(new ImageView(new Image("file:"+directory+"button.png",128*3,32*3,false,false)){{
                    setTranslateX(56*3);
                    setTranslateY(0);
                    setFitWidth(128*3);
                    setFitHeight(32*3);
                }});
                pane.getChildren().add(new ImageView(new Image("file:"+directory+"New Game.png",80*3,16*3,false,false)){{
                    setTranslateX(80*3);
                    setTranslateY(9*3);
                    setFitWidth(80*3);
                    setFitHeight(16*3);
                }});
                Pane savePane = new Pane();
                savePane.getChildren().add(new ImageView(new Image("file:"+directory+"savestate.png",224*3,32*3,false,false)){{
                    setTranslateX(12*3);
                    setTranslateY(32*3);
                    setFitWidth(224*3);
                    setFitHeight(32*3);
                }});
                savePane.getChildren().add(new ImageView(new Image("file:"+directory+"savestate.png",224*3,32*3,false,false)){{
                    setTranslateX(12*3);
                    setTranslateY(64*3);
                    setFitWidth(224*3);
                    setFitHeight(32*3);
                }});
                savePane.getChildren().add(new ImageView(new Image("file:"+directory+"savestate.png",224*3,32*3,false,false)){{
                    setTranslateX(12*3);
                    setTranslateY(96*3);
                    setFitWidth(224*3);
                    setFitHeight(32*3);
                }});
                savePane.getChildren().add(new ImageView(new Image("file:"+directory+"nodata.png",128*3,16*3,false,false)){{
                    setTranslateX(56*3);
                    setTranslateY(41*3);
                    setFitWidth(128*3);
                    setFitHeight(16*3);
                }});
                savePane.getChildren().add(new ImageView(new Image("file:"+directory+"nodata.png",128*3,16*3,false,false)){{
                    setTranslateX(56*3);
                    setTranslateY(73*3);
                    setFitWidth(128*3);
                    setFitHeight(16*3);
                }});
                savePane.getChildren().add(new ImageView(new Image("file:"+directory+"nodata.png",128*3,16*3,false,false)){{
                    setTranslateX(56*3);
                    setTranslateY(105*3);
                    setFitWidth(128*3);
                    setFitHeight(16*3);
                }});
                Pane playTime = new Pane();
                playTime.getChildren().add(new ImageView(new Image("file:"+directory+"smallButton.png",88*3,32*3,false,false)){{
                    setTranslateX(151*3);
                    setTranslateY(132*3);
                    setFitWidth(88*3);
                    setFitHeight(32*3);
                }});
                playTime.getChildren().add(new ImageView(new Image("file:"+directory+"play.png",64*3,8*3,false,false)){{
                    setTranslateX(159*3);
                    setTranslateY(130*3);
                    setFitWidth(64*3);
                    setFitHeight(8*3);
                }});

                playTime.getChildren().add(new ImageView(TI.convertTime(Save.loadTime(1),"")){{
                    setFitWidth(getImage().getWidth()/2);
                    setFitHeight(30);
                    setTranslateX(240*3-getFitWidth());
                    setTranslateY(144*3);

                }});
                Pane gPane = new Pane();
                glowingPane = gPane;
                gPane.getChildren().add(new ImageView(new Image("file:"+directory+"savestate(g).png",224*3,32*3,false,false)){
                    {
                        setTranslateX(12 * 3);
                        setTranslateY(32 * 3);
                        setFitWidth(224 * 3);
                        setFitHeight(32 * 3);
                    }});
                gPane.getChildren().add(new ImageView(new Image("file:"+directory+"nodata(g).png",128*3,16*3,false,false)){{
                    setTranslateX(56*3);
                    setTranslateY(41*3);
                    setFitWidth(128*3);
                    setFitHeight(16*3);
                }});
                ImageView dragon = new ImageView(){{
                    setFitWidth(32*3);
                    setFitHeight(32*3);
                    setTranslateX(4*3);
                    setTranslateY(32*3);
                }};
                gPane.getChildren().add(dragon);
                dragonAnimation = new  DragonAnimation(true);
                dragonAnimation.start(dragon);
                menuPane.getChildren().add(pane);
                menuPane.getChildren().add(savePane);
                menuPane.getChildren().add(playTime);
                menuPane.getChildren().add(gPane);
                break;
            case 10:
                background = new ImageView(new Image("file:"+directory+"background.png",512*3,160*3,false,false)){{
                    setFitHeight(160*3);
                    setFitWidth(240*3);
                    setViewport(new Rectangle2D(0,0,240*3,160*3));
                }};
                menuPane.getChildren().add(background);
                new ScrollTransition(background,256,15,1000000000,240,160).play();
                menuPane.getChildren().add(new ImageView(new Image("file:"+directory+"map.png",240*3,160*3,false,false)){{
                    setTranslateX(16*3);
                    setFitWidth(240*3);
                    setFitHeight(160*3);
                }});
                menuPane.getChildren().add(new ImageView(new Image("file:"+directory+"Info.png",40*6,16*6,false,false)){{
                    setFitWidth(40*3);
                    setFitHeight(16*3);
                    setTranslateX(184*3);
                    setTranslateY(16*3);
                }});
                ImageView scrollingMessage = new ImageView(new Image("file:"+directory+"press.png",536*6,16*6,false,false)){{
                    setFitWidth(240*3);
                    setFitHeight(16*3);
                    setViewport(new Rectangle2D(0,0,240*6,16*6));
                }};
                menuPane.getChildren().add(new Pane(){{
                    setWidth(240*3);
                    setHeight(16*3);
                    setTranslateY(144*3);
                    Polygon bg = new Polygon(
                            0, 4*3,
                            240*3, 4*3,
                            240*3,16*3,
                            0, 16*3
                    );
                    bg.setFill(Color.rgb(0,0,0,.1));

                    DropShadow ds = new DropShadow();
                    ds.setHeight(100);
                    ds.setSpread(.90);
                    ds.setColor(Color.rgb(0,0,0,1));
                    bg.effectProperty().set(ds);
                    getChildren().add(bg);
                    getChildren().add(scrollingMessage);
                }});
                new ScrollTransition(scrollingMessage,560,5,100000000,240*2,32).play();

                Font font1 = Font.loadFont("file:Resources/ThickFont.otf",38);
                Font font2 = Font.loadFont("file:Resources/ThinFont.otf",38);
                Pane textPane = new Pane();
                textPane.getChildren().add(new Text("Mark"){{
                    setTranslateX(386);
                    setTranslateY(150);
                    setFill(Color.web("#9c635a"));
                    setStyle("-fx-font-size: 38px;");
                    setFont(font1);
                }});
                textPane.getChildren().add(new Text("Mark"){{
                    setTranslateX(386);
                    setTranslateY(150);
                    setStyle("-fx-font-size: 38px;");
                    setFont(font2);
                }});
                textPane.getChildren().add(new Text("A"){{
                    setTranslateX(217);
                    setTranslateY(249);
                    setFill(Color.web("#9c635a"));
                    setStyle("-fx-font-size: 48px;");
                    setFont(font1);
                }});
                textPane.getChildren().add(new Text("A"){{
                    setTranslateX(217);
                    setTranslateY(249);
                    setStyle("-fx-font-size: 48px;");
                    setFont(font2);
                }});
                textPane.getChildren().add(new Text("January"){{
                    setTranslateX(355);
                    setTranslateY(252);
                    setFill(Color.web("#9c635a"));
                    setStyle("-fx-font-size: 38px;");
                    setFont(font1);
                }});
                textPane.getChildren().add(new Text("January"){{
                    setTranslateX(355);
                    setTranslateY(252);
                    setStyle("-fx-font-size: 38px;");
                    setFont(font2);
                }});
                textPane.getChildren().add(new Text("Male"){{
                    setTranslateX(541);
                    setTranslateY(252);
                    setFill(Color.web("#9c635a"));
                    setStyle("-fx-font-size: 38px;");
                    setFont(font1);
                }});
                textPane.getChildren().add(new Text("Male"){{
                    setTranslateX(541);
                    setTranslateY(252);
                    setStyle("-fx-font-size: 38px;");
                    setFont(font2);
                }});
                ResizableImage symbolsAsImg = new ResizableImage("file:Resources/FE7Symbols.png",472,274);
                menuPane.getChildren().add(new ImageView(symbolsAsImg.getSubimage((138+getTacticianAffinity("A",1)*15)*6,17*6,14*6,14*6)){{
                    setFitWidth(14*3);
                    setFitHeight(14*3);
                    setTranslateX(113*3);
                    setTranslateY(42*3);
                }});

                menuPane.getChildren().add(textPane);


                menuPointer = new MenuPointer();
                menuPointer.setTranslateX(94*3);
                menuPointer.setTranslateY(43*3);
                menuPointer.play();
                menuPane.getChildren().add(menuPointer);
                break;
            case 11:
                menuPointer2 = new MenuPointer();
                menuPointer2.setTranslateX(20*3);
                menuPointer2.setTranslateY(98*3);
                menuPointer2.play();
                menuPane.getChildren().add(menuPointer2);
                break;
            case 12:
                menuPointer2 = new MenuPointer();
                menuPointer2.setTranslateX(12*3);
                menuPointer2.setTranslateY(97*3);
                menuPointer2.play();
                menuPane.getChildren().add(menuPointer2);
                break;
            case 13:
                menuPointer2 = new MenuPointer();
                menuPointer2.setTranslateX(164*3);
                menuPointer2.setTranslateY(98*3);
                menuPointer2.play();
                menuPane.getChildren().add(menuPointer2);
                break;
            case 20:
                System.out.println(44);
                background = new ImageView(new Image("file:"+directory+"background.png",512*3,160*3,false,false)){{
                    setFitWidth(240*3);
                    setFitHeight(160*3);
                }};
                new ScrollTransition(background,256,15,1000000000,240,160).play();
                menuPane.getChildren().add(background);
                ImageView panel = new MainMenuInterface().drawRawPanel(26,8);
                panel.setTranslateX(4*3);
                panel.setTranslateY(68*3);
                menuPane.getChildren().add(panel);
                menuPane.getChildren().add(new ImageView(new Image("file:"+directory+"Entry.png",128*6,32*6,false,false)){{
                    setFitWidth(128*3);
                    setFitHeight(32*3);
                }});
                menuPane.getChildren().add(new ImageView(new Image("file:"+directory+"alphabet1.png",75*6,175*6,false,false)){{
                    setFitWidth(175*3);
                    setFitHeight(75*3);
                    setTranslateX(16*3);
                    setTranslateY(75*3);
                }});
                menuPane.getChildren().add(new ImageView(new Image("file:"+directory+"banner.png",80*6,32*6,false,false)){{
                    setFitWidth(80*3);
                    setFitHeight(32*3);
                    setTranslateX(80*3);
                    setTranslateY(32*3);
                }});
                menuPane.getChildren().add(new ImageView(new Image("file:"+directory+"name.png",32*6,8*6,false,false)){{
                    setFitWidth(32*3);
                    setFitHeight(8*3);
                    setTranslateX(104*3);
                    setTranslateY(32*3);
                }});
                menuPane.getChildren().add(new ImageView(new Image("file:"+directory+"ABC_dark.png",32*6,16*6,false,false)){{
                    setFitWidth(32*3);
                    setFitHeight(16*3);
                    setTranslateX(196*3);
                    setTranslateY(72*3);
                }});
                menuPane.getChildren().add(new ImageView(new Image("file:"+directory+"abc_light.png",32*6,16*6,false,false)){{
                    setFitWidth(32*3);
                    setFitHeight(16*3);
                    setTranslateX(196*3);
                    setTranslateY(88*3);
                }});
                menuPane.getChildren().add(new ImageView(new Image("file:"+directory+"delete.png",32*6,16*6,false,false)){{
                    setFitWidth(32*3);
                    setFitHeight(16*3);
                    setTranslateX(196*3);
                    setTranslateY(120*3);
                }});
                menuPane.getChildren().add(new ImageView(new Image("file:"+directory+"OK.png",32*6,16*6,false,false)){{
                    setFitWidth(32*3);
                    setFitHeight(16*3);
                    setTranslateX(196*3);
                    setTranslateY(136*3);
                }});
                cursor = new ImageView(){{
                    setFitWidth(16*3);
                    setFitHeight(16*3);
                    setTranslateX(12*3);
                    setTranslateY(73*3);
                }};
                new AnimationTimer(){
                    private double lastTime = System.nanoTime();

                    private final ArrayList<Image> images = new ArrayList<>(){{
                        for (int i = 1; i < 17; i++) {
                            add(new Image("file:Resources/MainMenu/Menu20/Cursor/"+i+".png",16*6,16*6,false,false));
                        }
                    }};
                    private int k = 0;
                    @Override
                    public void handle(long l) {
                        if (System.nanoTime() - lastTime > 100000000./3){
                            lastTime = System.nanoTime();
                            cursor.setImage(images.get(k));
                            k++;
                            if (k==15) k=0;
                        }
                }}.start();
                menuPane.getChildren().add(cursor);

                break;


        }

    }
    private class MenuEventListener implements EventHandler<KeyEvent>{
        @Override
        public void handle(KeyEvent event) {
            int delta;
            switch (menuId){
                case 0:
                    if (event.getCode()!= KeyCode.TAB && event.getCode()!= KeyCode.ALT) {
                        clear(1);
                        try {
                            loadMenu(1);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    break;
                case 1:
                    switch (event.getCode()){
                        case KeyCode.ENTER:
                            clear(2);
                            try {
                                loadMenu(2);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                            break;
                    }
                    break;
                case 2:
                    switch (event.getCode()){
                        case KeyCode.UP,KeyCode.DOWN:
                            try {
                                changeButton(glowingPane, event.getCode()==KeyCode.DOWN);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                            break;
                        case KeyCode.ENTER:
                            clear(3);
                            try {
                                loadMenu(10);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                    }
                    break;
                case 10:
                    switch (event.getCode()){
                        case KeyCode.DOWN:
                            if (menuPointer.getTranslateY() == 43*3) {
                                menuPointer.setTranslateY(76*3);
                                menuPointer.changeTranslateX(99*3);
                            }
                            break;
                        case KeyCode.UP:
                            if (menuPointer.getTranslateY() == 76*3) {
                                menuPointer.setTranslateY(43*3);
                                menuPointer.changeTranslateX(94*3);
                            }
                            break;
                        case KeyCode.LEFT:
                            if (menuPointer.getTranslateY() == 76*3 && menuPointer.getBaseX() == 99*3) menuPointer.changeTranslateX(52*3);
                            else if (menuPointer.getTranslateY() == 76*3 && menuPointer.getBaseX() == 160*3) menuPointer.changeTranslateX(99*3);
                            break;
                        case KeyCode.RIGHT:
                            if (menuPointer.getTranslateY() == 76*3 && menuPointer.getBaseX() == 52*3) menuPointer.changeTranslateX(99*3);
                            else if (menuPointer.getTranslateY() == 76*3 && menuPointer.getBaseX() == 99*3) menuPointer.changeTranslateX(160*3);
                            break;
                        case KeyCode.ENTER:
                            menuPointer.stop();
                            if (menuPointer.getTranslateY() == 43*3){
                                clear(10);
                                try {
                                    loadMenu(20);
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                            else if (menuPointer.getBaseX() == 52 * 3) {
                                MainMenuInterface MI;
                                try {
                                    MI = new MainMenuInterface();
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                                ImageView imageView = MI.drawBTPanel();
                                menuPane.getChildren().add(imageView);
                                try {
                                    loadMenu(11);
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                            else if (menuPointer.getBaseX() == 99 * 3) {
                                MainMenuInterface MI;
                                try {
                                    MI = new MainMenuInterface();
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                                ImageView imageView = MI.drawMonthPanel();
                                menuPane.getChildren().add(imageView);
                                try {
                                    loadMenu(12);
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                            else {
                                MainMenuInterface MI;
                                try {
                                    MI = new MainMenuInterface();
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                                ImageView imageView = MI.drawGenderPanel();
                                menuPane.getChildren().add(imageView);
                                try {
                                    loadMenu(13);
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                            break;


                    }
                    break;
                case 11:
                    switch (event.getCode()){
                        case KeyCode.RIGHT:
                            delta = (menuPointer2.getBaseX()==91*3?0:(menuPointer2.getBaseX()==44*3?23:24));
                            menuPointer2.changeTranslateX(menuPointer2.getBaseX()+delta*3);
                            break;
                        case KeyCode.LEFT:
                            delta = (menuPointer2.getBaseX()==20*3?0:(menuPointer2.getBaseX()==67*3?23:24));
                            menuPointer2.changeTranslateX(menuPointer2.getBaseX()-delta*3);
                            break;
                        case KeyCode.ENTER:
                            menuPane.getChildren().removeLast();
                            menuPane.getChildren().removeLast();
                            menuPointer.play();
                            bloodType = switch ((int) menuPointer2.getBaseX()) {
                                case 20 * 3 -> "A";
                                case 44 * 3 -> "B";
                                case 67 * 3 -> "O";
                                case 91 * 3 -> "AB";
                                default -> throw new RuntimeException();
                            };
                            menuId = 10;
                            Pane textPane = (Pane) menuPane.getChildren().get(menuPane.getChildren().size()-2);
                            textPane.getChildren().remove(2);
                            textPane.getChildren().remove(2);

                            textPane.getChildren().add(2,new Text(bloodType){{
                                setTranslateX(217);
                                setTranslateY(249);
                                setFill(Color.web("#9c635a"));
                                setStyle("-fx-font-size: 48px;");
                                setFont(Font.loadFont("file:Resources/ThickFont.otf",38));
                            }});
                            textPane.getChildren().add(3,new Text(bloodType){{
                                setTranslateX(217);
                                setTranslateY(249);
                                setStyle("-fx-font-size: 48px;");
                                setFont(Font.loadFont("file:Resources/ThinFont.otf",38));
                            }});
                            ResizableImage symbolsAsImg = new ResizableImage("file:Resources/FE7Symbols.png",472,274);
                            ResizableImage icon;
                            try {
                                icon = symbolsAsImg.getSubimage((138 + getTacticianAffinity(bloodType, month) * 15) * 6, 17 * 6, 14 * 6, 14 * 6);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                            menuPane.getChildren().remove(4);
                            menuPane.getChildren().add(4, new ImageView(icon) {{
                                setFitWidth(14 * 3);
                                setFitHeight(14 * 3);
                                setTranslateX(113 * 3);
                                setTranslateY(42 * 3);
                            }});
                            break;
                    }
                    break;
                case 12:
                    switch (event.getCode()){
                        case KeyCode.RIGHT:
                            delta = (menuPointer2.getBaseX()==171*3?0:(menuPointer2.getBaseX()==12*3?31:32));
                            menuPointer2.changeTranslateX(menuPointer2.getBaseX()+delta*3);
                            break;
                        case KeyCode.LEFT:
                            delta = (menuPointer2.getBaseX()==12*3?0:(menuPointer2.getBaseX()==43*3?31:32));
                            menuPointer2.changeTranslateX(menuPointer2.getBaseX()-delta*3);
                            break;
                        case KeyCode.DOWN:
                            menuPointer2.setTranslateY(114*3);
                            break;
                        case KeyCode.UP:
                            menuPointer2.setTranslateY(97*3);
                            break;
                        case KeyCode.ENTER:
                            month = (int) (menuPointer2.getBaseX()/3/32) +1 + (menuPointer2.getTranslateY()>100*3?6:0);
                            menuPane.getChildren().removeLast();
                            menuPane.getChildren().removeLast();
                            Pane textPane = (Pane) menuPane.getChildren().get(menuPane.getChildren().size()-2);
                            textPane.getChildren().remove(4);
                            textPane.getChildren().remove(4);
                            String _month = switch (month){
                                case 1 -> "January";
                                case 2 -> "February";
                                case 3 -> "March";
                                case 4 -> "April";
                                case 5 -> "May";
                                case 6 -> "June";
                                case 7 -> "July";
                                case 8 -> "August";
                                case 9 -> "September";
                                case 10 -> "October";
                                case 11 -> "November";
                                case 12 -> "December";
                                default -> throw new IllegalStateException("Unexpected value: " + month);
                            };
                            menuId = 10;
                            textPane.getChildren().add(4, new Text(_month){{
                                setTranslateX(355);
                                setTranslateY(252);
                                setFill(Color.web("#9c635a"));
                                setStyle("-fx-font-size: 38px;");
                                setFont(Font.loadFont("file:Resources/ThickFont.otf",38));
                            }});
                            textPane.getChildren().add(5, new Text(_month){{
                                setTranslateX(355);
                                setTranslateY(252);
                                setStyle("-fx-font-size: 38px;");
                                setFont(Font.loadFont("file:Resources/ThinFont.otf",38));
                            }});
                            ResizableImage symbolsAsImg = new ResizableImage("file:Resources/FE7Symbols.png",472,274);
                            ResizableImage icon;
                            try {
                                icon = symbolsAsImg.getSubimage((138 + getTacticianAffinity(bloodType, month) * 15) * 6, 17 * 6, 14 * 6, 14 * 6);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                            menuPane.getChildren().remove(4);
                            menuPane.getChildren().add(4, new ImageView(icon) {{
                                setFitWidth(14 * 3);
                                setFitHeight(14 * 3);
                                setTranslateX(113 * 3);
                                setTranslateY(42 * 3);
                            }});
                            menuPointer.play();
                            break;
                    }
                    break;
                case 13:
                    switch (event.getCode()){
                        case KeyCode.RIGHT:
                            if (menuPointer2.getBaseX()==164*3) menuPointer2.changeTranslateX(menuPointer2.getBaseX()+24*3);
                            break;
                        case KeyCode.LEFT:
                            if (menuPointer2.getBaseX()==188*3) menuPointer2.changeTranslateX(menuPointer2.getBaseX()-24*3);
                            break;
                        case KeyCode.ENTER:
                            menuId = 10;
                            if (menuPointer2.getBaseX() == 164*3) gender = "Male";
                            else gender = "Female";
                            menuPane.getChildren().removeLast();
                            menuPane.getChildren().removeLast();
                            Pane textPane = (Pane) menuPane.getChildren().get(menuPane.getChildren().size()-2);
                            textPane.getChildren().remove(6);
                            textPane.getChildren().remove(6);
                            textPane.getChildren().add(6, new Text(gender){{
                                setTranslateX(541);
                                setTranslateY(252);
                                setFill(Color.web("#9c635a"));
                                setStyle("-fx-font-size: 38px;");
                                setFont(Font.loadFont("file:Resources/ThickFont.otf",38));
                            }});
                            textPane.getChildren().add(7, new Text(gender){{
                                setTranslateX(541);
                                setTranslateY(252);
                                setStyle("-fx-font-size: 38px;");
                                setFont(Font.loadFont("file:Resources/ThinFont.otf", 38));
                            }});
                            menuPointer.play();
                            break;
                    }
                    break;
                case 20:
                    switch (event.getCode()){
                        case KeyCode.RIGHT:
                            switch ((int) cursor.getTranslateX()){
                                case 12*3:
                                    cursor.setTranslateX(cursor.getTranslateX()+9*3);
                                    break;
                                case 51*3,115*3:
                                    cursor.setTranslateX(cursor.getTranslateX()+24*3);
                                    break;
                                default:
                                    cursor.setTranslateX(cursor.getTranslateX()+10*3);
                                    break;
                            }
                            break;
                        case KeyCode.LEFT:
                            switch ((int) cursor.getTranslateX()){
                                case 12*3:
                                    break;
                                case 21*3:
                                    cursor.setTranslateX(cursor.getTranslateX()-9*3);
                                    break;
                                case 75*3,139*3:
                                    cursor.setTranslateX(cursor.getTranslateX()-24*3);
                                    break;
                                default:
                                    cursor.setTranslateX(cursor.getTranslateX()-10*3);
                                    break;
                            }
                            break;
                    }
                    break;
            }

        }
    }
    private void clear(int ID){
        switch (ID) {
            case 1, 3, 10:
                menuPane.getChildren().clear();
                break;
            case 2:
                menuPane.getChildren().removeLast();
                break;
        }
    }
    private void changeButton(Pane glowingPane, boolean down) throws IOException {
        int buttonNum = 3;
        ImageView dragonL = (ImageView) glowingPane.getChildren().removeLast();
        int delta = down?1:-1;
        buttonID+=delta;
        if (buttonID==3 || buttonID==-1){
            buttonID = (buttonID+3)%3;
            delta *= -(buttonNum-1);
        }
        final int deltaFinal = delta;
        glowingPane.getChildren().forEach( e->{
            e.setTranslateY(e.getTranslateY()+32*3*deltaFinal);
        });
        dragonAnimation.translateY(32*3*delta);
        glowingPane.getChildren().add(dragonL);
        int i = (int)((Pane) menuPane.getChildren().getLast()).getChildren().getFirst().getTranslateY()/96;
        ((ImageView)((Pane)menuPane.getChildren().get(4)).getChildren().getLast()).setImage(TI.convertTime(Save.loadTime(i),""));



    }
    private int getTacticianAffinity(String bloodType, int month){
        return switch (bloodType) {
            case "A" -> switch (month) {
                case 9 -> 1;
                case 6, 12 -> 2;
                case 1, 3, 10 -> 3;
                case 2, 8 -> 4;
                case 4, 7, 11 -> 5;
                case 5 -> 6;
                default -> throw new IllegalStateException("Unexpected value: " + month);
            };
            case "B" -> switch (month) {
                case 2, 4, 12 -> 1;
                case 5, 11 -> 2;
                case 9 -> 3;
                case 1, 3, 7 -> 4;
                case 6, 10 -> 5;
                case 8 -> 7;
                default -> throw new IllegalStateException("Unexpected value: " + month);
            };
            case "O" -> switch (month) {
                case 1, 5, 8 -> 1;
                case 2, 4 -> 2;
                case 7, 11 -> 3;
                case 6, 10, 12 -> 4;
                case 3, 9 -> 5;
                default -> throw new IllegalStateException("Unexpected value: " + month);
            };
            case "AB" -> switch (month) {
                case 6, 10 -> 1;
                case 3, 7, 9 -> 2;
                case 1, 4, 11 -> 3;
                case 5 -> 4;
                case 2, 8 -> 5;
                case 12 -> 6;

                default -> throw new IllegalStateException("Unexpected value: " + month);
            };
            default -> throw new IllegalStateException("Unexpected value: " + bloodType);
        };
    }
    private static class ScrollTransition extends Transition {
        double lastTime = System.nanoTime();
        private final double span, portWidth, portHeight;
        private final int timeSpan;
        ImageView scrollIMV;
        @Override
        protected void interpolate(double v) {
            if (System.nanoTime()-lastTime>timeSpan/60.){
                lastTime = System.nanoTime();
                double k = 3*span*v;
                scrollIMV.setViewport(new Rectangle2D(k,0,portWidth*3,portHeight*3));
            }}

        ScrollTransition(ImageView imv, double span,int timeDuration, int timeSpan, double portWidth, double portHeight){
            this.portWidth = portWidth;
            this.portHeight = portHeight;
            this.timeSpan = timeSpan;
            this.span = span;
            scrollIMV = imv;
            setCycleDuration(Duration.seconds(timeDuration));
            setCycleCount(INDEFINITE);
            setInterpolator(Interpolator.LINEAR);
        }
    }
    private class GlowingMenuPane extends AnimationTimer {

        String directory = "Resources/MainMenu/Menu" + menuId + "/";
        Image glowingButton = new Image("file:" + directory + "glowingPane.png", 512 * 3, 512 * 3, false, false);
        double lastTime = System.nanoTime();
        int c = 0;
        ImageView imv;

        public void start(ImageView imv) {
            this.imv = imv;
            this.imv.setImage(glowingButton);
            this.imv.setViewport(new Rectangle2D(0, 0, 3 * 512. / 4, 3 * 512. / 16));
            start();
        }

        @Override
        public void handle(long l) {

            if (System.nanoTime() - lastTime > 100000000./(30*48./64)) {
                lastTime = System.nanoTime();
                int h = c / 4 * (512 * 3 / 16);
                int w = c % 4 * (512 * 3 / 4);
                imv.setViewport(new Rectangle2D(w, h, 3 * 512. / 4, 3 * 512. / 16));
                c++;
                c = c % 64;
            }
        }
    }
    private class DragonAnimation extends AnimationTimer{
        private final Image dragon;
        private ImageView imv;
        private double y;
        DragonAnimation(boolean l){
            String directory = "Resources/MainMenu/Menu" + menuId + "/";
            dragon = new Image("file:"+directory+"dragon("+(l?"L":"R")+").png",96,96,false,false);
        }
        public void start(ImageView imv){
            this.imv = imv;
            this.imv.setImage(dragon);
            y = imv.getTranslateY();
            start();
        }
        public void translateY(double delta){
            y += delta;
        }
        int c = 0;
        double lastTime = System.nanoTime();
        @Override
        public void handle(long l) {
            if (System.nanoTime() - lastTime > 100000000./(30*48./64)) {
                lastTime = System.nanoTime();
                double offset = (c>8?3:0)+(c>16?3:0)+(c>24?3:0)-(c>40?3:0)-(c>48?3:0)-(c>56?3:0);
                imv.setTranslateY(y+offset);
                c++;
                c=c%64;
            }

        }
    }


    public static void main(String[] args) {
        launch();
    }
}
