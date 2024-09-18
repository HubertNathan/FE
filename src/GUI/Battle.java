package GUI;

import EventHandlers.KeyEventHandler;
import GUI.Animations.ColouredSquaresAnimation;
import GUI.Animations.ImageViewer;
import GUI.Combat.CombatRenderer;
import GameEngine.Board;
import GameEngine.Cursor;
import GUI.PanelInterface.GameInterface;
import GameEngine.MenuPointer;
import GameEngine.Save.LoadSaveBoard;
import Units.Unit;
import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.ParallelTransition;
import javafx.animation.Transition;
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
import java.util.List;

public class Battle {
    private static Stage window;
    private Board board;
    private static Pane root = new Pane();
    private static Scene gameScene = new Scene(root);
    private final Pane unitPane = new Pane(), colouredSquaresPane = new Pane(), arrowPane = new Pane();
    public final Pane MenuPane = new Pane();
    public GameInterface gameInterface;
    ColouredSquaresAnimation colouredSquaresAnimation;
    private ImageView background;
    private final MenuPointer menuPointer = new MenuPointer();
    private Cursor cursor;
    double tileWidth = 48, tileHeight = 48;
    ArrayList<Integer> currentPath;
    Transition CursorAnimation;
    int menuId = 0;
    private static final ParallelTransition sprites = new ParallelTransition();
    final HashMap<String, Image> arrows = new HashMap<>() {
        {
            put("01", new Image(getClass().getResource("Arrows/01.png").toExternalForm(), 92, 92, false, false));
            put("02", new Image(getClass().getResource("Arrows/02.png").toExternalForm(), 92, 92, false, false));
            put("03", new Image(getClass().getResource("Arrows/03.png").toExternalForm(), 92, 92, false, false));
            put("04", new Image(getClass().getResource("Arrows/04.png").toExternalForm(), 92, 92, false, false));
            put("10", new Image(getClass().getResource("Arrows/10.png").toExternalForm(), 92, 92, false, false));
            put("12", new Image(getClass().getResource("Arrows/12.png").toExternalForm(), 92, 92, false, false));
            put("13", new Image(getClass().getResource("Arrows/13.png").toExternalForm(), 92, 92, false, false));
            put("14", new Image(getClass().getResource("Arrows/14.png").toExternalForm(), 92, 92, false, false));
            put("20", new Image(getClass().getResource("Arrows/20.png").toExternalForm(), 92, 92, false, false));
            put("23", new Image(getClass().getResource("Arrows/23.png").toExternalForm(), 92, 92, false, false));
            put("24", new Image(getClass().getResource("Arrows/24.png").toExternalForm(), 92, 92, false, false));
            put("30", new Image(getClass().getResource("Arrows/30.png").toExternalForm(), 92, 92, false, false));
            put("34", new Image(getClass().getResource("Arrows/34.png").toExternalForm(), 92, 92, false, false));
            put("40", new Image(getClass().getResource("Arrows/40.png").toExternalForm(), 92, 92, false, false));

        }
    };
    public Battle(Stage stage) throws IOException {
        window = stage;
        gameScene.setFill(Color.WHITE);
        ReadMapFile mapFile = new ReadMapFile("CH1");
        loadBoard(mapFile);
        loadCursor();
        gameInterface = new GameInterface(board);
        MenuPane.getChildren().addAll(gameInterface.drawMenu(Integer.toString(cursor.getSquare().getTerrain().getDef()), Integer.toString(cursor.getSquare().getTerrain().getAvoid()), cursor.getSquare().getTerrain().toString(), mapFile.getObjectives()));
        window.setTitle("Fire Emblem");
        window.show();
        window.setScene(gameScene);
        colouredSquaresAnimation = new ColouredSquaresAnimation(board, colouredSquaresPane);
        addEventListeners();
    }

    public int getMenuId() {
        return menuId;
    }
    public ImageView getActiveMenu(int i){
        return (ImageView) MenuPane.getChildren().get(MenuPane.getChildren().size()-i);
    }

    public void b() throws IOException {
        root = (Pane) gameScene.getRoot();
        new CombatRenderer(this, window, board, board.get(2, 2).getUnit(), board.get(6, 5).getUnit());
    }

    public void revertToGameScene() {
        gameScene.setRoot(root);
        window.setScene(gameScene);
        menuId = 0;
        gameInterface.transitionIn(new HashMap<>(){{
            put((ImageView) MenuPane.getChildren().getFirst(), new Pair<>(3,false));
            put((ImageView) MenuPane.getChildren().getLast(), new Pair<>(2,false));
        }});
        cursor.getSelectedUnit().endTurn();
        cursor.unSelectUnit();
        cursor.getIMV().setVisible(true);
        cursor.setColor("yellow");
        colouredSquaresAnimation.stop();
    }

    class CursorTransition extends Transition {
        ImageView imv;
        public Image backup;

        CursorTransition(ImageView imv) {
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
            put(11, new Image("file:src/GUI/CursorSprites/Cursor11.png", 192, 192, false, false));
            put(12, new Image("file:src/GUI/CursorSprites/Cursor12.png", 192, 192, false, false));
            put(13, new Image("file:src/GUI/CursorSprites/Cursor13.png", 192, 192, false, false));
        }};

        public Image getImage() {
            return backup;
        }


        private int updateAnimation(double animFrame) {
            return 3 + (animFrame > 1 ? -1 : 0) + (animFrame > 3 ? -1 : 0) + (animFrame > 19 ? 1 : 0) + (animFrame > 21 ? 1 : 0);
        }

        @Override
        protected void interpolate(double k) {
            int index = updateAnimation(36 * k) + (cursor.getColor().equals("red") ? 10 : 0);
            if ((index != lastIndex) && ((cursor.getUnit() != null && !cursor.getUnit().getColor().equals("blue")) || cursor.getUnit() == null || cursor.getSelectedUnit() != null)) {
                backup = CursorMap.get(index);
                imv.setImage(CursorMap.get(index));
            }
            lastIndex = index;
        }

    }

    private void loadBoard(ReadMapFile mapReader) throws IOException {
        board = new Board(mapReader);
        loadBackground();
        root.getChildren().add(colouredSquaresPane);
        root.getChildren().add(arrowPane);
        new LoadSaveBoard("CH1").loadBoard(board);
        displayUnits(board);
        root.getChildren().add(MenuPane);
    }

    private void loadBackground() {
        background = new ImageView(board.drawFX(new Canvas(16 * board.getWidth(), 16 * board.getHeight())).snapshot(null, new WritableImage(16 * board.getWidth(), 16 * board.getHeight())));
        ResizeBackground();
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
            setArrowsFitWidth((Double) oldVal / board.getWidth(), tileWidth);
            setMenuFitWidth((Double) oldVal / board.getWidth(), tileWidth);

        });

        window.heightProperty().addListener((obs, oldVal, newVal) -> {
            background.setFitHeight((Double) newVal - 37);
            tileHeight = ((Double) newVal - 37) / board.getHeight();
            setUnitFitHeight(tileHeight);
            setCursorFitHeight(tileHeight);
            setSquaresFitHeight(tileHeight);
            setArrowsFitHeight(((Double) oldVal - 37) / board.getHeight(), tileHeight);
            setMenuFitHeight(((Double) oldVal - 37) / board.getHeight(), tileHeight);
        });
        gameScene.setOnKeyPressed(new KeyEventHandler(this));
    }

    private void ResizeBackground() {
        Image image = background.getImage();
        background = new ImageView(
                resample(
                        image
                )
        );
    }

    private Image resample(Image input) {
        final int W = (int) input.getWidth();
        final int H = (int) input.getHeight();
        final int S = 6;

        return getResampledImage(input, W, H, S);
    }

    public static Image getResampledImage(Image input, int w, int h, int s) {
        WritableImage output = new WritableImage(
                w * s,
                h * s
        );

        PixelReader reader = input.getPixelReader();
        PixelWriter writer = output.getPixelWriter();

        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                final int argb = reader.getArgb(x, y);
                for (int dy = 0; dy < s; dy++) {
                    for (int dx = 0; dx < s; dx++) {
                        writer.setArgb(x * s + dx, y * s + dy, argb);
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

    private void setArrowsFitWidth(double oldWidth, double newWidth) {
        arrowPane.getChildren().forEach(imv -> {
            if (imv instanceof ImageView) {
                ((ImageView) imv).setFitWidth(newWidth);
                imv.setTranslateX((imv.getTranslateX() / oldWidth) * newWidth);
            }
        });
    }

    private void setArrowsFitHeight(double oldHeight, double newHeight) {
        arrowPane.getChildren().forEach(imv -> {
            if (imv instanceof ImageView) {
                ((ImageView) imv).setFitHeight(newHeight);
                imv.setTranslateY((imv.getTranslateY() / oldHeight) * newHeight);
            }
        });
    }

    private void setMenuFitWidth(double oldW, double newW) {
        MenuPane.getChildren().forEach(pane -> {
            ((ImageView) pane).setFitWidth((((ImageView) pane).getFitWidth() / oldW) * newW);
            pane.setTranslateX(pane.getTranslateX() / oldW * newW);
        });
    }

    private void setMenuFitHeight(double oldH, double newH) {
        MenuPane.getChildren().forEach(pane -> {
            ((ImageView) pane).setFitHeight(((ImageView) pane).getImage().getHeight() / 96 * newH);
            if (pane.getTranslateY() > board.getHeight() * newH / 3) {
                pane.setTranslateY(board.getHeight() * newH - ((ImageView) pane).getFitHeight());
            } else pane.setTranslateY(pane.getTranslateY() * newH / oldH);

        });
    }

    private void displayUnits(Board board) {
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
                Animation animation = unit.getSpriteAnimation();
                sprites.getChildren().add(animation);
            }
        });
        root.getChildren().add(unitPane);
        sprites.play();

    }

    public static ParallelTransition getSprites() {
        return sprites;
    }
    private void displayCursor(ImageView imv) {
        CursorAnimation = new CursorTransition(imv);
        CursorAnimation.play();
    }

    @SuppressWarnings("IntegerDivisionInFloatingPointContext")
    private void displayPath(ArrayList<Integer> path) {
        int firstDir = path.get(1) - path.getFirst();
        int lastDir = path.getLast() - path.get(path.size() - 2);
        ImageView imv1;
        ImageView imv2;
        if (firstDir == 1) imv1 = new ImageView(arrows.get("04"));
        else if (firstDir == -1) imv1 = new ImageView(arrows.get("02"));
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
        imv1.setTranslateX(path.getFirst() % board.getWidth() * tileWidth);
        imv1.setTranslateY(path.getFirst() / board.getWidth() * tileHeight);
        imv2.setTranslateX(path.getLast() % board.getWidth() * tileWidth);
        imv2.setTranslateY(path.getLast() / board.getWidth() * tileHeight);
        arrowPane.getChildren().addAll(imv1, imv2);


        for (int i = 1; i < path.size() - 1; i++) {
            int node = path.get(i);
            int previousNode = path.get(i - 1);
            int nextNode = path.get(i + 1);
            ImageView imv = new ImageView();

            String trajectory = getTrajectory(node, previousNode, nextNode);
            imv.setImage(arrows.get(trajectory));
            imv.setFitHeight(tileHeight);
            imv.setFitWidth(tileWidth);
            imv.setTranslateX(path.get(i) % board.getWidth() * tileWidth);
            imv.setTranslateY(path.get(i) / board.getWidth() * tileHeight);
            arrowPane.getChildren().add(imv);

        }
    }

    private String getTrajectory(int node, int previousNode, int nextNode) {
        String trajectory = "";

        int previousDir = node - previousNode;
        if (previousDir == 1) trajectory += "2";
        else if ((previousDir == -1)) trajectory += "4";
        else if ((previousDir == board.getWidth())) trajectory += "1";
        else trajectory += "3";

        int nextDir = nextNode - node;
        if (nextDir == 1) trajectory += "4";
        else if ((nextDir == -1)) trajectory += "2";
        else if ((nextDir == board.getWidth())) trajectory += "3";
        else trajectory += "1";

        trajectory = switch (trajectory) {
            case "41" -> "14";
            case "42" -> "24";
            case "43" -> "34";
            case "32" -> "23";
            case "31" -> "13";
            case "21" -> "12";
            default -> trajectory;
        };
        return trajectory;
    }

    public void moveCursor(KeyCode c) throws IOException {
        ArrayList<Integer> path;
        arrowPane.getChildren().clear();
        switch (c) {
            case KeyCode.UP:
                path = cursor.moveCursor("up");
                cursor.getIMV().setTranslateY(cursor.getYValue() * tileHeight - tileHeight / 2 - tileHeight / 16);
                if (cursor.getXValue() > board.getWidth() / 2 && cursor.getYValue() == board.getHeight() / 2 - 1 && menuId==0) {
                    gameInterface.animate((ImageView) MenuPane.getChildren().getLast(), 2, true);
                }
                break;
            case KeyCode.DOWN:
                path = cursor.moveCursor("down");
                cursor.getIMV().setTranslateY(cursor.getYValue() * tileHeight - tileHeight / 2 - tileHeight / 16);
                if (cursor.getXValue() > board.getWidth() / 2 && cursor.getYValue() == board.getHeight() / 2 && menuId==0) {
                    gameInterface.animate((ImageView) MenuPane.getChildren().getLast(), 2, false);
                }
                break;
            case KeyCode.LEFT:
                path = cursor.moveCursor("left");
                cursor.getIMV().setTranslateX(cursor.getXValue() * tileWidth - tileWidth / 2);
                if (cursor.getXValue() == board.getWidth() / 2 && menuId==0) {
                    gameInterface.animate(new HashMap<>() {{
                        put(((ImageView) MenuPane.getChildren().getFirst()), new Pair<>(3, false));
                        if (cursor.getYValue() < board.getHeight() / 2) {
                            put(((ImageView) MenuPane.getChildren().get(1)), new Pair<>(2, false));
                        }
                    }});
                }
                break;
            case KeyCode.RIGHT:
                path = cursor.moveCursor("right");
                cursor.getIMV().setTranslateX(cursor.getXValue() * tileWidth - tileWidth / 2);
                //if (board.getWidth()-cursor.getXValue()<8) {
                if (cursor.getXValue() == board.getWidth() / 2 + 1 && menuId==0) {
                    gameInterface.animate(new HashMap<>() {{
                        put(((ImageView) MenuPane.getChildren().getFirst()), new Pair<>(3, true));
                        if (cursor.getYValue() < board.getHeight() / 2) {
                            put(((ImageView) MenuPane.getChildren().getLast()), new Pair<>(2, true));
                        }
                    }});
                }
                break;
            default:
                path = null;
        }
        gameInterface.updateTI(((ImageView) MenuPane.getChildren().getFirst()), cursor.getSquare().getTerrain().defToString(), cursor.getSquare().getTerrain().avoToString(), cursor.getSquare().getTerrain().toString());

        if (cursor.getSelectedUnit() != null) {
            if (cursor.getSelectedUnit().getAvailableMoves().contains(cursor.getSquare())) {
                currentPath = cursor.getSelectedUnit().BFS_Algorithm(cursor.getYValue() * board.getWidth() + cursor.getXValue());
            }
        }
        if (cursor.getIMV().getImage().getUrl().equals("file:src/GUI/CursorSprites/Cursor4.png"))
            cursor.getIMV().setImage(((CursorTransition) CursorAnimation).getImage());
        if (cursor.getUnit() != null && cursor.getSelectedUnit() == null && cursor.getUnit().getColor().equals("blue")) {
            cursor.getIMV().setImage(new Image("file:src/GUI/CursorSprites/Cursor4.png", 192, 192, false, false));
            cursor.getIMV().setTranslateY(cursor.getYValue() * tileHeight - tileHeight + tileHeight / 8);
        } else cursor.getIMV().setTranslateY(cursor.getYValue() * tileHeight - tileHeight / 2 - tileHeight / 16);
        if (path != null) {
            displayPath(path);
        }

    }

    public void moveMenuPointer(KeyCode c,double inf, double sup, int offset) throws IOException {
        if (c.equals(KeyCode.UP)) {
            if (menuPointer.getTranslateY() - 3 * 16 < inf) {
                menuPointer.setTranslateY(menuPointer.getTranslateY() + offset * 3 * 16);
            } else menuPointer.setTranslateY(menuPointer.getTranslateY() - 3 * 16);
        }
        if (c.equals(KeyCode.DOWN)) {
            if (menuPointer.getTranslateY() + 2 * 3 * 15 > sup) {
                menuPointer.setTranslateY(menuPointer.getTranslateY() - offset * 3 * 16);
            } else menuPointer.setTranslateY(menuPointer.getTranslateY() + 3 * 16);
        }
        if (menuId == 10){
            gameInterface.buildWeaponMenu(getActiveMenu(3), cursor.getSelectedUnit(),cursor.getSelectedUnit().getInventory().getWeapons().get((int)(menuPointer.getTranslateY()-27)/48));
        }
    }

    public void enter() throws IOException {
        arrowPane.getChildren().clear();
        switch (menuId) {
            case 0:
                if (cursor.getUnit() != null && !cursor.getUnit().getColor().equals("red")) {
                    cursor.selectUnit(cursor.getUnit());
                    cursor.getIMV().setTranslateX((cursor.getXValue()-1./2)*tileWidth);
                    cursor.getIMV().setTranslateY((cursor.getYValue()-9./16)*tileWidth);
                    colouredSquaresAnimation.play(tileWidth, tileHeight);
                    gameInterface.transitionOut(new HashMap<>() {{
                        put((ImageView) MenuPane.getChildren().getFirst(), new Pair<>(3, true));
                        put((ImageView) MenuPane.getChildren().get(1), new Pair<>(2, true));
                    }});
                    menuId += 1;
                }
                break;
            case 1:
                colouredSquaresAnimation.stop();
                cursor.getIMV().setVisible(false);
                Unit movingUnit = cursor.getSelectedUnit();
                int row = cursor.getYValue(), col = cursor.getXValue();
                movingUnit.makeMove(row, col, tileWidth, tileHeight, colouredSquaresAnimation);
                movingUnit.getMoveTransition().setOnFinished(event -> {
                    ImageView intermediateMenu;
                    board.setUnit(movingUnit, row, col);
                    movingUnit.endMove();
                    try {
                        intermediateMenu = gameInterface.drawIntermediateMenu(movingUnit.isInRange());
                        intermediateMenu.setTranslateY(28 * 3);
                        if (cursor.getXValue() > board.getWidth() / 2) {
                            intermediateMenu.setTranslateX(12 * 3);
                        } else {
                            intermediateMenu.setTranslateX(board.getWidth() * 16 * 3 - 60 * 3);
                        }

                        MenuPane.getChildren().add(intermediateMenu);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                    menuPointer.setTranslateX(intermediateMenu.getTranslateX() - 15 * 3);
                    menuPointer.setTranslateY(intermediateMenu.getTranslateY() + 3 * 7);

                    MenuPane.getChildren().add(menuPointer);
                    menuPointer.play();
                    colouredSquaresAnimation.play();
                });
                menuId += 1;
                break;
            case 2:
                menuPointer.stop();
                MenuPane.getChildren().removeLast();
                MenuPane.getChildren().removeLast();
                selectAction(cursor.getSelectedUnit(), cursor.getSelectedUnit().isInRange(), false);
                break;
            case 10:
                cursor.getSelectedUnit().setWieldedWeapon(cursor.getSelectedUnit().getInventory().getWeapons().get((int)(menuPointer.getTranslateY()/tileHeight)-1));
                menuPointer.stop();
                MenuPane.getChildren().removeLast();
                MenuPane.getChildren().removeLast();
                MenuPane.getChildren().removeLast();
                MenuPane.getChildren().removeLast();
                ArrayList<Unit> Enemies = cursor.getSelectedUnit().getAdjacentEnemies();
                cursor.setTo(Enemies.getFirst(),tileWidth,tileHeight);
                cursor.setColor("red");
                cursor.getIMV().setVisible(true);
                ImageView forecast = gameInterface.drawForecast(cursor.getSelectedUnit(),cursor.getUnit());
                MenuPane.getChildren().add(forecast);
                menuId+=1;
                break;
            case 11:
                MenuPane.getChildren().removeLast();
                root = (Pane) gameScene.getRoot();
                new CombatRenderer(this, window, board, cursor.getSelectedUnit(), cursor.getUnit());

        }
    }

    private void selectAction(Unit movingUnit, boolean isAttacking, boolean isTradingItems) throws IOException {
        switch ((int) menuPointer.getTranslateY() + (isAttacking ? 0 : 48) + (isTradingItems || (isAttacking && (int) menuPointer.getTranslateY() == 105) ? 0 : 48)) {
            case 105: //Attack
                List<ImageView> WeaponMenu = gameInterface.drawWeaponMenu(cursor.getSelectedUnit());
                WeaponMenu.getFirst().setTranslateX(tileWidth * 3 / 4);
                WeaponMenu.getFirst().setTranslateY(tileHeight * 3 / 4);
                WeaponMenu.get(1).setTranslateX(window.getWidth() - WeaponMenu.get(1).getImage().getWidth() / 2 - tileWidth * 11 / 16);
                WeaponMenu.get(1).setTranslateY((window.getHeight() - 37) - WeaponMenu.get(1).getImage().getHeight() / 2 - tileHeight * 11 / 16);
                WeaponMenu.getLast().setTranslateX(window.getWidth() - WeaponMenu.getLast().getFitWidth() - tileWidth);
                WeaponMenu.getLast().setTranslateY((window.getHeight() - 37) - WeaponMenu.get(1).getImage().getHeight() / 2 - WeaponMenu.getLast().getFitHeight() - tileHeight * 11 / 16);
                MenuPane.getChildren().addAll(WeaponMenu);
                MenuPane.getChildren().add(menuPointer);
                menuPointer.setTranslateX(tileWidth * 2 / 16);
                menuPointer.setTranslateY(tileWidth * 19 / 16);
                menuPointer.play();
                menuId = 10;
                break;
            case 153: //Trade
                break;
            case 201: //Item
                break;
            case 249: //Wait
                gameInterface.transitionIn(new HashMap<>(){{
                    put((ImageView) MenuPane.getChildren().getFirst(), new Pair<>(3,false));
                    put((ImageView) MenuPane.getChildren().getLast(), new Pair<>(2,false));
                }});
                cursor.getSelectedUnit().endTurn();
                cursor.unSelectUnit();
                cursor.getIMV().setVisible(true);
                colouredSquaresAnimation.stop();
                menuId=0;
                break;
        }
    }
}