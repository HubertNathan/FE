package GUI;

import EventHandlers.KeyEventHandler;
import GUI.Animations.ColouredSquaresAnimation;
import GUI.Animations.ImageViewer;
import GUI.Animations.SpriteAnimation;
import GameEngine.Board;
import GameEngine.Cursor;
import GameEngine.GameInterface;
import GameEngine.Square;
import Units.Brigand;
import Units.Cavalier;
import Units.Lyn_Lord;
import Units.Unit;
import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.ParallelTransition;
import javafx.animation.Transition;
import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.Pair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class FireEmblemApp extends Application {
    private Stage window;
    private Scene gameScene;
    private Board board;
    private Pane root = new Pane();
    private final Pane unitPane = new Pane();
    private final Pane colouredSquaresPane = new Pane();
    private final Pane arrowPane = new Pane();
    private Pane MenuPane = new Pane();
    private GameInterface gameInterface;
    ColouredSquaresAnimation colouredSquaresAnimation;
    private ImageView background;
    private Cursor cursor;
    double tileWidth = 48;
    double tileHeight = 48;
    ArrayList<Integer> currentPath;
    Transition CursorAnimation;
    final HashMap<String,Image> arrows = new HashMap<>(){
        {
            put("01", new Image(getClass().getResource("Arrows/01.png").toExternalForm(),92,92,false,false));
            put("02", new Image(getClass().getResource("Arrows/02.png").toExternalForm(),92,92,false,false));
            put("03", new Image(getClass().getResource("Arrows/03.png").toExternalForm(),92,92,false,false));
            put("04", new Image(getClass().getResource("Arrows/04.png").toExternalForm(),92,92,false,false));
            put("10", new Image(getClass().getResource("Arrows/10.png").toExternalForm(),92,92,false,false));
            put("12", new Image(getClass().getResource("Arrows/12.png").toExternalForm(),92,92,false,false));
            put("13", new Image(getClass().getResource("Arrows/13.png").toExternalForm(),92,92,false,false));
            put("14", new Image(getClass().getResource("Arrows/14.png").toExternalForm(),92,92,false,false));
            put("20", new Image(getClass().getResource("Arrows/20.png").toExternalForm(),92,92,false,false));
            put("23", new Image(getClass().getResource("Arrows/23.png").toExternalForm(),92,92,false,false));
            put("24", new Image(getClass().getResource("Arrows/24.png").toExternalForm(),92,92,false,false));
            put("30", new Image(getClass().getResource("Arrows/30.png").toExternalForm(),92,92,false,false));
            put("34", new Image(getClass().getResource("Arrows/34.png").toExternalForm(),92,92,false,false));
            put("40", new Image(getClass().getResource("Arrows/40.png").toExternalForm(),92,92,false,false));

        }};

    public void b() throws IOException {
        root = (Pane) gameScene.getRoot();
        new CombatRenderer(this,window,board,window.getScene(),board.get(2,2).getUnit(),board.get(6,5).getUnit());
    }
    public void revertToGameScene(){
        System.out.println(root.getChildren());
        gameScene.setRoot(root);
        window.setScene(gameScene);
    }

    class CursorTransition extends Transition {
        ImageView imv;
        public Image backup;
        CursorTransition(ImageView imv){
            this.imv = imv;
            cursor1 = cursor;
            cursor.setIMV(imv);
            cursorImageView = imv;
            setCycleDuration(Duration.millis(1200));
            setInterpolator(Interpolator.LINEAR);
            setCycleCount(Transition.INDEFINITE);
        }

        final Cursor cursor1;
        final ImageView cursorImageView;
        int lastIndex = 0;
        final HashMap<Integer, Image> CursorMap = new HashMap<>() {{
            put(1, new Image("file:src/GUI/CursorSprites/Cursor1.png", 192, 192, false, false));
            put(2, new Image("file:src/GUI/CursorSprites/Cursor2.png", 192, 192, false, false));
            put(3, new Image("file:src/GUI/CursorSprites/Cursor3.png", 192, 192, false, false));
        }};
        public Image getImage(){
            return backup;
        }


        private int updateAnimation(double animFrame) {
            return 3 + (animFrame > 1 ? -1 : 0) + (animFrame > 3 ? -1 : 0) + (animFrame > 19 ? 1 : 0) + (animFrame > 21 ? 1 : 0);
        }

        @Override
        protected void interpolate(double k) {
            int index = updateAnimation(36 * k);
            if ((index != lastIndex && cursor.getUnit() == null)||cursor.getSelectedUnit() != null) {
                backup = CursorMap.get(index);
                imv.setImage(CursorMap.get(index));
            }
            lastIndex = index;
        }

    };
    @Override
    public void start(Stage stage) throws Exception {
        window = stage;
        gameScene = new Scene(root);
        gameScene.setFill(Color.WHITE);
        ReadMapFile mapFile = new ReadMapFile("CH1");
        loadBoard(mapFile);
        loadCursor();
        gameInterface = new GameInterface(board);
        MenuPane.getChildren().addAll(gameInterface.drawMenu(Integer.toString(cursor.getSquare().getTerrain().getDef()),Integer.toString(cursor.getSquare().getTerrain().getAvoid()),cursor.getSquare().getTerrain().toString(),mapFile.getObjectives()));
        window.setTitle("Fire Emblem");
        window.show();
        window.setScene(gameScene);
        addEventListeners();
    }

    private void loadBoard(ReadMapFile mapReader) throws IOException {
        board = new Board(mapReader);
        loadBackground();
        root.getChildren().add(colouredSquaresPane);
        root.getChildren().add(arrowPane);
        colouredSquaresAnimation = new ColouredSquaresAnimation(board, colouredSquaresPane);
        board.setUnit(new Lyn_Lord(), 2, 2, true);
        board.setUnit(new Cavalier("Sain"), 4, 4);
        board.setUnit(new Brigand("Brigand"), 6, 5);
        displayUnits(board);
        root.getChildren().add(MenuPane);
    }

    private void loadBackground() {
        background = new ImageView(board.drawFX(new Canvas(16 * board.getWidth(), 16 * board.getHeight())).snapshot(null, new WritableImage(16 * board.getWidth(), 16 * board.getHeight())));
        ResizeBackground(6);
        background.setFitWidth(48 * board.getWidth());
        background.setFitHeight(48 * board.getHeight());
        root.getChildren().add(background);
    }

    private void loadCursor() {
        Unit leader = board.getLeader();
        cursor = new Cursor(leader, board);
        ImageView cursorImageView = new ImageView(new Image("file:src/GUI/CursorSprites/Cursor1.png", 192, 192, false, false));
        displayCursor(cursorImageView);
        cursorImageView.setFitHeight(96);
        cursorImageView.setFitWidth(96);
        cursorImageView.setTranslateX(leader.getXValue() * 48 - 24);
        cursorImageView.setTranslateY(leader.getYValue() * 48 - 12);
        root.getChildren().add(cursorImageView);
    }

    private void addEventListeners() {
        window.widthProperty().addListener((obs, oldVal, newVal) -> {
            background.setFitWidth((Double) newVal);
            tileWidth = (Double) newVal / board.getWidth();
            setUnitFitWidth(tileWidth);
            setCursorFitWidth(tileWidth);
            setSquaresFitWidth(tileWidth);
            setArrowsFitWidth((Double) oldVal / board.getWidth(),tileWidth);
            setMenuFitWidth((Double) oldVal / board.getWidth(),tileWidth);

        });

        window.heightProperty().addListener((obs, oldVal, newVal) -> {
            background.setFitHeight((Double) newVal - 37);
            tileHeight = ((Double) newVal - 37) / board.getHeight();
            setUnitFitHeight(tileHeight);
            setCursorFitHeight(tileHeight);
            setSquaresFitHeight(tileHeight);
            setArrowsFitHeight(((Double)oldVal-37 )/ board.getHeight(),tileHeight);
            setMenuFitHeight(((Double)oldVal-37)/ board.getHeight(),tileHeight);
        });
        gameScene.setOnKeyPressed(new KeyEventHandler(this));
    }

    private void ResizeBackground(int scaleFactor) {
        Image image = background.getImage();
        background = new ImageView(
                resample(
                        image,
                        scaleFactor
                )
        );
    }

    private Image resample(Image input, int scaleFactor) {
        final int W = (int) input.getWidth();
        final int H = (int) input.getHeight();
        final int S = scaleFactor;

        WritableImage output = new WritableImage(
                W * S,
                H * S
        );

        PixelReader reader = input.getPixelReader();
        PixelWriter writer = output.getPixelWriter();

        for (int y = 0; y < H; y++) {
            for (int x = 0; x < W; x++) {
                final int argb = reader.getArgb(x, y);
                for (int dy = 0; dy < S; dy++) {
                    for (int dx = 0; dx < S; dx++) {
                        writer.setArgb(x * S + dx, y * S + dy, argb);
                    }
                }
            }
        }

        return output;
    }

    private void setSquaresFitWidth(double width) {
        colouredSquaresPane.getChildren().forEach(sq -> {
            if (sq instanceof ImageViewer) {
                ((ImageViewer) sq).setFitWidth(width);
                sq.setTranslateX(width * ((ImageViewer) sq).getXValue());
            }
        });
    }

    private void setSquaresFitHeight(double height) {
        colouredSquaresPane.getChildren().forEach(sq -> {
            if (sq instanceof ImageViewer) {
                ((ImageViewer) sq).setFitHeight(height);
                sq.setTranslateY(height * ((ImageViewer) sq).getYValue());
            }
        });
    }

    private void setUnitFitWidth(double width) {
        board.getUnits().forEach(unit -> {
            if (unit != null) {
                ImageView imv = unit.getImv();
                imv.setFitWidth(2 * width);
                imv.setTranslateX(unit.getXValue() * width - width / 2);
            }
        });
    }
    private void setUnitFitHeight(double height) {
        board.getUnits().forEach(unit -> {
            if (unit != null) {
                ImageView imv = unit.getImv();
                imv.setFitHeight(2 * height);
                imv.setTranslateY(unit.getYValue() * height - height);
            }
        });
    }
    private void setCursorFitWidth(double width) {
        cursor.getIMV().setFitWidth(width * 2);
        cursor.getIMV().setTranslateX(cursor.getXValue() * width - width / 2);
    }
    private void setCursorFitHeight(double height) {
        cursor.getIMV().setFitHeight(height * 2);
        cursor.getIMV().setTranslateY(cursor.getYValue() * height - height / 2);
    }
    private void setArrowsFitWidth(double oldWidth, double newWidth){
        arrowPane.getChildren().forEach(imv->{
            if (imv instanceof ImageView) {
                ((ImageView) imv).setFitWidth(newWidth);
                imv.setTranslateX((imv.getTranslateX()/oldWidth)*newWidth);
            }
        });
    }
    private void setArrowsFitHeight(double oldHeight, double newHeight){
        arrowPane.getChildren().forEach(imv->{
            if (imv instanceof ImageView) {
                ((ImageView) imv).setFitHeight(newHeight);
                imv.setTranslateY((imv.getTranslateY()/oldHeight)*newHeight);
            }
        });
    }
    private void setMenuFitWidth(double oldW, double newW){
        MenuPane.getChildren().forEach(pane -> {
            ((ImageView)pane).setFitWidth((((ImageView) pane).getFitWidth()/oldW)*newW);
            pane.setTranslateX(pane.getTranslateX()/oldW*newW);
        });
    }
    private void setMenuFitHeight(double oldH, double newH){
        System.out.println(newH+"   "+oldH);
        MenuPane.getChildren().forEach(pane -> {
            ((ImageView)pane).setFitHeight(((ImageView) pane).getImage().getHeight()/96*newH);
            if (pane.getTranslateY() > board.getHeight()*newH/3) {
                System.out.println(pane.getTranslateY());
                System.out.println("coucou");
                pane.setTranslateY(board.getHeight() * newH - ((ImageView) pane).getFitHeight());
            } else pane.setTranslateY(((ImageView) pane).getTranslateY()*newH/oldH);

        });
    }

    private void displayUnit(Unit unit) {
        ImageView imv = new ImageView(unit.getSprites());
        imv.setViewport(new Rectangle2D(0, 6 * 16, 6 * 16, 6 * 16));
        imv.setFitWidth(48);
        imv.setFitHeight(48);
        Animation animation = new SpriteAnimation(unit, imv);
        animation.setCycleCount(Animation.INDEFINITE);
        animation.play();
        root.getChildren().add(imv);
    }

    private void displayUnits(Board board) {
        ParallelTransition spriteAnimation = new ParallelTransition();
        board.getUnits().forEach(unit -> {
            if (unit != null) {

                ImageView imv = new ImageView(unit.getSprites());
                imv.setViewport(new Rectangle2D(0, 0, 6 * 32, 6 * 32));
                unit.setImv(imv);
                imv.setFitWidth(96);
                imv.setFitHeight(96);
                imv.setTranslateX(unit.getXValue() * 48 - 24);
                imv.setTranslateY(unit.getYValue() * 48 - 48);
                unit.setImv(imv);
                unitPane.getChildren().add(imv);
                Animation animation = new SpriteAnimation(unit, imv);
                animation.setCycleCount(Animation.INDEFINITE);
                spriteAnimation.getChildren().add(animation);
            }
        });
        root.getChildren().add(unitPane);
        spriteAnimation.play();

    }

    private void displayCursor(ImageView imv) {
        CursorAnimation = new CursorTransition(imv);
        CursorAnimation.play();
        if(CursorAnimation instanceof CursorTransition) ((CursorTransition) CursorAnimation).getImage();
    }

    @SuppressWarnings("IntegerDivisionInFloatingPointContext")
    private void displayPath(ArrayList<Integer> path) {
        int firstDir = path.get(1) - path.getFirst();
        int lastDir = path.getLast() - path.get(path.size() - 2);
        ImageView imv1;
        ImageView imv2;
        if(firstDir ==1) imv1 = new ImageView(arrows.get("04"));
        else if (firstDir == -1)  imv1 = new ImageView(arrows.get("02"));
        else if (firstDir == board.getWidth()) imv1 = new ImageView(arrows.get("03"));
        else imv1 = new ImageView(arrows.get("01"));

        if (lastDir == 1) imv2 = new ImageView(arrows.get("20"));
        else if ((lastDir == -1)) imv2 = new ImageView(arrows.get("40"));
        else if ((lastDir == board.getWidth())) imv2 = new ImageView(arrows.get("10"));
        else imv2 = new ImageView(arrows.get("30"));

        imv1.setFitHeight(tileHeight);
        imv1.setFitWidth(tileWidth);
        imv2.setFitHeight(tileHeight);
        imv2.setFitWidth(tileWidth);
        imv1.setTranslateX(path.getFirst()%board.getWidth()*tileWidth);
        imv1.setTranslateY(path.getFirst() / board.getWidth() * tileHeight);
        imv2.setTranslateX(path.getLast()%board.getWidth() * tileWidth);
        imv2.setTranslateY(path.getLast() / board.getWidth() * tileHeight);
        arrowPane.getChildren().addAll(imv1,imv2);



        for (int i = 1; i < path.size()-1; i++) {
            int node = path.get(i);
            int previousNode = path.get(i-1);
            int nextNode = path.get(i+1);
            ImageView imv = new ImageView();

            String trajectory = "";

            int previousDir = node - previousNode;
            if (previousDir == 1) trajectory+="2";
            else if ((previousDir == -1)) trajectory+="4";
            else if ((previousDir == board.getWidth())) trajectory+="1";
            else trajectory+="3";

            int nextDir = nextNode - node;
            if (nextDir == 1) trajectory+="4";
            else if ((nextDir == -1)) trajectory+="2";
            else if ((nextDir == board.getWidth())) trajectory+="3";
            else trajectory+="1";

            switch (trajectory){
                case "41" : trajectory = "14";break;
                case "42" : trajectory = "24";break;
                case "43" : trajectory = "34";break;
                case "32" : trajectory = "23";break;
                case "31" : trajectory = "13";break;
                case "21" : trajectory = "12";break;
            }
            imv.setImage(arrows.get(trajectory));
            imv.setFitHeight(tileHeight);
            imv.setFitWidth(tileWidth);
            imv.setTranslateX(path.get(i)%board.getWidth()*tileWidth);
            imv.setTranslateY(path.get(i) / board.getWidth() * tileHeight);
            arrowPane.getChildren().add(imv);

        }
    }

    public void moveCursor(KeyCode c) throws IOException {
        ArrayList<Integer> path;
        arrowPane.getChildren().clear();
        switch (c) {
            case KeyCode.UP:
                path = cursor.moveCursor("up");
                cursor.getIMV().setTranslateY(cursor.getYValue() * tileHeight - tileHeight/2- tileHeight/16);
                if (cursor.getXValue()>board.getWidth()/2 && cursor.getYValue() == board.getHeight()/2-1) {
                    gameInterface.animate((ImageView) MenuPane.getChildren().getLast(), 2, true);
                }
                break;
            case KeyCode.DOWN:
                path =cursor.moveCursor("down");
                cursor.getIMV().setTranslateY(cursor.getYValue() * tileHeight - tileHeight/2- tileHeight/16);
                if (cursor.getXValue()>board.getWidth()/2 && cursor.getYValue() == board.getHeight()/2) {
                    gameInterface.animate((ImageView) MenuPane.getChildren().getLast(), 2, false);
                }
                break;
            case KeyCode.LEFT:
                path = cursor.moveCursor("left");
                cursor.getIMV().setTranslateX(cursor.getXValue() * tileWidth - tileWidth / 2);
                if (cursor.getXValue()==board.getWidth()/2) {
                    gameInterface.animate(new HashMap<>() {{
                            put(((ImageView) MenuPane.getChildren().getFirst()), new Pair<>(3, false));
                            if (cursor.getYValue()<board.getHeight()/2) {
                                put(((ImageView) MenuPane.getChildren().getLast()), new Pair<>(2, false));
                            }
                    }});
                }
                break;
            case KeyCode.RIGHT:
                path = cursor.moveCursor("right");
                cursor.getIMV().setTranslateX(cursor.getXValue() * tileWidth - tileWidth / 2);
                //if (board.getWidth()-cursor.getXValue()<8) {
                if (cursor.getXValue()==board.getWidth()/2+1) {
                    gameInterface.animate(new HashMap<>() {{
                            put(((ImageView) MenuPane.getChildren().getFirst()), new Pair<>(3, true));
                        if (cursor.getYValue()<board.getHeight()/2) {
                            put(((ImageView) MenuPane.getChildren().getLast()), new Pair<>(2, true));
                        }
                    }});
                }
                break;
            default:
                path = null;
        }
        gameInterface.updateTI(((ImageView) MenuPane.getChildren().getFirst()),cursor.getSquare().getTerrain().defToString(),cursor.getSquare().getTerrain().avoToString(),cursor.getSquare().getTerrain().toString());

        if (cursor.getSelectedUnit() != null){
            if (cursor.getSelectedUnit().getAvailableMoves().contains(cursor.getSquare())) {
                currentPath = cursor.getSelectedUnit().BFS_Algorithm(cursor.getYValue()*board.getWidth()+cursor.getXValue());
            }
        }
        if (cursor.getIMV().getImage().getUrl().equals("file:src/GUI/CursorSprites/Cursor4.png")) cursor.getIMV().setImage(((CursorTransition)CursorAnimation).getImage());
        if (cursor.getUnit() != null && cursor.getSelectedUnit() == null) {
            cursor.getIMV().setImage(new Image("file:src/GUI/CursorSprites/Cursor4.png", 192, 192, false, false));
            cursor.getIMV().setTranslateY(cursor.getYValue() * tileHeight - tileHeight+tileHeight/8);
        } else cursor.getIMV().setTranslateY(cursor.getYValue() * tileHeight - tileHeight/2- tileHeight/16);
        if (path != null){
            displayPath(path);
        }


    }

    public static void main(String[] args) {
        launch();
    }

    public void enter() {
        arrowPane.getChildren().clear();
        colouredSquaresAnimation.stop();
        if (cursor.getUnit() != null) {
            selectUnit();
            colouredSquaresAnimation.play(tileWidth, tileHeight);

        } else if (cursor.getSelectedUnit() != null){
            Unit movingUnit = cursor.getSelectedUnit();

            movingUnit.makeMove(cursor.getYValue(), cursor.getXValue(),tileWidth,tileHeight);
            cursor.unSelectUnit();
            movingUnit.getImv().setTranslateX(movingUnit.getXValue()*tileWidth - tileWidth/2);
            movingUnit.getImv().setTranslateY(movingUnit.getYValue()*tileHeight- tileHeight);


        }
    }

    private void selectUnit() {
        cursor.selectUnit(cursor.getUnit());
        Unit movingUnit = cursor.getSelectedUnit();
        if (movingUnit == null) return;
        cursor.getIMV().setTranslateY(cursor.getYValue()*tileHeight- tileHeight/2 - tileHeight/16);
        ArrayList<Square> availableMoves = movingUnit.getAvailableMoves();

    }
}
