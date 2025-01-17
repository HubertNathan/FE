package units;

import core.*;
import gui.Animations.SpriteAnimation;
import gui.Battle;
import gui.ResizableImage;
import core.game_engine.Battle.BattleEngine;
import core.game_engine.helper_functions.Dijkstra;
import Items.Weapons.Weapon;
import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import javafx.util.Pair;

import java.io.IOException;
import java.util.*;

import static core.game_engine.Battle.BattleEngine.tileSize;

public abstract class Unit extends Pane {
    protected Stats stats;
    protected String name;
    protected short health;
    protected String unitType;
    protected HashMap<String,Pair<ResizableImage,ResizableImage>> Sprites;
    protected short x, y, lastX,lastY;
    protected short attack_range = 1;
    private final Board board = BattleEngine.getBoard();
    protected boolean isLeader = false, isSelected = false;
    protected String mode = "standing", color = "blue";
    protected boolean isEnemy = false;
    protected ArrayList<Coord> availableMoves, squaresInRange;
    protected ImageView imv;
    protected Weapon wieldedWeapon;
    protected Inventory inventory;
    protected String skin = "";
    protected Transition moveTransition;
    protected boolean inRange = false;
    protected boolean promoted = false;
    protected boolean boss = false;
    protected int xp = 0;
    protected  short classPower = 3;
    Animation spriteAnimation;

    Unit(String name, String color, short[] stats, Inventory inventory) throws IOException {
        this.inventory = inventory;
        this.color = color;
        wieldedWeapon = inventory.getWeapons().getFirst();
        this.stats = new Stats(stats);
        this.name = name;
        load();
    }
    Unit(String name, String color, short[] stats, Weapon weapon) throws IOException {
        wieldedWeapon = weapon;
        this.color = color;
        this.name = name;
        this.stats = new Stats(stats);
        //load();
    }
    public boolean addXP(Unit enemy, boolean hit, boolean kill){
        short modeDivisor = 2;
        if (hit) xp+=1;
        else {
            xp += Math.max(0,(int) Math.ceil((31 + enemy.getLVL() + (enemy.isPromoted()?20:0)- stats.getLVL() - (promoted?20:0))/(double)classPower));
            if(kill){
                // replace false by enemy instanceof Bishop , valkyrie, Rogue, Assassin
                int enemyClassBonus = false?40:enemy.isPromoted()?60:0;
                int allyClassBonus = false?40:promoted?60:0;
                // add thief bonus (enemy instanceof Rogue, Thief, Assassin)?20:0
                xp+= Math.max(0,(int)Math.ceil(20+(enemy.isBoss()?40:0)+(enemy.getLVL()*enemy.getClassPower() + enemyClassBonus) - ((double)stats.getLVL()*classPower +allyClassBonus)/modeDivisor));
            }
        }
        if (xp >100){
            xp = xp%100;
            return true;
        }
        return false;
    }
    public abstract void load() throws IOException;
    public abstract ImageView getPortrait();
    public String getColor() {
        return color;
    }

    public String getSkin() {
        return skin;
    }
    public Image getSprites(){
        if (mode.equals(STANDING)){
            return Sprites.get(color).getKey();
        }
        return Sprites.get(color).getValue();
    }
    public Inventory getInventory(){
        return inventory;
    }
    public void setWieldedWeapon(Weapon weapon){this.wieldedWeapon = weapon;}
    public Weapon getWieldedWeapon() {
        return wieldedWeapon;
    }
    public boolean isPromoted(){return promoted;}
    public boolean isBoss(){return boss;}
    public void setBoss(boolean boss) {
        this.boss = boss;
    }
    public short getClassPower(){
        return classPower;
    }
    public void equipWeapon(Weapon weapon){
        wieldedWeapon = weapon;
    }
    public ImageView getImv(){
        return imv;
    }

    public boolean isInRange() {
        return inRange;
    }
    public void setInRange(boolean b) {
        inRange = b;
    }

    public void setImv(ImageView imv){
        this.imv = imv;
    }
    public int animation(int animFrame, boolean select, boolean moving) {
        if (select) return ((animFrame >= 20) ? 1 : 0) + ((animFrame >= 24) ? 1 : 0) - ((animFrame >= 44) ? 1 : 0);
        if (moving) return ((animFrame >= 12) ? 1 : 0) + ((animFrame >= 18) ? 1 : 0) + ((animFrame >= 30) ? 1 : 0);
        else return ((animFrame >= 32) ? 1 : 0) + ((animFrame >= 36) ? 1 : 0) - ((animFrame >= 68) ? 1 : 0);
    }
    public String getResourceDirectory(){
        return getBaseResourceDirectory()+"Battle Animations/"+getSkin()+"/"+getWieldedWeapon().getType();
    }
    protected abstract String getBaseResourceDirectory();
    public int animation(int animFrame){
        if (mode.equals("standing")){
            return animation((2*animFrame)%72, false,false);
        }
        else if (mode.equals("select")){
            return animation((2*animFrame)%48,true,false);
        }
        else return animation((2*animFrame)%36,false,true);
    }
    public void findMoves (){
        Pair<ArrayList<Coord>,ArrayList<Coord>> reachableTiles = Dijkstra.findMoves(board,y,x,stats.getMov(),unitType);
        availableMoves = reachableTiles.getKey();
        squaresInRange = reachableTiles.getValue();

    }
    public Unit select(){
        if (this instanceof Lyn_Lord) mode = Unit.DOWN;
        else mode = Unit.RIGHT;

        isSelected = true;
        findMoves();
        for (Coord pos : availableMoves) {
            board.get(pos).reach(true);
        }
        for (Coord pos : squaresInRange) {
            board.get(pos).range(true);
        }
        return this;
    }
    public void unselect(String mode) {
        this.mode = mode;
        if (mode.equals("standing")) {
            isSelected = false;
        }
        for (Square square : board.squareAsList()){
            square.reach(false);
            square.range(false);
        }
    }
    public void endMove(){
        for (Coord pos : availableMoves) {
            board.get(pos).reach(true);
        }
        for (Coord pos : squaresInRange) {
            Square square = board.get(pos);
            if (distanceTo(square)>attack_range ||(square.getXValue() == this.x && square.getYValue() == this.y))  {
                square.range(false);
            } else if (square.getUnit() == null || (square.getUnit() != null && !square.getUnit().getColor().equals("red"))) {
                square.range(false);

            } else {
                inRange = true;
            }
        }
    }
    private int distanceTo(Square square){
        return Math.abs(square.getXValue() - this.getXValue()) + Math.abs(square.getYValue() - this.getYValue());
    }
    public void setX(short x){
        this.x = x;
    }

    public void setY(short y) {
        this.y = y;
    }
    public int getXValue(){
        return x;
    }
    public int getYValue(){
        return y;
    }
    public Coord getPosition(){
        return new Coord(x,y);
    }
    public Transition getMoveTransition(){
        return moveTransition;
    }
    public ArrayList<Unit> findEnemiesInReach(){
        ArrayList<Unit> AdjacentUnits = new ArrayList<>();
        if (y>0 && board.get(y-1,x).getUnit() != null && board.get(y-1,x).getUnit().getColor().equals("red")) AdjacentUnits.add(board.get(y-1,x).getUnit());
        if (y< board.getHeight() - 1 && board.get(y+1,x).getUnit() != null && board.get(y+1,x).getUnit().getColor().equals("red")) AdjacentUnits.add(board.get(y+1,x).getUnit());
        if (x>0&&board.get(y,x-1).getUnit() != null&& board.get(y,x-1).getUnit().getColor().equals("red")) AdjacentUnits.add(board.get(y,x-1).getUnit());
        if (x<board.getWidth()-1&&board.get(y,x+1).getUnit() != null&& board.get(y,x+1).getUnit().getColor().equals("red")) AdjacentUnits.add(board.get(y,x+1).getUnit());
        return AdjacentUnits;
    }
    public ArrayList<Unit> findAlliesInReach(){
        ArrayList<Unit> AdjacentUnits = new ArrayList<>();
        if (y>0 && board.get(y-1,x).getUnit() != null && board.get(y-1,x).getUnit().getColor().equals("blue")) AdjacentUnits.add(board.get(y-1,x).getUnit());
        if (y< board.getHeight() - 1 && board.get(y+1,x).getUnit() != null && board.get(y+1,x).getUnit().getColor().equals("blue")) AdjacentUnits.add(board.get(y+1,x).getUnit());
        if (x>0&&board.get(y,x-1).getUnit() != null&& board.get(y,x-1).getUnit().getColor().equals("blue")) AdjacentUnits.add(board.get(y,x-1).getUnit());
        if (x<board.getWidth()-1&&board.get(y,x+1).getUnit() != null&& board.get(y,x+1).getUnit().getColor().equals("blue")) AdjacentUnits.add(board.get(y,x+1).getUnit());
        return AdjacentUnits;
    }
    public void setLeader(boolean isLeader){
        this.isLeader = isLeader;

    }
    public boolean isLeader(){
        return isLeader;
    }
    public String getMode() {return mode;}

    public void setMode(String mode){
        this.mode = mode;
    }

    public Transition makeMove(ArrayList<Coord> path){
        board.removeUnit(this,y,x);
        lastX = x;
        lastY = y;
        board.setUnit(this, path.getLast().getLast(), path.getLast().getFirst());
        Unit unit = this;

        final int len = path.size() - 1;
        return new Transition() {
            {
                setCycleDuration(Duration.seconds((double) len/8));
                setCycleCount(1);
                setInterpolator(Interpolator.LINEAR);
                setOnFinished(e -> {
                    imv.setTranslateX(path.getLast().getFirst() * tileSize - tileSize / 2);
                    imv.setTranslateY(path.getLast().getLast() * tileSize - tileSize);
                }
                );
            }
            @Override
            protected void interpolate(double k){
                int index = Math.min((int) (k*len),len -1);
                double t = k*len - index;

                int deltaX = path.get(index + 1).getFirst() - path.get(index).getFirst();
                int deltaY = path.get(index + 1).getLast() - path.get(index).getLast();

                if (deltaX>0){
                    unit.setMode(RIGHT);
                } else if (deltaX<0) {
                    unit.setMode(LEFT);
                } else if (deltaY>0) {
                    unit.setMode(DOWN);
                } else if (deltaY<0) {
                    unit.setMode(UP);
                }
                imv.setTranslateX((path.get(index).getFirst() + t * deltaX) * tileSize - tileSize / 2);
                imv.setTranslateY((path.get(index).getLast() + t * deltaY) * tileSize - tileSize);
            }
        };
    }
    public void revertMove(){
        board.removeUnit(this,y,x);
        x = lastX;
        y = lastY;
        board.setUnit(this,y,x);
        imv.setTranslateX(x * tileSize - tileSize / 2);
        imv.setTranslateY(y * tileSize - tileSize);
    }
    public ArrayList<Coord> getAvailableMoves(){return availableMoves;}

    public String getUnitType() {
        return unitType;
    }


    public Animation getSpriteAnimation(){
        spriteAnimation = new SpriteAnimation(this, imv){{setCycleCount(INDEFINITE);}};
        return spriteAnimation;
    }
    public void die(){
        Battle.getSprites().getChildren().remove(spriteAnimation);
        spriteAnimation.stop();
        imv.setEffect(new ColorAdjust(){{
            setBrightness(1);
        }});
        new Transition(){
            {
                setCycleCount(1);
                setCycleDuration(Duration.seconds(1));
                setOnFinished(event->board.removeUnit(Unit.this,y,x));
                play();
            }
            @Override
            protected void interpolate(double v) {
                Unit.this.imv.setOpacity(1-v);
            }};

    }
    @Override
    public String toString() {
        return name;
    }
    public void endTurn(){
        color = "gray";
    }
    public void newTurn(){
        imv.setEffect(null);
    }
    public short getHealth(){
        return health;
    }
    public void setHealth(short health){
        this.health = health;
    }


    public short getLVL(){
        return stats.getLVL();
    }
    public short getHP() {
        return stats.getHP();
    }
    public short getStr(){
        return stats.getStr();
    }
    public short getMag() {
        return stats.getMag();
    }
    public short getSkl() {
        return stats.getSkl();
    }
    public short getSpd() {
        return stats.getSpd();
    }
    public short getLck() {
        return stats.getLck();
    }
    public short getDef() {
        return stats.getLck();
    }
    public short getRes() {
        return stats.getRes();
    }
    public short getCon() {
        return stats.getCon();
    }
    public void setHP(short HP) {
        this.stats.setHP(HP);
    }

    public static final String RIGHT = "movRight";
    public static final String LEFT = "movLeft";
    public static final String UP = "movUp";
    public static final String DOWN = "movDown";
    public static final String STANDING = "standing";
    public static final String SELECT = "select";
}
